/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.hnac.hzinfo.modules.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hnac.hzinfo.common.utils.DataGridModel;
import com.hnac.hzinfo.common.web.BaseController;
import com.hnac.hzinfo.modules.sys.entity.Menu;
import com.hnac.hzinfo.modules.sys.service.SystemService;

/**
 * 用户Controller
 * 
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemService systemService;

	@RequestMapping(value = "/roleManage")
	public String roleManage(HttpServletRequest request, HttpServletResponse response) {
		return "modules/sys/rolemanage";
	}

	@RequestMapping(value = "/menuManage")
	public String menuManage(HttpServletRequest request, HttpServletResponse response) {
		return "modules/sys/menumanage";
	}

	@RequestMapping(value = "/noticePublish")
	public String noticePublish(HttpServletRequest request, HttpServletResponse response) {
		return "modules/sys/noticepublish";
	}

	@RequestMapping(value = "/noticesList")
	public String noticesList(HttpServletRequest request, HttpServletResponse response) {
		return "modules/sys/noticeslist";
	}

	@RequestMapping(value = "/noticeDetail/{noticeID}")
	public String noticeDetail(@PathVariable("noticeID") int noticeID, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("noticeID", noticeID);
		return "modules/sys/noticeDetail";
	}

	@RequestMapping(value = "/userManage")
	public String userManage(HttpServletRequest request, HttpServletResponse response) {
		return "modules/sys/usermanage";
	}

	@RequestMapping(value = "/userEdit")
	public String userEdit(HttpServletRequest request, HttpServletResponse response, String account) {
		HttpSession hs = request.getSession();
		hs.setAttribute("userdetail", account);
		return "modules/sys/userdetail";
	}

	@RequestMapping(value = "/userAdd")
	public String userAdd(HttpServletRequest request, HttpServletResponse response) {
		HttpSession hs = request.getSession();
		hs.setAttribute("userdetail", "");
		return "modules/sys/userdetail";
	}

	@RequestMapping(value = "/getMenuList")
	@ResponseBody
	public DataGridModel<Menu> getMenuList(HttpServletRequest request, HttpServletResponse response,
			String roleidfilter) {
		try {
			response.setCharacterEncoding("UTF-8");
			List<Menu> tmpret = systemService.findAllMenu(roleidfilter);
			DataGridModel<Menu> ret = new DataGridModel<Menu>();
			ret.setTotal(tmpret.size());
			ret.setRows(tmpret);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return new DataGridModel<Menu>();
		}
	}

	@RequestMapping(value = "/setRoleMenu", method = RequestMethod.POST)
	@ResponseBody
	public boolean setRoleMenu(String roleid, String menuids) {
		try {
			String[] menuidsstr = menuids.split("\\|");
			return systemService.setRoleMenu(roleid, menuidsstr);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
