package com.zxx.wechart.store.domain.about;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: 周星星
 * @DateTime: 2020/2/29 12:06
 * @Description: 喜欢的音乐class 给前端返回的数据格式
 */
@Alias("enjoyMusicMap")
public class EnjoyMusic implements Serializable {
    private int music_id;
    private String user_open_id;
    private Date create_date;

    public int getMusic_id() {
        return music_id;
    }

    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }

    public String getUser_open_id() {
        return user_open_id;
    }

    public void setUser_open_id(String user_open_id) {
        this.user_open_id = user_open_id;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    @Override
    public String toString() {
        return "EnjoyMusic{" +
                "music_id=" + music_id +
                ", user_open_id='" + user_open_id + '\'' +
                ", create_date=" + create_date +
                '}';
    }
}
