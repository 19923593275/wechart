package com.zxx.wechart.store.common;

import java.util.Arrays;

/**
 * @Author: 周星星
 * @DateTime: 2019/12/29 0029 19:41
 * @Description: 产品说明
 */
public class Product {

    public enum ProductId{
        WechatLife("9527");

        private String weChat = "0";

        private ProductId(String weChat) {
            this.weChat = weChat;
        }

        public String getWeChat() {
            return this.weChat;
        }

        public String weChat(){
            return this.weChat;
        }

        public static ProductId findProduct(String _weChat) {
            return Arrays.stream(ProductId.values()).filter(x -> x.weChat.equals(_weChat)).findAny().orElse(null);
        }
    }

}
