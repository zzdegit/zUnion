package com.zz.lib.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zz.lib.configuration.Configuration;
import com.zz.lib.orm.core.DBManager;
import com.zz.lib.orm.core.TableContext;
import com.zz.lib.spring.core.Container;

public class Server {
    /**
     * ServerSocket
     */
    private ServerSocket server;

    /**
     * 线程池
     */
    private ExecutorService fixedThreadPool;

    /**
     * 是否关闭服务器
     */
    private boolean isShutdown = false;

    /**
     * 是否初始化成功
     */
    private boolean ifInitSuccess = false;

    /**
     * 启动参数初始化 服务器初始化 线程池初始化
     */
    public Server() {
        try {
            long t1 = System.currentTimeMillis();
            System.out.println("----- 开始初始化 -----");
            // 初始化服务器
            server = new ServerSocket(Configuration.getInstance().getPort());
            fixedThreadPool = Executors.newFixedThreadPool(Configuration.getInstance().getThreadPoolSize());
            System.out.println("服务器:初始化成功...");

            // 初始化容器
            Container.getInstance().init();
            System.out.println("容器:初始化成功...");

            // 初始化连接池
            DBManager.init();
            System.out.println("数据库连接池:初始化成功...");

            // 初始化TableContext
            TableContext.init();
            System.out.println("数据库上下文:初始化成功...");

            // 初始化成功
            ifInitSuccess = true;

            System.out.println("开始接收请求...");
            System.out.println("启动用时:" + (System.currentTimeMillis() - t1) / 1000.0 + "s");
            System.out.println();
            System.out.println("http://localhost:" + Configuration.getInstance().getPort() + "/index");
            System.out.println("----- 初始化结束 -----");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
            // 初始化失败则关闭服务器
            this.stop();
        }
    }

    /**
     * 服务器开始接收请求
     */
    public void start() {
        if (this.ifInitSuccess) {
            try {
                this.receive();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 服务器初始化失败、或 服务器运行时异常退出 则关闭服务器
        this.stop();
    }

    /**
     * 接收客户端消息
     * 
     * @throws Exception
     */
    private void receive() throws Exception {
        while (!isShutdown) {
            // 开始接收请求
            Socket client = server.accept();
            client.setSoTimeout(Configuration.getInstance().getTimeout());
            // 创建服务员
            Waiter waiter = new Waiter(client);
            // 分配调度员线程并开始工作
            fixedThreadPool.execute(waiter);
        }
    }

    /**
     * 停止服务器、线程池关闭
     */
    private void stop() {
        try {
            this.isShutdown = true;
            if (null != server) {
                server.close();
            }
            server = null;
            if (null != fixedThreadPool) {
                fixedThreadPool.shutdown();
            }
            fixedThreadPool = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    public void setShutdown(boolean isShutdown) {
        this.isShutdown = isShutdown;
    }

}
