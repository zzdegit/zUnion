package com.zz.custom.user.action;

import java.util.HashMap;
import java.util.Map;

import com.zz.custom.user.po.User;
import com.zz.custom.user.service.ILoginService;
import com.zz.lib.server.servlet.request.Request;
import com.zz.lib.server.servlet.response.Response;
import com.zz.lib.spring.annotation.ZAutowried;
import com.zz.lib.spring.annotation.ZController;
import com.zz.lib.spring.annotation.ZRequestMapping;

@ZController
public class LoginController {

    @ZAutowried
    private ILoginService loginService;

    @ZRequestMapping("/login")
    public void login(Request request, Response response, User user) throws Exception {
        Map<String, Object> params = loginService.login(user.getName(), user.getPwd());
        response.printHTML("main.ftl", params);
    }
    
    @ZRequestMapping("/toLoginPage")
    public void toLoginPage(int i,Request request, Response response) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("title", "欢迎登陆");
        response.printHTML("toLoginPage.ftl", param);
    }

}
