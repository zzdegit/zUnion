package com.zz.lib.exception;

/**
 * 系统异常:架构原因导致的异常
 */
public class SystemException extends Exception {

    private static final long serialVersionUID = -716798102118954309L;

    private int code;

    private String message;

    public SystemException() {
        super();
    }

    public SystemException(int code, String message) {
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
