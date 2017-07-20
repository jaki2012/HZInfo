package com.hnac.hzinfo.modules.hz3000data.entity;

import java.io.Serializable;

/**
 * 数据点基础类
 * 
 * @author ypj
 *
 */
public abstract class BasePoint implements Serializable {
	private static final long serialVersionUID = 6878843178471415799L;
	private String id;
	private String name;
	private String groupId;
	private String groupName;
	private String factoryId;
	private String factoryName;
	private String channelIndex;
	private String moduleIndex;
	private String addressIndex;
	private int dataType;

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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}
	
	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getChannelIndex() {
		return channelIndex;
	}

	public void setChannelIndex(String channelIndex) {
		this.channelIndex = channelIndex;
	}

	public String getModuleIndex() {
		return moduleIndex;
	}

	public void setModuleIndex(String moduleIndex) {
		this.moduleIndex = moduleIndex;
	}
	
	public String getAddressIndex() {
		return addressIndex;
	}

	public void setAddressIndex(String addressIndex) {
		this.addressIndex = addressIndex;
	}
	
	/**
	 * @return the data type
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * @param type the data type to set
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

}
