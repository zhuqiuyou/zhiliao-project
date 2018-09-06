 package com.cn.thinkx.pms.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.thinkx.pms.controller.base.BaseController;
import com.cn.thinkx.pms.pageModel.base.Json;
import com.cn.thinkx.pms.pageModel.base.SessionInfo;
import com.cn.thinkx.pms.pageModel.sys.User;
import com.cn.thinkx.pms.service.sys.ResourceServiceI;
import com.cn.thinkx.pms.service.sys.UserServiceI;
import com.cn.thinkx.pms.utils.GlobalConstant;

@Controller
@RequestMapping("/admin")
public class IndexController extends BaseController {

	@Autowired
	private UserServiceI userService;
	
	@Autowired
	private ResourceServiceI resourceService;

	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		if ((sessionInfo != null) && (sessionInfo.getId() != null)) {
			return "/index";
		}
		return "/login";
	}

	@ResponseBody
	@RequestMapping("/login")
	public Json login(User user, HttpSession session) {
		Json j = new Json();
		User sysuser = userService.login(user);
		if (sysuser != null) {
			j.setSuccess(true);
			j.setMsg("登陆成功！");

			SessionInfo sessionInfo = new SessionInfo();
			sessionInfo.setId(sysuser.getId());
			sessionInfo.setLoginname(sysuser.getLoginname());
			sessionInfo.setName(sysuser.getName());
			// user.setIp(IpUtil.getIpAddr(getRequest()));
			sessionInfo.setResourceList(userService.resourceList(sysuser.getId()));
			sessionInfo.setResourceAllList(resourceService.resourceAllList());
			session.setAttribute(GlobalConstant.SESSION_INFO, sessionInfo);
		} else {
			j.setMsg("用户名或密码错误！");
		}
		return j;
	}

	@ResponseBody
	@RequestMapping("/logout")
	public Json logout(HttpSession session) {
		Json j = new Json();
		if (session != null) {
			session.invalidate();
		}
		j.setSuccess(true);
		j.setMsg("注销成功！");
		return j;
	}

}
