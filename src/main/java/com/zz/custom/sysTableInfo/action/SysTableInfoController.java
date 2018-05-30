package com.zz.custom.sysTableInfo.action;

import com.zz.custom.sysTableInfo.po.SysTableInfoPo;
import com.zz.custom.sysTableInfo.service.ISysTableInfoService;
import com.zz.lib.server.servlet.request.Request;
import com.zz.lib.server.servlet.response.Response;
import com.zz.lib.server.servlet.response.Result;
import com.zz.lib.spring.annotation.ZAutowried;
import com.zz.lib.spring.annotation.ZController;
import com.zz.lib.spring.annotation.ZRequestMapping;
import com.zz.lib.spring.annotation.ZRequestParam;

@ZController
@ZRequestMapping("/sysTableInfo")
public class SysTableInfoController {

    @ZAutowried
    private ISysTableInfoService sysTableInfoService;

    @ZRequestMapping("/saveOrUpdate")
    public void saveOrUpdate(Request request, Response response, SysTableInfoPo sysTableInfoPo)
            throws Exception {
        Result result = sysTableInfoService.saveOrUpdate(sysTableInfoPo);
        response.printJSON(result);
    }

    @ZRequestMapping("/delete")
    public void delete(Request request, Response response, @ZRequestParam("id") String id) throws Exception {
        Result result = sysTableInfoService.delete(id);
        response.printJSON(result);
    }

    @ZRequestMapping("/list")
    public void list(Request request, Response response) throws Exception {
        Result result = sysTableInfoService.list();
        response.printJSON(result);
    }

}