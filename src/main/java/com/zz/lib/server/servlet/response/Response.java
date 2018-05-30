package com.zz.lib.server.servlet.response;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.zz.lib.constant.Constants;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 响应封装
 */
public class Response {
    // 流
    private BufferedWriter bw;

    // 存储头信息
    private StringBuilder headInfo;

    // 正文
    private StringBuilder content;

    // 存储正文长度
    private int contentLength;

    // 响应内容类型
    private String contentType;

    public Response() {
        this.headInfo = new StringBuilder();
        this.content = new StringBuilder();
        this.contentLength = 0;
        this.contentType = Constants.CONTENT_TYPE.HTML;
    }

    public Response(OutputStream os) {
        this();
        this.bw = new BufferedWriter(new OutputStreamWriter(os));
    }

    public Response(OutputStream os, String contentType) {
        this();
        this.bw = new BufferedWriter(new OutputStreamWriter(os));
        this.contentType = contentType;
    }

    /**
     * 构建响应头
     */
    private void createHeadInfo(int code) {
        // (1)http协议版本、状态代码、描述
        headInfo.append("HTTP/1.1").append(Constants.BLANK).append(code).append(Constants.BLANK);
        if (Constants.RESULT.SUCCESS == code) {
            headInfo.append("OK");
        } else if (Constants.RESULT.NOT_FOUND == code) {
            headInfo.append(Constants.RESULT.NOT_FOUND_MSG);
        } else if (Constants.RESULT.SERVER_ERROR == code) {
            headInfo.append(Constants.RESULT.SERVER_ERROR_MSG);
        }
        headInfo.append(Constants.CRLF);
        // (2)相应头 Response Head
        headInfo.append("Server:zz Server/0.0.1").append(Constants.CRLF);
        headInfo.append("Date:").append(new Date()).append(Constants.CRLF);
        headInfo.append("Content-type:" + contentType + ";charset=UTF-8").append(Constants.CRLF);
        // 正文长度：字节长度,浏览器只能解析给的字节长度，如果字节长度短于实际的长度，就只能解析给定的字节长度
        headInfo.append("Content-Length:").append(contentLength).append(Constants.CRLF);
    }

    /**
     * 推送到客户端
     * 
     * @throws IOException
     */
    public void pushToClient(int code) throws IOException {

        this.createHeadInfo(code);

        StringBuilder responseInfo = new StringBuilder();
        responseInfo.append(headInfo.toString());
        responseInfo.append(Constants.CRLF);
        responseInfo.append(content.toString());

        bw.append(responseInfo.toString());
        bw.flush();

        if (com.zz.lib.configuration.Configuration.getInstance().getIsDebug()) {
            System.out.println("-----返回信息-----");
            System.out.println(responseInfo.toString());
            System.out.println("---------------");
        }
    }

    /**
     * 关闭流
     */
    public void close() throws IOException {
        if (null != bw) {
            bw.close();
        }
    }

    /**
     * 设置响应类型为html 使用FreeMarker技术 根据 模板文件 ftlName 以及填充参数param，获取处理后的字符串
     */
    public void printHTML(String ftlName, Result result) throws Exception {
        this.printHTML("src/main/resources/view", ftlName, result);
    }

    /**
     * 设置响应类型为html
     * 
     * @param path
     * @param ftlName
     * @param param
     * @throws Exception
     */
    public void printHTML(String path, String ftlName, Result result) throws Exception {
        // 设置响应类型为html
        this.contentType = Constants.CONTENT_TYPE.HTML;
        // 使用FreeMarker技术 根据 模板文件 ftlName 以及填充参数param，获取处理后的字符串
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(new File(path));
        cfg.setDefaultEncoding(Constants.UTF_8);

        Template template = cfg.getTemplate(ftlName);
        StringWriter stringWriter = new StringWriter();
        template.process(result, stringWriter);
        this.print(stringWriter.toString());
    }

    /**
     * 设置响应类型为html 使用FreeMarker技术 根据 模板文件 ftlName 以及填充参数param，获取处理后的字符串
     */
    public void printHTML(String ftlName, Map<String, Object> param) throws Exception {
        this.printHTML("src/main/resources/view", ftlName, param);
    }

    /**
     * 设置响应类型为html
     * 
     * @param path
     * @param ftlName
     * @param param
     * @throws Exception
     */
    public void printHTML(String path, String ftlName, Map<String, Object> param) throws Exception {
        // 设置响应类型为html
        this.contentType = Constants.CONTENT_TYPE.HTML;
        // 使用FreeMarker技术 根据 模板文件 ftlName 以及填充参数param，获取处理后的字符串
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(new File(path));
        cfg.setDefaultEncoding(Constants.UTF_8);

        Template template = cfg.getTemplate(ftlName);
        StringWriter stringWriter = new StringWriter();
        template.process(param, stringWriter);
        this.print(stringWriter.toString());
    }

    /**
     * 设置响应类型为json , result转成JSON
     * 
     * @throws Exception
     */
    public void printJSON(Result result) throws Exception {
        // 设置响应类型为json
        this.contentType = Constants.CONTENT_TYPE.JSON;
        this.print(JSON.toJSONString(result));
    }

    /**
     * 构建正文
     * 
     * @throws Exception
     */
    public Response print(String info) throws Exception {
        if (null != info) {
            content.append(info);
            contentLength += info.getBytes(Constants.UTF_8).length;
        }
        return this;
    }

    /**
     * 构建正文加回车
     * 
     * @throws Exception
     */
    public Response println(String info) throws Exception {
        if (null != info) {
            content.append(info).append(Constants.CRLF);
            contentLength += (info + Constants.CRLF).getBytes(Constants.UTF_8).length;
        }
        return this;
    }
}