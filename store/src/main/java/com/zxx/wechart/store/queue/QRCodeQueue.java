package com.zxx.wechart.store.queue;

import com.zxx.wechart.store.config.WechatConfig;
import com.zxx.wechart.store.utils.PosterUtil;
import com.zxx.wechart.store.utils.QRCodeUtil;
import com.zxx.wechart.store.utils.UploadUtil;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Author: 周星星
 * @DateTime: 2019/12/14 0014 16:23
 * @Description: 阻塞队列，用于监听生成海报的事件
 */
public class QRCodeQueue {

    //存放pienid的阻塞队列。openid即微信推送的数据中的FromUserName
    public static BlockingQueue<String> codeQueue = new LinkedBlockingDeque<>();
    //监听队列的线程数量,这里开启15个线程去处理(并不是越多越好),提高吞吐量
    public static final int THREADS = 15;

    public static void startListen() {
        for (int i=0; i< THREADS; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String openId = codeQueue.take();
                            //创建二维码将用户的openid作为参数,用户后期数据消费,同时获取ticket
                            String ticket = QRCodeUtil.createQRCode(openId);
                            //生成海报 背景图
                            File file = PosterUtil.createPoster(WechatConfig.GET_QRCODE_URL+ "?ticket=" + ticket,
                                    "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3172678312,3965332525&fm=26&gp=0.jpg");
                            //将海报上传至素材
                            String mediaId = UploadUtil.uploadImage(file);
                            System.out.println("上传海报成功!" + mediaId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            new Thread(runnable).run();
        }
    }

}
