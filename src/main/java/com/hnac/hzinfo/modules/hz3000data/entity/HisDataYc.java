package com.hnac.hzinfo.modules.hz3000data.entity;

import java.util.Date;

public class HisDataYc {
	private static final long serialVersionUID = -3968192603625634742L;
	private double val;
	private Date datatime;
	private int q;
	private int _msec;
	
	public double getVal() {
		return val;
	}

	public void setVal(double val) {
		this.val = val;
	}
	
	public Date getDatatime() {
		return datatime;
	}

	public void setDatatime(Date datatime) {
		this.datatime = datatime;
	}
	
	public int getQ() {
		return q;
	}

	public void setQ(int q) {
		this.q = q;
	}
	
	public int get_Msec() {
		return _msec;
	}

	public void set_Msec(int _msec) {
		this._msec = _msec;
	}
}
