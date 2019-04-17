# JAVA_OPTS参数配置

Linux修改catalina.sh文件

如： JAVA_OPTS=”-server -Dfile.encoding=UTF-8 -Xms=512m -Xmx1024m -XX:PermSize=128m -XX:MaxPermSize=256m -verbose:gc -Xloggc:${CATALINA_HOME}/logs/gc.log`date +%Y-%m-%d-%H-%M` -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -noclassgc”

如： JAVA_OPTS="-server -Xms5120m -Xmx5120m -Xss256k"  

堆(Heap)和非堆(Non-heap)内存

​    按照官方的说法：“Java 虚拟机具有一个堆，堆是运行时数据区域，所有类实例和数组的内存均从此处分配。堆是在 Java 虚拟机启动时创建的。”“在JVM中堆之外的内存称为非堆内存(Non-heap memory)”。可以看出JVM主要管理两种类型的内存：堆和非堆。简单来说堆就是Java代码可及的内存，是留给开发人员使用的；非堆就是JVM留给自己用的，所以方法区、JVM内部处理或优化所需的内存(如JIT编译后的代码缓存)、每个类结构(如运行时常数池、字段和方法数据)以及方法和构造方法的代码都在非堆内存中。

**堆内存分配**

​    JVM初始分配的内存由-Xms指定，默认是物理内存的1/64；JVM最大分配的内存由-Xmx指定，默认是物理内存的1/4。默认空余堆内存小于40%时，JVM就会增大堆直到-Xmx的最大限制；空余堆内存大于70%时，JVM会减少堆直到-Xms的最小限制。因此服务器一般设置-Xms、-Xmx相等以避免在每次GC 后调整堆的大小。

**非堆内存分配**

​    JVM使用-XX:PermSize设置非堆内存初始值，默认是物理内存的1/64；由XX:MaxPermSize设置最大非堆内存的大小，默认是物理内存的1/4。 

**JVM内存限制(最大值)**

​    首先JVM内存限制于实际的最大物理内存，假设物理内存无限大的话，JVM内存的最大值跟操作系统有很大的关系。简单的说就32位处理器虽然可控内存空间有4GB,但是具体的操作系统会给一个限制，这个限制一般是2GB-3GB（一般来说Windows系统下为1.5G-2G，Linux系统下为2G-3G），而64bit以上的处理器就不会有限制了。

2. 为什么有的机器我将-Xmx和-XX:MaxPermSize都设置为512M之后tomcat可以启动，而有些机器无法启动？

   1) 参数中-Xms的值大于-Xmx，或者-XX:PermSize的值大于-XX:MaxPermSize；

   2) -Xmx的值和-XX:MaxPermSize的总和超过了JVM内存的最大限制，比如当前操作系统最大内存限制，或者实际的物理内存等等。说到实际物理内存这里需要说明一点的是，如果你的内存是1024MB，但实际系统中用到的并不可能是1024MB，因为有一部分被硬件占用了。





## tomcat 高并发配置 与优化

TOMCAT_HOME/bin/catalina.sh

添加一行：**JAVA_OPTS=" -XX:PermSize=64M -XX:MaxPermSize=128m"**

问题解决（可能为调用JAR包过多原因）下面是网上看到一些设置

**JAVA_OPTS="-server -Xms800m -Xmx800m -XX:PermSize=64M -XX:MaxNewSize=256m -XX:MaxPermSize=128m -Djava.awt.headless=true "**

 

当在对其进行并发测试时，基本上30个USER上去就当机了，还要修改默认连接数设置：以下四行参数默认是没有，手工加上就可以了，基本上可以解决连接数过大引起的死机。具体数值可跟据实际情况设置

```java
<Connector port="80" protocol="HTTP/1.1" 
               maxThreads="600"      
minSpareThreads="100"
maxSpareThreads="500"
acceptCount="700"
connectionTimeout="20000" 
redirectPort="8443" />
```

这样设置以后，基本上没有再当机过。。。。。

```java
maxThreads="600"       ///最大线程数
minSpareThreads="100"///初始化时创建的线程数
maxSpareThreads="500"///一旦创建的线程超过这个值，Tomcat就会关闭不再需要的socket线程。
acceptCount="700"//指定当所有可以使用的处理请求的线程数都被使用时，可以放到处理队列中的请求数，超过这个数的请求将不予处理
 
```

\----------------------------------------------------------------------------------------------------------------------------------

**Tomcat的JVM提示内存溢出**

查看%TOMCAT_HOME%\logs文件夹下，日志文件是否有内存溢出错误

**修改Tomcat的JVM**

**1、错误提示：java.lang.OutOfMemoryError: Java heap space**

Tomcat默认可以使用的内存为128MB，在较大型的应用项目中，这点内存是不够的，有可能导致系统无法运行。常见的问题是报Tomcat内存溢出错误，Out of Memory(系统内存不足)的异常，从而导致客户端显示500错误，一般调整Tomcat的使用内存即可解决此问题。 

Windows环境下修改“%TOMCAT_HOME%\bin\catalina.bat”文件，在文件开头增加如下设置：**set JAVA_OPTS=-Xms256m -Xmx512m** 

Linux环境下修改“%TOMCAT_HOME%\bin\catalina.sh”文件，在文件开头增加如下设置：**JAVA_OPTS=’-Xms256m -Xmx512m’** 
其中，-Xms设置初始化内存大小，-Xmx设置可以使用的最大内存。

**2、错误提示：java.lang.OutOfMemoryError: PermGen space**

**原因：**PermGen space的全称是Permanent Generation space,是指内存的永久保存区域，这块内存主要是被JVM存
放Class和Meta信息的,Class在被Loader时就会被放到PermGen space中，它和存放类实例(Instance)的
Heap区域不同,GC(Garbage Collection)不会在主程序运行期对PermGen space进行清理，所以如果你的应用
中有很CLASS的话,就很可能出现PermGen space错误，这种错误常见在web服务器对JSP进行pre compile的
时候。如果你的WEB APP下都用了大量的第三方jar, 其大小超过了jvm默认的大小(4M)那么就会产生此错误信
息了。
**解决方法：**

在Windows---catalina.bat的第一行增加：
**set JAVA_OPTS=-Xms64m -Xmx256m -XX:PermSize=128M -XX:MaxNewSize=256m -XX:MaxPermSize=256m**
在Linux ---catalina.sh的第一行增加：
**JAVA_OPTS=-Xms64m -Xmx256m -XX:PermSize=128M -XX:MaxNewSize=256m -XX:MaxPermSize=256m**

**3、JVM设置**

堆的尺寸 
-Xmssize in bytes 
    设定Java堆的初始尺寸，缺省尺寸是2097152 (2MB)。这个值必须是1024个字节（1KB）的倍数，且比它大。（-server选项把缺省尺寸增加到32M。） 
-Xmnsize in bytes 
    为Eden对象设定初始Java堆的大小，缺省值为640K。（-server选项把缺省尺寸增加到2M。) 
-Xmxsize in bytes 
    设定Java堆的最大尺寸，缺省值为64M，（-server选项把缺省尺寸增加到128M。） 最大的堆尺寸达到将近2GB（2048MB）。 

请注意：很多垃圾收集器的选项依赖于堆大小的设定。请在微调垃圾收集器使用内存空间的方式之前，确认是否已经正确设定了堆的尺寸。 

垃圾收集:内存的使用 
-XX:MinHeapFreeRatio=percentage as a whole number 
    修改垃圾回收之后堆中可用内存的最小百分比，缺省值是40。如果垃圾回收后至少还有40%的堆内存没有被释放，则系统将增加堆的尺寸。 
-XX:MaxHeapFreeRatio=percentage as a whole number 
    改变垃圾回收之后和堆内存缩小之前可用堆内存的最大百分比，缺省值为70。这意味着如果在垃圾回收之后还有大于70%的堆内存，则系统就会减少堆的尺寸。 
-XX:NewSize=size in bytes 
    为已分配内存的对象中的Eden代设置缺省的内存尺寸。它的缺省值是640K。（-server选项把缺省尺寸增加到2M。） 
-XX:MaxNewSize=size in bytes 
    允许您改变初期对象空间的上限，新建对象所需的内存就是从这个空间中分配来的，这个选项的缺省值是640K。（-server选项把缺省尺寸增加到2M。） 
-XX:NewRatio=value 
    改变新旧空间的尺寸比例，这个比例的缺省值是8，意思是新空间的尺寸是旧空间的1/8。 
-XX:SurvivorRatio=number 
    改变Eden对象空间和残存空间的尺寸比例，这个比例的缺省值是10，意思是Eden对象空间的尺寸比残存空间大survivorRatio+2倍。 
-XX:TargetSurvivorRatio=percentage 
    设定您所期望的空间提取后被使用的残存空间的百分比，缺省值是50。 
-XX:MaxPermSize=size in MB 
    长久代（permanent generation）的尺寸，缺省值为32（32MB）。

**Tomcat连接数设置**

在tomcat配置文件server.xml中的<Connector ... />配置中，和连接数相关的参数有：
**minProcessors**：最小空闲连接线程数，用于提高系统处理性能，默认值为10
**maxProcessors**：最大连接线程数，即：并发处理的最大请求数，默认值为75
**acceptCount**：允许的最大连接数，应大于等于maxProcessors，默认值为100
**enableLookups**：是否反查域名，取值为：true或false。为了提高处理能力，应设置为false
**connectionTimeout**：网络连接超时，单位：毫秒。设置为0表示永不超时，这样设置有隐患的。通常可设置为30000毫秒。

其中和最大连接数相关的参数为maxProcessors和acceptCount。如果要加大并发连接数，应同时加大这两个参数。 



## **配置gc日志**

TOMCAT_HOME/bin/catalina.sh

```bash
-XX:+PrintGC  // 开启gc日志监控
-XX:+PrintGCDetails // 可以详细了解GC中的变化
-XX:+PrintGCTimeStamps  // 可以了解这些垃圾收集发生的时间，自JVM启动以后以秒计量
-XX:+PrintGCDateStamps  // GC发生的时间信息
-XX:+PrintHeapAtGC  // 了解堆的更详细的信息
-Xloggc:/opt/tomcat/logs/gc.log
```

**JAVA_OPTS="-Xloggc:../logs/gc_$$.log**"