

chown：用来更改某个目录或文件的用户名和用户组

 chmod: 用来更改某个目录或文件的访问权限

首先通过 `ll`命令查看目录下文件

```
-rw-------. 1 root root 1289 6月   1 2017 anaconda-ks.cfg
-rw-r--r--. 1 root root  786 6月   8 2017 app_account.sql
drwxr-xr-x. 8 root root 4096 2月  10 2018 homestead_server
-rw-r--r--. 1 root root 7995 6月   1 2017 install.log
-rw-r--r--. 1 root root 3384 6月   1 2017 install.log.syslog
-rw-r--r--. 1 root root    0 8月  20 12:37 mysqlaccess.log
-rw-r--r--. 1 root root   55 8月  24 17:25 test.txt
```

主要看最前面一列,我把`drwxr-xr-x.  `拿出来说，

```
d:目录
rwx:可读、可写、可执行   2-4位
r-x:可读、可执行	      5-7位
r-x：可读、可执行       8-10位
```

可见一共有十位。`-[rw-][r--][r--]`.其中第一个[-]代表的是类型，其中第一位为d代表目录，

每三位代表一个权限位

2-4位代表**所有者**拥有的权限

5-7位代表**群组**拥有的权限

8-10位代表**其他人**拥有的权限

第三和第四列两个root ,分别代表用户名和用户组；

```
useradd yu_test        //添加用户yu_test
chown yu_test test.txt //改变文件用户名权限
-rw-r--r--. 1 yu_test root   55 8月  24 17:25 test.txt
```

**十进制表示**

```
r ： 4 
w ： 2
x ： 1
- :  0 
```

将rwx看成是二进制的数，有用1表示，没有用0表示，那么`rwx r-x r--`就可以表示成：`111 101 100`,将其转换成为一个十进制数就是：`754`。看到`777` 和`rwxrwxrwx`是一样的

**权限操作**

```
+ 表示添加权限
- 表示删除权限
= 重置权限
```

**修改文件权限**

```
chmod o+w test.txt ：表示给其他人授予写test.txt这个文件的权限
chmod go-rw test.txt : 表示群组和其他人删除对test.txt文件的读写权限
chmod ugo+r test.txt：所有人皆可读取
chmod a+r text.txt:所有人皆可读取
chmod ug+w,o-w text.txt:设为该档案拥有者，与其所属同一个群体者可写入，但其他以外的人则不可写入
chmod u+x test.txt: 创建者拥有执行权限 
chmod -R a+r ./www/ ：将www下的所有档案与子目录皆设为任何人可读取
chmod a-x test.txt :收回所有用户的对test.txt的执行权限
chmod 777 test.txt: 所有人可读，写，执行
```

```
u：代表文件所有者(user)
g:代表所有者所在的群组(group)
o：代表其他人，但不是u和g(other)
a：a和一起指定ugo效果一样
```

**修改目录权限**

```
chmod 700 /opt/elasticsearch  #修改目录权限
chmod -R 744 /opt/elasticsearch #修改目目录以下所有的权限   
-R             # 以递归方式更改所有的文件及子目录
```

**chown 修改用户组**  

修改 test.txt 目录所属用户为 root，用户组为 root

```
chown -R root:root test.txt   
-rw-r--r--. 1 root root   55 8月  24 17:25 test.txt
```

常见权限**

```
-rw------- (600) 只有所有者才有读和写的权限。
-rw-r--r-- (644) 只有所有者才有读和写的权限，群组和其他人只有读的权限。
-rw-rw-rw- (666) 每个人都有读写的权限
-rwx------ (700) 只有所有者才有读，写和执行的权限。
-rwx--x--x (711) 只有所有者才有读，写和执行的权限，群组和其他人只有执行的权限。
-rwxr-xr-x (755) 只有所有者才有读，写，执行的权限，群组和其他人只有读和执行的权限。
-rwxrwxrwx (777) 每个人都有读，写和执行的权限
```

**实践**

```
chmod 400 test.txt   #修改text.txt为可读文件
vi text.txt  #执行该命令后，该文件就无法进行写入操作 提示下面信息
-- INSERT -- W10: Warning: Changing a readonly file
chmod 777 text.txt 
-rwxrwxrwx. 1 root root   55 8月  24 17:25 test.txt
chmod rwxr--r-- test.txt   #异常，不能使用该命令来修改权限
```

最后了解一下



