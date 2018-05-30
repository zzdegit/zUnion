package com.zz.lib.server.servlet.preset;

import com.zz.lib.constant.Constants;
import com.zz.lib.server.servlet.Servlet;
import com.zz.lib.server.servlet.request.Request;
import com.zz.lib.server.servlet.response.Response;
import com.zz.lib.server.servlet.response.Result;

public class SysBadRequestServlet implements Servlet {

    @Override
    public void service(Request request, Response response) throws Exception {
        Result result = new Result(Constants.RESULT.BAD_REQUEST, Constants.RESULT.BAD_REQUEST_MSG);
        response.printJSON(result);
    }

}
