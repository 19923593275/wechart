package com.zxx.wechart.store.config;

import java.io.Serializable;

/**
 * @Author ： 周星星
 * @Date ： 2020/1/13 16:53
 * @DES : 微信用户授权信息类
 */
public class WechatUserToken implements Serializable {
    private String accessToken;//网页授权凭证
    private int expiresIn;//凭证有效时间
    private String refreshToken;//用户刷新access_token
    private String openiId;//用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
    private String scope;//用户授权的作用域，使用逗号（,）分隔

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpeniId() {
        return openiId;
    }

    public void setOpeniId(String openiId) {
        this.openiId = openiId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "WechatUserToken{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", openiId='" + openiId + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
