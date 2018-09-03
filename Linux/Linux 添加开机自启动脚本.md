Linux设置服务开机自动启动的方式有好多种，这里介绍一下通过chkconfig命令添加脚本为开机自动启动的方法。

**1. 编写脚本autostart.sh（这里以开机启动redis服务为例），脚本内容如下：**

```
#!/bin/sh
#chkconfig: 2345 80 90
#description:开机自动启动的脚本程序

# 开启redis服务 端口为6379
/usr/local/service/redis-2.8.3/src/redis-server --port 6379 &
1234567
```

脚本第一行 “#!/bin/sh” 告诉系统使用的shell； 
脚本第二行 “#chkconfig: 2345 80 90” 表示在2/3/4/5运行级别启动，启动序号(S80)，关闭序号(K90)； 
脚本第三行 表示的是服务的描述信息

**注意：** 第二行和第三行必写，负责会出现如“**服务 autostart.sh 不支持 chkconfig**”这样的错误。

**2. 将写好的autostart.sh脚本移动到/etc/rc.d/init.d/目录下**

**3. 给脚本赋可执行权限**

```
cd /etc/rc.d/init.d/
chmod +x autostart.sh12
```

**4. 添加脚本到开机自动启动项目中**

```
chkconfig --add autostart.sh
chkconfig autostart.sh on
```