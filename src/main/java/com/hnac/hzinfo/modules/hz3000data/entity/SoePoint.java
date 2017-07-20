package com.hnac.hzinfo.modules.hz3000data.entity;

public class SoePoint extends BasePoint {

	private static final long serialVersionUID = 4592781482217840848L;
	private String type;
	private String alarmType;
	private String alarmSound;
	private String actionNameList;
	private String actionUnitList;
	private String groupId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getAlarmSound() {
		return alarmSound;
	}

	public void setAlarmSound(String alarmSound) {
		this.alarmSound = alarmSound;
	}

	public String getActionNameList() {
		return actionNameList;
	}

	public void setActionNameList(String actionNameList) {
		this.actionNameList = actionNameList;
	}

	public String getActionUnitList() {
		return actionUnitList;
	}

	public void setActionUnitList(String actionUnitList) {
		this.actionUnitList = actionUnitList;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
