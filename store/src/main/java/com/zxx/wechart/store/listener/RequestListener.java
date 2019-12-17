package com.zxx.wechart.store.listener;

import com.zxx.wechart.store.config.TokenUtil;
import com.zxx.wechart.store.queue.QRCodeQueue;
import com.zxx.wechart.store.utils.MenuUtil;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * 监听器，项目启动时开始监听
 */
//@WebListener
//public class RequestListener implements ServletRequestListener {
//
//    @Override
//    public void requestDestroyed(ServletRequestEvent sre) {
//        //定时获取access_token
//        TokenUtil.startTask();
//        System.out.println("============requestDestroyed===========");
//
//        //监听生成海报的事件
//        QRCodeQueue.startListen();
//        System.out.println("============监听生成海报=======");
//    }
//
//    public void requestInitialized(ServletRequestEvent sre) {
//        System.out.println("============requestInitialized=========");
//    }
//}
@Component
public class RequestListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        //定时获取access_token
        TokenUtil.startTask();
        System.out.println("============定时获取access_token===========");
        //监听生成海报的事件
//        QRCodeQueue.startListen();
//        System.out.println("============监听生成海报的事件===========");
    }
}
