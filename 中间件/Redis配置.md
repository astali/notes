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
#### 怎么禁用或重命名危险命令？

看下 `redis.conf` 默认配置文件，找到 `SECURITY` 区域，如以下所示。

**1）禁用命令**

```
rename-command KEYS     ""
rename-command FLUSHALL ""
rename-command FLUSHDB  ""
rename-command CONFIG   ""
```



## bigkeys

随着项目越做越大，缓存使用越来越不规范。我们如何检查生产环境上一些有问题的数据。`bigkeys` 就派上用场了，用法如下：

```
redis-cli -p 6380 --bigkeys
```

执行结果如下：

```
... ...
-------- summary -------

Sampled 526 keys in the keyspace!
Total key length in bytes is 1524 (avg len 2.90)

Biggest string found 'test' has 10005 bytes
Biggest   list found 'commentlist' has 13 items

524 strings with 15181 bytes (99.62% of keys, avg size 28.97)
2 lists with 19 items (00.38% of keys, avg size 9.50)
0 sets with 0 members (00.00% of keys, avg size 0.00)
0 hashs with 0 fields (00.00% of keys, avg size 0.00)
0 zsets with 0 members (00.00% of keys, avg size 0.00)
```

最后 5 行可知，没有 set,hash,zset 几种数据结构的数据。string 类型有 524 个，list 类型有两个；通过 `Biggest ... ...`可知，最大 string 结构的 key 是 `test` ，最大 list 结构的 key 是`commentlist`。

需要注意的是，这个**bigkeys得到的最大，不一定是最大**。说明原因前，首先说明 `bigkeys` 的原理，非常简单，通过 scan 命令遍历，各种不同数据结构的 key ，分别通过不同的命令得到最大的 key：

- 如果是 string 结构，通过 `strlen` 判断；
- 如果是 list 结构，通过 `llen` 判断；
- 如果是 hash 结构，通过 `hlen` 判断；
- 如果是 set 结构，通过 `scard` 判断；
- 如果是 sorted set 结构，通过 `zcard` 判断。
