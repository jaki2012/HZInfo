/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.hnac.hzinfo.modules.sys.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.hnac.hzinfo.common.utils.SpringContextHolder;
import com.hnac.hzinfo.modules.sys.dao.MenuDao;
import com.hnac.hzinfo.modules.sys.entity.Menu;
import com.hnac.hzinfo.modules.sys.service.SystemService;

/**
 * 用户工具类
 * 
 * @author ThinkGem
 * @version 2013-12-05
 */
public class UserUtils {

	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static SystemService systemService = SpringContextHolder.getBean(SystemService.class);

	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ACCOUNT_ = "account_";
	public static final String USER_CACHE_NAME_ = "name_";
	public static final String CURRENT_USER = "currentUser";
	public static final String CURRENT_USERINFO = "currentUserInfo";
	

	public static final String CURRENT_USER_ROLE_LIST = "currentUserRoleList";
	public static final String CURRENT_USER_MENU_LIST = "currentUserMenuList";


	/**
	 * 获取所有菜单
	 * 
	 * @return
	 */
	public static List<Menu> getSysMenuList(String roleidfilter) {

		List<Menu> menuList = new ArrayList<Menu>();
		if (Strings.isNullOrEmpty(roleidfilter)) {
			// 目录列表
			menuList = menuDao.findAllList(new Menu());
		} else {
			// 指定用户目录列表
			menuList = menuDao.findByRoleId(roleidfilter);
		}
		return menuList;
	}

	public static List<Menu> getSysMenuListByRole(String roleidfilter) {

		List<Menu> menuList = menuDao.findByRoleId(roleidfilter);
		return menuList;
	}

	public static int bindMenuByRole(String roleid, String menuid) {
		return menuDao.bindMenuByRole(roleid, menuid);
	}

	public static int unbindMenuByRole(String roleid, String menuid) {
		return menuDao.unbindMenuByRole(roleid, menuid);
	}

	public static String getUserNameBySession(){
		return "胡晓Huxiao";
	}

	public static boolean acquirePermission(String username){
		if("胡晓Huxiao".equals(username)){
			return true;
		} else {
			return false;
		}
	}

	

}
