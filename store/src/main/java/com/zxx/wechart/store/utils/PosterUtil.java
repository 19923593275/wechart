package com.zxx.wechart.store.utils;

import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

/**
 * @Author: 周星星
 * @DateTime: 2019/12/14 0014 15:54
 * @Description: 生成海报工具类
 */
public class PosterUtil {

    @Value("poster.img.addr")
    private static String posterAddr;

    public static File createPoster(String qrcodeUrl, String backgroundUrl) {
        //最终的海报图
        BufferedImage posterImg = new BufferedImage(530, 950, BufferedImage.TYPE_INT_RGB);
        //二维码图片
        BufferedImage qrcodeImg;
        //海报背景图
        BufferedImage backgroundImg;
        //File对象，将生成的海报保存到这个随机文件名，最后再返回给调用者
        File file = new File(posterAddr +"\\" + System.currentTimeMillis() + ".jpg");
        try {
            //读取二维码图片
            qrcodeImg = ImageIO.read(new URL(qrcodeUrl));
            backgroundImg = ImageIO.read(new URL(backgroundUrl));

            Graphics graphics = posterImg.createGraphics();//开启画图
            graphics.drawImage(backgroundImg.getScaledInstance(550, 978, Image.SCALE_DEFAULT),0, 0, null);
            graphics.drawImage(qrcodeImg.getScaledInstance(126, 126, Image.SCALE_DEFAULT), 47, 817, null);
            graphics.setColor(Color.black);
            graphics.dispose();
            ImageIO.write(posterImg, "jpg", file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return file;
    }

}
