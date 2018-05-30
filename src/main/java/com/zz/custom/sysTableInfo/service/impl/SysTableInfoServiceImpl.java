package com.zz.custom.sysTableInfo.service.impl;

import java.util.List;

import com.zz.custom.sysTableInfo.dao.ISysTableInfoDao;
import com.zz.custom.sysTableInfo.po.SysTableInfoPo;
import com.zz.custom.sysTableInfo.service.ISysTableInfoService;
import com.zz.lib.server.servlet.response.Result;
import com.zz.lib.spring.annotation.ZAutowried;
import com.zz.lib.spring.annotation.ZService;
import com.zz.lib.utils.StringUtils;

@ZService
public class SysTableInfoServiceImpl implements ISysTableInfoService {

    @ZAutowried
    private ISysTableInfoDao sysTableInfoDao;

    @Override
    public Result saveOrUpdate(SysTableInfoPo param) {
        // 增加
        if (StringUtils.isBlank(param.getId())) {
            sysTableInfoDao.insert(param);
        }
        // 修改
        else {
            SysTableInfoPo db = sysTableInfoDao.findById(param.getId());
            // name
            if (StringUtils.isNotBlank(param.getName()) && (!param.getName().equals(db.getName()))) {
                db.setName(param.getName());
            }
            // operateTime
            if ((null != param.getOperateTime()) && (!param.getOperateTime().equals(db.getOperateTime()))) {
                db.setOperateTime(param.getOperateTime());
            }
            sysTableInfoDao.update(db);
        }
        return new Result();
    }

    @Override
    public Result delete(String id) {
        sysTableInfoDao.delete(id);
        return new Result();
    }

    @Override
    public Result list() {
        List<SysTableInfoPo> list = sysTableInfoDao.list();
        return new Result(list);
    }

}
