package com.zxx.wechart.store.config;

import com.alibaba.fastjson.JSON;
import com.zxx.wechart.store.utils.HttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

public class TokenUtil {
    public static String getToken() {
//        while (true) {
//            //客户端
//            OkHttpClient client = null;
//            //响应体
//            Response response = null;
//            //请求体
//            Request request = null;
//            try {
//                //创建一个可以访问HTTPS的客户机
//                client = new OkHttpClient.Builder()
//                        .sslSocketFactory(SSLConfig.getSslSocketFactory(), new SSLConfig.TrustAllManager())
//                        .hostnameVerifier(new SSLConfig.TrustAllHost()).build();
//                //构建请求体
//                request = new Request.Builder().url(WechatConfig.ACCESS_TOKEN_URL).get().build();
//                //发起请求，获取响应体
//                response = client.newCall(request).execute();
//                if(response.isSuccessful()) {
//                    String token = JSON.parseObject(response.body().string()).getString("access_token");
//                    WechatConfig.setToken(token);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if(response != null) {
//                    response.close();
//                }
//                client.dispatcher().executorService().shutdown();
//            }
//            System.out.println("此次获取的token是:" + WechatConfig.getToken());
//            //两个小时获取一次
//            TimeUnit.MINUTES.sleep(115);
//        }
        String token = null;
        String json = HttpUtil.httpGet(WechatConfig.ACCESS_TOKEN_URL);
        if (json != null) {
            token = JSON.parseObject(json).getString("access_token");
            WechatConfig.setToken(token);
        }
        System.out.println("此次获取的token是:" + WechatConfig.getToken());
        return token;
    }

    public static String getTicket() {
        String ticket = null;
        String url = WechatConfig.GET_JS_SDK_TICKET_URL;
        url = url.replace("ACCESS_TOKEN", WechatConfig.getToken());
        String json = HttpUtil.httpGet(url);
        if (json != null) {
            ticket = JSON.parseObject(json).getString("ticket");
        }
        return ticket;
    }

    public static void getTokenAndTicket() {
        while (true) {
            try {
                String token = TokenUtil.getToken();
                String ticket = null;
                if (token != null) {
                    ticket = TokenUtil.getTicket();
                    if (ticket != null) {
                        WechatConfig.setToken(token);
                        WechatConfig.setTicket(ticket);
                        TimeUnit.MINUTES.sleep(115);
                    } else {
                        TimeUnit.SECONDS.sleep(3);
                    }
                } else {
                    TimeUnit.SECONDS.sleep(3);
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch ( Exception e1) {
                    e1.printStackTrace();
                }
            }
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
                    TokenUtil.getTokenAndTicket();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}
