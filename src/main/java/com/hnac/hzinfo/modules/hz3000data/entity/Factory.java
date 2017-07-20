package com.hnac.hzinfo.modules.hz3000data.entity;

import java.io.Serializable;
import java.util.List;

public class Factory implements Serializable {
	private static final long serialVersionUID = -4443313438904921892L;
	private String id;
	private String name;
	private String stationNum;
	private String theGroupId;
	private String theGroupNum;
	private List<DataGroup> groups;
	private String chn;
	private String eng;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}

	public String getTheGroupId() {
		return theGroupId;
	}

	public void setTheGroupId(String theGroupId) {
		this.theGroupId = theGroupId;
	}

	public String getTheGroupNum() {
		return theGroupNum;
	}

	public void setTheGroupNum(String theGroupNum) {
		this.theGroupNum = theGroupNum;
	}

	public List<DataGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<DataGroup> groups) {
		this.groups = groups;
	}

	public String getChn() {
		return chn;
	}

	public void setChn(String chn) {
		this.chn = chn;
	}

	public String getEng() {
		return eng;
	}

	public void setEng(String eng) {
		this.eng = eng;
	}
}
