package com.zxx.wechart.store.service;

import com.zxx.wechart.store.common.Response;
import com.zxx.wechart.store.common.ServiceException;
import com.zxx.wechart.store.common.UserCache;
import com.zxx.wechart.store.common.UserRsp;
import com.zxx.wechart.store.domain.user.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 周星星
 * @DateTime: 2020/1/12 0012 14:26
 * @Description: User接口
 */
public interface UserService {

    /**
     * 用户登陆接口
     * @param request
     * @param code
     * @return
     * @throws ServiceException
     */
    UserRsp login(HttpServletRequest request, String code) throws ServiceException;

    String sendCode(HttpServletRequest request, String serviceType) throws ServiceException;

    Response bindPhone(HttpServletRequest request, UserCache userCache, String phone, String code, String serviceType) throws ServiceException;
}
