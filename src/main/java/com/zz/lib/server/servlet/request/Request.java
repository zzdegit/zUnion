package com.zz.lib.server.servlet.request;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.zz.lib.constant.Constants;
import com.zz.lib.exception.BusinessException;
import com.zz.lib.utils.ArrayUtils;
import com.zz.lib.utils.StringUtils;

/**
 * 请求封装
 */
public class Request {

    // 请求方式
    private String method;

    // 请求资源
    private String url;

    // 请求参数
    private Map<String, List<String>> parameterMapValues;

    // 请求参数
    private Map<String, Object> parameterMap;

    // 请求消息
    private String requestInfo;

    @SuppressWarnings("unused")
    private InputStream is;

    public Request() {
        this.method = "";
        this.url = "";
        this.parameterMapValues = new HashMap<String, List<String>>();
        this.requestInfo = "";
    }

    public Request(InputStream is) throws Exception {
        this();
        this.is = is;
        /*
         * 
         * */
        StringBuilder requestInfoSb = new StringBuilder();
        /*
         * 解析http协议，每一行放到lineArr，每一行长度不能大于Constants.DEFAULT_REQUEST_INFO_SIZE， 使用char[]为了性能
         */
        char[] lineArr = new char[Constants.DEFAULT_REQUEST_INFO_SIZE];// 10240
        /*
         * 游标，lineArr 从0 开始到 point指向的lineArr位置 是有效数据，其他为垃圾数据，
         */
        int point = 0;
        /*
         * POST 请求的请求体的内容大小
         */
        int contentLength = 0;
        /*
         * 是否是第一行，第一行获取method、url、以及GET请求的param
         */
        boolean ifFirstLine = true;
        /*
         * 获取 \r\n\r\n 这个特殊的位置
         */
        int ifBreak = 0;
        while (true) {
            char c = (char) is.read();
            if (c == '\r' || c == '\n') {
                ifBreak++;
            } else {
                ifBreak = 0;
            }
            if (c != '\n') {
                lineArr[point] = c;
                if (point == (lineArr.length - 1)) {
                    throw new BusinessException(Constants.RESULT.BAD_REQUEST,
                            Constants.RESULT.BAD_REQUEST_MSG);
                }
                point++;
                continue;
            }
            if (4 == ifBreak) {
                break;
            } else if (point > 0) {
                String line = new String(lineArr, 0, point);
                requestInfoSb.append(line);
                if (ifFirstLine) {
                    String[] firstLineArr = line.trim().split("\\s+");
                    this.method = firstLineArr[0];
                    String[] urlAndParamsArr = firstLineArr[1].split("\\?");
                    this.url = urlAndParamsArr[0];

                    if ("GET".equalsIgnoreCase(this.method) && (urlAndParamsArr.length > 1)
                            && StringUtils.isNotBlank(urlAndParamsArr[1])) {
                        parseParams(urlAndParamsArr[1]);
                    }

                    ifFirstLine = false;
                } else {
                    String[] keyAndValueArr = line.trim().split(":");
                    if ("POST".equalsIgnoreCase(this.method)
                            && "Content-Length".equals(keyAndValueArr[0].trim())) {
                        contentLength = Integer.parseInt(keyAndValueArr[1].trim());
                    }
                }
            }
            point = 0;
        }

        if ("POST".equalsIgnoreCase(this.method) && (contentLength > 0)) {
            byte[] data = new byte[contentLength];
            int len = is.read(data);
            String params = null;
            if (-1 != len) {
                params = new String(data, 0, len, Constants.UTF_8).trim();
            }
            requestInfoSb.append(params);
            if (StringUtils.isNotBlank(params)) {
                this.parseParams(params);
            }
        }

        requestInfo = requestInfoSb.toString();
        if (com.zz.lib.configuration.Configuration.getInstance().getIsDebug()) {
            System.out.println("-----请求消息-----");
            System.out.println(requestInfo);
            System.out.println("---------------");
        }

        // parameterMap
        if (null == parameterMap) {
            parameterMap = new HashMap<String, Object>();
            Iterator<String> it = parameterMapValues.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                List<String> valueList = parameterMapValues.get(key);
                if ((null != valueList) && (valueList.size() > 0)) {
                    if (1 == valueList.size()) {
                        parameterMap.put(key, valueList.get(0));
                    } else {
                        parameterMap.put(key, valueList);
                    }
                }
            }
        }
    }

    /**
     * 分解http请求的传入参数
     */
    @SuppressWarnings("unchecked")
    private void parseParams(String params) {
        params = StringUtils.decode(params).trim();
        if (StringUtils.isNotBlank(params)) {
            if (StringUtils.isJSONStr(params)) {
                Map<String, String> map = (Map<String, String>) JSON.parse(params);
                Iterator<String> it = map.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = map.get(key);
                    if (!parameterMapValues.containsKey(key)) {
                        parameterMapValues.put(key, new ArrayList<String>());
                    }
                    parameterMapValues.get(key).add(value);
                }
            } else {
                String[] arr0 = params.split("&");
                if (ArrayUtils.isNotEmpty(arr0)) {
                    for (String item0 : arr0) {
                        String[] arr1 = item0.split("=");
                        if (ArrayUtils.isNotEmpty(arr1)) {
                            if (!this.parameterMapValues.containsKey(arr1[0])) {
                                this.parameterMapValues.put(arr1[0], new ArrayList<String>());
                            }
                            this.parameterMapValues.get(arr1[0]).add((1 == arr1.length) ? "" : arr1[1]);
                        }
                    }
                }
            }
        }
    }

    /**
     * 请求参数
     */
    public Map<String, Object> getParameters() {
        return parameterMap;
    }

    /**
     * 根据页面的name获取对应的值
     */
    public String getParameter(String name) {
        String[] values = this.getParameterValues(name);
        if (null == values) {
            return null;
        } else {
            return values[0];
        }
    }

    /**
     * 根据页面的name获取对应的多个值
     */
    public String[] getParameterValues(String name) {
        List<String> values = null;
        if ((values = parameterMapValues.get(name)) == null) {
            return null;
        } else {
            return values.toArray(new String[0]);
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url.replaceAll("/+", "/");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

}