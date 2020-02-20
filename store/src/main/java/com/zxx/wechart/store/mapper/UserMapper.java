package com.zxx.wechart.store.mapper;

import com.zxx.wechart.store.domain.user.User;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author ： 周星星
 * @Date ： 2020/1/15 13:58
 * @DES : 用于操作用户表相关的mapper
 */
@Mapper
@Component
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public interface UserMapper {

    User queryUserInfoByOpenId(@Param("openId") String openId);

    @Insert("insert into wechat_user (user_open_id,create_date,is_follow,follow_date,gender,nick_name,head_img) values (#{openId},#{createDate},#{isfllow},#{createDate},#{gender},#{nickName},#{headImg})")
    void addUserInfo(@Param("openId") String openId, @Param("createDate") Date createDate, @Param("isfllow") int isfllow, @Param("gender") int gender, @Param("nickName") String nickName, @Param("headImg") String headImg);

    @Update("update wechat_user set nick_name = #{nickName},head_img = #{headImg},gender = #{gender},is_follow = #{isfllow} where user_open_id = #{openId}")
    void updateUserInfo(@Param("openId") String openId, @Param("nickName") String nickName, @Param("headImg") String headImg, @Param("gender") int gender, @Param("isfllow") int isfllow);

    @Update("update wechat_user set user_tel = #{phone} where user_open_id = #{openId}")
    void bindPhone(@Param("openId") String openId, @Param("phone") String phone);
}
