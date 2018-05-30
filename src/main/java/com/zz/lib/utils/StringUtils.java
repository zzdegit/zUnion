package com.zz.lib.utils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zz.lib.constant.Constants;

/**
 * 封装常用字符串操作
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 是否是JSON字符串
     */
    public static boolean isJSONStr(String str) {
        try {
            JSONObject.parseObject(str);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(str);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Md5加密
     */
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    /**
     * 获取随机字符串
     */
    public static String generateRandomStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * 将数据库样式的名称转换为java类型： eg: class_teacher 变成 classTeacher
     * 
     * @param dbVar
     * @return
     */
    public static String formatVarDB2Java(String dbVar) {
        if (StringUtils.isNotBlank(dbVar)) {
            StringBuilder sb = new StringBuilder();
            String[] arr = dbVar.split("_");
            sb.append(arr[0]);
            for (int i = 1; i < arr.length; i++) {
                sb.append(StringUtils.capitalize(arr[i]));
            }
            return sb.toString();
        }
        return dbVar;
    }

    /**
     * url 解码
     */
    public static String decode(String value) {
        String str = null;
        try {
            str = java.net.URLDecoder.decode(value, Constants.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

}
