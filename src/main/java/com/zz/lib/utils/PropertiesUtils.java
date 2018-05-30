package com.zz.lib.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Properties;

import com.zz.lib.constant.Constants;

/**
 * properties 配置文件读取工具类
 */
public class PropertiesUtils {

    private Properties properties;

    public PropertiesUtils(String fileName) {
        InputStreamReader inputStream = null;
        try {
            properties = new Properties();
            inputStream = new InputStreamReader(
                    this.getClass().getClassLoader().getResourceAsStream(fileName), Constants.UTF_8);
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }

    public Integer getInt(String key) {
        return Integer.parseInt(this.getString(key));
    }

    public Long getLong(String key) {
        return Long.parseLong(this.getString(key));
    }

    public Double getDouble(String key) {
        return Double.parseDouble(this.getString(key));
    }

    public Float getFloat(String key) {
        return Float.parseFloat(this.getString(key));
    }

    public BigDecimal getBigDecimal(String key) {
        return new BigDecimal(this.getString(key));
    }

    public BigInteger getBigInteger(String key) {
        return new BigInteger(this.getString(key));
    }

    public Boolean getBoolean(String key) {
        return Boolean.parseBoolean(this.getString(key));
    }

}
