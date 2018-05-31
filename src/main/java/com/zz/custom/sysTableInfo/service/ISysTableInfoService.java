package com.zz.custom.sysTableInfo.service;

import com.zz.custom.sysTableInfo.po.SysTableInfoPo;
import com.zz.lib.server.servlet.response.Result;

public interface ISysTableInfoService {

    Result saveOrUpdate(SysTableInfoPo sysTableInfoPo);

    Result toSaveOrUpdatePage(SysTableInfoPo sysTableInfoPo);
    
    Result delete(String id);

    Result list();

}
