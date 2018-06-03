package com.zz.lib.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 针对targetObject的所有methodList类进行aopList处理
 */
public class AopHandler implements InvocationHandler {
    /**
     * 针对targetObject处理的Aop
     */
    private List<Aop> aopList;

    /**
     * 目标类
     */
    private Object targetObject;

    /**
     * 针对目标类的哪些method进行aop处理
     */
    private List<Method> methodList;

    public AopHandler(List<Aop> aopList, Object targetObject, List<Method> methodList) {
        super();
        this.aopList = aopList;
        this.targetObject = targetObject;
        this.methodList = methodList;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 是否进行aop处理，默认是false
        boolean ifAopHandle = false;
        if ((null != aopList) && (aopList.size() > 0) && (null != aopList) && (aopList.size() > 0)) {
            for (Method methodItem : methodList) {
                // 针对那些targetObject类下的method跟methodList所有函数名称不一样的，直接跳过，不进行aop处理
                if (methodItem.getName().equals(method.getName())) {
                    boolean ifParameterTypesSame = true;
                    // 同一名称，还得是参数一致才行，也就是方法签名一致才能进行aop处理
                    for (int i = 0; i < methodItem.getParameterTypes().length; i++) {
                        for (int j = 0; j < method.getParameterTypes().length; j++) {
                            if (i == j) {
                                if (!methodItem.getParameterTypes()[i]
                                        .equals(method.getParameterTypes()[j])) {
                                    // 那些method在methodList里面的才进行aop处理
                                    ifParameterTypesSame = false;
                                }
                            }
                        }
                    }
                    ifAopHandle = ifParameterTypesSame;
                }
            }
        }
        if (ifAopHandle) {
            Object resultObj = null;
            try {
                for (Aop aop : aopList) {
                    aop.doBefore(method, args);
                }
                resultObj = method.invoke(targetObject, args);
                for (Aop aop : aopList) {
                    aop.doAfter(method, args);
                }
            } catch (Exception e) {
                for (Aop aop : aopList) {
                    aop.doException(method, args, e);
                }
            } finally {
                for (Aop aop : aopList) {
                    aop.doFinally(method, args);
                }
            }
            return resultObj;
        } else {
            return method.invoke(targetObject, args);
        }

    }

}
