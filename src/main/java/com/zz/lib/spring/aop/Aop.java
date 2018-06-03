package com.zz.lib.spring.aop;

import java.lang.reflect.Method;

public interface Aop {
    // 目标方法执行前
    void doBefore(Method method, Object[] args);

    // 目标方法执行后
    void doAfter(Method method, Object[] args);

    // 目标方法发生异常
    void doException(Method method, Object[] args, Exception e);

    // 最终执行
    void doFinally(Method method, Object[] args);
}
