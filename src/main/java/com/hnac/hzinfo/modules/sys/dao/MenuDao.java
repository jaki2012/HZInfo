/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.hnac.hzinfo.modules.sys.dao;

import java.util.List;

import com.hnac.hzinfo.common.persistence.CrudDao;
import com.hnac.hzinfo.common.persistence.annotation.MyBatisDao;
import com.hnac.hzinfo.modules.sys.entity.Menu;

/**
 * 菜单DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface MenuDao extends CrudDao<Menu> {

	//public List<Menu> findByParentIdsLike(Menu menu);

	//public List<Menu> findByUserId(Menu menu);
	
	//public int updateParentIds(Menu menu);
	
	//public int updateSort(Menu menu);
	
	public List<Menu> findByRoleId(String roleid);
	public List<Menu> getByIds(List<String> idList);

	public int bindMenuByRole(String roleid, String menuid);
	public int unbindMenuByRole(String roleid, String menuid);
}
