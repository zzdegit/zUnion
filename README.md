>温馨提示:重要的事说三遍:  

 - 1.zUnion只是用于交流学习框架，若有任何问题欢迎跟我讨论,特别是大牛的指点  

 - 2.zUnion只是用于交流学习框架，若有任何问题欢迎跟我讨论,特别是大牛的指点  

 - 3.zUnion只是用于交流学习框架，若有任何问题欢迎跟我讨论,特别是大牛的指点  
    
***

>zUnion使用说明:  

 - 0.使用本框架事先搞定如下  
    
    - JDK
    - mysql
    - STS
    - maven
    

 - 1.src/main/resources 下的sys.properties 为系统配置文件
    
    - port 端口号
    - threadPoolSize 线程池线程数量
    - driver 驱动类
    - url jdbc的url
    - user 账号名
    - pwd 密码
    - poolMaxConnSize 数据库连接池最大连接数
    - poolMinConnSize 数据库连接池最小连接数 
    - isDebug 是否是调试模式 默认:true
    - timeout 请求超时时间默认:60 * 1000 
    - queryClass 查询query实现类，默认:com.zz.lib.orm.query.MySqlQuery
    
 - 2.业务代码只能放到com.zz.custom包下 ,com.zz.lib包下的为系统代码com.zz.CodeGenerate生成的PO文件自动位于com.zz.custom下    
    
 - 3.注解仿照springmvc，但为了区别(主要是方法使用快捷键)前面加前缀Z  
    - @ZAutowried 注入  
    - @ZCompont 组件
    - @ZController controller
    - @ZDao dao
    - @ZRequestMapping 请求映射
    - @ZRequestParam 请求参数注解
    - @ZService service
    - @ZAop  
        eg:使用@ZAop注解普通java bean、methodRegex是个正则表达式对全局所有的service的函数Method进行正则匹配，也就是调用反射获取的Method对象的toString获得的值进行正则匹配    
        @ZAop(methodRegex="public.*saveOrUpdate\\(.*SysTableInfoPo\\)")  
        public class LogAop implements Aop {  
		
        }  
    如上的案例匹配到的完整Method是:public com.zz.lib.server.servlet.response.Result com.zz.custom.sysTableInfo.service.impl.SysTableInfoServiceImpl.saveOrUpdate(com.zz.custom.sysTableInfo.po.SysTableInfoPo)  
    
 - 4.controller的method传入参数为非po类型等javabean类需要使用注解@ZRequestParam("paramName"),PO等javabean类无需注解  

 - 5.数据库访问封装类Query使用@ZAutowried注解即可自动注入，无需实现  

 - 6.运行com.zz.Run启动服务器,开始接收消息  

 - 7.GET请求url只能接受10221个字符，超出范围返回400错误  

 - 8.html文件放到src/main/resources 下的view文件夹下  

 - 9.每张表只能有一个主键，不支持联合主键等

 - 10.po类只能使用包装类型，不能使用基本类型  

 - 11.表主键统一为uuid形式，不能使用自增长  

 - 12.统一数据库时间类型为DATETIME或TIMESTAMP,java 类型统一为java.sql.Timestamp

 - 13.系统错误码返回信息,统一为json类型:
    - {"returnCode":500,"returnMsg":"SERVER ERROR"}  
    - {"returnCode":404,"returnMsg":"NOT FOUND"}  
    - {"returnCode":400,"returnMsg":"BAD REQUEST"}
 
***

>zUnion后续计划: 

 - 0.各种优化代码，先不说功能和稳定性，先把性能提升到硬撼SSM的程度   

 - 1.增加文件上传功能  

 - 2.根据表结构自动生成全套增删改查基本代码  

 - 3.边学习设计模式边重构zUnion   

 - 4.zUnion作为节点开发整套分布式集群一体化解决方案  

 - 5.使用zUnion开发一些方便程序员工作的工具  
