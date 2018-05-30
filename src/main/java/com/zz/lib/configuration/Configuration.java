package com.zz.lib.configuration;

import com.zz.lib.constant.Constants;
import com.zz.lib.utils.PropertiesUtils;
import com.zz.lib.utils.StringUtils;

/**
 * 管理配置信息
 */
public class Configuration {

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 线程数
     */
    private Integer threadPoolSize;

    /**
     * 驱动类
     */
    private String driver;

    /**
     * jdbc的url
     */
    private String url;

    /**
     * 数据库的用户名
     */
    private String user;

    /**
     * 数据库的密码
     */
    private String pwd;

    /**
     * 扫描生成java类的包（po的意思是object持久化对象）
     */
    private String poPackage;

    /**
     * 扫描
     */
    private String scanPackage;

    /**
     * 查询query的class
     */
    private String queryClass;

    /**
     * 数据库连接池最大连接数
     */
    private Integer poolMaxConnSize;

    /**
     * 数据库连接池最小连接数
     */
    private Integer poolMinConnSize;

    /**
     * 是否是调试模式
     */
    private Boolean isDebug;

    /**
     * 超时时间
     */
    private Integer timeout;

    private static class ConfigurationSingleton {
        private static final Configuration instance = new Configuration();
    }

    public static Configuration getInstance() {
        return ConfigurationSingleton.instance;
    }

    private Configuration() {
        super();
        PropertiesUtils pro = new PropertiesUtils(Constants.SYS_PROPERTIES);
        this.port = pro.getInt("port");
        this.threadPoolSize = pro.getInt("threadPoolSize");

        this.driver = pro.getString("driver");
        this.url = pro.getString("url");
        this.user = pro.getString("user");
        this.pwd = pro.getString("pwd");
        // po生成位置
        this.poPackage = pro.getString("poPackage");
        if (StringUtils.isBlank(this.poPackage)) {
            this.poPackage = "com.zz.custom.po";
        }
        // 扫描包
        this.scanPackage = pro.getString("scanPackage");
        if (StringUtils.isBlank(this.scanPackage)) {
            this.scanPackage = "com.zz.custom";
        }
        // 查询query
        this.queryClass = pro.getString("queryClass");
        if (StringUtils.isBlank(this.queryClass)) {
            this.queryClass = "com.zz.lib.orm.query.MySqlQuery";
        }
        // poolMaxConnSize
        this.poolMaxConnSize = pro.getInt("poolMaxConnSize");
        // poolMinConnSize
        this.poolMinConnSize = pro.getInt("poolMinConnSize");
        // isDebug
        this.isDebug = pro.getBoolean("isDebug");
        if (null != this.isDebug) {
            this.isDebug = true;
        }
        // timeout
        String timeout = pro.getString("timeout");
        if (StringUtils.isBlank(timeout)) {
            this.timeout = 60 * 1000;
        } else {
            this.timeout = Integer.parseInt(timeout);
        }
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(Integer threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPoPackage() {
        return poPackage;
    }

    public void setPoPackage(String poPackage) {
        this.poPackage = poPackage;
    }

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public String getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(String queryClass) {
        this.queryClass = queryClass;
    }

    public int getPoolMaxConnSize() {
        return poolMaxConnSize;
    }

    public void setPoolMaxConnSize(int poolMaxConnSize) {
        this.poolMaxConnSize = poolMaxConnSize;
    }

    public int getPoolMinConnSize() {
        return poolMinConnSize;
    }

    public void setPoolMinConnSize(int poolMinConnSize) {
        this.poolMinConnSize = poolMinConnSize;
    }

    public Boolean getIsDebug() {
        return isDebug;
    }

    public void setIsDebug(Boolean isDebug) {
        this.isDebug = isDebug;
    }

    public void setPoolMaxConnSize(Integer poolMaxConnSize) {
        this.poolMaxConnSize = poolMaxConnSize;
    }

    public void setPoolMinConnSize(Integer poolMinConnSize) {
        this.poolMinConnSize = poolMinConnSize;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

}
