package com.hnac.hzinfo.modules.hz3000data.entity;

import java.io.Serializable;
import java.util.Date;

public class HisDataSoe implements Serializable{
	private static final long serialVersionUID = -3968194563625652542L;
	private long id;
	private String hzrealid;
	private Date datatime;
	private String _explain;
	private int soetype;
	private int _status;
	private String optionvals;
	private int _msec;
	//以下属性为后期赋值属性
	public String factory;
	public String group;
	public String pointname;
	public String soetypename;
	public String soestatusname;
	public int optionnums;
	
	public long getId() {
		return id;
	}

	public void setId(long val) {
		this.id = val;
	}
	
	public String getHzRealid() {
		return hzrealid;
	}

	public void setHzRealid(String val) {
		this.hzrealid = val;
	}
	
	public Date getDatatime() {
		return datatime;
	}

	public void setDatatime(Date datatime) {
		this.datatime = datatime;
	}
	
	public String get_Explain() {
		return _explain;
	}

	public void set_Explain(String val) {
		this._explain = val;
	}
	
	public int getSoeType() {
		return soetype;
	}

	public void setSoeType(int val) {
		this.soetype = val;
	}
	
	public int get_Status() {
		return _status;
	}

	public void set_Status(int val) {
		this._status = val;
	}
	
	public String getOptionVals() {
		return optionvals;
	}

	public void setOptionVals(String val) {
		this.optionvals = val;
	}
	
	public int getOptionNums() {
		return optionnums;
	}

	public void setOptionNums(int num) {
		this.optionnums = num;
	}
	
	public int get_Msec() {
		return _msec;
	}

	public void set_Msec(int _msec) {
		this._msec = _msec;
	}
	
	public String get_Factory() {
		return factory;
	}
	public String get_Group() {
		return group;
	}
	public String get_PointName() {
		return pointname;
	}
	public String get_SoeTypeName() {
		return soetypename;
	}
	public String get_SoeStatusName() {
		return soestatusname;
	}
}
