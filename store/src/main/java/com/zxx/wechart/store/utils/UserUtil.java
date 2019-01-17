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

}
