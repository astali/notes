save 配置
```c
CONFIG GET save         #查看redis持久化配置
CONFIG SET save "21600 1000" #修改redis持久化配置
```
RDB 持久化配置
```c
save 900 1
save 300 10
save 60 10000
dbfilename "dump.rdb"          #持久化文件名称
dir "/data/dbs/redis/6381"    #持久化数据文件存放的路径
```
RDB持久化也分两种：SAVE和BGSAVE

      SAVE是阻塞式的RDB持久化，当执行这个命令时redis的主进程把内存里的数据库状态写入到RDB文件（即上面的dump.rdb）中，直到该文件创建完毕的这段时间内redis将不能处理任何命令请求。

      BGSAVE属于非阻塞式的持久化，它会创建一个子进程专门去把内存中的数据库状态写入RDB文件里，同时主进程还可以处理来自客户端的命令请求。但子进程基本是复制的父进程，这等于两个相同大小的redis进程在系统上运行，会造成内存使用率的大幅增加。
AOF 持久化配置
```c
dir "/data/dbs/redis/6381"           #AOF文件存放目录
appendonly yes                       #开启AOF持久化，默认关闭
appendfilename "appendonly.aof"      #AOF文件名称（默认）
appendfsync no                       #AOF持久化策略
auto-aof-rewrite-percentage 100      #触发AOF文件重写的条件（默认）
auto-aof-rewrite-min-size 64mb       #触发AOF文件重写的条件（默认）
```