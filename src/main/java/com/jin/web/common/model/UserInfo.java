package com.jin.web.common.model;
/**
 *
 *@author xun.wang
 *@date 2017年10月9日 上午11:24:01
 *@remark 
 **/

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String token;//token

	private String accountId;//登录账户表id

	private String userId;//用户id：如店员的id
	
	private String userName;//用户名称
	
	private String appId;

	private String version;//版本

	private String deviceidMd5;//设备md5
	
	private String ip;//ip

	private String eqId;//推送id

	private String status;//状态
	
	private String sysplatform;//系统平台
	
	private Date createTime;//创建时间，也是登录成功的时间


	
}
