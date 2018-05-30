package com.zz.custom.user.po;

import java.sql.Timestamp;

public class User {

    /**
     * 生日
     */
    private Timestamp birthday;
    /**
     * 名称
     */
    private String name;
    /**
     * 主键
     */
    private String id;
    /**
     * 密码
     */
    private String pwd;

    public User() {
    	
    }
    
    public User(Timestamp birthday,String name,String id,String pwd) {
        this.birthday = birthday;
        this.name = name;
        this.id = id;
        this.pwd = pwd;
    }
    
    public Timestamp getBirthday() {
        return birthday;
    }
    
    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
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
    
    public String getPwd() {
        return pwd;
    }
    
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
}
