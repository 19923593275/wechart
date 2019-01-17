package com.zxx.wechart.store.domain.user;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author ： 周星星
 * @Date ： 2020/1/15 13:47
 * @DES : 对应user用户表数据
 */
@Alias("userMap")
public class User implements Serializable {

    private String user_open_id;
    private String user_name;
    private int user_tel;
    private Date create_date;
    private int is_follow;
    private Date follow_date;
    private int gender;
    private String nick_name;
    private String head_img;

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

    public int getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(int user_tel) {
        this.user_tel = user_tel;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public Date getFollow_date() {
        return follow_date;
    }

    public void setFollow_date(Date follow_date) {
        this.follow_date = follow_date;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_open_id='" + user_open_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_tel='" + user_tel + '\'' +
                ", create_date=" + create_date +
                ", is_follow=" + is_follow +
                ", follow_date=" + follow_date +
                ", gender=" + gender +
                ", nick_name='" + nick_name + '\'' +
                ", head_img='" + head_img + '\'' +
                '}';
    }
}
