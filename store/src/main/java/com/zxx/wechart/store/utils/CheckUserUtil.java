package com.zxx.wechart.store.utils;

import com.zxx.wechart.store.common.CodeConstant;
import com.zxx.wechart.store.common.Response;
import com.zxx.wechart.store.common.UserCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author: 周星星
 * @DateTime: 2020/2/19 0019 16:16
 * @Description: 用户检测用户是否登录以及是否绑定手机号码
 */
@Component
public class CheckUserUtil {

    @Autowired
    private UserUtil userUtil;

    public Response checkUser(HttpServletRequest request, boolean isCheckPhone) {
        Response response = null;
        HttpSession session = request.getSession();
        if (session == null) {
            return Response.error(CodeConstant.WECHART_LOGIN_OUT.getValue(), CodeConstant.WECHART_LOGIN_OUT.getMessage());
        }
        UserCache userCache = userUtil.getUserInfoBySession(session);
        if (userCache == null) {
            return Response.error(CodeConstant.WECHART_LOGIN_OUT.getValue(), CodeConstant.WECHART_LOGIN_OUT.getMessage());
        }
        if (isCheckPhone) {
            if (StringUtils.isEmpty(userCache.getUser_tel())) {
                return Response.error(CodeConstant.WECHAT_NOT_BIND_PHONE.getValue(), CodeConstant.WECHAT_NOT_BIND_PHONE.getMessage());
            }
        }
        response = Response.success(userCache);
        return response;
    }

}
