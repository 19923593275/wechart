package com.zxx.wechart.store.common;

import java.io.Serializable;

/**
 * @Author ： 周星星
 * @Date ： 2020/1/13 13:57
 * @DES : 用户登陆session缓存数据
 */
public class UserCache implements Serializable {

    private String user_open_id;
    private String user_name;
    private String user_tel;
    private int is_follow;
    private int gengder;
    private String nick_name;
    private String userToken;

    public String getUser_open_id() {
        return user_open_id;
    }

    public void setUser_open_id(String user_open_id) {
        this.user_open_id = user_open_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public int getGengder() {
        return gengder;
    }

    public void setGengder(int gengder) {
        this.gengder = gengder;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString() {
        return "UserCache{" +
                "user_open_id='" + user_open_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_tel=" + user_tel +
                ", is_follow=" + is_follow +
                ", gengder=" + gengder +
                ", nick_name='" + nick_name + '\'' +
                ", userToken='" + userToken + '\'' +
                '}';
    }
}
