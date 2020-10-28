package com.zxx.wechart.store.utils;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @Author: 周星星
 * @DateTime: 2020/2/19 0019 15:14
 * @Description: 产生随机数
 */
@Component
public class RoundNumUtil {

    public final static String chars = "abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWSYZ0123456789";

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

    public static String createNumCode(int len) {
        if(len < 6){
            len = 6;
        }
         //数组，用于存放随机字符
         char[] chArr = new char[len];
         //为了保证必须包含数字、大小写字母
         chArr[0] = (char)('0' + RandCode.uniform(0,10));
         chArr[1] = (char)('A' + RandCode.uniform(0,26));
         chArr[2] = (char)('a' + RandCode.uniform(0,26));

         char[] codes = { '0','1','2','3','4','5','6','7','8','9',
                                  'A','B','C','D','E','F','G','H','I','J',
                                  'K','L','M','N','O','P','Q','R','S','T',
                                  'U','V','W','X','Y','Z','a','b','c','d',
                                  'e','f','g','h','i','j','k','l','m','n',
                                  'o','p','q','r','s','t','u','v','w','x',
                                  'y','z'};
         //charArr[3..len-1]随机生成codes中的字符
         for(int i = 3; i < len; i++){
             chArr[i] = codes[RandCode.uniform(0,codes.length)];
         }
         //将数组chArr随机排序
         for(int i = 0; i < len; i++){
             int r = i + RandCode.uniform(len - i);
             char temp = chArr[i];
             chArr[i] = chArr[r];
             chArr[r] = temp;
         }
         return new String(chArr);
    }

    public static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 生成一个自定义长度的随机码
     * @param len
     * @return
     */
    public static String randonString(int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int number = RandomUtils.nextInt(0, chars.length());
            sb.append(chars.charAt(number));
        }
        return sb.toString();
    }
}
