

# 安装Elasticsearch

1. wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.1.2.tar.gz

2. tar zxvf elasticsearch-6.1.2.tar.gz

   修改配置文件：注意分号后面的空格

   ```
   $ vi config/elasticsearch.yml
   #cluster namecluster.name: sojson-application 
   #节点名称
   node.name: node-1
   #绑定IP和端口
   network.host: 123.88.88.88
   http.port: 9200
   ```

3. ./bin/elasticsearch -d#后台运行

4. 因为版本的问题，最新的版本安全级别提高了，不允许采用root帐号启动，所以我们要添加一个用户。

   `添加一个用户：elasticsearch`

   `$useradd elasticsearch`

   `#给用户elasticsearch设置密码，连续输入2次`

   `$passwd elasticsearch`

   `#创建一个用户组 es`

   `groupadd es`

   `#分配 elasticsearch 到 es 组`

   `usermod -G elasticsearch es`

   `#这里注意下，如果提示用户“es”不存在，那么是因为服务器版本问题，你可以换成 usermod -G es elasticsearch ,也就是用户和用户组对调一下使用。`

   `#这里感谢【武汉|Java|竹木鸟】发现这个问题，并告知我。`

   `#在elasticsearch 根目录下，给定用户权限。-R表示逐级（N层目录） ， * 表示 任何文件`

   `chown  -R elasticsearch.es *`

   `#切换到elasticsearch用户`

   `su elasticsearch`

   //授权

		chown elasticsearch/opt/elasticsearch -R



# nodejs安装

nodejs安装

安装nodejs,同时还需要安装grunt，通过修改`/etc/profile`来配置nodejs的环境变量不要用，所以通过软连接来配置

```
#解压,注意是 tar.xz文件
tar -xvf /usr/local/nodejs/node-v8.9.0-linux-x64.tar.xz

#建立node的软连接
ln -s /usr/local/nodejs/node-v8.9.0-linux-x64/bin/node /usr/bin/node
ln -s /usr/local/nodejs/node-v8.9.0-linux-x64/bin/npm /usr/bin/npm

#设定nodejs安装软件的代理服务器
npm config set registry https://registry.npm.taobao.org

#执行npm,安装grunt
npm install -g grunt
npm install -g grunt-cli

#建立软连接 grunt 
ln -s /usr/local/nodejs/node-v8.9.0-linux-x64/bin/grunt /usr/bin/grunt 
```

# phantomjs安装

这个js是在head编译的时候需要

```
# 下载好后进行解压（由于是bz2格式，要先进行bzip2解压成tar格式，再使用tar解压）
bzip2 -d phantomjs-2.1.1-linux-x86_64.tar.bz2

# 再使用tar进行解压到/usr/local/目录下边
tar -xvf phantomjs-2.1.1-linux-x86_64.tar -C /usr/local/

# 安装依赖软件
yum -y install wget fontconfig

# 最后一步就是建立软连接了（在/usr/bin/目录下生产一个phantomjs的软连接，/usr/bin/是啥目录应该清楚，不清楚使用 echo $PATH查看）
ln -s /usr/local/phantomjs/bin/phantomjs /usr/bin/ 
```

# 安装head

```
#下载地址
wget http://yellowcong.qiniudn.com/elasticsearch-head-master.zip

#安装unzip解压工具(如果没有unzip，就需要安装这个)
yum install -y unzip 

#解压文件到 head
unzip elasticsearch-head-master.zip 

#进入head的目录
cd elasticsearch-head-master/

#安装一下这个bzip2 ，如果没有，在编译head，会报错
yum install -y bzip2

#安装（进入 elasticsearch-head-master）的目录，执行安装命令
sudo npm install

#配置head
#head的启动端口是9100
vim Gruntfile.js
options: {
    //配置0.0.0.0 表示匹配这个主机的所有ip，匹配不上的就会走这个了
        hostname:'0.0.0.0',
        port: 9100,
        base: '.',
        keepalive: true
}

#启动服务
grunt server
```

**启用http配置**

```
#修改elasticsearch.yml配置文件
vim config/elasticsearch.yml

#加入下面配置
http.cors.enabled: true 
http.cors.allow-origin: "*"123456
```

**错误合集**

**/usr/bin/env: node: 没有那个文件或目录**

![这里写图片描述](https://img-blog.csdn.net/20171213135101530?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveWVsbGxvd2Nvbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

没有找到node这个命令，需要建立软连接到usr/bin目录

```
ln -s /usr/local/nodejs/node-v8.9.0-linux-x64/bin/node /usr/bin/node1
```

tar (child): bzip2：无法 exec: 没有那个文件或目录

```
Download already available at /tmp/phantomjs/phantomjs-2.1.1-linux-x86_64.tar.bz2
Verified checksum of previously downloaded file
Extracting tar contents (via spawned process)
Error extracting archive
Phantom installation failed { Error: Command failed: tar jxf /tmp/phantomjs/phantomjs-2.1.1-linux-x86_64.tar.bz2
tar (child): bzip2：无法 exec: 没有那个文件或目录
tar (child): Error is not recoverable: exiting now
tar: Child returned status 2
tar: Error is not recoverable: exiting now
12345678910
```

解决方案

```
#安装解压包
yum install -y bzip212
```

Error: EACCES: permission denied

```
#需要使用sudo 用管理员来安装，不然没有权限
sudo npm install12
```

head没有连接上集群

可以发现集群没有连接上的问题 

修改config/elasticsearch.yml配置

```
#修改elasticsearch.yml配置文件
vim config/elasticsearch.yml

#加入下面配置
http.cors.enabled: true 
http.cors.allow-origin: "*"

授权
chown es.ess /opt/elasticsearch/* -R
```

**Elasticserach6.x之Head插件https://blog.csdn.net/yelllowcong/article/details/78787012**





# Elasticsearch

Elasticsearch是一个基于Apache Lucene(TM)的开源搜索引擎。无论在开源还是专有领域，Lucene可以被认为是迄今为止最先进、性能最好的、功能最全的搜索引擎库。
但是，Lucene只是一个库。想要使用它，你必须使用Java来作为开发语言并将其直接集成到你的应用中，更糟糕的是，Lucene非常复杂，你需要深入了解检索的相关知识来理解它是如何工作的。

Elasticsearch也使用Java开发并使用Lucene作为其核心来实现所有索引和搜索的功能，但是它的目的是通过简单的RESTful API来隐藏Lucene的复杂性，从而让全文搜索变得简单。
不过，Elasticsearch不仅仅是Lucene和全文搜索，我们还能这样去描述它：

- 分布式的实时文件存储，每个字段都被索引并可被搜索
- 分布式的实时分析搜索引擎
- 可以扩展到上百台服务器，处理PB级结构化或非结构化数据

[官方网址](https://www.elastic.co/cn/)

[官方Github](https://github.com/elastic/elasticsearch)

[官方中文权威指南](https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html)

[官方Java API文档](https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/index.html)

### 推荐学习资源

- [Elasticsearch Java API 手册](https://es.quanke.name/)
- [Elasticsearch权威指南](http://www.learnes.net/index.html)

### 安装环境

- CentOS7
- 依赖 Java 8+

### 安装Elasticsearch

- [官方下载地址](https://www.elastic.co/downloads/elasticsearch)

- 解压安装包 `# tar zxvf elasticsearch-5.6.2.tar.gz`

- 运行elasticsearch脚本启动

  `# cd /elasticsearch-5.6.2/bin`

  `# ./elasticsearch`

  后台启动：`# ./elasticsearch -d`

### 踩坑解决错误

1. 内存不足

   `Java HotSpot(TM) 64-Bit Server VM warning: INFO: os::commit_memory(0x0000000085330000, 2060255232, 0) failed; error='Cannot allocate memory' (errno=12)`

- 因个人服务器只有1g内存，需配置 `elasticsearch/config/jvm.options`，只有根据服务器改小分配堆空间的最大值了([或者启用Swap交换分区](https://github.com/Exrick/xmall/blob/master/study/Linux.md))：

```
################################################################

# Xms represents the initial size of total heap space
# Xmx represents the maximum size of total heap space

-Xms128m
-Xmx128m

################################################################
```

1. root账号错误

   `[WARN ][o.e.b.ElasticsearchUncaughtExceptionHandler] [] uncaught exception in thread [main]
   org.elasticsearch.bootstrap.StartupException: java.lang.RuntimeException: can not run elasticsearch as root
   at org.elasticsearch.bootstrap.Elasticsearch.init(Elasticsearch.java:125) ~[elasticsearch-5.2.0.jar:5.2.0]
   at org.elasticsearch.bootstrap.Elasticsearch.execute(Elasticsearch.java:112) ~[elasticsearch-5.2.0.jar:5.2.0]
   at 
   ... 6 more`

   由于ElasticSearch可以接收用户输入的脚本并且执行，为了系统安全考虑，建议创建一个单独的用户用来运行ElasticSearch

- 创建elsearch用户组及elsearch用户

  `groupadd es`

  `useradd es -g es -p es`

- 更改Elasticsearch文件夹及内部文件的所属用户及组为es:es
  `chown -R es:es  /usr/local/elasticsearch`

- 切换到elsearch用户再启动

  `su es`

  `cd elasticsearch/bin`

  `./elasticsearch`

1. 最大虚拟内存过小错误

   `ERROR: bootstrap checks failed
   max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]`

- 切换至root用户：`su root`
- 修改虚拟内存配置至提示的最低值：`sysctl -w vm.max_map_count=262144`

1. 日志权限错误

- 进入 `elasticsearch/logs` 文件夹下修改文件权限：`chmod 744 *`

1. 外网无法访问 `http://你的服务器IP:9200/`

- 修改 `elasticsearch/config/elasticsearch.yml` 配置文件：network.host: 0.0.0.0

```
# ---------------------------------- Network -----------------------------------
#
# Set the bind address to a specific IP (IPv4 or IPv6):
#
network.host: 0.0.0.0
# Set a custom port for HTTP:
#
http.port: 9200
#
# For more information, consult the network module documentation.
#
# --------------------------------- Discovery ----------------------------------
```

### 测试成功

![](http://oweupqzdv.bkt.clouddn.com/QQ%E6%88%AA%E5%9B%BE20170928164116.png)

### 插件Elasticsearch-head安装

[Head-GitHub官网](https://github.com/mobz/elasticsearch-head)

- 依赖：Git Node.js
- 官网安装教程

```
git clone git://github.com/mobz/elasticsearch-head.git
cd elasticsearch-head
npm install
npm run start
```

- 打开浏览器输入：IP:9100

- 正常的话可以看到已经连接了ES，但是看不到连接信息，这时候需要在在 es 的 elasticsearch.yml 里添加如下配置：

  `http.cors.enabled: true`

  `http.cors.allow-origin: "*"`

### 插件Elasticsearch-Analysis-IK安装

[IK Analysis-GitHub官网](https://github.com/medcl/elasticsearch-analysis-ik)

[预编译安装包下载](https://github.com/medcl/elasticsearch-analysis-ik/releases)(注意需下载对应Elasticsearch版本的IK插件)

- 官网安装教程

解压预编译包 `elasticsearch-analysis-ik-{version}.zip` 后拷贝至elasticsearch安装目录plugins文件夹下，重命名解压后的文件夹名为`ik`： `your-es-root/plugins/ik`

- 重启Elasticsearch即可（关闭ES：杀进程：`ps -aux|grep elasticsearch`、`kill -9 进程ID号`