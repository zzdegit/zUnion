package com.zz.custom.sysTableInfo.service.aop;

import java.lang.reflect.Method;

import com.zz.custom.sysTableInfo.po.SysTableInfoPo;
import com.zz.lib.spring.annotation.ZAop;
import com.zz.lib.spring.aop.Aop;

/**
 * 指定对哪个service进行aop
 */
@ZAop(methodRegex = "public.*saveOrUpdate\\(.*SysTableInfoPo\\)")
public class LogAop implements Aop {

    @Override
    public void doBefore(Method method, Object[] args) {
        System.out.println("----------");
        SysTableInfoPo sysTableInfoPo = (SysTableInfoPo) args[0];
        System.out.println("name:" + sysTableInfoPo.getName());
        System.out.println("operateTime:" + sysTableInfoPo.getOperateTime());
        System.out.println("执行doBefore。。。");
    }

    @Override
    public void doAfter(Method method, Object[] args) {
        System.out.println("----------");
        SysTableInfoPo sysTableInfoPo = (SysTableInfoPo) args[0];
        System.out.println("name:" + sysTableInfoPo.getName());
        System.out.println("operateTime:" + sysTableInfoPo.getOperateTime());
        System.out.println("执行doAfter。。。");
    }

    @Override
    public void doException(Method method, Object[] args, Exception e) {
        System.out.println("----------");
        SysTableInfoPo sysTableInfoPo = (SysTableInfoPo) args[0];
        System.out.println("name:" + sysTableInfoPo.getName());
        System.out.println("operateTime:" + sysTableInfoPo.getOperateTime());
        System.out.println("exception:" + e.getMessage());
        System.out.println("执行doException。。。");
    }

    @Override
    public void doFinally(Method method, Object[] args) {
        System.out.println("----------");
        SysTableInfoPo sysTableInfoPo = (SysTableInfoPo) args[0];
        System.out.println("name:" + sysTableInfoPo.getName());
        System.out.println("operateTime:" + sysTableInfoPo.getOperateTime());
        System.out.println("执行doFinally。。。");
    }

}
