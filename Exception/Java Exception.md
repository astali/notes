Java Exception Complany

**1、java 编译错误**

Target level '1.5' is incompatible with source level '1.7' .A target level '1.7' or better is required

解决办法

![](E:\Desktop\md\img\1.png)

**2.解决Invalid character found in the request target. The valid characters are defined in RFC 7230 and RFC 6389**

解决办法tomcat/conf/catalina.properties添加配置tomcat.util.http.parser.HttpParser.requestTargetAllow=|{}

**3.springmvc -shiro 版本冲突 shiro-all 1.2.5与org.aspectj1.8.10 冲突**

RequestMappingHandlerMapping': Initialization of bean failed; nested exception is java.lang.NullPointerException

解决办法org.aspectj1.8.9

**4.IDE启动报错信息**

**Application Server was not connected before run configuration stop, reason: Unable to ping server at localhost:1088**



**5.java.lang.OutOfMemoryError: PermGen space有效解决方法**

​	1.windows系统  在 TOMCAT_HOME/bin/catalina.bat 文件 echo Using CATALINA_BASE:   "%CATALINA_BASE%"上面加入以下行：set JAVA_OPTS=%JAVA_OPTS% -server -XX:PermSize=256M -XX:MaxPermSize=512m
​	2.unix系统 在 catalina.sh 文件 echo "Using CATALINA_BASE:   $CATALINA_BASE"上面或者第一行加入以下行：set JAVA_OPTS=%JAVA_OPTS% -server -XX:PermSize=256M -XX:MaxPermSize=512m　或者加入 JAVA_OPTS="-server -Xms800m -Xmx800m -XX:PermSize=64M -XX:MaxNewSize=256m -XX:MaxPermSize=128m -Djava.awt.headless=true "''

**6.创建Controllers失败，无添加@Service**

**7.myeclipse2014总是弹出update progress错误提示解决方法?**  

把progress那个窗口关掉

**8java.net.SocketException: Permission denied: connect**

IDEA / Eclipse /maven 设置 -Djava.net.preferIPv4Stack=true


9.windows启动activemq报端口被占用

Windows的一个服务占用了这个端口Internet Connection Sharing (ICS)把这个服务关闭即可



dial tcp 120.92.169.246:3306: connect: no route to host