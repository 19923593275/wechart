package com.zxx.wechart.store.utils;

import java.util.Date;

public class MessageUtil {

    /**
     * 要回复的消息
     * @param fromUser 发送方
     * @param toUser 接收方
     * @param content 回复给用户的内容
     * @return 整理好的XML文本
     * */
    public static String setMessage(String fromUser,String toUser,String content, String msgId){
        String time = new Date().getTime() + "";

        return "<xml>" +
                "  <ToUserName><![CDATA["+ toUser +"]]></ToUserName>" +
                "  <FromUserName><![CDATA["+ fromUser +"]]></FromUserName>" +
                "  <CreateTime>"+ time +"</CreateTime>" +
                "  <MsgType><![CDATA[text]]></MsgType>" +
                "  <Content><![CDATA["+ content +"]]></Content>" +
                "  <MsgId>"+ msgId +"</MsgId>" +
                "</xml>";

    }
}
