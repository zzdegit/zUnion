package com.zz.lib.orm.typeConvertor;

/**
 * java数据类型和数据库数据类型的相互转换
 */
public interface TypeConvertor {
    /**
     * 将数据库数据类型转换成java的数据类型
     * 
     * @param columnType 数据库字段的数据类型
     * @return java的数据类型
     */
    public String databaseType2JavaType(String columnType);

    /**
     * 将java的数据类型 转换成 数据库数据类型
     * 
     * @param javaType java数据类型
     * @return 数据库数据类型
     */
    public String javaType2DatabaseType(String javaType);

    /**
     * 将java的数据类型 转换成 数据库数据类型,只返回类似java.sql.Date 带有完整包路径的,需要import的
     * 
     * @param javaType java数据类型
     * @return 数据库数据类型
     */
    public String databaseType2JavaTypeNeedImport(String dataType);
}
