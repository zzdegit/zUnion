package com.zz.lib.server.servlet.preset;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import com.zz.lib.constant.Constants;
import com.zz.lib.exception.BusinessException;
import com.zz.lib.server.servlet.Servlet;
import com.zz.lib.server.servlet.request.Request;
import com.zz.lib.server.servlet.response.Response;
import com.zz.lib.server.servlet.response.Result;
import com.zz.lib.spring.annotation.ZRequestParam;
import com.zz.lib.spring.bean.Handler;
import com.zz.lib.spring.core.Container;
import com.zz.lib.utils.ReflectUtils;

public class DispatcherServlet implements Servlet {

    @Override
    public void service(Request request, Response response) throws Exception {
        Handler handler = Container.getInstance().getHandlerByUrl(request.getUrl());
        if (handler == null) {
            response.printJSON(new Result(Constants.RESULT.NOT_FOUND, Constants.RESULT.NOT_FOUND_MSG));
            throw new BusinessException(Constants.RESULT.NOT_FOUND, Constants.RESULT.NOT_FOUND_MSG);
        }
        // 参数值
        List<Object> paramValueList = new ArrayList<Object>();
        // 获取方法的参数数组
        Class<?>[] paramTypes = handler.getMethod().getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            if (paramType.equals(Request.class)) {
                paramValueList.add(request);
            } else if (paramType.equals(Response.class)) {
                paramValueList.add(response);
            } else {
                if (ReflectUtils.isBasicParamType(paramType)) {
                    Annotation[][] annArr = handler.getMethod().getParameterAnnotations();
                    for (int j = 0; j < annArr.length; j++) {
                        if (i == j) {
                            boolean ifAnn = false;
                            for (Annotation ann : annArr[j]) {
                                if (ann instanceof ZRequestParam) {
                                    ifAnn = true;
                                    String paramName = ((ZRequestParam) ann).value().trim();
                                    if (StringUtils.isNotBlank(paramName)) {
                                        String paramValue = request.getParameter(paramName);
                                        paramValueList
                                                .add(ReflectUtils.parseByParamType(paramType, paramValue));
                                    }
                                    continue;
                                }
                            }
                            if (!ifAnn) {
                                paramValueList.add(ReflectUtils.getDefaultBasicParamValue(paramType));
                            }
                        }
                    }
                } else {
                    Object paramObj = paramType.newInstance();
                    BeanUtils.populate(paramObj, request.getParameters());
                    paramValueList.add(paramObj);
                }
            }
        }
        // 执行
        handler.getMethod().invoke(handler.getController(),
                paramValueList.toArray(new Object[paramValueList.size()]));
    }

}
