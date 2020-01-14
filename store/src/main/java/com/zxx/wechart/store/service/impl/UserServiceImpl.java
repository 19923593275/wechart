package com.zxx.wechart.store.service.impl;

import com.zxx.wechart.store.common.ServiceException;
import com.zxx.wechart.store.common.UserCache;
import com.zxx.wechart.store.common.UserRsp;
import com.zxx.wechart.store.service.UserService;
import com.zxx.wechart.store.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    @Override
    public UserRsp login(HttpServletRequest request, String code) throws ServiceException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserCache userCache = userUtil.getUserInfoBySession(session);
            logger.info(userCache.getUser_open_id() + "login sesson invalidate");
            session.invalidate();
        }

        session = request.getSession();
        //TODO 通过wxcode获取用户token和openid

        //TODO 通过openid获取用户信息
        return null;
    }

}
