package com.jin.web.common.context;

import com.jin.web.common.constant.Consts;
import com.jin.web.common.model.UserInfo;
import com.jin.web.common.util.NetworkUtil;

import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

/**
 * 来自OSChina的思想
 * @description
 * @author: xun.wang
 * @date:
 */
public class RequestContext {

	private final static ThreadLocal<RequestContext> contexts = new ThreadLocal<RequestContext>();
	private String              reqNo;//每个请求生成一个序列id
	private String            	params;
	private UserInfo user;//登录用户
	private Locale               lang;
	private ServletContext      context;
	private HttpServletRequest  request;
	private HttpServletResponse response;

	public Locale getLang() {
		return lang;
	}

	public void setLang(Locale lang) {
		this.lang = lang;
	}

	public <T> T getLoginUser(Class<T> clz) {
		return (T)user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * 初始化请求上下文
	 * 
	 * @param ctx
	 * @param req
	 * @param res
	 */
	public static RequestContext begin(ServletContext ctx, HttpServletRequest req, HttpServletResponse res) {
		RequestContext rc = new RequestContext();
		rc.context = ctx;
		rc.request = req;
		rc.response = res;
		rc.response.setCharacterEncoding(Consts.CODING_UTF8);
		rc.reqNo = uuid();
		contexts.set(rc);
		return rc;
	}
	public static RequestContext begin(ServletContext ctx,
                                     HttpServletRequest req, HttpServletResponse res,String reqNo) {
		RequestContext rc = new RequestContext();
		rc.context = ctx;
		rc.request = req;
		rc.response = res;
		rc.response.setCharacterEncoding(Consts.CODING_UTF8);
		rc.reqNo= reqNo;
		contexts.set(rc);
		return rc;
	}
	public static RequestContext beginApi(HttpServletRequest req, HttpServletResponse res){
		RequestContext rc = new RequestContext();
		rc.request = req;
		rc.response = res;
		rc.response.setCharacterEncoding(Consts.CODING_UTF8);
		contexts.set(rc);
		return rc;
	}
	/**
	 * 获取当前请求的上下文
	 * 
	 * @return
	 */
	public static RequestContext get() {
		return contexts.get();
	}

	public void end() {
		this.context = null;
		this.request = null;
		this.response = null;
		contexts.remove();
	}
	public Locale locale() {
		return request.getLocale();
	}
	public void closeCache() {
		header("Pragma", "No-cache");
		header("Cache-Control", "no-cache");
		header("Expires", 0L);
	}
	public String ip() throws IOException {
		return NetworkUtil.getIpAddress(request);
	}
	public String uri() {
		return request.getRequestURI();
	}
	public String ctx() {
		return request.getContextPath();
	}
	public void redirect(String uri) throws IOException {
		response.sendRedirect(uri);
	}
	public void forward(String uri) throws ServletException, IOException {
		RequestDispatcher rd = context.getRequestDispatcher(uri);
		rd.forward(request, response);
	}
	public void include(String uri) throws ServletException, IOException {
		RequestDispatcher rd = context.getRequestDispatcher(uri);
		rd.include(request, response);
	}
	public boolean isUpload() {
		return (request instanceof MultipartRequest );
	}
	/**
	 * 输出信息到浏览器
	 * @param msg
	 * @throws IOException
	 */
	public void print(Object msg) throws IOException {
		if (!Consts.CODING_UTF8.equalsIgnoreCase(response.getCharacterEncoding()))
			response.setCharacterEncoding(Consts.CODING_UTF8);
		response.getWriter().print(msg);
	}

	public void printXml(Object msg) throws IOException {
		response.setContentType("application/xml");
		print(msg);
	}
	/**
	 * 返回错误的http状态码
	 * 
	 * @param code
	 * @param msg
	 * @throws IOException
	 */
	public void error(int code, String... msg) throws IOException {
		if (msg.length > 0) {
			response.sendError(code, msg[0]);
		}else {
			response.sendError(code);
		}
	}
	public void forbidden() throws IOException {
		error(HttpServletResponse.SC_FORBIDDEN);
	}

	public void notFound() throws IOException {
		error(HttpServletResponse.SC_NOT_FOUND);
	}
	public ServletContext context() {
		return context;
	}

	public HttpServletRequest request() {
		return request;
	}

	public HttpServletResponse response() {
		return response;
	}

	public static String getRequestPayload(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		try(BufferedReader reader = request.getReader()) {
			char[]buff = new char[1024];
			int len;
			while((len = reader.read(buff)) != -1) {
				sb.append(buff,0, len);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	public String getRequestPayload() {
		return getRequestPayload(request);
	}
	public String header(String name) {
		return request.getHeader(name);
	}
	public void header(String name, String value) {
		response.setHeader(name, value);
	}

	public void header(String name, int value) {
		response.setIntHeader(name, value);
	}

	public void header(String name, long value) {
		response.setDateHeader(name, value);
	}

	private static String uuid(){
		return UUID.randomUUID().toString().replace("-","");
	}
}
