package com.jin.web.common.filter;

import com.jin.web.common.util.BodyReaderHttpServletRequestWrapper;
import com.jin.web.common.util.WebUtils;
import com.jin.web.common.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @description
 * @author: xun.wang
 * @date: 2017/8/3 11:05
 *
 */
//@WebFilter
public class HttpFilter implements Filter {
  private static Logger log = LoggerFactory.getLogger(HttpFilter.class);
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse resp = (HttpServletResponse) servletResponse;
    long startTime = System.currentTimeMillis();
    String reqNo=uuid();
    try {
      //转换req
      HttpServletRequest httpServletRequest =req;
      //防止文件上传问题读取body问题
      if(!WebUtils.isMultipartRequest(req)) {
        //转换req
        httpServletRequest = new BodyReaderHttpServletRequestWrapper(req);
      }
      //保存Context上下文
      RequestContext.begin(null, httpServletRequest, resp,reqNo);
      //filter放行
      filterChain.doFilter(httpServletRequest, servletResponse);
    } finally {
      if (RequestContext.get() != null) {
        RequestContext.get().end();
      }
    }
    log.info("请求序列:{} 请求url【{}】耗时【{}ms】",reqNo,req.getRequestURL().toString(),(System.currentTimeMillis() - startTime));
  }
  @Override
  public void destroy() {

  }
  private static String uuid(){
    return UUID.randomUUID().toString().replace("-", "");
  }
}
