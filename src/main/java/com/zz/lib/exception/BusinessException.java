package com.zz.lib.exception;

/**
 * 业务异常 : 业务异常属于客户端等原因造成的异常,不属于框架异常
 */
public class BusinessException extends Exception {

    private static final long serialVersionUID = 1441081379218528020L;

    private int code;

    private String message;

    public BusinessException() {
        super();
    }

    public BusinessException(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
