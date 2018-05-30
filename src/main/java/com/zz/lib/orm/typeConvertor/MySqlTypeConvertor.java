package com.zz.lib.orm.typeConvertor;

import java.util.HashMap;
import java.util.Map;

/**
 * mysql数据类型和java数据类型转换
 */
public class MySqlTypeConvertor implements TypeConvertor {

    private static Map<String, String> databaseType2JavaTypeMap;

    private static Map<String, String> javaType2DatabaseTypeMap;

    static {
        databaseType2JavaTypeMap = new HashMap<String, String>();

        databaseType2JavaTypeMap.put("varchar", "String");
        databaseType2JavaTypeMap.put("char", "String");

        databaseType2JavaTypeMap.put("int", "Integer");
        databaseType2JavaTypeMap.put("tinyint", "Integer");
        databaseType2JavaTypeMap.put("smallint", "Integer");
        databaseType2JavaTypeMap.put("integer", "Integer");

        databaseType2JavaTypeMap.put("bigint", "Long");

        databaseType2JavaTypeMap.put("double", "Double");
        databaseType2JavaTypeMap.put("float", "Double");

        databaseType2JavaTypeMap.put("clob", "java.sql.Clob");
        databaseType2JavaTypeMap.put("blob", "java.sql.Blob");

        databaseType2JavaTypeMap.put("date", "java.sql.Date");
        databaseType2JavaTypeMap.put("datetime", "java.sql.Timestamp");

        databaseType2JavaTypeMap.put("time", "java.sql.Time");

        databaseType2JavaTypeMap.put("timestamp", "java.sql.Timestamp");
        //
        javaType2DatabaseTypeMap = new HashMap<String, String>();
    }

    public String databaseType2JavaType(String columnType) {
        String str = databaseType2JavaTypeMap.get(columnType.toLowerCase());
        if (-1 == str.indexOf(".")) {
            return str;
        } else {
            return str.substring(str.lastIndexOf(".") + 1, str.length());
        }
    }

    public String databaseType2JavaTypeNeedImport(String columnType) {
        String str = databaseType2JavaTypeMap.get(columnType.toLowerCase());
        if (-1 == str.indexOf(".")) {
            return null;
        } else {
            return databaseType2JavaTypeMap.get(columnType.toLowerCase());
        }
    }

    public String javaType2DatabaseType(String javaType) {
        return javaType2DatabaseTypeMap.get(javaType.toLowerCase());
    }

}
