package com.zz.lib.orm.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zz.lib.orm.bean.ColumnInfo;
import com.zz.lib.orm.bean.TableInfo;
import com.zz.lib.orm.core.DBManager;

/**
 * 封装常用jdbc操作
 */
public class JDBCUtils {
    /**
     * 给sql设参
     */
    public static void setPreparedStatementParams(PreparedStatement ps, Object[] params) {
        if (null != params) {
            for (int i = 0; i < params.length; i++) {
                try {
                    ps.setObject(i + 1, params[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取数据库表结构
     */
    public static Map<String, TableInfo> getTableInfoMap() {
        Map<String, TableInfo> tableTableInfoMap = new HashMap<String, TableInfo>();
        try {
            Connection connection = DBManager.getConnection();
            DatabaseMetaData dbmd = connection.getMetaData();
            ResultSet resultSet0 = dbmd.getTables(null, "%", "%", new String[] { "TABLE" });
            while (resultSet0.next()) {
                String tableName = (String) resultSet0.getObject("TABLE_NAME");

                TableInfo tableInfo = new TableInfo(tableName, new ArrayList<ColumnInfo>(),new HashMap<String, ColumnInfo>());
                tableTableInfoMap.put(tableName, tableInfo);

                ResultSet resultSet1 = dbmd.getColumns(null, "%", tableName, "%");// 查询表中的所有字段
                while (resultSet1.next()) {
                    ColumnInfo columnInfo = new ColumnInfo(resultSet1.getString("COLUMN_NAME"),resultSet1.getString("TYPE_NAME"), resultSet1.getString("REMARKS"), 0);
                    tableInfo.getColumns().put(resultSet1.getString("COLUMN_NAME"), columnInfo);
                }

                ResultSet resultSet2 = dbmd.getPrimaryKeys(null, "%", tableName);
                while (resultSet2.next()) {
                    ColumnInfo columnInfo = (ColumnInfo) tableInfo.getColumns().get(resultSet2.getObject("COLUMN_NAME"));
                    columnInfo.setKeyType(1);// 设置为主键类型
                    tableInfo.getPriKeys().add(columnInfo);
                }

                if (tableInfo.getPriKeys().size() > 0) {
                    tableInfo.setOnlyPriKey(tableInfo.getPriKeys().get(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableTableInfoMap;
    }
}
