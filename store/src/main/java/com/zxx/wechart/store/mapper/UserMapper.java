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

    @Select("select * from wechat_user where user_open_id = #{openId}")
    User queryUserInfoByOpenId(@Param("openId") String openId);

    @Insert("insert into wechat_user () values ()")
    void addUserInfo(@Param("openId") String openId, @Param("createDate") Date createDate, @Param("isfllow") int isfllow, @Param("gender") int gender, @Param("nickName") String nickName, @Param("headImg") String headImg);

    @Update("update")
    void updateUserInfo(String openId, String nickName, String headImg, int gender, int isfllow);
}
