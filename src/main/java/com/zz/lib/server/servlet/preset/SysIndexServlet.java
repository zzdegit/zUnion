package com.zz.lib.server.servlet.preset;

import java.util.HashMap;
import java.util.Map;

import com.zz.lib.server.servlet.Servlet;
import com.zz.lib.server.servlet.request.Request;
import com.zz.lib.server.servlet.response.Response;

public class SysIndexServlet implements Servlet {

    @Override
    public void service(Request req, Response rep) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        rep.printHTML("src/main/java/com/zz/lib/server/servlet/preset", "sysIndex.ftl", param);
    }

}
