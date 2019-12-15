package com.zxx.wechart.store.utils;

import com.alibaba.fastjson.JSON;
import com.zxx.wechart.store.config.SSLConfig;
import com.zxx.wechart.store.config.WechatConfig;
import okhttp3.*;

/**
 * @Author: 周星星
 * @DateTime: 2019/12/14 0014 15:43
 * @Description: 创建二维码工具类
 */
public class QRCodeUtil {

    /**
     * 创建二维码
     * @param param 二维码参数
     * @return 获取到的ticket 调用换取二维码图片的接口时，需要用到ticket
     */
    public static String createQRCode(String param) {
        OkHttpClient client = null;
        Request request = null;
        Response response = null;
        String ticket = "";
        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(SSLConfig.getSslSocketFactory(), new SSLConfig.TrustAllManager())
                    .hostnameVerifier(new SSLConfig.TrustAllHost()).build();
            //请求体参数，JSON字符串，scene_str的value就是我们要放到二维码中的参数
            String body = "{\"expire_seconds\": 604800, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+param+"\"}}}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), body);
            //实例化请求对象
            request = new Request.Builder().url(WechatConfig.CREATE_QRCODE_URL+"?access_token=" + WechatConfig.getToken())
                    .post(requestBody).build();
            //发起请求获取响应体
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ticket = JSON.parseObject(response.body().string()).getString("ticket");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response == null) {
                response.close();
            }
            client.dispatcher().executorService().shutdown();
        }
        return ticket;
    }
}
