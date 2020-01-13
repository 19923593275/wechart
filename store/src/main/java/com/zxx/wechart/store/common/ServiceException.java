package com.zxx.wechart.store.common;

import java.io.Serializable;

/**
 * @Author ： 周星星
 * @Date ： 2020/1/13 17:05
 * @DES : 用于业务类型抛出异常
 */
public class ServiceException extends Exception implements Serializable {
    private CodeConstant codeConstant;

    public CodeConstant getCodeConstant() {
        return codeConstant;
    }

    public void setCodeConstant(CodeConstant codeConstant) {
        this.codeConstant = codeConstant;
    }

    public ServiceException(CodeConstant code){
        super(code.getInternalMsg());
        codeConstant = code;
    }

    public ServiceException(int code){
        super(CodeConstant.valueOf(code).getInternalMsg());
        codeConstant = CodeConstant.valueOf(code);
    }

    public ServiceException(CodeConstant code, String msg){
        super(msg);
        codeConstant = code;
    }

    public Response toResponse() {
        return Response.error(codeConstant, this.getMessage());
    }
}
