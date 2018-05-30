package com.zz.lib.server.core;

import java.net.Socket;

import com.zz.lib.constant.Constants;
import com.zz.lib.exception.BusinessException;
import com.zz.lib.server.servlet.Servlet;
import com.zz.lib.server.servlet.preset.DispatcherServlet;
import com.zz.lib.server.servlet.preset.SysBadRequestServlet;
import com.zz.lib.server.servlet.preset.SysIndexServlet;
import com.zz.lib.server.servlet.preset.SysServerErrorServlet;
import com.zz.lib.server.servlet.request.Request;
import com.zz.lib.server.servlet.response.Response;

/**
 * 服务员:一个http请求被一个服务员所服务
 */
public class Waiter implements Runnable {
    /**
     * Socket
     */
    private Socket client;

    /**
     * 请求封装
     */
    private Request request;

    /**
     * 响应封装
     */
    private Response response;

    /**
     * 返回状态码
     */
    private int code = Constants.RESULT.SUCCESS;

    /**
     * 调度员初始化 1.初始化Socket 2.请求封装 3.响应封装
     */
    public Waiter(Socket client) {
        // 初始化客户端Socket
        this.client = client;
        // 初始化Request
        try {
            request = new Request(client.getInputStream());
        } catch (Exception e) {
            this.handelInitException(e);
        }
        // 初始化Response
        try {
            response = new Response(client.getOutputStream());
        } catch (Exception e) {
            this.handelInitException(e);
        }
    }

    /**
     * 处理调度员初始化时产生的异常
     * 
     * @param e
     */
    private void handelInitException(Exception e) {
        // 服务生初始化失败
        if (e instanceof BusinessException) {
            // 业务异常,如客户端错误请求等非框架原因造成的异常
            this.code = ((BusinessException) e).getCode();
        } else {
            // 其他异常
            e.printStackTrace();
            this.code = Constants.RESULT.SERVER_ERROR;
        }
    }

    @Override
    public void run() {
        try {
            if ((null != request) && (null != response)) {
                Servlet handelServlet = null;
                // 500错误
                if (Constants.RESULT.SERVER_ERROR == this.code) {
                    handelServlet = new SysServerErrorServlet();
                }
                // 400错误
                else if (Constants.RESULT.BAD_REQUEST == this.code) {
                    handelServlet = new SysBadRequestServlet();
                }
                // index
                else if ("/".equals(request.getUrl()) || "/index".equals(request.getUrl())) {
                    handelServlet = new SysIndexServlet();
                }
                // 其他
                else {
                    handelServlet = new DispatcherServlet();
                }
                try {
                    handelServlet.service(request, response);
                } catch (Exception e) {
                    this.handelInitException(e);
                }
            }
            if (null != response) {
                // 返回结果
                response.pushToClient(this.code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束后关闭socket
            this.close();
        }
    }

    /**
     * 关闭Socket
     */
    private void close() {
        try {
            if (null != client) {
                client.close();
            }
            request = null;
            response = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}