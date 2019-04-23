package com.jin.web.common.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jin.web.common.anno.NotLogin;
import com.jin.web.common.enums.ResultCodeMsgEnum;
import com.jin.web.common.model.Response;
import com.jin.web.common.model.UserInfo;
import com.jin.web.common.service.IUserSecurityService;
import com.jin.web.common.util.NetworkUtil;
import com.jin.web.common.util.WebUtils;
import com.jin.web.common.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by xun.wang
 *
 */
public class UserSecurityInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(UserSecurityInterceptor.class);

    private IUserSecurityService<UserInfo> userSecurityService;
	//是否过滤验证token
	private boolean isSafe = true;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            //保存请求参数和body参数
            String bodyString="";
            if(!WebUtils.isMultipartRequest(request)) {
                bodyString=RequestContext.getRequestPayload(request);
            }
            log.info("请求序列:{} ,url:{} ,请求参数:{} ,body参数:{}", RequestContext.get().getReqNo(),
                    request.getRequestURL(),JSONObject.toJSONString(request.getParameterMap()), bodyString);

            boolean needLogin = AnnotationUtils.findAnnotation(hm.getMethod(), NotLogin.class) != null || AnnotationUtils.findAnnotationDeclaringClass(NotLogin.class, hm.getBean().getClass()) != null;
            if (!needLogin) {
                Response respMsg = validateRequest(request);
                if (!StringUtils.equals(ResultCodeMsgEnum.SUCCESS.getCode(), ResultCodeMsgEnum.SUCCESS.getCode())) {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setCharacterEncoding("utf-8");
                    //返回客户端信息
                    response.getWriter().write(JSON.toJSONString(respMsg));
                    return false;
                }
            }
        }
        return true;
    }

    private Response validateRequest(HttpServletRequest request) throws IOException {
        if (null == userSecurityService) {
            log.info("userSecurityService为空，请实现userSecurityService...");
            return Response.buildMsg(ResultCodeMsgEnum.FAILURE500);
        }
        String token = request.getHeader("token");//token
        if (StringUtils.isBlank(token)) {
            return Response.buildMsg(ResultCodeMsgEnum.FAILURE10010, "token不能为空");
        }
        String deviceidmd5 = request.getHeader("deviceidmd5");//设备md5
        if (StringUtils.isBlank(deviceidmd5)) {
            return Response.buildMsg(ResultCodeMsgEnum.FAILURE10010, "deviceidmd5不能为空");
        }
        //获取用户
        UserInfo user = userSecurityService.getUserByToken(token);
        log.info("验证token：{},ip:{}，设备：{}，用户信息：{}", token, NetworkUtil.getIpAddress(request), deviceidmd5, JSON.toJSON(user));
        if (null == user) {
            return Response.buildMsg(ResultCodeMsgEnum.FAILURE41001);
        }
        if (!deviceidmd5.equals(user.getDeviceidMd5())) {//设备校验错误
            return Response.buildMsg(ResultCodeMsgEnum.FAILURE41001);
        }
        Response respMsg = new Response();
        RequestContext.get().setUser(user);//存到线程上下文
        return respMsg;
    }

    public UserSecurityInterceptor(IUserSecurityService userSecurityService) {
        super();
        this.userSecurityService = userSecurityService;
    }
    
    public void setSafe(boolean safe) {
        isSafe = safe;
    }

    public boolean isSafe() {
        return isSafe;
    }
}
