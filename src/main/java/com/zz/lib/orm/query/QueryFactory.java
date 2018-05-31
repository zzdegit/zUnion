package com.zz.lib.orm.query;

import com.zz.lib.configuration.Configuration;

public class QueryFactory {

    private static Query prototype;

    // 静态内部类 类型的 单例模式
    private static class QueryFactorySingleton {
        private static final QueryFactory instance = new QueryFactory();
    }

    public static QueryFactory getInstance() {
        return QueryFactorySingleton.instance;
    }

    private QueryFactory() {
        try {
            String queryClass = Configuration.getInstance().getQueryClass();
            Class<?> clazz = Class.forName(queryClass);
            prototype = (Query)clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Query createQuery() {
        Query query = null;
        try {
            // 原型模式
            query = (Query) prototype.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return query;
    }

}
