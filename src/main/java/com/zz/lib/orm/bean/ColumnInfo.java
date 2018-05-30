package com.zz.lib.orm.bean;

/**
 * 表中一个字段的信息
 */
public class ColumnInfo {
    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段的数据类型
     */
    private String dataType;

    /**
     * 字段注释
     */
    private String remarks;

    /**
     * 字段的键类型(0:普通 1:主键 2:外键)
     */
    private int keyType;

    public ColumnInfo() {
        super();
    }

    public ColumnInfo(String name, String dataType, String remarks, int keyType) {
        super();
        this.name = name;
        this.dataType = dataType;
        this.remarks = remarks;
        this.keyType = keyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

}
