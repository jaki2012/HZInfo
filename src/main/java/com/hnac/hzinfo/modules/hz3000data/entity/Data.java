package com.hnac.hzinfo.modules.hz3000data.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 数据类
 * @author ypj
 *
 */
public class Data implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3968193306625634915L;
	private double value;
	private Date time;
	private BasePoint point;
	private int q;
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public BasePoint getPoint() {
		return point;
	}
	public void setPoint(BasePoint point) {
		this.point = point;
	}
	/**
	 * @return the p
	 */
	public int getQ() {
		return q;
	}
	/**
	 * @param p the p to set
	 */
	public void setQ(int q) {
		this.q = q;
	}
	
}
