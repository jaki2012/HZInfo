package com.hnac.hzinfo.modules.sys.entity;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/***********************************************************************
 * Module:  SysUser.java
 * Author:  tree
 * Purpose: Defines the Class SysUser
 ***********************************************************************/

/** @pdOid c2853359-5e1f-4ee7-b04b-95a6f29b4e34 */
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1075992481563570218L;
	/** @pdOid 50cce6a8-6e85-49eb-8341-85bb0cbc3b80 */
	private java.lang.String id;
	/** @pdOid 719e3a6c-a3e4-4cea-93d6-f7af19016dda */
	private java.lang.String password;
	/** @pdOid 5f227749-d55b-4562-bf94-251f8bfb3337 */
	private java.lang.String name;
	/** @pdOid 561e5520-cfe6-4c98-9104-e5a8d2f74da0 */
	private java.lang.String phone;
	/** @pdOid ea94b368-4adc-4d73-9391-ec752aca13c5 */
	private java.util.Date registTime;
	/** @pdOid ec608edb-c0e5-4537-b966-d858d59574ff */
	private java.lang.String email;
	/** @pdOid 94be5a60-943c-4c2a-92af-f10b60a2f03f */
	private java.util.Date lastLonginTime;
	/** @pdOid 75a51e9c-dd0b-476e-ae20-93a929169c3c */
	private java.lang.String remark;
	
	private List<Role> roleList = Lists.newArrayList();
	private List<Group> groupList = Lists.newArrayList();

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getPhone() {
		return phone;
	}

	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}

	public java.util.Date getRegistTime() {
		return registTime;
	}

	public void setRegistTime(java.util.Date registTime) {
		this.registTime = registTime;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.util.Date getLastLonginTime() {
		return lastLonginTime;
	}

	public void setLastLonginTime(java.util.Date lastLonginTime) {
		this.lastLonginTime = lastLonginTime;
	}

	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public List<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

}