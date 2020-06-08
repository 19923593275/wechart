package com.zxx.wechart.store.mapper;

import com.zxx.wechart.store.domain.about.CommentMusic;
import com.zxx.wechart.store.domain.about.EnjoyMusic;
import com.zxx.wechart.store.domain.about.Music;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author: 周星星
 * @DateTime: 2020/2/28 19:08
 * @Description: aaout相关操作数据库的mapper
 */
@Mapper
@Component
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public interface AboutMapper {

    List<Music> getPhbMusic();

    List<EnjoyMusic> getEnJoyMusicByOpenId(@Param("openId") String openId);

    @Insert("insert into wechat_user_like_music (user_open_id, music_id, create_date) values(#{openId}, #{musicId}, #{date})")
    void addEnjoyMusic(@Param("openId") String openId, @Param("musicId") String musicId, @Param("date") Date date);

    @Delete("delete from wechat_user_like_music where user_open_id = #{openId} and music_id = #{musicId}")
    void cancleEnjoyMusic(@Param("openId") String openId, @Param("musicId") String musicId);

    @Insert("insert into wechat_user_comment_music (comment_id, user_open_id, music_id, create_date, content) values(#{commentId}, #{openId}, #{musicId}, #{createDate, jdbcType=TIMESTAMP}, #{content})")
    void addMusicComment(@Param("commentId") String commentId, @Param("openId") String openId, @Param("musicId") String musicId, @Param("createDate") Date createDate, @Param("content") String content);

    List<CommentMusic> findPageQueryMusicComment(@Param("musicId") int musicId);
}
