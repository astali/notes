# Linux常用命令

- 查看内存使用情况：`free -h`

  -h：显示可用mb单位

- 修改文件权限：`chmod 777 *`

- 切换用户： `su root`

- 解压 tar包：`tar -xvf file.tar`

- 解压tar.gz：`tar -xzvf file.tar.gz`

- 解压tar.xz：`tar xvJf file.tar.xz`

- 解压rar：`unrar e file.rar`

- 解压zip：`unzip file.zip`

- 下载网络文件 `wget http://file.zip`

- 改变文件所有者 `chown [选项] [所有者]:[组] file`

  -R 处理指定目录以及其子目录下的所有文件

- 查看进程  `ps -ef | grep java` 或 `ps -aux | grep java`

　　-aux 显示所有状态

- 查看端口 `netstat -nap|grep 端口号`

- 终止进程 `kill -9 [PID]`

- Tomcat查看日志 `cd logs   tail -f catalina.out`

- `yum -y install` 包名（支持*） ：自动选择y，全自动

- `yum install` 包名（支持*） ：手动选择y or n

- `yum remove` 包名（不支持*）

- `rpm -ivh` 包名（支持*）：安装rpm包

- `rpm -e` 包名（不支持*）：卸载rpm包

- 更改用户名密码 : `passwd [用户名]`

- 查看端口: ` netstat -anp | grep 8080 `

- 立刻关机: `shutdown -h now`

- tomcat脚本启动 并授权:

  ```
  vim start_tomcat.sh 
  		/usr/local/tomcat/bin/start.sh  #添加内容tomcat安装目录
  chmod -R 777 /start_tomcat.sh  //授权
  ```

- 建立软链接(快捷方式)，以及硬链接的命令。
```
软链接： ln -s slink source
硬链接： ln link source
```
- 查看文件内容有哪些命令可以使用？
```
 vi 文件名 #编辑方式查看，可修改
cat 文件名 #显示全部文件内容
more 文件名 #分页显示文件内容
less 文件名 #与 more 相似，更好的是可以往前翻页
tail 文件名 #仅查看尾部，还可以指定行数
head 文件名 #仅查看头部,还可以指定行数
```
- 移动文件用哪个命令？改名用哪个命令？
```
mv mv
```
- 怎么使一个命令在后台运行?
` 一般都是使用 & 在命令结尾来让程序自动运行。(命令后可以不追加空格) `
- 使用什么命令查看磁盘使用空间？ 空闲空间呢?
`df -hl`

**使用什么命令查看网络是否连通?**
netstat
**查看各类环境变量用什么命令?**
查看所有 env
**通过什么命令查找执行命令?**
which 只能查可执行文件
whereis 只能查二进制文件、说明文档，源文件等
**怎样一页一页地查看一个大文件的内容呢？**
cat file_name.txt | more

**`netstat`命令各个参数说明如下：**

　　**-t : 指明显示TCP端口**

　　**-u : 指明显示UDP端口**

　　**-l : 仅显示监听套接字(所谓套接字就是使应用程序能够读写与收发通讯协议(protocol)与资料的程序)**

　　**-p : 显示进程标识符和程序名称，每一个套接字/端口都属于一个程序。**

　　**-n : 不进行DNS轮询，显示IP(可以加速操作)**

**即可显示当前服务器上所有端口及进程服务，于grep结合可查看某个具体端口及服务情况··**

**netstat -ntlp   //查看当前所有tcp端口·**

**netstat -ntulp |grep 80   //查看所有80端口使用情况·**

**netstat -an | grep 3306   //查看所有3306端口使用情况·**

# Linux常用命令

## 系统服务管理

### systemctl

- 启动服务：systemctl start httpd.service
- 关闭服务：systemctl stop httpd.service
- 重启服务（不管是否在运行）：systemctl restart httpd.service
- 重新载入配置（不中断服务）：systemctl reload httpd.service
- 查看运行状态：systemctl status httpd.service
- 设置开机启动：systemctl enable httpd.service
- 禁止开机启动：systemctl disable httpd.service
- 查看系统安装的服务：systemctl list-units --type=service

## 文件管理

### ls
列出/home目录下的子目录：ls -l /home

### pwd
显示当前工作目录

### cd
切换目录： cd /usr/local

### date
以指定格式显示日期；date '+date:%x time:%X'

### passwd
修改root密码：passwd root

### su
普通用户切换到超级用户：su -

### clear
清除屏幕信息

### man
查看ls命令的帮助信息：man ls

### who
- 查看当前运行级别：who -r
- 显示用的登录详情：who -buT

### free
以MB显示内存使用状态：free -m

### ps
查看系统所有进程：ps -ef
查看运行的java进程： ps -ef | grep java

### top
查看系统当前活跃进程信息

### mkdir
创建目录

### more 

分页查看  
每10行显示一屏查看：more -c -10 

### cat
查看config文件：cat -Ab config

### rm
- 删除文件：rm a.txt
- 删除文件夹： rm -rf a/

### touch
创建一个文件：touch a.txt

### cp
将目录a的文件拷贝到目录b: cp -r /home/a /home/b

### mv
移动或覆盖文件：mv a.txt b.txt

## 压缩与解压

### tar
- 打包文件夹到单独的文件：tar -cvf /opt/etc.tar /etc
- 压缩文件夹到压缩文件（gzip）：tar -zcvf /opt/etc.tar.gz /etc
- 压缩文件夹到压缩文件（bzip2）：tar -jcvf /opt/etc.tar.bz2 /etc
- 查阅压缩包中内容（gzip）：tar -ztvf /opt/etc.tar.gz /etc
- 解压文件到当前目录（gzip）：tar -zxvf /opt/etc.tar.gz

## 磁盘和网络管理

### df
查看磁盘占用情况：df -hT

### ifconfig
查看当前网络接口状态

### netstat

- 查看路由信息：netstat -rn
- 查看所有有效TCP连接：netstat -an
- 查看系统中启动的监听服务：netstat -tulnp
- 查看处于连接状态的系统资源信息：netstat -atunp

### wget
从网络上下载软件

## 软件的安装与管理

### rpm

- 安装软件包：rpm -ivh nginx-1.12.2-2.el7.x86_64.rpm
- 模糊搜索软件包：rpm -qa | grep nginx
- 精确查找软件包：rpm -qa nginx
- 查询软件包的安装路径：rpm -ql nginx-1.12.2-2.el7.x86_64
- 查看软件包的概要信息：rpm -qi nginx-1.12.2-2.el7.x86_64
- 验证软件包内容和安装文件是否一致：rpm -V nginx-1.12.2-2.el7.x86_64
- 更新软件包：rpm -Uvh nginx-1.12.2-2.el7.x86_64
- 删除软件包：rpm -e nginx-1.12.2-2.el7.x86_64

### yum

- 安装软件包： yum install nginx
- 检查可以更新的软件包：yum check-update
- 更新指定的软件包：yum update nginx
- 在资源库中查找软件包信息：yum info nginx*
- 列出已经安装的所有软件包：yum info installed
- 列出软件包名称：yum list redis*
- 模糊搜索软件包：yum search redis

## 网络安全

### iptables

- 开启防火墙：systemctl start iptables.service
- 关闭防火墙：systemctl stop iptables.service
- 查看防火墙状态：systemctl status iptables.service
- 设置开机启动：systemctl enable iptables.service
- 禁用开机启动：systemctl disable iptables.service
- 查看filter表的链信息：iptables -L -n
- 查看NAT表的链信息：iptables -t nat -L -n
- 清除防火墙所有规则：iptables -F;iptables -X;iptables -Z;
- 添加过滤规则（开发80端口）：iptables -I INPUT -p tcp --dport 80 -j ACCEPT
- 查找规则所做行号：iptables -L INPUT --line-numbers -n
- 根据行号删除过滤规则：iptables -D INPUT 1



