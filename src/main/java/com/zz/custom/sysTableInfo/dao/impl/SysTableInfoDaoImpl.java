package com.zz.custom.sysTableInfo.dao.impl;

import java.util.List;

import com.zz.custom.sysTableInfo.dao.ISysTableInfoDao;
import com.zz.custom.sysTableInfo.po.SysTableInfoPo;
import com.zz.lib.orm.query.Query;
import com.zz.lib.spring.annotation.ZAutowried;
import com.zz.lib.spring.annotation.ZDao;

@ZDao
public class SysTableInfoDaoImpl implements ISysTableInfoDao {

    @ZAutowried
    private Query query;

    @Override
    public void insert(SysTableInfoPo sysTableInfoPo) {
        query.insert(sysTableInfoPo);
    }

    @Override
    public SysTableInfoPo findById(String id) {
        return (SysTableInfoPo) query.queryPoById(SysTableInfoPo.class, id);
    }

    @Override
    public void update(SysTableInfoPo sysTableInfoPo) {
        query.update(sysTableInfoPo);
    }

    @Override
    public void delete(String id) {
        query.delete(SysTableInfoPo.class,id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SysTableInfoPo> list() {
        String sql = " select * from sys_table_info ";
        List<SysTableInfoPo> list = query.queryRows(sql, SysTableInfoPo.class, null);
        return list;
    }

}
