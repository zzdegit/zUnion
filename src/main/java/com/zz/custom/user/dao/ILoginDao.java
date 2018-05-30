package com.zz.custom.user.dao;

import com.zz.custom.user.po.User;

public interface ILoginDao {

    User findUserByName(String name);

}
