package com.zz.lib.server.servlet;

import com.zz.lib.server.servlet.request.Request;
import com.zz.lib.server.servlet.response.Response;

public interface Servlet {
    void service(Request req, Response rep) throws Exception;
}
