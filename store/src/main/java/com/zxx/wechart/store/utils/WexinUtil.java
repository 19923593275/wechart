package com.zxx.wechart.store.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Formatter;
import java.util.Map;

/**
 * @Author ： 周星星
 * @Date ： 2020/12/21 14:40
 * @DES : @TODO
 */
public class WexinUtil {

    private static Logger log = LoggerFactory.getLogger(WexinUtil.class);
    //微信的请求url
    //获取access_token的接口地址（GET） 限200（次/天）
    public final static String access_token_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpId}&corpsecret={corpsecret}";
    //获取jsapi_ticket的接口地址（GET） 限200（次/天）
    public final static String jsapi_ticket_url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESSTOKEN";

    /**
     * 2.发送https请求之获取临时素材
     * @param requestUrl
     * @param savePath  文件的保存路径，此时还缺一个扩展名
     * @return
     * @throws Exception
     */
    public static File getFile(String requestUrl, String savePath) throws Exception {
        URL url = new URL(requestUrl);
        HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();

        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);
        httpUrlConn.setUseCaches(false);
        // 设置请求方式（GET/POST）
        httpUrlConn.setRequestMethod("GET");

        httpUrlConn.connect();

        //获取文件扩展名
        String ext=getExt(httpUrlConn.getContentType());
        savePath=savePath+ext;
        System.out.println("savePath"+savePath);
        //下载文件到f文件
        File file = new File(savePath);


        // 获取微信返回的输入流
        InputStream in = httpUrlConn.getInputStream();

        //输出流，将微信返回的输入流内容写到文件中
        FileOutputStream out = new FileOutputStream(file);

        int length=100*1024;
        byte[] byteBuffer = new byte[length]; //存储文件内容

        int byteread =0;
        int bytesum=0;

        while (( byteread=in.read(byteBuffer)) != -1) {
            bytesum += byteread; //字节数 文件大小
            out.write(byteBuffer,0,byteread);

        }
        System.out.println("bytesum: "+bytesum);

        in.close();
        // 释放资源
        out.close();
        in = null;
        out=null;

        httpUrlConn.disconnect();


        return file;
    }

    /**
     * 方法名：byteToHex</br>
     * 详述：字符串加密辅助方法 </br>
     * 开发人员：souvc  </br>
     * 创建时间：2016-1-5  </br>
     * @param hash
     * @return 说明返回值含义
     * @throws 说明发生此异常的条件
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;

    }



    private static String getExt(String contentType){
        if("image/jpeg".equals(contentType)){
            return ".jpg";
        }else if("image/png".equals(contentType)){
            return ".png";
        }else if("image/gif".equals(contentType)){
            return ".gif";
        }

        return null;
    }
}
