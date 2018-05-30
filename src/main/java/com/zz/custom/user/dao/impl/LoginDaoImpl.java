package com.zz.custom.user.dao.impl;

import com.zz.custom.user.dao.ILoginDao;
import com.zz.custom.user.po.User;
import com.zz.lib.orm.query.Query;
import com.zz.lib.spring.annotation.ZAutowried;
import com.zz.lib.spring.annotation.ZDao;

@ZDao
public class LoginDaoImpl implements ILoginDao {

    @ZAutowried
    private Query query;
    
    @Override
    public User findUserByName(String name) {
        String sql = "select * from user where name=?";
        User user = (User) query.queryUniqueRow(sql, User.class, new Object[] { name });
        return user;
    }

}
