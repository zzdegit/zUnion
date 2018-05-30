package com.zz.lib.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * 封装常用反射操作
 */
@SuppressWarnings("all")
public class ReflectUtils {
    /**
     * 调用obj对象的columnName 属性 的 get方法
     */
    public static Object invokeGet(Object obj, String columnName) {
        Object invokeValue = null;
        try {
            Method method = obj.getClass().getDeclaredMethod("get" + StringUtils.capitalize(columnName),null);
            invokeValue = method.invoke(obj, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invokeValue;
    }

    /**
     * 调用obj对象的columnName 属性 的 set方法
     */
    public static Object invokeSet(Object obj, String columnName, Object columnValue) {
        Object invokeValue = null;
        try {
            if (null != columnValue) {
                Method method = obj.getClass().getDeclaredMethod("set" + StringUtils.capitalize(columnName),
                        columnValue.getClass());
                invokeValue = method.invoke(obj, columnValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invokeValue;
    }

    /**
     * 判断是否是基本类型（非po类型）
     */
    public static boolean isBasicParamType(Class<?> paramType) {
        boolean isBasicParamType = false;
        // Boolean
        if (paramType.equals(boolean.class)) {
            isBasicParamType = true;
        } else if (paramType.equals(Boolean.class)) {
            isBasicParamType = true;
        }
        // Byte
        else if (paramType.equals(Byte.class)) {
            isBasicParamType = true;
        } else if (paramType.equals(byte.class)) {
            isBasicParamType = true;
        }
        // Char
        if (paramType.equals(Character.class)) {
            isBasicParamType = true;
        } else if (paramType.equals(char.class)) {
            isBasicParamType = true;
        }
        // Short
        else if (paramType.equals(Short.class)) {
            isBasicParamType = true;
        } else if (paramType.equals(short.class)) {
            isBasicParamType = true;
        }
        // Integer
        else if (paramType.equals(Integer.class)) {
            isBasicParamType = true;
        } else if (paramType.equals(int.class)) {
            isBasicParamType = true;
        }
        // Long
        else if (paramType.equals(Long.class)) {
            isBasicParamType = true;
        } else if (paramType.equals(long.class)) {
            isBasicParamType = true;
        }
        // Double
        else if (paramType.equals(Double.class)) {
            isBasicParamType = true;
        } else if (paramType.equals(double.class)) {
            isBasicParamType = true;
        }
        // Float
        else if (paramType.equals(Float.class)) {
            isBasicParamType = true;
        } else if (paramType.equals(float.class)) {
            isBasicParamType = true;
        }
        // Timestamp
        else if (paramType.equals(Timestamp.class)) {
            isBasicParamType = true;
        }
        // Date
        else if (paramType.equals(java.util.Date.class)) {
            isBasicParamType = true;
        }
        // Date
        else if (paramType.equals(java.sql.Date.class)) {
            isBasicParamType = true;
        }
        // BigDecimal
        else if (paramType.equals(BigDecimal.class)) {
            isBasicParamType = true;
        }
        // BigInteger
        else if (paramType.equals(BigInteger.class)) {
            isBasicParamType = true;
        }
        // String
        else if (paramType.equals(String.class)) {
            isBasicParamType = true;
        }
        return isBasicParamType;
    }

    /**
     * 根据paramType类型获取默认值
     */
    public static Object getDefaultBasicParamValue(Class<?> paramType) {
        Object defaultBasicParamValue = null;
        // Boolean
        if (paramType.equals(boolean.class)) {
            defaultBasicParamValue = false;
        }
        // Byte
        if (paramType.equals(byte.class)) {
            defaultBasicParamValue = 0;
        }
        // Char
        else if (paramType.equals(char.class)) {
            defaultBasicParamValue = '0';
        }
        // Short
        else if (paramType.equals(short.class)) {
            defaultBasicParamValue = 0;
        }
        // Integer
        else if (paramType.equals(int.class)) {
            defaultBasicParamValue = 0;
        }
        // Long
        else if (paramType.equals(long.class)) {
            defaultBasicParamValue = 0L;
        }
        // Double
        else if (paramType.equals(double.class)) {
            defaultBasicParamValue = 0.0D;
        }
        // Float
        else if (paramType.equals(float.class)) {
            defaultBasicParamValue = 0.0F;
        }
        return defaultBasicParamValue;
    }

    /**
     * 根据paramType类型将字符串paramValue转换成对应的类型
     */
    public static Object parseByParamType(Class<?> paramType, String paramValue) {
        Object resultObj = false;
        // Boolean
        if (paramType.equals(Boolean.class)) {
            resultObj = Boolean.parseBoolean(paramValue);
        } else if (paramType.equals(boolean.class)) {
            Boolean obj = Boolean.parseBoolean(paramValue);
            resultObj = obj.booleanValue();
        }
        // Byte
        else if (paramType.equals(Byte.class)) {
            resultObj = Byte.parseByte(paramValue);
        } else if (paramType.equals(byte.class)) {
            Byte obj = Byte.parseByte(paramValue);
            resultObj = obj.byteValue();
        }
        // Char
        if (paramType.equals(Character.class)) {
            resultObj = Character.valueOf(paramValue.charAt(0));
        } else if (paramType.equals(char.class)) {
            Character obj = Character.valueOf(paramValue.charAt(0));
            resultObj = obj.charValue();
        }
        // Short
        else if (paramType.equals(Short.class)) {
            resultObj = Short.parseShort(paramValue);
        } else if (paramType.equals(short.class)) {
            Short obj = Short.parseShort(paramValue);
            resultObj = obj.shortValue();
        }
        // Integer
        else if (paramType.equals(Integer.class)) {
            resultObj = Integer.parseInt(paramValue);
        } else if (paramType.equals(int.class)) {
            Integer obj = Integer.parseInt(paramValue);
            resultObj = obj.intValue();
        }
        // Long
        else if (paramType.equals(Long.class)) {
            resultObj = Long.parseLong(paramValue);
        } else if (paramType.equals(long.class)) {
            Long obj = Long.parseLong(paramValue);
            resultObj = obj.longValue();
        }
        // Double
        else if (paramType.equals(Double.class)) {
            resultObj = Double.parseDouble(paramValue);
        } else if (paramType.equals(double.class)) {
            Double obj = Double.parseDouble(paramValue);
            resultObj = obj.doubleValue();
        }
        // Float
        else if (paramType.equals(Float.class)) {
            resultObj = Float.parseFloat(paramValue);
        } else if (paramType.equals(float.class)) {
            Float obj = Float.parseFloat(paramValue);
            resultObj = obj.floatValue();
        }
        // Timestamp
        else if (paramType.equals(Timestamp.class)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                resultObj = new Timestamp(sdf.parse(paramValue).getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Date
        else if (paramType.equals(java.util.Date.class)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                resultObj = sdf.parse(paramValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Date
        else if (paramType.equals(java.sql.Date.class)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                resultObj = new java.sql.Date(sdf.parse(paramValue).getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // BigDecimal
        else if (paramType.equals(BigDecimal.class)) {
            resultObj = new BigDecimal(paramValue);
        }
        // BigInteger
        else if (paramType.equals(BigInteger.class)) {
            resultObj = new BigInteger(paramValue);
        }
        // String
        else if (paramType.equals(String.class)) {
            resultObj = paramValue;
        }
        return resultObj;
    }
}
