package com.zxx.wechart.store.common;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: 周星星
 * @DateTime: 2019/12/29 0029 19:40
 * @Description: TODO
 */
public enum  CodeConstant {
    SUUC_CODE(0,"成功"),

    //系统 10001-10999
    WECHART_INIT_ERR(10001, "业务暂停受理"),
    WECHART_BUSITYPE_NULL(10002, "业务类型为空"),
    WECHART_LOGIN_OUT(10003, "用户未登录"),
    WECHART_CODE_ERR(10004, "生成验证码失败"),
    WECHAET_CONFIG_URL_NULL(10005, "url为空"),
    WECHART_SQL_ERR(10006, "数据操作异常"),

    //用户相关 20001-20999
    WECHAT_USER_TOKEN_NULL(20001, "获取用户token失败"),
    WECHAT_USER_OPENID_NULL(20002, "用户openId为空"),
    WECHAT_USER_INFO_NULL(20003, "获取用户信息失败"),
    WECHAT_BIND_PHONE_NULL(20004, "手机号码为空"),
    WECHAT_BIND_CODE_NULL(20005, "验证码为空"),
    WECHAT_BIND_PHONE_ERR(20006, "绑定手机号码异常"),
    WECHAT_NOT_BIND_PHONE(20007, "未绑定手机号码"),
    WECHAT_BIND_CODE_OVER(20008, "验证码过期"),
    WECHAT_BIND_CODE_ERR(20009, "验证码错误"),
    WECHAT_TEST_USER_NULL(20010, "测试模式找不到当前用户"),

    //about相关 30001-30999
    WECHAT_MUSICID_NULL(30001, "音乐id为空"),
    WECHAT_COMMENT_NULL(30002, "音乐评论内容为空"),
    WECHAT_PAGE_QUERY_ERR(30003, "分页错误,超出总页数"),




    WECHAT_ZW_NULL(88888, "填空");

    /**
     * 值
     */
    private int value;

    /**
     * 给客户端返回的消息
     */
    private String message;

    /**
     * 内部记录详细日志
     */
    private String internalMsg;

    private final static Map<Integer, CodeConstant> valueMap = new ConcurrentHashMap<>();

    static {
        Arrays.stream(CodeConstant.values()).forEach(x -> {
            if(valueMap.containsKey(x.value)) {
                System.err.println("CodeConstant code重复 Name = " + x + "值=" + x.value);
            } else {
                valueMap.put(x.value, x);
            }
        });
    }

    CodeConstant(int value, String message){
        this(value,message,null);
    }

    CodeConstant(int value, String message, String internalMsg){
        this.value  = value;
        this.message = message;
        this.internalMsg = internalMsg;
    }

    public static CodeConstant valueOf(int value) {
        return valueMap.get(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMessage() {
        return message + "错误码 : " + this.value;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInternalMsg() {
        return internalMsg;
    }

    public void setInternalMsg(String internalMsg) {
        this.internalMsg = (StringUtils.isEmpty(internalMsg) ? message : internalMsg) + "错误码 : " + this.value;
    }
}
