## Java应用频繁FullGC分析

https://yq.aliyun.com/tags/type_blog-tagid_17521/)

*摘要：* ### 一、JVM的内存分布 #### 1.1 JVM内存分布概况 ![img](https://img.alicdn.com/tfs/TB1bhRnRFXXXXa2XVXXXXXXXXXX-510-401.png) 

* 堆内存划分为： Eden、Survivor 和 Tenured/Old 空间 ![img](https://img.alicdn.com/tfs/TB1EVh

### 一、JVM的内存分布

#### 1.1 JVM内存分布概况

![img](https://img.alicdn.com/tfs/TB1bhRnRFXXXXa2XVXXXXXXXXXX-510-401.png)

- 堆内存划分为： Eden、Survivor 和 Tenured/Old 空间
  ![img](https://img.alicdn.com/tfs/TB1EVhqRFXXXXcFXFXXXXXXXXXX-583-183.png)

#### 1.2 Minor GC、Major GC、Full GC

#### 1.3 JVM垃圾回收算法

![img](https://img.alicdn.com/tfs/TB1z9BsRFXXXXXzXVXXXXXXXXXX-865-704.png)

### 二、应用的GC日志配置

#### 2.1 应用GC日志配置

JVM的GC日志的主要参数包括如下几个：

```
-XX:+PrintGC 输出GC日志
-verbose:gc 示输出虚拟机中GC的详细情况
-XX:+PrintGCDetails 输出GC的详细日志
-XX:+PrintGCTimeStamps 输出GC的时间戳（以基准时间的形式）
-XX:+PrintGCDateStamps 输出GC的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）
-XX:+PrintHeapAtGC 在进行GC的前后打印出堆的信息
-Xloggc:../logs/gc.log 日志文件的输出路径
```

-verbose:gc 中参数-verbose:gc 表示输出虚拟机中GC的详细情况.

使用后输出如下:

```
[Full GC 168K->97K(1984K)， 0.0253873 secs]
```

解读如下:

　　箭头前后的数据168K和97K分别表示垃圾收集GC前后所有存活对象使用的内存容量，说明有168K-97K=71K的对象容量被回收，括号内的数据1984K为堆内存的总容量，收集所需要的时间是0.0253873秒（这个时间在每次执行的时候会有所不同）

##### 2.2 线上应用配置实例

![img](https://img.alicdn.com/tfs/TB1l8dyRFXXXXX_XFXXXXXXXXXX-1343-216.png)

#### 2.2 应用GC日志分析

```
2017-06-02T15:10:11.930+0800: 68752.147: [GC2017-06-02T15:10:11.930+0800: 68752.147: [ParNew: 1679677K->1878K(1887488K), 0.0176620 secs] 2204253K->526489K(6753536K), 0.0178770 secs] [Times: user=0.07 sys=0.00, real=0.02 secs]
2017-06-02T15:10:18.522+0800: 68758.739: [GC2017-06-02T15:10:18.522+0800: 68758.739: [ParNew: 1679702K->2122K(1887488K), 0.0184380 secs] 2204313K->526767K(6753536K), 0.0186610 secs] [Times: user=0.06 sys=0.00, real=0.02 secs]
2017-06-02T15:10:22.812+0800: 68763.029: [GC2017-06-02T15:10:22.812+0800: 68763.030: [ParNew: 1679946K->2104K(1887488K), 0.0166490 secs] 2204591K->526796K(6753536K), 0.0168640 secs] [Times: user=0.06 sys=0.00, real=0.01 secs]
2017-06-02T15:10:29.874+0800: 68770.091: [GC2017-06-02T15:10:29.874+0800: 68770.091: [ParNew: 1679928K->1646K(1887488K), 0.0174360 secs] 2204620K->526439K(6753536K), 0.0176530 secs] [Times: user=0.06 sys=0.00, real=0.02 secs]
```

取倒数第一条记录分析一下各个字段都代表了什么含义

```
2017-06-02T15:10:29.874+0800: 68770.091: （时间）[GC（Young GC）2017-06-02T15:10:29.874+0800: 68770.091: [ParNew（）使用ParNew作为年轻代的垃圾回收）: 1679928K（年轻代垃圾回收前的大小）->1646K年轻代垃圾回收以后的大小）(1887488K)（年轻代的总大小）, 0.0174360 secs（回收时间）]] 2204620K（堆区垃圾回收前的大小）->526439K（堆区垃圾回收后的大小）(6753536K（堆区总大小), 0.0176530 secs（回收时间）] [Times: user=0.06Young GC用户耗时） sys=0.00（Young GC系统耗时）, real=0.02 secsYoung GC实际耗时）]
```

我们再对数据做一个简单的分析:

```
从最后一条GC记录中我们可以看到 Young GC回收了 1679928-1646=1678282K的内存
Heap区通过这次回收总共减少了 2204620-526439=1678181K的内存。

1678282-1678181=101K说明通过该次Young GC有101K的内存被移动到了Old Gen
```

我们来验证一下

```
在最后一次Young GC的回收以前 Old Gen的大小为526796（倒数第二条堆内存）-2104=524692 <br/>
回收以后Old Gen的内存使用为526439-1646=524793
Old Gen在该次Young GC以后内存增加了524793-524692=10K 与预计的相符
```

### 三、常见GC查看工具

![img](https://img.alicdn.com/tfs/TB1I0XIRFXXXXXXXpXXXXXXXXXX-930-564.png)