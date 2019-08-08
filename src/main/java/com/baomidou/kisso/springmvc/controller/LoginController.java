/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso.springmvc.controller;

import com.baomidou.kisso.common.util.HttpUtil;
import com.baomidou.kisso.springmvc.model.User;
import com.baomidou.kisso.springmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.annotation.Action;
import com.baomidou.kisso.annotation.Login;
import com.baomidou.kisso.security.token.SSOToken;
import com.baomidou.kisso.web.waf.request.WafRequestWrapper;

/**
 * 登录
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    UserService userService;

    /**
     * 登录 （注解跳过权限验证）
     */
    @Login(action = Action.Skip)
    @RequestMapping("/login")
    public String login() {
        SSOToken st = SSOHelper.getSSOToken(request);
        String returnURL = request.getParameter("ReturnURL");
        request.setAttribute("ReturnURL", returnURL);
        if (st != null) {
            returnURL = HttpUtil.decodeURL(returnURL);
            return redirectTo(returnURL);
        }
        return "login";
    }

    /**
     * 登录 （注解跳过权限验证）
     */
    @Login(action = Action.Skip)
    @RequestMapping("/loginpost")
    public String loginpost() {
        /**
         * 生产环境需要过滤sql注入
         */
        String returnURL = request.getParameter("ReturnURL");
        WafRequestWrapper req = new WafRequestWrapper(request);
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User user = userService.getUser(username, password);
        if (user != null) {

            //记住密码，设置 cookie 时长 1 周 = 604800 秒 【动态设置 maxAge 实现记住密码功能】
            //String rememberMe = req.getParameter("rememberMe");
            //if ( "on".equals(rememberMe) ) {
            //	request.setAttribute(SSOConfig.SSO_COOKIE_MAXAGE, 604800);
            //}
            SSOHelper.setCookie(request, response,
                    SSOToken.create().setId(user.getId()).setIssuer(user.getUsername()),
                    true);// true 会销毁当前 JsessionId 如果用到了 session 相关改为 false

            returnURL = HttpUtil.decodeURL(returnURL);
            request.getSession().removeAttribute("ReturnURL");
			/*
             * 登录需要跳转登录前页面，自己处理 ReturnURL 使用
			 * HttpUtil.decodeURL(xx) 解码后重定向
			 */
			if (returnURL != null){
                return redirectTo(returnURL);
            }
            return redirectTo("/index.html");
        }
        return "login";
    }
}
