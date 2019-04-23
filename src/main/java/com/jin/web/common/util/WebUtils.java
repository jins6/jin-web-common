package com.jin.web.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 *@author xun.wang
 *@date 2017年10月9日 上午11:57:10
 *@remark 
 **/
public class WebUtils extends org.springframework.web.util.WebUtils {

	public static boolean isAjaxRequest(HttpServletRequest request) {
		if (request == null) {
			return false;
		}
		return (null!=request.getHeader("accept")&&request.getHeader("accept").indexOf("application/json") > -1 || (request
				.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").indexOf(
				"XMLHttpRequest") > -1));
	}
	public static boolean isGetRequest(HttpServletRequest request){
		return "GET".equalsIgnoreCase(request.getMethod());
	}
	public static boolean isMultipartRequest(HttpServletRequest request) {
		if (request == null) {
			return false;
		}
		String type=request.getContentType();
		if(StringUtils.isBlank(type)){
			return false;
		}
		return type.indexOf("multipart/form-data")>-1;
	}
}
