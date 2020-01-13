package com.zxx.wechart.store.config;

import java.io.Serializable;

/**
 * @Author ： 周星星
 * @Date ： 2020/1/13 16:53
 * @DES : 微信用户授权信息类
 */
public class WechatUserToken implements Serializable {
    private String accessToken;//网页授权凭证
    private int expre;//凭证有效时间
}
