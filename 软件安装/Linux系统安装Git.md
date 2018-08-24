安装上传和下载命令：**yum install -y lrzsz**

# **Git源码**

1. 安装依赖的包 
   yum install curl-devel expat-devel gettext-devel openssl-devel zlib-devel gcc perl-ExtUtils-MakeMaker
2. 下载git源码并解压 
   解压 tar zxvf git-2.11.0.tar.gz 
   cd git-2.11.0
3. 编译安装 
   make prefix=/usr/local/git all 
   make prefix=/usr/local/git install
4. 查看git 
   whereis git 
   git –version
5. 配置环境变量 
   vim /etc/profile 
   加入export PATH=$PATH:/usr/local/git/bin 
   生效配置文件 source /etc/profile.alipay.com/270/105899/


6.tomcat.sh 脚本生成

vim start_tomcat.sh  --->/usr/local/tomcat/bin/start.sh --->chmod -R 777 /start_tomcat.sh







 