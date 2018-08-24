# **ActiveMQ安装**

MQ: Message Queue消息队列

ActiveMQ：Apache出品，最流行的，能力强劲的开源消息总线。

**用途和优点**

1.将数据从一个应用程序传送到另一个应用程序，或者从软件的一个模块传送到另外一个模块；

2.负责建立网络通信的通道，进行数据的可靠传送。

3.保证数据不重复，不丢失

4.能够实现跨平台操作

**ActiveMQ应用场景**

1，多个项目之间集成

2，降低系统间模块的耦合度，解耦

3，系统前后端隔离

**安装**

1.下载地址http://activemq.apache.org/download.html

2.解压安装

```java
tar -zxvf apache-activemq-5.15.3-bin.tar.gz -c /usr/local
```

3.重命名

```java
mv apache-activemq-5.15.3 activemq
```

4.修改配置文件vim activemq/conf/activemq.xml

机器1.2.3修改**集群名** 三台机器必须一样

```
brokerName="activemq-cluster"
```

修改vim activemq/conf/activemq.xml持久化方式

```java
<persistenceAdapter>
	<replicatedLevelDB 
		directory="${activemq.data}/leveldb"
		replicas="3"  //集群节点个数
		bind="tcp://0.0.0.0.61621" //集群通讯端口
		//zookeeper 集群地址
		zkAddress="192.168.16.128:2181,192.168.16.130:2181,192.168.16.129:2181"
		hostname="192.168.16.128" //本机ip，hosts ip对应hostname
		zkPath="/activemq/leveldb-stores"/>
</persistenceAdapter>
```

修改vim activemq/conf/activemq.xml的消息端口 51516

```java
<transportConnector name="openwire" uri="tcp://0.0.0.0:51516?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
```

修改vim activemq/conf/jetty.xml管控台端口 网页访问时候对应的端口（192.168.16.128:8161）

```java
<bean id="jettyPort" class="org.apache.activemq.web.WebConsolePort" init-method="start">
        <property name="host" value="0.0.0.0"/>
        <property name="port" value="8161"/>  
  </bean>
```

根据以上步骤修改每台机器上的配置

| 机器IP         | 管控台端口 | 集群通讯端口 | 消息端口 |
| -------------- | ---------- | ------------ | -------- |
| 192.168.16.128 | 8161       | 61621        | 51516    |
| 192.168.16.129 | 8162       | 61622        | 51517    |
| 192.168.16.130 | 8163       | 61623        | 51518    |

**启动**

启动前提安装JDK

zookeeper集群安装请查看https://mp.weixin.qq.com/s/aZe1Z-6qsGwUTYlObu5CVA

1.首先启动zookeeper集群 /zkServer.sh start

2.查看/zkServer.sh status 状态

3.启动activemq  ./activemq    start / stop /status/console(启动并查看日志)

4.查看zookeeper信息 ./zkCli.sh 

查看是否有activemq信息 ls /

[activemq, zookeeper]

查看activemq集群是否持久化到zookeeper  ls /activemq/leveldb-stores

[00000000086, 00000000084, 00000000085]

**界面访问**

分别访问

http://192.168.16.130:8163/

http://192.168.16.129:8162/

 http://192.168.16.128:8161/

有两个链接不能访问是属于正常，两个链接属于待机状态并没有真正实现MQ,当Master宕机，从两台Slave中选举一个为Master。

安装异常问题

Q:遇到未知的服务和名称

A:修改hosts文件 192.168.16.128  jingyu(hostname)

```
hostname 查看
```

Q:集群搭建成功，当Master宕机，Slave没有选举为Master

A:三台集群端口一致为tcp://0.0.0.0.0，还有默认端口和消息端口使用默认。修改为不同端口，重启解决。

Q:单MQ消息队列启动失败

A:JDK版本与MQ版本是否对应

启动Zookeeper与ActiveMQ之前是需要安装JDK

**什么情况下使用ActiveMQ?**

多个项目之间集成 
(1) 跨平台 
(2) 多语言 
(3) 多项目
降低系统间模块的耦合度，解耦 
(1) 软件扩展性
系统前后端隔离 
(1) 前后端隔离，屏蔽高安全区