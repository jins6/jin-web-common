package com.jin.web.common.service;

import com.jin.web.common.model.UserInfo;

/**
 *
 *@author xun.wang
 *@date 2017年10月9日 上午11:32:32
 *@remark 
 **/
public interface IUserSecurityService<T extends UserInfo> {
	/**
	 * 根据token获取用户信息
	 * @param token
	 * @return
	 */
	T getUserByToken(String token);

}
