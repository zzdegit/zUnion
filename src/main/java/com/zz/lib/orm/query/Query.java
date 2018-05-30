package com.zz.lib.orm.query;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import com.zz.lib.orm.bean.ColumnInfo;
import com.zz.lib.orm.bean.TableInfo;
import com.zz.lib.orm.core.DBManager;
import com.zz.lib.orm.core.TableContext;
import com.zz.lib.orm.utils.JDBCUtils;
import com.zz.lib.utils.ReflectUtils;
import com.zz.lib.utils.StringUtils;

/**
 * 负责查询(对外提供服务的核心类)
 */
@SuppressWarnings("all")
public abstract class Query implements Cloneable, Serializable {

    private static final long serialVersionUID = -1316403868878870145L;

    /**
     * 执行DML语句
     * 
     * @param sql DML sql语句
     * @param params 参数数组
     * @return 执行sql语句后影响记录的行数
     */
    public int executeDML(String sql, Object[] params) {
        Connection connection = DBManager.getConnection();
        int count = 0;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            JDBCUtils.setPreparedStatementParams(preparedStatement, params);
            count = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(preparedStatement, connection);
        }
        return count;
    }

    /**
     * 将一个对象存储到数据库中, 只存不为null的, 若传入参数obj主键为null则自动设置为uuid值， 若主键值不为null则使用传入的id值作为主键
     */
    public void insert(Object obj) {
        Class<?> clazz = obj.getClass();
        TableInfo tableInfo = TableContext.getPoClassTableInfoMap().get(clazz);

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("insert into " + tableInfo.getName() + "(");

        List<Object> fieldValueList = new ArrayList<Object>();

        Field[] fieldArr = clazz.getDeclaredFields();
        for (Field field : fieldArr) {
            String fieldName = field.getName();
            Object fieldValue = ReflectUtils.invokeGet(obj, fieldName);
            if (fieldName.equals(tableInfo.getOnlyPriKey().getName()) && (null == fieldValue)) {
                fieldValue = StringUtils.generateRandomStr();
            }
            if (null != fieldValue) {
                sqlSb.append(fieldName + ",");
                fieldValueList.add(fieldValue);
            }
        }

        sqlSb.setCharAt(sqlSb.length() - 1, ')');
        sqlSb.append(" values(");
        for (int i = 0; i < fieldValueList.size(); i++) {
            sqlSb.append("?,");
        }
        sqlSb.setCharAt(sqlSb.length() - 1, ')');
        this.executeDML(sqlSb.toString(), fieldValueList.toArray());
    }

    /**
     * 删除clazz表示类对应的指定主键值id 表中的记录
     * 
     * @param clazz 跟表对应的类的Class对象
     * @param id 主键的值
     */
    public void delete(Class<?> clazz, Object id) {
        // 通过Class对象找TableInfo
        TableInfo tableInfo = TableContext.getPoClassTableInfoMap().get(clazz);
        // 获取主键
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
        // 拼接sql语句
        String sql = "delete from " + tableInfo.getName() + " where " + onlyPriKey.getName() + " = ? ";
        // 执行dml
        this.executeDML(sql, new Object[] { id });
    }

    /**
     * 删除对象在数据库中对应的记录（对象所在的类对应到表，对象的主键的值对应到记录）
     * 
     */
    public void delete(Object obj) {
        Class<?> clazz = obj.getClass();
        // 通过Class对象找TableInfo
        TableInfo tableInfo = TableContext.getPoClassTableInfoMap().get(clazz);
        // 获取主键
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
        // 通过反射机制，调用属性对应的get或set方法
        Object onlyPriKeyValue = ReflectUtils.invokeGet(obj, onlyPriKey.getName());
        this.delete(clazz, onlyPriKeyValue);
    }

    /**
     * 更新对象对应的记录，并且只更新指定的字段的值 如果fieldNames没有传值则默认更新所有字段
     * 
     * @param obj 所要更新的对象
     * @param fieldNames 更新的属性列表
     * @return 执行sql语句后影响记录的行数
     */
    public int update(Object obj, String[] fieldNameArr) {
        Class<?> clazz = obj.getClass();
        TableInfo tableInfo = TableContext.getPoClassTableInfoMap().get(clazz);
        // 如果fieldNames没有传值则默认更新所有字段
        if ((null == fieldNameArr) || (fieldNameArr.length > 0)) {
            List<String> fieldNameList = new ArrayList<String>();
            for (ColumnInfo columnInfo : tableInfo.getColumns().values()) {
                fieldNameList.add(columnInfo.getName());
            }
            fieldNameArr = fieldNameList.toArray(new String[] {});
        }
        // 获取主键
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("update " + tableInfo.getName() + " set ");

        List<Object> fieldValueList = new ArrayList<Object>();
        for (String fieldName : fieldNameArr) {
            Object fieldValue = ReflectUtils.invokeGet(obj, fieldName);
            if (!fieldName.equals(onlyPriKey.getName())) {
                sqlSb.append(fieldName + " = ?,");
                fieldValueList.add(fieldValue);
            }
        }
        sqlSb.setCharAt(sqlSb.length() - 1, ' ');
        sqlSb.append("where " + onlyPriKey.getName() + " = ? ");
        fieldValueList.add(ReflectUtils.invokeGet(obj, onlyPriKey.getName()));

        return this.executeDML(sqlSb.toString(), fieldValueList.toArray());
    }

    /**
     * 更新对象 属性不为null 对应的表的字段，
     * 
     * @param obj 所要更新的对象
     * @return 执行sql语句后影响记录的行数
     */
    public int update(Object obj) {
        return this.update(obj, null);
    }

    /**
     * 采用模板模式将jdbc操作封装成模板，便于重用
     * 
     * @param sql
     * @param clazz
     * @param params
     * @param callBack 实现回调
     * @return
     */
    public Object executeQueryTemplate(String sql, Class<?> clazz, Object[] params, CallBack callBack) {
        Connection conn = DBManager.getConnection();
        Object queryResultObj = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            JDBCUtils.setPreparedStatementParams(ps, params);
            rs = ps.executeQuery();

            queryResultObj = callBack.execute(conn, ps, rs, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(ps, conn);
        }
        return queryResultObj;
    }

    /**
     * 查询返回多行记录，并将每行记录封装到clazz指定的类的对象中
     * 
     * @param sql 查询语句
     * @param clazz 封装数据的javabean类的Class对象
     * @param params sql的参数
     * @return 查询到的结果
     */
    public List queryRows(String sql, Class<?> clazz, Object[] params) {
        return (List) executeQueryTemplate(sql, clazz, params, new CallBack() {
            public Object execute(Connection conn, PreparedStatement ps, ResultSet rs, Class clazz) {
                List queryRowList = new ArrayList();
                try {
                    ResultSetMetaData resultSetMetaData = rs.getMetaData();
                    // 遍历行
                    while (rs.next()) {
                        // 调用无参构造器，创建对象
                        Object rowObj = clazz.newInstance();
                        // 遍历列
                        for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                            // 获取列名
                            String columnName = resultSetMetaData.getColumnLabel(i + 1);
                            // 获取列值
                            Object columnValue = rs.getObject(i + 1);
                            // 使用反射调用rowObj的set方法设置值
                            ReflectUtils.invokeSet(rowObj, StringUtils.formatVarDB2Java(columnName),columnValue);
                        }
                        queryRowList.add(rowObj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return queryRowList;
            }
        });
    }

    /**
     * 查询返回一行记录，并将改记录封装到clazz指定的类的对象中
     * 
     * @param sql 查询语句
     * @param clazz 封装数据的javabean类的Class对象
     * @param params sql的参数
     * @return 查询到的结果
     */
    public Object queryUniqueRow(String sql, Class<?> clazz, Object[] params) {
        List list = this.queryRows(sql, clazz, params);
        return ((null == list) || (list.size() == 0)) ? null : list.get(0);
    }

    /**
     * 查询返回一个值（一行一列），并将改值返回
     * 
     * @param sql 查询语句
     * @param params sql的参数
     * @return 查询到的结果
     */
    public Object queryValue(String sql, Object[] params) {
        return executeQueryTemplate(sql, null, params, new CallBack() {
            public Object execute(Connection conn, PreparedStatement ps, ResultSet rs, Class clazz) {
                Object queryValue = null;
                try {
                    if (rs.next()) {
                        queryValue = rs.getObject(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return queryValue;
            }
        });
    }

    /**
     * 查询返回一个Long型数据（一行一列），并将值返回
     * 
     * @param sql 查询语句
     * @param params sql的参数
     * @return 查询到的结果
     */
    public Long queryLong(String sql, Object[] params) {
        return (Long) this.queryValue(sql, params);
    }
    
    /**
     * 查询返回一个Integer型数据（一行一列），并将值返回
     * 
     * @param sql 查询语句
     * @param params sql的参数
     * @return 查询到的结果
     */
    public Integer queryInteger(String sql, Object[] params) {
        return (Integer) this.queryValue(sql, params);
    }

    /**
     * 查询返回一个字符串（一行一列），并将改值返回
     * 
     * @param sql 查询语句
     * @param params sql的参数
     * @return 查询到的结果
     */
    public String queryString(String sql, Object[] params) {
        return (String) this.queryValue(sql, params);
    }

    /**
     * 根据主键获取clazz对应的实体类
     * 
     * @param clazz
     * @param id
     * @return
     */
    public Object queryPoById(Class<?> clazz, Object id) {
        TableInfo tableInfo = TableContext.getPoClassTableInfoMap().get(clazz);
        String sql = "select *from " + tableInfo.getName() + " where " + tableInfo.getOnlyPriKey().getName() + " = ? ";
        return this.queryUniqueRow(sql, clazz, new Object[] { id });
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
