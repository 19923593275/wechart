package com.zxx.wechart.store.config;

import com.alibaba.fastjson.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

public class TokenUtil {
    public static void getToken() throws InterruptedException {
        while (true) {
            //客户端
            OkHttpClient client = null;
            //响应体
            Response response = null;
            //请求体
            Request request = null;
            try {
                //创建一个可以访问HTTPS的客户机
                client = new OkHttpClient.Builder()
                        .sslSocketFactory(SSLConfig.getSslSocketFactory(), new SSLConfig.TrustAllManager())
                        .hostnameVerifier(new SSLConfig.TrustAllHost()).build();
                //构建请求体
                request = new Request.Builder().url(WechatConfig.ACCESS_TOKEN_URL).get().build();
                //发起请求，获取响应体
                response = client.newCall(request).execute();
                if(response.isSuccessful()) {
                    String token = JSON.parseObject(response.body().string()).getString("access_token");
                    WechatConfig.setToken(token);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(response != null) {
                    response.close();
                }
                client.dispatcher().executorService().shutdown();
            }
            System.out.println("此次获取的token是:" + WechatConfig.getToken());
            //两个小时获取一次
            TimeUnit.MINUTES.sleep(115);
        }
    }

    /**
     * 开始任务
     */
    public static void startTask() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    TokenUtil.getToken();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}
