package com.zz.lib.spring.core;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.zz.lib.configuration.Configuration;
import com.zz.lib.constant.Constants;
import com.zz.lib.orm.query.Query;
import com.zz.lib.orm.query.QueryFactory;
import com.zz.lib.spring.annotation.ZAop;
import com.zz.lib.spring.annotation.ZAutowried;
import com.zz.lib.spring.annotation.ZCompont;
import com.zz.lib.spring.annotation.ZController;
import com.zz.lib.spring.annotation.ZDao;
import com.zz.lib.spring.annotation.ZPo;
import com.zz.lib.spring.annotation.ZRequestMapping;
import com.zz.lib.spring.annotation.ZService;
import com.zz.lib.spring.aop.Aop;
import com.zz.lib.spring.aop.AopHandler;
import com.zz.lib.spring.bean.Handler;

public class Container {

    // 所有被扫描进容器的类名
    private List<String> classNameList;

    // ioc容器
    private Map<String, Object> IOC;

    // 处理url
    private List<Handler> handlerList;

    private static class ContainerSingleton {
        private static final Container instance = new Container();
    }

    public static Container getInstance() {
        return ContainerSingleton.instance;
    }

    private Container() {
        this.classNameList = new ArrayList<String>();
        this.IOC = new HashMap<String, Object>();
        this.handlerList = new ArrayList<Handler>();
    }

    public void init() {
        // 1.扫描scanPackage下的类
        this.scanClass(Configuration.getInstance().getScanPackage());
        // 3.实例化并初始化IOC容器
        this.initIOC();
        // 4.默认自动扫描
        this.initSystemIOC();
        // 5.自动依赖注入
        this.autoAutowried();
        // 6.初始化handlerList
        this.initHandlerList();
    }

    /**
     * 初始化handlerList
     */
    private void initHandlerList() {
        if ((null == IOC) || (IOC.isEmpty())) {
            return;
        }
        Iterator<String> it = IOC.keySet().iterator();
        while (it.hasNext()) {
            Object instance = IOC.get(it.next());
            Class<?> clazz = instance.getClass();
            if (!clazz.isAnnotationPresent(ZController.class)) {
                continue;
            }
            String url = "";
            if (clazz.isAnnotationPresent(ZRequestMapping.class)) {
                ZRequestMapping ann = clazz.getAnnotation(ZRequestMapping.class);
                url += ann.value();
            }
            Method[] methodArr = clazz.getMethods();
            for (Method method : methodArr) {
                if (!method.isAnnotationPresent(ZRequestMapping.class)) {
                    continue;
                }
                ZRequestMapping ann = method.getAnnotation(ZRequestMapping.class);
                Pattern pattern = Pattern.compile(("/" + url + "/" + ann.value()).replaceAll("/+", "/"));
                handlerList.add(new Handler(pattern, instance, method));
            }
        }

    }

    /**
     * 自动依赖注入
     */
    private void autoAutowried() {
        if ((null == IOC) || (IOC.isEmpty())) {
            return;
        }
        // 为了效率提前从容器查出所有aop
        List<Aop> aopList = this.getObjectListFromIOCByAnnotationClass(ZAop.class);
        Iterator<String> it = IOC.keySet().iterator();
        while (it.hasNext()) {
            // 被注入的对象
            Object passiveObject = IOC.get(it.next());
            // 获取所有的字段field 全部都注入
            Field[] passiveObjectFieldArr = passiveObject.getClass().getDeclaredFields();
            for (Field passiveObjectField : passiveObjectFieldArr) {
                try {
                    // 只有被ZAutowried注解的才能自动注入
                    if (!passiveObjectField.isAnnotationPresent(ZAutowried.class)) {
                        continue;
                    }
                    ZAutowried ann = passiveObjectField.getAnnotation(ZAutowried.class);
                    String activeClassName = ann.value().trim();
                    if (StringUtils.isBlank(activeClassName)) {
                        activeClassName = passiveObjectField.getType().getName();
                    }
                    // 即使是private也要注入，强制授权访问
                    passiveObjectField.setAccessible(true);

                    Object activeObject = IOC.get(activeClassName);
                    // 主动注入Class
                    Class<?> activeClass = activeObject.getClass();
                    Object autowriedObject = null;
                    /*
                     * 主动注入的是ZService说明，主动注入的是service 此时就需要知道其是否是被ZAop，注解的，若被注解就不能注入 真实的对象，而应该是代理对象
                     */
                    if (activeClass.isAnnotationPresent(ZService.class)) {
                        // 从容器中获取所有的被ZAop注解的aop
                        List<Aop> handleAopList = new ArrayList<Aop>();
                        List<Method> methodList = new ArrayList<Method>();
                        for (Aop aop : aopList) {
                            for (Method method : activeClass.getDeclaredMethods()) {
                                /*
                                 * 开始进行匹配
                                 */
                                String methodRegex = aop.getClass().getAnnotation(ZAop.class).methodRegex();
                                if (method.toString().matches(methodRegex)) {
                                    handleAopList.add(aop);
                                    methodList.add(method);
                                }
                            }
                        }
                        if ((handleAopList.size() > 0) && (methodList.size() > 0)) {
                            AopHandler aopHandler = new AopHandler(handleAopList, activeObject, methodList);
                            autowriedObject = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                                    new Class[] { passiveObjectField.getType() }, aopHandler);
                        } else {
                            autowriedObject = activeObject;
                        }
                    } else {
                        autowriedObject = activeObject;
                    }
                    passiveObjectField.set(passiveObject, autowriedObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 实例化并初始化IOC容器
     */
    private void initIOC() {
        if ((null == classNameList) || (classNameList.isEmpty())) {
            return;
        }
        try {
            for (String className : classNameList) {
                Class<?> clazz = Class.forName(className);
                String clazzName = null;
                // ZController
                if (clazz.isAnnotationPresent(ZController.class)) {
                    ZController ann = clazz.getAnnotation(ZController.class);
                    clazzName = ann.value().trim();
                }
                // ZService
                else if (clazz.isAnnotationPresent(ZService.class)) {
                    ZService ann = clazz.getAnnotation(ZService.class);
                    clazzName = ann.value().trim();
                }
                // ZDao
                else if (clazz.isAnnotationPresent(ZDao.class)) {
                    ZDao ann = clazz.getAnnotation(ZDao.class);
                    clazzName = ann.value().trim();
                }
                // ZCompont
                else if (clazz.isAnnotationPresent(ZCompont.class)) {
                    ZCompont ann = clazz.getAnnotation(ZCompont.class);
                    clazzName = ann.value().trim();
                }
                // ZPo
                else if (clazz.isAnnotationPresent(ZPo.class)) {
                    ZPo ann = clazz.getAnnotation(ZPo.class);
                    clazzName = ann.value().trim();
                }
                // 其他
                else {
                    continue;
                }
                // 如果注解没有获取到自定义名字的话，就使用类名
                if (StringUtils.isBlank(clazzName)) {
                    clazzName = clazz.getName();
                }
                // 实例化
                Object instance = clazz.newInstance();
                IOC.put(clazzName, instance);
                // 如果实现了接口的话:接口类型作为key，对象实例作为value
                Class<?>[] interfaceArr = clazz.getInterfaces();
                for (Class<?> i : interfaceArr) {
                    IOC.put(i.getName(), instance);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化系统bean，扫描进ioc
     */
    private void initSystemIOC() {
        if ((null == classNameList) || (classNameList.isEmpty()) || (null == IOC)) {
            return;
        }
        // 1.查询类Query
        Query query = QueryFactory.getInstance().createQuery();
        IOC.put(Query.class.getName(), query);
        // 2.Aop
        try {
            for (String className : classNameList) {
                Class<?> clazz = Class.forName(className);
                String clazzName = null;
                // ZAop
                if (clazz.isAnnotationPresent(ZAop.class)) {
                    clazzName = clazz.getName();
                    // 实例化
                    Object instance = clazz.newInstance();
                    IOC.put(clazzName, instance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描scanPackage下的类
     */
    private void scanClass(String scanPackage) {
        try {
            URL url = this.getClass().getClassLoader().getResource(scanPackage.replaceAll("\\.", "/"));
            File root = new File(URLDecoder.decode(url.getFile(), Constants.UTF_8));
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    this.scanClass(scanPackage + "." + file.getName());
                } else {
                    String className = scanPackage + "." + file.getName().replace(".class", "");
                    classNameList.add(className);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据Url获取Handler
     */
    public Handler getHandlerByUrl(String url) {
        if ((null != handlerList) && handlerList.isEmpty()) {
            return null;
        }
        for (Handler handler : handlerList) {
            try {
                Matcher matcher = handler.getPattern().matcher(url);
                if (!matcher.matches()) {
                    continue;
                }
                return handler;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据传入参数annotationClass(注解类型)，从IOC容器中获取对应的Class集合
     */
    public List<Class<?>> getClassListFromIOCByAnnotationClass(Class<? extends Annotation> annotationClass) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        if ((null == IOC) || (IOC.isEmpty())) {
            return classList;
        }
        Iterator<String> it = IOC.keySet().iterator();
        while (it.hasNext()) {
            Class<?> clazz = IOC.get(it.next()).getClass();
            if (clazz.isAnnotationPresent(annotationClass)) {
                classList.add(clazz);
            }
        }
        return classList;
    }

    /**
     * 根据传入参数annotationClass(注解类型)，从IOC容器中获取对应的对象集合
     */
    public <T> List<T> getObjectListFromIOCByAnnotationClass(Class<? extends Annotation> annotationClass) {
        List<T> objectList = new ArrayList<T>();
        if ((null == IOC) || (IOC.isEmpty())) {
            return objectList;
        }
        Iterator<String> it = IOC.keySet().iterator();
        while (it.hasNext()) {
            @SuppressWarnings("unchecked")
            T object = (T) IOC.get(it.next());
            if (object.getClass().isAnnotationPresent(annotationClass)) {
                objectList.add(object);
            }
        }
        return objectList;
    }
}
