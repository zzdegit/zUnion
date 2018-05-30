package com.zz.custom.user.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.zz.custom.user.dao.ILoginDao;
import com.zz.custom.user.po.User;
import com.zz.custom.user.service.ILoginService;
import com.zz.lib.spring.annotation.ZAutowried;
import com.zz.lib.spring.annotation.ZService;

@ZService
public class LoginServiceImpl implements ILoginService {

    @ZAutowried
    private ILoginDao loginDao;

    @Override
    public Map<String, Object> login(String name, String pwd) {
        User user = loginDao.findUserByName(name);
        Map<String, Object> params = new HashMap<String, Object>();
        if (null == user) {
            params.put("msg", "账号未注册");
        } else if (!pwd.equals(user.getPwd())) {
            params.put("msg", "密码错误");
        } else {
            params.put("msg", "登录成功");
        }
        return params;
    }

}
