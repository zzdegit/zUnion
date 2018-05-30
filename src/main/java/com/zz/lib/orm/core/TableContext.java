package com.zz.lib.orm.core;

import java.util.HashMap;
import java.util.Map;

import com.zz.lib.configuration.Configuration;
import com.zz.lib.orm.bean.TableInfo;
import com.zz.lib.orm.utils.JDBCUtils;
import com.zz.lib.utils.StringUtils;

/**
 * 获取管理数据库所有表结构和类结构的关系
 */
public class TableContext {
    /**
     * key:表名 
     * value:表信息对象
     */
    private static Map<String, TableInfo> tables;

    /**
     * 将po的class对象和表信息对象关联起来，便于重用
     */
    private static Map<Class<?>, TableInfo> poClassTableInfoMap;

    static {
        tables = new HashMap<String, TableInfo>();
        poClassTableInfoMap = new HashMap<Class<?>, TableInfo>();
        // 获取数据库表结构
        tables = JDBCUtils.getTableInfoMap();
        // 加载po包下面的所有的类，便于重用提高效率
        loadPO();
    }

    private TableContext() {

    }

    /**
     * 加载po包下面的类
     */
    private static void loadPO() {
        for (TableInfo tableInfo : TableContext.tables.values()) {
            try {
                Class<?> clazz = Class.forName(Configuration.getInstance().getPoPackage() + "."
                        + StringUtils.capitalize(tableInfo.getName()));
                poClassTableInfoMap.put(clazz, tableInfo);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, TableInfo> getTables() {
        return tables;
    }

    public static void setTables(Map<String, TableInfo> tables) {
        TableContext.tables = tables;
    }

    public static Map<Class<?>, TableInfo> getPoClassTableInfoMap() {
        return poClassTableInfoMap;
    }

    public static void setPoClassTableInfoMap(Map<Class<?>, TableInfo> poClassTableMap) {
        TableContext.poClassTableInfoMap = poClassTableMap;
    }

}