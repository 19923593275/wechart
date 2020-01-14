package com.zxx.wechart.store.utils;

import com.alibaba.fastjson.JSON;
import com.zxx.wechart.store.config.SSLConfig;
import com.zxx.wechart.store.config.WechatConfig;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author ： 周星星
 * @Date ： 2020/1/14 10:48
 * @DES : http请求工具类
 */
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * get请求
     * @param url
     * @return
     */
    public static String httpGet(String url){
        String result = null;
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
            request = new Request.Builder().url(url).get().build();
            //发起请求，获取响应体
            response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                result = response.body().string();
                logger.info("httpGet url = "+ url);
            }
        } catch (Exception e) {
            logger.error("httpGet error", e);
        } finally {
            if(response != null) {
                response.close();
            }
            client.dispatcher().executorService().shutdown();
        }
        return result;
    }

    /**
     * post请求
     * @param url
     * @param paramStr
     * @return
     */
    public static String httpPost(String url, String paramStr) {
        String result = null;
        OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(SSLConfig.getSslSocketFactory(),new SSLConfig.TrustAllManager())
                .hostnameVerifier(new SSLConfig.TrustAllHost()).build();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),paramStr);
        Request request = new Request.Builder()
                .url(WechatConfig.CREATE_MENU_URL+"?access_token=" + WechatConfig.getToken())
                .post(requestBody).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()){
                result = response.body().string();
                logger.info("httpPost url = "+ url +",param = " + paramStr);
            }
        }catch (IOException e){
            logger.error("httpPost error", e);
        }finally {
            if (response!=null){
                response.close();
            }
            client.dispatcher().executorService().shutdown();
        }
        return result;
    }
}
