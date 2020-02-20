package com.zxx.wechart.store.utils;

import com.zxx.wechart.store.common.UserCache;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @Author ： 周星星
 * @Date ： 2020/1/13 14:05
 * @DES : 用户信息相关的Util
 */
@Component
public class UserUtil {
    public final static String SESSION_LOGIN_KEY = "user_login";
    public final static String SESSION_PHONE_CODE_KEY = "bind_phone_code";

    /**
     * 将用户信息存入session缓存中
     * @param session
     * @param userCache
     */
    public static void saveUserSession(HttpSession session, UserCache userCache){
        if (userCache == null) {
            session.removeAttribute(UserUtil.SESSION_LOGIN_KEY);
        } else {
            session.setAttribute(UserUtil.SESSION_LOGIN_KEY, userCache);
        }
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    public static UserCache getUserInfoBySession(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object obj = session.getAttribute(UserUtil.SESSION_LOGIN_KEY);
        if (obj == null) {
            return null;
        }
        return (UserCache) obj;
    }

    /**
     * 将绑定手机验证码存放到session
     * @param session
     * @param code
     */
    public static void savePhoneCodeSession(HttpSession session, String code){
        if (code == null) {
            session.removeAttribute(UserUtil.SESSION_PHONE_CODE_KEY);
        } else {
            session.setAttribute(UserUtil.SESSION_PHONE_CODE_KEY, code);
        }
    }

    /**
     * 获取绑定手机号码的验证码
     * @param session
     * @return
     */
    public static String getPhoneCodeSession(HttpSession session){
        if (session == null) {
            return null;
        }
        Object obj = session.getAttribute(UserUtil.SESSION_PHONE_CODE_KEY);
        if (obj == null) {
            return null;
        }
        return (String) obj;
    }

}
