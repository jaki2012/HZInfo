/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.hnac.hzinfo.modules.sys.entity;

import java.beans.Transient;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
//import com.hnac.hzinfo.AMServer.AMSClient;
//import com.hnac.hzinfo.AMServer.base.RoleInfoHolder;
import com.hnac.hzinfo.common.config.Global;
import com.hnac.hzinfo.common.persistence.DataEntity;

/**
 * 角色Entity
 * @author ThinkGem
 * @version 2013-12-05
 */
public class Role extends DataEntity<Role> {
	
	private static final long serialVersionUID = 1L;
	private String id;	// 归属机构
	private String name; 	// 角色名称
	private User user;	 //该对象所属用户
	
	private List<User> userList = Lists.newArrayList(); // 拥有用户列表
	private List<Menu> menuList = Lists.newArrayList(); // 拥有菜单列表
	
	public Role() {
		super();
	}
	
	public Role(String id){
//		AMSClient client = AMSClient.getInstance();
//		RoleInfoHolder rih = new RoleInfoHolder();
//		client.GetRole(id, "", "", rih);
//		this.id = rih.value.RoleID;
//		this.name = rih.value.RoleName;
	}
	
	public Role(User user) {
		this();
		this.user = user;
	}


	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	public List<String> getUserIdList() {
		List<String> nameIdList = Lists.newArrayList();
		for (User user : userList) {
			nameIdList.add(user.getId());
		}
		return nameIdList;
	}

	public String getUserIds() {
		return StringUtils.join(getUserIdList(), ",");
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<String> getMenuIdList() {
		List<String> menuIdList = Lists.newArrayList();
		for (Menu menu : menuList) {
			menuIdList.add(menu.getId());
		}
		return menuIdList;
	}

	public void setMenuIdList(List<String> menuIdList) {
		menuList = Lists.newArrayList();
		for (String menuId : menuIdList) {
			Menu menu = new Menu();
			menu.setId(menuId);
			menuList.add(menu);
		}
	}

	public String getMenuIds() {
		return StringUtils.join(getMenuIdList(), ",");
	}
	
	public void setMenuIds(String menuIds) {
		menuList = Lists.newArrayList();
		if (menuIds != null){
			String[] ids = StringUtils.split(menuIds, ",");
			setMenuIdList(Lists.newArrayList(ids));
		}
	}
	
	/**
	 * 获取权限字符串列表
	 */
	public List<String> getPermissions() {
		List<String> permissions = Lists.newArrayList();
		for (Menu menu : menuList) {
			if (menu.getPermission()!=null && !"".equals(menu.getPermission())){
				permissions.add(menu.getPermission());
			}
		}
		return permissions;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	public static boolean isAdmin(String id){
		return id != null && "1".equals(id);
	}
	
	@Transient
	public String getMenuNames() {
		List<String> menuNameList = Lists.newArrayList();
		for (Menu menu : menuList) {
			menuNameList.add(menu.getName());
		}
		return StringUtils.join(menuNameList, ",");
	}
}
