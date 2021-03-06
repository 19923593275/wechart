package com.zxx.wechart.store.service.impl;

import com.zxx.wechart.store.common.*;
import com.zxx.wechart.store.config.WechatConfig;
import com.zxx.wechart.store.config.WechatUserInfo;
import com.zxx.wechart.store.config.WechatUserToken;
import com.zxx.wechart.store.domain.user.User;
import com.zxx.wechart.store.mapper.UserMapper;
import com.zxx.wechart.store.service.UserService;
import com.zxx.wechart.store.utils.RoundNumUtil;
import com.zxx.wechart.store.utils.UserUtil;
import com.zxx.wechart.store.utils.WechatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Author: 周星星
 * @DateTime: 2020/1/12 0012 15:44
 * @Description: User实现类
 */
@Service("UserService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService{

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private UserMapper userMapper;

    @Value("${test.enable}")
    private boolean isTest;

    @Override
    public UserRsp login(HttpServletRequest request, String code) throws ServiceException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserCache userCache = userUtil.getUserInfoBySession(session);
            if (userCache != null) {
                logger.info(userCache.getUser_open_id() + "login sesson invalidate");
            } else {
                logger.info("login sesson userCache null");
            }
            session.invalidate();
            logger.info("==============清空session==============");
        }
        session = request.getSession();

        if (isTest && !StringUtils.isEmpty(request.getHeader(WechatConfig.IS_TEST_HEADER_KEY))) {
            return passWechatAuthForTest(request, session);
        }

        WechatUserToken wechatUserToken = WechatUtil.getOpenIdByCode(code);
        if (wechatUserToken == null){
            throw new ServiceException(CodeConstant.WECHAT_USER_TOKEN_NULL);
        }
        String openId = wechatUserToken.getOpeniId();
        if (StringUtils.isEmpty(openId)) {
            throw new ServiceException(CodeConstant.WECHAT_USER_OPENID_NULL);
        }
        User user = this.queryUserInfoByoenId(openId);
        WechatUserInfo wechatUserInfo = WechatUtil.getBasicUserInfoByOpenId(openId);
        if (wechatUserInfo == null) {
            throw new ServiceException(CodeConstant.WECHAT_USER_INFO_NULL);
        }
        UserRsp userRsp = this.addOrUpdateUser(user, wechatUserInfo);
        if (userRsp == null || userRsp.getIs_follow() != 1) {
            throw new ServiceException(CodeConstant.WECHAT_USER_INFO_NULL);
        } else {
            userRsp.setUser_token(wechatUserToken.getAccessToken());
            UserCache userCache = createUserCache(userRsp);
            UserUtil.saveUserSession(session, userCache);
        }
        logger.info("userRsp =========" + userRsp);
        return userRsp;
    }

    private UserRsp passWechatAuthForTest(HttpServletRequest request, HttpSession session) throws ServiceException {
        String userOpenId = request.getHeader(WechatConfig.IS_TEST_HEADER_KEY);
        logger.info("openid ===== " + userOpenId);
        if (StringUtils.isEmpty(userOpenId)) {
            userOpenId = "okmaY1U76nDTg3r2r16ffRym4c2M";
            logger.info("openid ===== " + userOpenId);
        } else {
            logger.info("vue 自定义 openid ===== " + userOpenId);
        }
        User user = queryUserInfoByoenId(userOpenId);
        if (user == null) {
            throw new ServiceException(CodeConstant.WECHAT_TEST_USER_NULL);
        }
        UserRsp userRsp = createUserRspByUser(user);
        userRsp.setUser_token(RoundNumUtil.randonString(10));
        UserCache userCache = createUserCache(userRsp);
        logger.info(" saveUserSession userCache = " + userCache.toString());
        userUtil.saveUserSession(session, userCache);
        return userRsp;
    }

    private UserCache createUserCache(UserRsp userRsp) {
        UserCache userCache = new UserCache();
        userCache.setGengder(userRsp.getGengder());
        userCache.setIs_follow(userRsp.getIs_follow());
        userCache.setNick_name(userRsp.getNick_name());
        userCache.setUser_name(userRsp.getUser_name());
        userCache.setUser_open_id(userRsp.getUser_open_id());
        userCache.setUser_tel(userRsp.getUser_tel());
        userCache.setUserToken(userRsp.getUser_token());
        return userCache;
    }

    private UserRsp createUserRspByUser(User user) {
        UserRsp userRsp = new UserRsp();
        userRsp.setIs_follow(user.getIs_follow());
        userRsp.setGengder(user.getGender());
        userRsp.setCreate_date(user.getCreate_date());
        userRsp.setUser_name(user.getUser_name());
        userRsp.setHead_img(user.getHead_img());
        userRsp.setNick_name(user.getNick_name());
        userRsp.setUser_open_id(user.getUser_open_id());
        userRsp.setUser_tel(user.getUser_tel());
        return userRsp;
    }

    @Override
    public String sendCode(HttpServletRequest request, String serviceType) {
        String code;
        try {
            HttpSession session = request.getSession();
            if (session == null) {
                throw new ServiceException(CodeConstant.WECHART_LOGIN_OUT);
            }
            UserCache userCache = userUtil.getUserInfoBySession(session);
            if(userCache == null) {
                throw new ServiceException(CodeConstant.WECHART_LOGIN_OUT);
            }
            code = RoundNumUtil.roundCode();
            userUtil.savePhoneCodeSession(session, code);
        } catch (Exception e) {
            code = null;
            logger.error("生成验证码异常", e);
        }
        return code;
    }

    @Override
    public Response bindPhone(HttpServletRequest request, UserCache userCache, String phone, String code, String serviceType) {
        Response response;
        try {
            HttpSession session = request.getSession();
            String sessCode = userUtil.getPhoneCodeSession(session);
            if (sessCode == null) {
                response = Response.error(CodeConstant.WECHAT_BIND_CODE_OVER.getValue(), CodeConstant.WECHAT_BIND_CODE_OVER.getMessage());
                return response;
            }
            if (!sessCode.equals(code)) {
                response = Response.error(CodeConstant.WECHAT_BIND_CODE_ERR.getValue(), CodeConstant.WECHAT_BIND_CODE_ERR.getMessage());
                return response;
            }
            userMapper.bindPhone(userCache.getUser_open_id(), phone);
            response = Response.success(phone);
        } catch (Exception e) {
            logger.error("绑定手机号码异常", e);
            response = Response.error(CodeConstant.WECHART_INIT_ERR.getValue(), CodeConstant.WECHART_INIT_ERR.getMessage());
        }
        return response;
    }

    public User queryUserInfoByoenId(String openId){
        User user = null;
        try {
            user = userMapper.queryUserInfoByOpenId(openId);
            logger.info("queryUserInfoByoenId user ============ " + user);
        }catch (Exception e) {
            logger.error("queryUserInfoByoenId err", e);
        }
        return user;
    }

    public UserRsp addOrUpdateUser(User user, WechatUserInfo wechatUserInfo) {
        UserRsp userRsp = new UserRsp();
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
                logger.info("修改用户成功");
            }
        } catch (Exception e) {
            userRsp = null;
            logger.error("addOrUpdateUser error", e);
        }
        return userRsp;
    }

}
