package com.zxx.wechart.store.utils;

import com.zxx.wechart.store.config.SSLConfig;
import com.zxx.wechart.store.config.WechatConfig;
import okhttp3.*;

/**
 * @Author: 周星星
 * @DateTime: 2019/12/14 0014 17:56
 * @Description: 客户工具类 用于给用户推送消息
 */
public class ServiceUtil {

    /**
     * 创建客服
     */
    public static void createService() {
        OkHttpClient client = null;
        Request request = null;
        Response response = null;
        try{
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(SSLConfig.getSslSocketFactory(), new SSLConfig.TrustAllManager())
                    .hostnameVerifier(new SSLConfig.TrustAllHost())
                    .build();
            String param = "{\"kf_account\":\"service-1\",\"nickname\" : \"客服1\",\"password\" : \"pswd\"}";
            RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), param);
            request = new Request.Builder().url(WechatConfig.ADD_SERVICE_URL + "?access_token=" + WechatConfig.getToken())
                    .post(body).build();
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createService();
    }

    public static void sendImage(String openId, String mediaId) {
        OkHttpClient client = null;
        Request request = null;
        Response response = null;
        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(SSLConfig.getSslSocketFactory(), new SSLConfig.TrustAllManager())
                    .hostnameVerifier(new SSLConfig.TrustAllHost())
                    .build();
            String param = "{\"touser\":\""+openId+"\",\"msgtype\":\"image\",\"image\":{\"media_id\":\""+mediaId+"\"}}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), param);
            request = new Request.Builder().url(WechatConfig.SEND_MESSAGE_URL+"?access_token=" + WechatConfig.getToken())
                    .post(requestBody)
                    .build();
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println("发送成功");
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(response == null) {
                response.close();
            }
            client.dispatcher().executorService().shutdown();
        }
    }
}
