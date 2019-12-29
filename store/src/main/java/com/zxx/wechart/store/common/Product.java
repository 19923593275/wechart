package com.zxx.wechart.store.common;

/**
 * @Author: 周星星
 * @DateTime: 2019/12/29 0029 19:41
 * @Description: 产品说明
 */
public class Product {

    public enum ProductId{
        WechatLife("9527");

        private String weChat;

        private ProductId(String weChat) {
            this.weChat = weChat;
        }

        public String getWeChat() {
            return weChat;
        }

        public void setWeChat(String weChat) {
            this.weChat = weChat;
        }
    }

}
