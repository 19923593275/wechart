package com.zxx.wechart.store.service.impl;

import com.zxx.wechart.store.common.CodeConstant;
import com.zxx.wechart.store.common.ServiceException;
import com.zxx.wechart.store.common.UserCache;
import com.zxx.wechart.store.common.UserRsp;
import com.zxx.wechart.store.config.WechatUserInfo;
import com.zxx.wechart.store.config.WechatUserToken;
import com.zxx.wechart.store.domain.user.User;
import com.zxx.wechart.store.mapper.UserMapper;
import com.zxx.wechart.store.service.UserService;
import com.zxx.wechart.store.utils.HttpUtil;
import com.zxx.wechart.store.utils.UserUtil;
import com.zxx.wechart.store.utils.WechatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @Author: 周星星
 * @DateTime: 2020/1/12 0012 15:44
 * @Description: User实现类
 */
@Service("UserService")
public class UserServiceImpl implements UserService{

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserRsp login(HttpServletRequest request, String code) throws ServiceException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserCache userCache = userUtil.getUserInfoBySession(session);
            logger.info(userCache.getUser_open_id() + "login sesson invalidate");
            session.invalidate();
        }
        session = request.getSession();
        WechatUserToken wechatUserToken = WechatUtil.getOpenIdByCode(code);
        if (wechatUserToken == null){
            throw new ServiceException(CodeConstant.WECHAT_USER_TOKEN_NULL);
        }
        String openId = wechatUserToken.getOpeniId();
        if (StringUtils.isEmpty(openId)) {
            throw new ServiceException(CodeConstant.WECHAT_USER_OPENID_NULL);
        }
        User user = this.queryUserInfoByoenId(openId);
        //TODO 通过openid获取用户信息
        WechatUserInfo wechatUserInfo = WechatUtil.getBasicUserInfoByOpenId(openId);
        if (wechatUserInfo == null) {
            throw new ServiceException(CodeConstant.WECHAT_USER_INFO_NULL);
        }
        UserRsp userRsp = this.addOrUpdateUser(user, wechatUserInfo);
        if (userRsp == null) {
            throw new ServiceException(CodeConstant.WECHAT_USER_INFO_NULL);
        }
        return userRsp;
    }

    public User queryUserInfoByoenId(String openId){
        User user = null;
        try {
            user = userMapper.queryUserInfoByOpenId(openId);
        }catch (Exception e) {
            logger.error("queryUserInfoByoenId err", e);
        }
        return user;
    }

    public UserRsp addOrUpdateUser(User user, WechatUserInfo wechatUserInfo) {
        UserRsp userRsp = null;
        try{
            int isfllow = wechatUserInfo.getSubscribe();
            if (isfllow == 0) {
                logger.info("用户未关注 直接返回null");
                return null;
            }
            int gender = wechatUserInfo.getSex();
            String headImg = wechatUserInfo.getHeadimgurl();
            String nickName = wechatUserInfo.getNickName();
            String openId = wechatUserInfo.getOpenId();
            if (user == null) {
                Date createDate = new Date();
                userRsp.setCreate_date(createDate);
                userRsp.setGengder(gender);
                userRsp.setHead_img(headImg);
                userRsp.setIs_follow(isfllow);
                userRsp.setNick_name(nickName);
                userRsp.setUser_open_id(openId);
                userMapper.addUserInfo(openId, createDate, isfllow, gender, nickName, headImg);
                logger.info("新增用户成功");
            } else {
                userRsp.setUser_open_id(user.getUser_open_id());
                userRsp.setNick_name(nickName);
                userRsp.setIs_follow(isfllow);
                userRsp.setHead_img(headImg);
                userRsp.setUser_name(user.getUser_name());
                userRsp.setUser_tel(user.getUser_tel());
                userRsp.setCreate_date(user.getCreate_date());
                userRsp.setGengder(gender);
                userMapper.updateUserInfo(openId, nickName, headImg, gender, isfllow);
            }
        } catch (Exception e) {
            userRsp = null;
            logger.error("addOrUpdateUser error", e);
        }
        return userRsp;
    }

}
