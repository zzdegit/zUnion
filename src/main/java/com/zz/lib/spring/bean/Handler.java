package com.zz.lib.spring.bean;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class Handler {

    // url正则表达式的编译表示
    private Pattern pattern;

    // url对应处理的controller
    private Object controller;

    // url对应处理的方法
    private Method method;

    public Handler(Pattern pattern, Object controller, Method method) {
        this.pattern = pattern;
        this.controller = controller;
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

}
