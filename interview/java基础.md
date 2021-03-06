##### **1.Web页面优化**

1. 减少http请求(多图合一, 合并静态组件, 图片延时加载)
2. 使用内容分发网络(和web组建分离类似, 激发浏览器并发, 分担web服务器负载)
3. 启动浏览器缓存(添加Expire头信息)
4. 压缩静态组件(等同服务器压缩传输)
5. 将css放在页面顶部
6. 将js放在页面底部
7. 避免css表达式
8. 外部引用js和css
9. 减少DNS查询(DNS缓存,TTL设置, Keep-alive, 使用更少的域名, 同域名内静态文件加载使用/代替域名)
10. 简化压缩静态js和css
11. 避免重定向(这里指静态资源的获取过程)
12. 去除多余的js
13. 配置ETags
14. 缓存ajax结果
15. DNS预取(<link rel="dns-prefetch" href="//预取域名">)
16. 资源预取(内联img标签, <link rel="prefetch" href="图片地址">)
17. 渐进的JPEG(导出jpeg图片时勾选对应选项)

##### **2.堆栈区别**

栈的优势是：存取速度比堆（heap）要快，堆主要用来存放对象的，栈主要是用来执行程序的
堆栈空间分配区别：

　　1、栈（操作系统）：由操作系统自动分配释放 ，存放函数的参数值，局部变量的值等。其操作方式类似于数据结构中的栈； 
　　2、堆（操作系统）： 一般由程序员分配释放， 若程序员不释放，程序结束时可能由OS回收，分配方式倒是类似于链表。

##### **3.cookie 和session 的区别**

1. cookie数据存放在客户的浏览器上，session数据放在服务器上。

2. cookie不是很安全，别人可以分析存放在本地的COOKIE并进行COOKIE欺骗
      考虑到安全应当使用session。

3. session会在一定时间内保存在服务器上。当访问增多，会比较占用你服务器的性能
      考虑到减轻服务器性能方面，应当使用COOKIE。

4. 单个cookie保存的数据不能超过4K，很多浏览器都限制一个站点最多保存20个cookie。

5. 所以个人建议：
      将登陆信息等重要信息存放为SESSION
      其他信息如果需要保留，可以放在COOKIE中


##### **4. fail-fast 机制**

fail-fast机制在遍历一个集合时，当集合结构被修改，会抛出**Concurrent Modification Exception**
fail-fast会在以下两种情况下抛出ConcurrentModificationException
1.单线程环境
	集合被创建后，在遍历它的过程中修改了结构。
	注意 remove()方法会让expectModcount和modcount 相等，所以是不会抛出这个异常。
2.多线程环境
	当一个线程在遍历这个集合，而另一个线程对这个集合的结构进行了修改。

##### **5.fail-safe机制**

fail-safe任何对集合结构的修改都会在一个复制的集合上进行修改，因此不会抛出ConcurrentModificationException
fail-safe机制有两个问题
（1）需要复制集合，产生大量的无效对象，开销大
（2）无法保证读取的数据是目前原始数据结构中的数据。

##### **6.IO 和 NIO的区别**

IO                   NIO
面向流            面向缓冲
阻塞IO            非阻塞IO
无                选择器
优势在于一个线程管理多个通道，但是数据的处理将会变得复杂，如果需要管理同时打开成千上万个连接，这些连接每次只是发送少量的数据。


**get与post区别**

get提交的数据最大是2k 
post理论上没有限制。实际上IIS4中最大量为80KB，IIS5中为100KB。

GET产生一个TCP数据包，浏览器会把http header和data一并发送出去，服务器响应200(返回数据); 
POST产生两个TCP数据包，浏览器先发送header，服务器响应100 continue，浏览器再发送data，服务器响应200 ok(返回数据)。

GET在浏览器回退时是无害的，POST会再次提交请求。

GET产生的URL地址可以被Bookmark，而POST不可以。

GET请求会被浏览器主动cache，而POST不会，除非手动设置。

GET请求只能进行url编码，而POST支持多种编码方式。

GET请求参数会被完整保留在浏览器历史记录里，而POST中的参数不会被保留。

GET只接受ASCII字符的参数的数据类型，而POST没有限制

那么，post那么好为什么还用get？get效率高！。

##### **HTTP与HTTPS有什么区别？**

　　HTTP协议传输的数据都是未加密的，也就是明文的，因此使用HTTP协议传输隐私信息非常不安全，为了保证这些隐私数据能加密传输，于是网景公司设计了SSL（Secure Sockets Layer）协议用于对HTTP协议传输的数据进行加密，从而就诞生了HTTPS。简单来说，HTTPS协议是由SSL+HTTP协议构建的可进行加密传输、身份认证的网络协议，要比http协议安全。

　　HTTPS和HTTP的区别主要如下：

　　1、https协议需要到ca申请证书，一般免费证书较少，因而需要一定费用。

　　2、http是超文本传输协议，信息是明文传输，https则是具有安全性的ssl加密传输协议。

　　3、http和https使用的是完全不同的连接方式，用的端口也不一样，前者是80，后者是443。

　　4、http的连接很简单，是无状态的；HTTPS协议是由SSL+HTTP协议构建的可进行加密传输、身份认证的网络协议，比http协议安全。



**7.JVM哪些区域可能出现内存溢出（OOM）**

1.Java堆 java heap space

2.java栈 unable to create new native thread

3.方法区和运行时常量池 PermGen space

4.直接内存溢出



**行锁和表锁，何时触发行锁**

MySQL的InnoDB存储引擎支持事务，默认是行锁，所以数据库支持高并发

SQL的更新或删除使用索引，触发行锁，未使用索引时触发表锁



java8新特性有哪些

1.Lambda表达式

2.Optional类解决空指针异常

3.DataTimeAPI 时间

4.Stream API 

5.方法引用

6.Nashorn JavaScript引擎