package com.jin.web.common.model;


import com.jin.web.common.enums.ResultCodeMsgEnum;
import lombok.Data;

@Data
public class Response<T>{

    private String code;
    private String message;

    public Response() {
        this.setCode(ResultCodeMsgEnum.SUCCESS.getCode());
    }

    public static final Response buildMsg(String status,String msg){
        Response res = new Response();
        res.setCode(status);
        res.setMessage(msg);
        return res;
    }
    public static final Response buildMsg(ResultCodeMsgEnum resp){
        Response rs = new Response();
        rs.setCode(resp.getCode());
        rs.setMessage(resp.getMsg());
        return rs;
    }
    public static final Response buildMsg(ResultCodeMsgEnum re,String msg){
        Response resp=buildMsg(re);
        resp.setMessage(msg);
        return resp;
    }

}
