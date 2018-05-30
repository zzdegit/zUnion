package com.zz.lib.orm.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import com.zz.lib.configuration.Configuration;

/**
 * 数据库连接池
 */
public class DBConnectionPool {
    /**
     * 连接池对象
     */
    private List<Connection> pool;

    /**
     * 最大链接数
     */
    private static final int POOL_MAX_CONN_SIZE = Configuration.getInstance().getPoolMaxConnSize();

    /**
     * 最小链接数
     */
    private static final int POOL_MIN_CONN_SIZE = Configuration.getInstance().getPoolMinConnSize();

    public DBConnectionPool() {
        this.initDBPool();
    }

    /**
     * 初始化数据库连接池
     */
    public void initDBPool() {
        if (pool == null) {
            pool = new ArrayList<Connection>();
        }
        for (int i = 0; i < POOL_MIN_CONN_SIZE; i++) {
            Connection connection = this.getRealConnection();
            if (null != connection) {
                pool.add(connection);
            }
        }
    }

    /**
     * 从连接池中取出一个链接,并从池中移除（不是真实关闭数据库链接）
     * 若池中无链接则获取正式数据库链接
     */
    public synchronized Connection getConnection() {
        Connection connection = null;
        if (pool.size() == 0) {
            connection = this.getRealConnection();
        } else {
            int lastIndex = pool.size() - 1;
            connection = pool.get(lastIndex);
            pool.remove(lastIndex);
        }
        return connection;
    }

    /**
     * 将数据库链接放回到连接池中 
     * 若超过最大连接数就真实关闭数据库链接
     */
    public synchronized void closeConnection(Connection connection) {
        if (pool.size() >= POOL_MAX_CONN_SIZE) {
            this.closeRealConnection(connection);
        } else {
            pool.add(connection);
        }
    }

    /**
     * 跟数据库建立真实链接，本方法是私有的，即不允许外界直接绕过连接池直接创建数据库链接
     */
    private Connection getRealConnection() {
        Connection connection = null;
        try {
            Configuration conf = Configuration.getInstance();
            Class.forName(conf.getDriver());
            connection = DriverManager.getConnection(conf.getUrl(), conf.getUser(), conf.getPwd());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭数据库连接
     */
    private void closeRealConnection(Connection connection) {
        try {
            if (null != connection) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
