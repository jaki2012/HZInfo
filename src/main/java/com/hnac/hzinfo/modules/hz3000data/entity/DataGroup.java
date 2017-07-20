package com.hnac.hzinfo.modules.hz3000data.entity;

import java.io.Serializable;
import java.util.List;

public class DataGroup implements Serializable {
	private static final long serialVersionUID = -5427155903332937386L;
	private String name;
	private String id;
	private String factoryId;
	private boolean unlock;
	private String chn;
	private String eng;
	private List<YcPoint> telemetryPointList;
	private List<YkPoint> telecontrolPointList;
	private List<YxPoint> telesignalPointList;
	private List<SoePoint> eventPointList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	public boolean isUnlock() {
		return unlock;
	}

	public void setUnlock(boolean unlock) {
		this.unlock = unlock;
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

	public List<YcPoint> getTelemetryPointList() {
		return telemetryPointList;
	}

	public void setTelemetryPointList(List<YcPoint> telemetryPointList) {
		this.telemetryPointList = telemetryPointList;
	}

	public List<YkPoint> getTelecontrolPointList() {
		return telecontrolPointList;
	}

	public void setTelecontrolPointList(List<YkPoint> telecontrolPointList) {
		this.telecontrolPointList = telecontrolPointList;
	}

	public List<YxPoint> getTelesignalPointList() {
		return telesignalPointList;
	}

	public void setTelesignalPointList(List<YxPoint> telesignalPointList) {
		this.telesignalPointList = telesignalPointList;
	}

	public List<SoePoint> getEventPointList() {
		return eventPointList;
	}

	public void setEventPointList(List<SoePoint> eventPointList) {
		this.eventPointList = eventPointList;
	}

}
