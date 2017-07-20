package com.hnac.hzinfo.common.utils;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * @author ypj
 * 适配datatables的DataGrid
 * @param <T>
 */
public class DataGridModel<T> implements Serializable {
	private static final long serialVersionUID = 8591890308861767849L;
	private long total = 0;
	private List<T> rows;
	private List<T> footer;
	public List<T> getFooter() {
		return footer;
	}
	public void setFooter(List<T> footer) {
		this.footer = footer;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
