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

  ​



