package com.zxx.wechart.store.utils;

import java.util.Random;

/**
 * @Author: 周星星
 * @DateTime: 2020/2/19 0019 15:14
 * @Description: 产生随机数
 */
public class RoundNumUtil {

    /**
     * 6位验证码
     * @return
     */
    public static String roundCode() {
        Random random = new Random();
        int x = random.nextInt(899999);
        x = x+100000;
        return x + "";
    }
}
