/**
 * 
 */
package com.hnac.hzinfo.modules.hz3000data.entity;

import java.io.Serializable;

/**
 * @author ypj 2016年12月15日
 */
@SuppressWarnings("serial")
public class Cell implements Serializable{
	private String cellid;
	private int rowindex;
	private int columnindex;
	private String realid;
	private int starttime;
	private int duration;
	private double diffmin;
	private String dataquerytype;
	private String DateTimeType;
	public double DBValue;
	public boolean DBSorted;

	/**
	 * @return the cellid
	 */
	public String getCellid() {
		return cellid;
	}

	/**
	 * @param cellid
	 *            the cellid to set
	 */
	public void setCellid(String cellid) {
		this.cellid = cellid;
	}

	/**
	 * @return the rowindex
	 */
	public int getRowindex() {
		return rowindex;
	}

	/**
	 * @param rowindex
	 *            the rowindex to set
	 */
	public void setRowindex(int rowindex) {
		this.rowindex = rowindex;
	}

	/**
	 * @return the columnindex
	 */
	public int getColumnindex() {
		return columnindex;
	}

	/**
	 * @param columnindex
	 *            the columnindex to set
	 */
	public void setColumnindex(int columnindex) {
		this.columnindex = columnindex;
	}

	/**
	 * @return the realid
	 */
	public String getRealid() {
		return realid;
	}

	/**
	 * @param realid
	 *            the realid to set
	 */
	public void setRealid(String realid) {
		this.realid = realid;
	}

	/**
	 * @return the starttime
	 */
	public int getStarttime() {
		return starttime;
	}

	/**
	 * @param starttime
	 *            the starttime to set
	 */
	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return the diffmin
	 */
	public double getDiffmin() {
		return diffmin;
	}

	/**
	 * @param diffmin
	 *            the diffmin to set
	 */
	public void setDiffmin(double diffmin) {
		this.diffmin = diffmin;
	}

	/**
	 * @return the dataquerytype
	 */
	public String getDataquerytype() {
		return dataquerytype;
	}

	/**
	 * @param dataquerytype
	 *            the dataquerytype to set
	 */
	public void setDataquerytype(String dataquerytype) {
		this.dataquerytype = dataquerytype;
	}

	/**
	 * @return the dateTimeType
	 */
	public String getDateTimeType() {
		return DateTimeType;
	}

	/**
	 * @param dateTimeType
	 *            the dateTimeType to set
	 */
	public void setDateTimeType(String dateTimeType) {
		DateTimeType = dateTimeType;
	}

}
