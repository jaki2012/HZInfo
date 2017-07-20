/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.hnac.hzinfo.common.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.hnac.hzinfo.common.persistence.TreeDao;
import com.hnac.hzinfo.common.persistence.TreeEntity;
import com.hnac.hzinfo.common.utils.Reflections;
import com.hnac.hzinfo.common.utils.StringUtils;

/**
 * Service基类
 * @author ThinkGem
 * @version 2014-05-16
 */
@Transactional(readOnly = true)
public abstract class TreeService<D extends TreeDao<T>, T extends TreeEntity<T>> extends CrudService<D, T> {
	
	@Transactional(readOnly = false)
	public void save(T entity) {
		
	
	}
	
	/**
	 * 预留接口，用户更新子节前调用
	 * @param childEntity
	 */
	protected void preUpdateChild(T entity, T childEntity) {
		
	}

}
