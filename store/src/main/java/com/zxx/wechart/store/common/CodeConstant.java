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

    WECHART_INIT_ERR(10001, "业务暂停受理");

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
