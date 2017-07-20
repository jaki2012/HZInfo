/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.hnac.hzinfo.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnac.hzinfo.common.config.Global;
import com.hnac.hzinfo.common.security.Digests;
import com.hnac.hzinfo.common.service.BaseService;
import com.hnac.hzinfo.common.utils.Encodes;
import com.hnac.hzinfo.modules.sys.dao.MenuDao;
import com.hnac.hzinfo.modules.sys.entity.Menu;
import com.hnac.hzinfo.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @author ThinkGem
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;


	@Autowired
	private MenuDao menuDao;

	// -- User Service --//



	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
	}

	/**
	 * 验证密码
	 * 
	 * @param plainPassword
	 *            明文密码
	 * @param password
	 *            密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Encodes.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
	}


	// -- Menu Service --//

	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu(String roleidfilter) {
		return UserUtils.getSysMenuList(roleidfilter);
	}

	@Transactional(readOnly = false)
	public boolean setRoleMenu(String roleid, String[] menuids) {
		List<Menu> rihad = this.findAllMenu(roleid);
		int totalfalse = 0;
		for (int i = 0; i < rihad.size(); i++) {
			boolean finded = false;
			for (int j = 0; j < menuids.length; j++) {
				if (rihad.get(i).getId() == menuids[j]) {
					finded = true;
					break;
				}
			}
			if (finded == false)
				if (UserUtils.unbindMenuByRole(roleid, rihad.get(i).getId()) < 0)
					totalfalse++;
		}
		for (int i = 0; i < menuids.length; i++) {
			if (menuids[i].length() == 0)
				continue;
			boolean finded = false;
			for (int j = 0; j < rihad.size(); j++) {
				if (menuids[i] == rihad.get(j).getId()) {
					finded = true;
					break;
				}
			}
			if (finded == false) {
				int ret = UserUtils.bindMenuByRole(roleid, menuids[i]);
				if (ret < 0)
					totalfalse++;
			}
		}
		if (totalfalse == 0)
			return true;
		else
			return false;
	}

	/**
	 * 获得目录集合的包括父级目录的
	 * 
	 * @author ypj
	 * @param menuList
	 * @return
	 */
	public List<Menu> getFullMenu(List<Menu> menuList) {
		List<String> parentIdList = new ArrayList<String>();
		for (Menu menu : menuList) {
			String parentIdsString = menu.getParentIds();
			String[] parentIds = parentIdsString.split(",");
			for (String parentId : parentIds) {
				if (!parentIdList.contains(parentId)) {
					parentIdList.add(parentId);
				}
			}
			if (!parentIdList.contains(menu.getId())) {
				parentIdList.add(menu.getId());
			}
		}
		return menuDao.getByIds(parentIdList);
	}

	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    欢迎使用 " + Global.getConfig("productName") + "  - Powered By http://jeesite.com\r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

	}

}
