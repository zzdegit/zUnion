package com.zz.lib.constant;

public class Constants {
    //
    public static final String CRLF = "\r\n";

    public static final String BLANK = " ";

    public static final String UTF_8 = "UTF-8";

    /**
     * 系统响应错误码
     */
    public static class RESULT {
        /*
         * 成功
         */
        public static final int SUCCESS = 200;

        public static final String SUCCESS_MSG = "SUCCESS";

        /*
         * 服务器错误
         */
        public static final int SERVER_ERROR = 500;

        public static final String SERVER_ERROR_MSG = "SERVER ERROR";

        /*
         * 未发现
         */
        public static final int NOT_FOUND = 404;

        public static final String NOT_FOUND_MSG = "NOT FOUND";

        /*
         * 请求错误:客户端错误请求
         */
        public static final int BAD_REQUEST = 400;

        public static final String BAD_REQUEST_MSG = "BAD REQUEST";
    }

    /**
     * 请求体大小(单位:byte)
     */
    public static final int DEFAULT_REQUEST_INFO_SIZE = 10240;

    /**
     * 服务器启动所必须的参数来源配置文件
     */
    public static final String SYS_PROPERTIES = "sys.properties";

    /**
     * 返回内容类型
     */
    public static class CONTENT_TYPE {
        public static final String JSON = "application/json";

        public static final String HTML = "text/html";
    }

    /**
     * freeMarker
     */
    public static final String VIEW_FOLDER = "view";
}
