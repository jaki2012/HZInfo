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
public class Sheet implements Serializable{
	private String reporttype;
	private List<Cell> cells;

	/**
	 * @return the reporttype
	 */
	public String getReporttype() {
		return reporttype;
	}

	/**
	 * @param reporttype
	 *            the reporttype to set
	 */
	public void setReporttype(String reporttype) {
		this.reporttype = reporttype;
	}

	/**
	 * @return the cells
	 */
	public List<Cell> getCells() {
		return cells;
	}

	/**
	 * @param cells
	 *            the cells to set
	 */
	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

}
