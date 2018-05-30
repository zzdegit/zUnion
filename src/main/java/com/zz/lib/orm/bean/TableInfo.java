package com.zz.lib.orm.bean;

import java.util.List;
import java.util.Map;

/**
 * 表结构的信息
 */
public class TableInfo {
    /**
     * 表名
     */
    private String name;

    /**
     * 唯一主键
     */
    private ColumnInfo onlyPriKey;

    /**
     * 联合主键
     */
    private List<ColumnInfo> priKeys;

    /**
     * 所有字段的信息
     */
    private Map<String, ColumnInfo> columns;

    public TableInfo() {
        super();
    }

    public TableInfo(String name, ColumnInfo onlyPriKey, Map<String, ColumnInfo> columns) {
        super();
        this.name = name;
        this.onlyPriKey = onlyPriKey;
        this.columns = columns;
    }

    public TableInfo(String name, List<ColumnInfo> priKeys, Map<String, ColumnInfo> columns) {
        super();
        this.name = name;
        this.priKeys = priKeys;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, ColumnInfo> columns) {
        this.columns = columns;
    }

    public ColumnInfo getOnlyPriKey() {
        return onlyPriKey;
    }

    public void setOnlyPriKey(ColumnInfo onlyPriKey) {
        this.onlyPriKey = onlyPriKey;
    }

    public List<ColumnInfo> getPriKeys() {
        return priKeys;
    }

    public void setPriKeys(List<ColumnInfo> priKeys) {
        this.priKeys = priKeys;
    }
}
