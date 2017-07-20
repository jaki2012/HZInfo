/**
 * 
 */
package com.hnac.hzinfo.modules.hz3000data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author ypj 2016年12月15日
 */
@SuppressWarnings("serial")
public class WorkBook implements Serializable{
	private String datetime;
	private List<Sheet> sheets;

	/**
	 * @return the datetime
	 */
	public String getDatetime() {
		return datetime;
	}

	/**
	 * @param datetime
	 *            the datetime to set
	 */
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	/**
	 * @return the sheets
	 */
	public List<Sheet> getSheets() {
		return sheets;
	}

	/**
	 * @param sheets
	 *            the sheets to set
	 */
	public void setSheets(List<Sheet> sheets) {
		this.sheets = sheets;
	}

}
