package com.zxx.wechart.store.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zxx.wechart.store.config.WechatConfig;
import com.zxx.wechart.store.config.WechatUserInfo;
import com.zxx.wechart.store.config.WechatUserToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author ： 周星星
 * @Date ： 2020/1/14 13:40
 * @DES : 请求微信用户信息相关的方法
 */
public class WechatUtil {

    private static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    public static WechatUserToken getOpenIdByCode(String code){
        WechatUserToken wechatUserToken = new WechatUserToken();
        try{
            String wechatResponse = null;
            String url = WechatConfig.GET_USER_TOKEN_URL;
            url = String.format(url, WechatConfig.APPID, WechatConfig.SECRET, code);
            wechatResponse = HttpUtil.httpGet(url);
            JSONObject jsonObject = JSON.parseObject(wechatResponse);
            if (jsonObject == null) {
                return null;
            }
            logger.info("wechartTOken = =====" + jsonObject.toString());
            logger.info("openId ===" + jsonObject.getString("openid"));
            wechatUserToken.setAccessToken(jsonObject.getString("access_token"));
            wechatUserToken.setExpiresIn(jsonObject.getByteValue("expires_in"));
            wechatUserToken.setOpeniId(jsonObject.getString("openid"));
            wechatUserToken.setRefreshToken(jsonObject.getString("refresh_token"));
            wechatUserToken.setScope(jsonObject.getString("scope"));
            return wechatUserToken;
        }catch (Exception e){
            logger.error("getUserInfoByCode error", e);
            return null;
        }
    }

    public static WechatUserInfo getUserInfoByOpenId(String userToken, String openId){
        WechatUserInfo wechatUserInfo = new WechatUserInfo();
        try{
            String wechatResponse = null;
            String url = WechatConfig.GET_CODE_INFO_URL;
            url = url.replace("ACCESS_TOKEN", userToken);
            url = url.replace("OPENID", openId);
            wechatResponse = HttpUtil.httpGet(url);
            JSONObject jsonObject = JSON.parseObject(wechatResponse);
            if (jsonObject == null) {
                return null;
            }
            wechatUserInfo.setCity(jsonObject.getString("city"));
            wechatUserInfo.setCountry(jsonObject.getString("country"));
            wechatUserInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
            wechatUserInfo.setNickName(jsonObject.getString("nickname"));
            wechatUserInfo.setOpenId(jsonObject.getString("openid"));
//            wechatUserInfo.setPrivilege(jsonObject.getJSONArray());
            wechatUserInfo.setSex(jsonObject.getInteger("sex"));
            wechatUserInfo.setUnionid(jsonObject.getString("unionid"));
            return wechatUserInfo;
        } catch (Exception e) {
            logger.error("getUserInfoByOpenId error", e);
            return null;
        }
    }

    public static WechatUserInfo getBasicUserInfoByOpenId(String openId) {
        WechatUserInfo wechatUserInfo = new WechatUserInfo();
        try {
            String wechatResponse = null;
            String url = WechatConfig.GET_BASIC_INFO_URL;
            if (StringUtils.isEmpty(WechatConfig.getToken())) {
                logger.info("WechatUserInfo WechatConfig.getToken为空");
                return null;
            }
            url = url.replace("ACCESS_TOKEN", WechatConfig.getToken());
            url = url.replace("OPENID", openId);
            wechatResponse = HttpUtil.httpGet(url);
            logger.info("getBasicUserInfoByOpenId result ==== " + wechatResponse);
            JSONObject jsonObject = JSON.parseObject(wechatResponse);
            if (jsonObject == null) {
                return null;
            }
            wechatUserInfo.setSex(jsonObject.getInteger("sex"));
            wechatUserInfo.setOpenId(jsonObject.getString("openid"));
            wechatUserInfo.setNickName(jsonObject.getString("nickname"));
            wechatUserInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
            wechatUserInfo.setCountry(jsonObject.getString("country"));
            wechatUserInfo.setCity(jsonObject.getString("city"));
            wechatUserInfo.setProvince(jsonObject.getString("province"));
            wechatUserInfo.setSubscribe(jsonObject.getInteger("subscribe"));
            return wechatUserInfo;
        }catch (Exception e) {
            logger.error("getBasicUserInfoByOpenId error", e);
            return null;
        }
    }

}
