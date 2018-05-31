package com.zz.lib.orm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.zz.lib.orm.pool.DBConnectionPool;

/**
 * 数据库管理
 */
public class DBManager {

    private static DBConnectionPool DB_CONN_POOL;
    
    public static void init() {
        DB_CONN_POOL = new DBConnectionPool();
        DB_CONN_POOL.initDBPool();
    }

    public static Connection getConnection() {
        return DB_CONN_POOL.getConnection();
    }

    public static void close(PreparedStatement ps, Connection conn) {
        try {
            if (null != ps) {
                ps.close();
            }
            if (null != conn) {
                DB_CONN_POOL.closeConnection(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Connection conn) {
        DB_CONN_POOL.closeConnection(conn);
    }

}
