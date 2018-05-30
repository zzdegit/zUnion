package com.zz.lib.server.servlet.response;

import com.zz.lib.constant.Constants;

public class Result {

    private int returnCode = Constants.RESULT.SUCCESS;

    private String returnMsg = Constants.RESULT.SUCCESS_MSG;

    private Object returnData;

    public Result() {
        super();
    }

    public Result(Object returnData) {
        super();
        this.returnData = returnData;
    }

    public Result(int returnCode, String returnMsg) {
        super();
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
    }

    public Result(int returnCode, String returnMsg, Object returnData) {
        super();
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
        this.returnData = returnData;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public Object getReturnData() {
        return returnData;
    }

    public void setReturnData(Object returnData) {
        this.returnData = returnData;
    }

}
