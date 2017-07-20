package com.hnac.hzinfo.modules.pumpstation.entity;

public class PumpLine {
	private String stationId;
	private String unitId;
	private float length;
	private String lineModel;

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public String getLineModel() {
		return lineModel;
	}

	public void setLineModel(String lineModel) {
		this.lineModel = lineModel;
	}

}
