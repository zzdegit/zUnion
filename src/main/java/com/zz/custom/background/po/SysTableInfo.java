package com.zz.custom.background.po;

import java.sql.Timestamp;

public class SysTableInfo {

    /**
     * 表名
     */
    private String name;
    /**
     * 主键
     */
    private String id;
    /**
     * 操作时间
     */
    private Timestamp operateTime;

    public SysTableInfo() {
    	
    }
    
    public SysTableInfo(String name,String id,Timestamp operateTime) {
        this.name = name;
        this.id = id;
        this.operateTime = operateTime;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Timestamp getOperateTime() {
        return operateTime;
    }
    
    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
    }
    
}
