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