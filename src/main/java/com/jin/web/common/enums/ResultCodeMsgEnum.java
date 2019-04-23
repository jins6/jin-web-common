package com.jin.web.common.enums;

import lombok.Getter;

/**
 * 响应码枚举类
 * 
 * @author xun.wang
 * @version $
 */
@Getter
public enum ResultCodeMsgEnum {

    SUCCESS("200", "成功"),
    SUCCESS21001("21001", "验签通过"),
    FAILURE10010("10010", "参数为空"),
    FAILURE40204("40204", "请求参数异常"),
    FAILURE41001("41001", "签名验证不通过"),
    FAILURE41002("41002", "访问接口失败"),
    FAILURE41003("41003", "校验不通过"),
    FAILURE500("500", "系统异常");

    /**
     * 错误码
     */
    private String code;
    
    /**
     * 错误描述
     */
    private String msg;

    private ResultCodeMsgEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
