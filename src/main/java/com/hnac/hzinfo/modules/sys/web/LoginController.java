/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.hnac.hzinfo.modules.sys.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.hnac.hzinfo.AMServer.base.UserInfo;
import com.hnac.hzinfo.common.utils.CacheUtils;
import com.hnac.hzinfo.common.utils.CookieUtils;
import com.hnac.hzinfo.common.utils.StringUtils;
import com.hnac.hzinfo.common.web.BaseController;
import com.hnac.hzinfo.modules.sys.entity.Menu;
import com.hnac.hzinfo.modules.sys.entity.Role;
import com.hnac.hzinfo.modules.sys.entity.User;
import com.hnac.hzinfo.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * 
 * @author ThinkGem
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController {

	/**
	 * 登录用于验证用户名密码
	 * 
	 * @author ypj
	 * @param account
	 * @param pwd
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, String account, String pwd) {

//		AMSClient client = AMSClient.getInstance();
//		if (client.ValidatePwd(account, pwd) <= 0) {
//			return "0";
//		}
//		User user = UserUtils.getUser(account);
//		if (user == null || user.getId() == null) {
//			return "0";
//		}
		
		return "1";
	}

	/**
	 * 登录页表单提交入口，进行表单提交后的跳转
	 * 
	 * @author ypj
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String loginFail(HttpServletRequest request, HttpServletResponse response) {
		User user = new User();
		UserInfo userInfo = new UserInfo();
		HttpSession session = request.getSession();
		List<Role> roleList = new ArrayList<Role>();
		List<Menu> menuList = new ArrayList<Menu>();
		
		session.setAttribute(UserUtils.CURRENT_USERINFO, userInfo);
	
		session.setAttribute(UserUtils.CURRENT_USER, user);
		session.setAttribute(UserUtils.CURRENT_USER_ROLE_LIST, roleList);
		session.setAttribute(UserUtils.CURRENT_USER_MENU_LIST, menuList);
		return "redirect:/index";
	}

	/**
	 * 登录页访问路径
	 */
	@RequestMapping(value = "login")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "login";
	}

	/**
	 * 登录页访问路径
	 */
	@RequestMapping(value = "toindex")
	public String toindex(HttpServletRequest request, HttpServletResponse response) {
		return "index";
	}

	/**
	 * 访问主页时进行session判断
	 * 
	 * @author ypj
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "index")
	public String toIndex(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(UserUtils.CURRENT_USER);
		if (null == user || user.getId() == null) {
			return "login";
		}
		return "index";
	}

	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request,
			HttpServletResponse response) {
		if (StringUtils.isNotBlank(theme)) {
			CookieUtils.setCookie(response, "theme", theme);
		} else {
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:" + request.getParameter("url");
	}

	/**
	 * 是否是验证码登录
	 * 
	 * @param useruame
	 *            用户名
	 * @param isFail
	 *            计数加1
	 * @param clean
	 *            计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean) {
		Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.get("loginFailMap");
		if (loginFailMap == null) {
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum == null) {
			loginFailNum = 0;
		}
		if (isFail) {
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean) {
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
}
