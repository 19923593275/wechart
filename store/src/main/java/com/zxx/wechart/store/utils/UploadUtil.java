package com.zxx.wechart.store.utils;

import com.alibaba.fastjson.JSON;
import com.zxx.wechart.store.config.SSLConfig;
import com.zxx.wechart.store.config.WechatConfig;
import okhttp3.*;

import java.io.File;

/**
 * @Author: 周星星
 * @DateTime: 2019/12/14 0014 17:44
 * @Description: 上传素材的工具类
 */
public class UploadUtil {
    /**
     * 上传图片素材
     * @param file
     * @return
     */
    public static String uploadImage(File file) {
        OkHttpClient client = null;
        Request request = null;
        Response response = null;
        String mediaId = "";
        try{
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(SSLConfig.getSslSocketFactory(), new SSLConfig.TrustAllManager())
                    .hostnameVerifier(new SSLConfig.TrustAllHost()).build();
            MultipartBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("media", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                    .build();
            request = new Request.Builder().url(WechatConfig.UPLOAD_FILE_URL+ "?access_token=" + WechatConfig.getToken()+"&type=image")
                    .post(body).build();
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                //从响应体中获取media_id
                mediaId = JSON.parseObject(response.body().string()).getString("media_id");
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (response == null) {
                response.close();
            }
            client.dispatcher().executorService().shutdown();
        }
        return mediaId;
    }
}
