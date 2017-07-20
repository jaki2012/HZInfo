package com.hnac.hzinfo.common.utils;

import java.io.Serializable;
/**
 * 
 * @author moze
 *	用于向客户端单个对象交互的场景
 */
public class ResponseModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean success = false;

	private String msg = "";

	private Object obj = null;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
}
