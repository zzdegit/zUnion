package com.zz.custom.sysTableInfo.dao;

import java.util.List;

import com.zz.custom.sysTableInfo.po.SysTableInfoPo;

public interface ISysTableInfoDao {

    void insert(SysTableInfoPo param);

    SysTableInfoPo findById(String id);

    void update(SysTableInfoPo db);

    void delete(String id);

    List<SysTableInfoPo> list();


}
