package com.zxx.wechart.store.utils;

import com.zxx.wechart.store.config.SSLConfig;
import com.zxx.wechart.store.config.WechatConfig;
import okhttp3.*;
import org.apache.tomcat.jni.SSLConf;

import java.io.IOException;

/**
 * 创建菜单的工具类
 */
public class MenuUtil {
    //今日热点的URL，这里跳向网易新闻
    private static final String HOTSPOT = "https://www.163.com/";
    //激流勇进的URL，跳向百度百科
    private static final String GAME = "https://baike.baidu.com/item/%E6%BF%80%E6%B5%81%E5%8B%87%E8%BF%9B/66432?fr=aladdin";
    //全民冒险的URL，这里跳到吃鸡首页
    private static final String ADVENTURE = "https://gp.qq.com/main.shtml?ADTAG=media.buy.baidukeyword.fppc_HPJY_u24796905.k121990619513.a29552693737";
    //折扣专场，跳到京东
    private static final String BUY = "https://h5.m.jd.com/pc/dev/2QurYgV498yahfXFcbmXeNuQpCyQ/index.html?unionActId=31067&d=CoY67X&s=&cu=true&utm_source=home.firefoxchina.cn&utm_medium=tuiguang&utm_campaign=t_220520384_&utm_term=8a904ba935904ef1b59178369b0faca7";
    //我的订单URL，跳向当前项目中的页面
    private static final String ORDER = "http://sc2ess.natappfree.cc/home.html";

    public static void createMenu() {
        String paramStr = "{\n" +
                "  \"button\":[\n" +
                "    {\n" +
                "      \"type\":\"view\",\n" +
                "      \"name\":\"今日热点\",\n" +
                "      \"url\":\""+HOTSPOT+"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"热情一夏\",\n" +
                "      \"sub_button\":[\n" +
                "        {\n" +
                "          \"type\":\"view\",\n" +
                "          \"name\":\"激流勇进\",\n" +
                "          \"url\":\""+GAME+"\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\":\"view\",\n" +
                "          \"name\":\"全民冒险\",\n" +
                "          \"url\":\""+ADVENTURE+"\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"更多服务\",\n" +
                "      \"sub_button\":[\n" +
                "        {\n" +
                "          \"type\":\"view\",\n" +
                "          \"name\":\"我的订单\",\n" +
                "          \"url\":\""+ORDER+"\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\":\"click\",\n" +
                "          \"name\":\"生成海报\",\n" +
                "          \"key\":\"CREATE_POSTER\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\":\"view\",\n" +
                "          \"name\":\"折扣专场\",\n" +
                "          \"url\":\""+BUY+"\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

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
                System.out.println(response.body().string());
            }
        }catch (IOException e){

        }finally {
            if (response!=null){
                response.close();
            }
            client.dispatcher().executorService().shutdown();
        }
    }
}
