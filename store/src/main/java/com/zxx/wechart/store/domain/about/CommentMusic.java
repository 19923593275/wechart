package com.zxx.wechart.store.domain.about;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: 周星星
 * @DateTime: 2020/3/4 16:48
 * @Description: 音乐的评论信息(包含评论人信息)class 给前端返回的数据格式
 */
@Alias("commentMusicMap")
public class CommentMusic implements Serializable {

    private String comment_id;
    private String user_open_id;
    private int music_id;
    @JsonFormat(timezone = "GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date create_date;
    private String content;
    private int laud_sum;
    private String nick_name;
    private String head_img;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getUser_open_id() {
        return user_open_id;
    }

    public void setUser_open_id(String user_open_id) {
        this.user_open_id = user_open_id;
    }

    public int getMusic_id() {
        return music_id;
    }

    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLaud_sum() {
        return laud_sum;
    }

    public void setLaud_sum(int laud_sum) {
        this.laud_sum = laud_sum;
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
        return "CommentMusic{" +
                "comment_id='" + comment_id + '\'' +
                ", user_open_id='" + user_open_id + '\'' +
                ", music_id=" + music_id +
                ", create_date=" + create_date +
                ", content='" + content + '\'' +
                ", laud_sum=" + laud_sum +
                ", nick_name='" + nick_name + '\'' +
                ", head_img='" + head_img + '\'' +
                '}';
    }
}
