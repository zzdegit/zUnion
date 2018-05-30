package com.zz.lib.orm.utils;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.zz.lib.constant.Constants;
import com.zz.lib.orm.bean.ColumnInfo;
import com.zz.lib.orm.bean.TableInfo;
import com.zz.lib.orm.typeConvertor.MySqlTypeConvertor;
import com.zz.lib.orm.typeConvertor.TypeConvertor;
import com.zz.lib.utils.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 封装java文件操作
 */
public class JavaFileUtils {
    /**
     * 根据表信息生成java类的源代码
     * 
     * @param tableInfo 表信息
     * @param convertor 数据类型转换器
     * @return java类的源代码
     */
    public static String createJavaPO(TableInfo tableInfo, TypeConvertor convertor) {
        String javaSrc = null;
        try {
            Map<String, ColumnInfo> columns = tableInfo.getColumns();

            Map<String, Object> param = new HashMap<String, Object>();
            // 生成模块名
            param.put("moduleName", StringUtils.formatVarDB2Java(tableInfo.getName()));
            // 生成import语句
            Set<String> importSet = new HashSet<String>();
            for (ColumnInfo columnInfo : columns.values()) {
                String importStr = convertor.databaseType2JavaTypeNeedImport(columnInfo.getDataType());
                if (null != importStr) {
                    importSet.add(importStr);
                }
            }
            param.put("importSet", importSet);
            // 生成类名
            param.put("className",
                    StringUtils.capitalize(StringUtils.formatVarDB2Java(tableInfo.getName())) + "Po");
            // 生成属性列表
            List<Map<String, String>> fieldList = new ArrayList<Map<String, String>>();
            for (ColumnInfo columnInfo : columns.values()) {
                Map<String, String> field = new HashMap<String, String>();
                field.put("remarks", columnInfo.getRemarks());
                field.put("type", convertor.databaseType2JavaType(columnInfo.getDataType()));
                field.put("name", StringUtils.formatVarDB2Java(columnInfo.getName()));
                fieldList.add(field);
            }
            param.put("fieldList", fieldList);

            Configuration cfg = new Configuration();
            cfg.setDirectoryForTemplateLoading(new File("src/main/java/com/zz/lib/orm/utils"));
            cfg.setDefaultEncoding(Constants.UTF_8);

            Template template = cfg.getTemplate("po.ftl");
            StringWriter stringWriter = new StringWriter();
            template.process(param, stringWriter);

            javaSrc = stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return javaSrc;
    }

    /**
     * 生成po
     */
    public static void createJavaPOFile(TableInfo tableInfo, TypeConvertor convertor) {
        String tableName = StringUtils.formatVarDB2Java(tableInfo.getName());
        String path = "src/main/java/com/zz/custom/" + tableName + "/po/" + StringUtils.capitalize(tableName)
                + "Po.java";
        File file = new File(path);
        System.out.println(file.getAbsolutePath());
        String javaSrc = createJavaPO(tableInfo, convertor);
        try {
            FileUtils.writeStringToFile(file, javaSrc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String createJavaController(TableInfo tableInfo, TypeConvertor convertor) {

        return null;
    }

    /**
     * 生成controller
     */
    private static void createJavaControllerFile(TableInfo tableInfo, TypeConvertor convertor) {
        String tableName = StringUtils.formatVarDB2Java(tableInfo.getName());
        String path = "src/main/java/com/zz/custom/" + tableName + "/controller/"
                + StringUtils.capitalize(tableName) + "Controller.java";
        File file = new File(path);
        System.out.println(file.getAbsolutePath());
        String javaSrc = createJavaController(tableInfo, convertor);
        try {
            FileUtils.writeStringToFile(file, javaSrc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据表名生成java模块代码
     */
    public static void createModuleByTableName(String tableName) {
        TableInfo tableInfo = JDBCUtils.getTableInfoMap().get(tableName);
        JavaFileUtils.createJavaPOFile(tableInfo, new MySqlTypeConvertor());
        JavaFileUtils.createJavaControllerFile(tableInfo, new MySqlTypeConvertor());
    }

    /**
     * 生成所有表,java模块代码
     */
    public static void createAllJavaPOFile() {
        Map<String, TableInfo> tables = JDBCUtils.getTableInfoMap();
        for (TableInfo tableInfo : tables.values()) {
            JavaFileUtils.createJavaPOFile(tableInfo, new MySqlTypeConvertor());
        }
    }

}