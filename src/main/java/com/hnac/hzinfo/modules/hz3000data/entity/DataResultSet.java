/**
 * 
 */
package com.hnac.hzinfo.modules.hz3000data.entity;

import java.util.Map;

/**
 * @author ypj
 * 2016年12月13日
 */
public class DataResultSet {
	private String status;
	private String errorMsg;
	private Map<String,Data> result ;
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}
	/**
	 * @param errorMsg the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	/**
	 * @return the result
	 */
	public Map<String,Data>  getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(Map<String,Data>  result) {
		this.result = result;
	}
	
	
}
