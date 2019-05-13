## Linux性能测试与调优的15条常用命令

http://www.yunweipai.com/archives/28317.html)

## **# CPU性能评估**

通过下面的命令能了解到CPU是否出现性能瓶颈，再结合top、ps等命令进一步检查，即可定位到那些进程导致CPU负载过大

**vmstat命令：查看CPU负载**

> [blackfox@localhost ~]$ vmstat 2 3 # 间隔2s,打印3次
>
> procs ———–memory———- —swap– —–io—- -system– ——cpu—–
>
> r b swpd free buff cache si so bi bo in cs us sy id wa st
>
> 4 0 0 244824 932 497892 0 0 0 0 3 9 0 0 100 0 0
>
> 0 0 0 244824 932 497892 0 0 0 0 38 91 0 1 99 0 0
>
> 0 0 0 244824 932 497892 0 0 0 0 40 90 0 1 99 0 0

**sar命令：统计CPU性能**

场景：在一个多CPU的系统中，CPU的整体使用率不高，但是系统应用响应缓慢。 结论：单线程只使用一个CPU，导致这个CPU占用率为100%，无法处理其他请求，而其他的CPU却闲置，这就导致了整体CPU使用率不高，而应用缓慢现象的发生。

> \# sar -P 0 3 5 # 对第一个CPU进行统计
>
> [blackfox@localhost ~]$ sar -u 3 5 # 显示CPU利用率，间隔3s，显示5次
>
> Linux 3.10.0-327.el7.x86_64 (localhost.localdomain) 01/22/2017 _x86_64_	(1 CPU)
>
> 07:35:52 AM CPU %user %nice %system %iowait %steal %idle
>
> 07:35:55 AM all 0.00 0.00 0.00 0.00 0.00 100.00
>
> 07:35:58 AM all 0.00 0.00 0.34 0.00 0.00 99.66
>
> 07:36:01 AM all 0.34 0.00 0.34 0.00 0.00 99.32
>
> 07:36:04 AM all 0.00 0.00 0.34 0.00 0.00 99.66
>
> 07:36:07 AM all 0.34 0.00 0.00 0.00 0.00 99.66
>
> Average: all 0.14 0.00 0.20 0.00 0.00 99.66

**iostat命令：查看CPU使用情况**

> [blackfox@localhost ~]$ iostat -c
>
> Linux 3.10.0-327.el7.x86_64 (localhost.localdomain) 01/22/2017 _x86_64_	(1 CPU)
>
> avg-cpu: %user %nice %system %iowait %steal %idle
>
> 0.13 0.00 0.23 0.01 0.00 99.63

**uptime命令：1、5、15分钟平均负载**

经验：8核CPU，load average三个值长期大于8，说明负载很高，会影响系统性能。

> [blackfox@localhost ~]$ uptime
>
> 07:54:27 up 17 days, 16:56, 1 user, load average: 0.00, 0.02, 0.05

## **# 内存性能评估**

**free命令：查看内存使用情况**

经验：可用内存/物理内存<20%，说明内存紧缺，需要增加内存

> [blackfox@localhost ~]$ free -m # -m 以MB为单位，-s 时间段内不间断监控
>
> total used free shared buff/cache available
>
> Mem: 977 249 240 49 487 488
>
> Swap: 2048 0 2048

**vmstat命令：监控内存**

**sar -r命令：监控内存**

**# 磁盘I/O性能评估**

**sar -d命令：统计磁盘I/O状态**

经验：

- 1、正常svctm<await。
- 2、svctm接近await表示几乎没有I/O等待，性能很好
- 3、cpu/内存复合/过多请求都会使svctm增加
- 4、%util接近100%，表示I/O满负荷。

> [blackfox@localhost ~]$ sar -d
>
> Linux 3.10.0-327.el7.x86_64 (localhost.localdomain) 01/22/2017 _x86_64_	(1 CPU)
>
> 12:00:01 AM DEV tps rd_sec/s wr_sec/s avgrq-sz avgqu-sz await svctm %util
>
> 12:10:01 AM dev8-0 0.10 0.00 0.89 9.03 0.00 3.97 1.10 0.01
>
> 12:20:01 AM dev8-0 0.05 0.00 0.35 7.56 0.00 64.63 61.70 0.28
>
> 12:30:01 AM dev8-0 0.02 0.00 0.16 7.08 0.01 254.77 128.

**iostat -d命令：**

> [blackfox@localhost ~]$ iostat -dx /dev/sda3
>
> Linux 3.10.0-327.el7.x86_64 (localhost.localdomain) 01/22/2017 _x86_64_	(1 CPU)
>
> Device: rrqm/s wrqm/s r/s w/s rkB/s wkB/s avgrq-sz avgqu-sz await r_await w_await svctm %util
>
> sda3 0.00 0.01 0.01 0.05 0.21 0.34 17.98 0.00 41.36 30.56 42.60 7.77 0.05

## **# 网络性能评估**

**ping命令：检测网络连通**

**netstat命令：-i 查看网络接口信息，-r 检测系统路由表信息**

**sar -n命令：显示系统网络运行状态**

[blackfox@localhost ~]$ sar -n DEV 2 3 # DEV：网络接口，EDEV：网络错误统计数据，SOCK：套接字信息，FULL：显示所有

> Linux 3.10.0-327.el7.x86_64 (localhost.localdomain) 01/22/2017 _x86_64_	(1 CPU)
>
> 09:26:26 AM IFACE rxpck/s txpck/s rxkB/s txkB/s rxcmp/s txcmp/s rxmcst/s
>
> 09:26:28 AM eno16777736 0.00 0.00 0.00 0.00 0.00 0.00 0.00
>
> 09:26:28 AM lo 0.00 0.00 0.00 0.00 0.00 0.00 0.00

**traceroute命令：跟踪数据包传输路径**

**nslookup命令：判断DNS解析信息**

## **# 动态监控性能**

**watch命令：动态监控，默认2秒钟执行一次，执行结果更新在屏幕上**

> [blackfox@localhost ~]$ watch -n 3 -d free # -n 重复执行时间，-d 高亮显示变动
>
> Every 3.0s: free Sun Jan 22 09:21:48 2017
>
> total used free	shared buff/cache available
>
> Mem: 1001332	256792 245500	50948 499040	498864
>
> Swap: 2098172 0 2098172

