package com.zz.lib.server.servlet.preset;

import com.zz.lib.constant.Constants;
import com.zz.lib.server.servlet.Servlet;
import com.zz.lib.server.servlet.request.Request;
import com.zz.lib.server.servlet.response.Response;
import com.zz.lib.server.servlet.response.Result;

public class SysServerErrorServlet implements Servlet {

    @Override
    public void service(Request request, Response response) throws Exception {
        Result result = new Result(Constants.RESULT.SERVER_ERROR, Constants.RESULT.SERVER_ERROR_MSG);
        response.printJSON(result);
    }

}
