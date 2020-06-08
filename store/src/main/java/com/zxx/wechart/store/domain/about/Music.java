package com.zxx.wechart.store.domain.about;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * @Author: 周星星
 * @DateTime: 2020/2/28 19:12
 * @Description: 音乐class类 给前端返回数据格式
 */
@Alias("musicMap")
public class Music implements Serializable {
    private int music_id;
    private String music_name;
    private String music_url;
    private String music_lyric_url;
    private String singer;
    private String singer_img_url;
    private String is_chosen;
    private int like_sum;
    private int comment_sum;
    private boolean myEnjoy;
    private int xh;

    public int getMusic_id() {
        return music_id;
    }

    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public String getMusic_url() {
        return music_url;
    }

    public void setMusic_url(String music_url) {
        this.music_url = music_url;
    }

    public String getMusic_lyric_url() {
        return music_lyric_url;
    }

    public void setMusic_lyric_url(String music_lyric_url) {
        this.music_lyric_url = music_lyric_url;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSinger_img_url() {
        return singer_img_url;
    }

    public void setSinger_img_url(String singer_img_url) {
        this.singer_img_url = singer_img_url;
    }

    public String getIs_chosen() {
        return is_chosen;
    }

    public void setIs_chosen(String is_chosen) {
        this.is_chosen = is_chosen;
    }

    public int getLike_sum() {
        return like_sum;
    }

    public void setLike_sum(int like_sum) {
        this.like_sum = like_sum;
    }

    public int getComment_sum() {
        return comment_sum;
    }

    public void setComment_sum(int comment_sum) {
        this.comment_sum = comment_sum;
    }

    public boolean isMyEnjoy() {
        return myEnjoy;
    }

    public void setMyEnjoy(boolean myEnjoy) {
        this.myEnjoy = myEnjoy;
    }

    public int getXh() {
        return xh;
    }

    public void setXh(int xh) {
        this.xh = xh;
    }

    @Override
    public String toString() {
        return "Music{" +
                "music_id=" + music_id +
                ", music_name='" + music_name + '\'' +
                ", music_url='" + music_url + '\'' +
                ", music_lyric_url='" + music_lyric_url + '\'' +
                ", singer='" + singer + '\'' +
                ", singer_img_url='" + singer_img_url + '\'' +
                ", is_chosen='" + is_chosen + '\'' +
                ", like_sum=" + like_sum +
                ", comment_sum=" + comment_sum +
                ", myEnjoy=" + myEnjoy +
                ", xh=" + xh +
                '}';
    }
}
