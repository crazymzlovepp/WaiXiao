package com.waiXiao.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 
 * @author 		mym
 * @date   		2018年7月31日下午3:15:27
 * @describe	登录相关操作
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController {
	/**
	 * 
	 * @author 		mym
	 * @date   		2018年7月31日下午3:19:31
	 * @describe	
	 * @return		String
	 *
	 */
	@RequestMapping("/toLoginPage")
	public String goToLoginPage(){
			return "login";
	}
	/**
	 * 
	 * @author 		mym
	 * @date   		2018年7月31日下午10:59:09
	 * @describe	登录成功进入主页否则重定向到登录页
	 * @return		String
	 *
	 */
	@RequestMapping("/login")
	public ModelAndView login(ModelAndView model,String userName,String password,HttpServletRequest request){
		//因为后台管理不对外开放  所以此处进行伪登录
		try {
			model.setViewName("index");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
}
