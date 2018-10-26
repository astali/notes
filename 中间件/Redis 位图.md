> **导读**
>     位图引出
>     位图命令详解
>     位图应用场景
>     位图如何存亿级数据

![img](https://mmbiz.qpic.cn/mmbiz_png/XQOjSBdpFjkAlxSAzaFQak4gVNlbiakvFBnMHjgn4ljZMv82KXfQJDPEGVotQTUheUnhJw2iazln2xiaNmkhyszvg/?wx_fmt=png) 

位图引出

![img](https://mmbiz.qpic.cn/mmbiz_png/XQOjSBdpFjkAlxSAzaFQak4gVNlbiakvFRgqkZ93QBbQFNn7xQ4JsKHBvvOv2nVAb6IVm5xyhcIlQ0otbqoiaYGA/?wx_fmt=png)

在我们平时开发过程中，会有一些 bool 型数据需要存取，比如用户一年的签到记录，签了是 1，没签是 0，要记录 365 天。如果使用普通的 key/value，每个用户要记录 365 个，当用户上亿的时候，需要的存储空间是惊人的。 为了解决这个问题，Redis 提供了位图数据结构，这样每天的签到记录只占据一个位，365 天就是 365 个位，46 个字节 (一个稍长一点的字符串) 就可以完全容纳下，这就大大节约了存储空间。

![img](https://mmbiz.qpic.cn/mmbiz_gif/pv2bs6eV9FnXDoEM1y4cD954WIWK0kMicic8nkLWDeclP6ubicxWAQpDicueAPCWsSVJTIibob2JyNRYcurGmJBppmw/?wx_fmt=gif)

![img](https://mmbiz.qpic.cn/mmbiz_png/XQOjSBdpFjkAlxSAzaFQak4gVNlbiakvFBnMHjgn4ljZMv82KXfQJDPEGVotQTUheUnhJw2iazln2xiaNmkhyszvg/?wx_fmt=png) 

位图命令详解

![img](https://mmbiz.qpic.cn/mmbiz_png/XQOjSBdpFjkAlxSAzaFQak4gVNlbiakvFRgqkZ93QBbQFNn7xQ4JsKHBvvOv2nVAb6IVm5xyhcIlQ0otbqoiaYGA/?wx_fmt=png)

这种位图数据结构是属于String类型

```
阿里云:0>set muzidao muzidao
"OK"
阿里云:0>bitcount muzidao     #字符串中bit值为1的个数
"31"
```

我把画出muzidao的二进制图如下，便一目了然, 操作众多，请详读

![img](https://mmbiz.qpic.cn/mmbiz_png/pv2bs6eV9Fnb7wLd3byfw2X0JxCwk5Y8hVnp0jnEvGPCFXEicbbF4bPK7wbP1Ks2VqzGITB19icO3wFzFuSPcq8w/?wx_fmt=png)

每个字母对应8位的二进制码，不满8位在前面自动补全0，为1的数量确实是31

##### **BITCOUNT key [start end]**

计算给定字符串中，被设置为 1 的比特位的数量。

一般情况下，给定的整个字符串都会被进行计数，通过指定额外的 start 或 end 参数，可以让计数只在特定的位上进行。

 比如 -1 表示最后一个字节， -2 表示倒数第二个字节，以此类推。

对一个不存在的 key 进行 BITCOUNT 操作，结果为 0 。

##### 返回值：被设置为 1 的位的数量。

```
阿里云:0>bitcount muzidao     #字符串中bit值为1的个数
"31"
阿里云:0>bitcount muzidao 0 5  #前五个字符为1的个数
"25"
```

**GETBIT key offset**

对 key 所储存的字符串值，获取指定偏移量上的位(bit)。

当 offset 比字符串值的长度大，或者 key 不存在时，返回 0 。

##### 返回值：字符串值指定偏移量上的位(bit)。

```
阿里云:0>getbit muzidao 0    #偏移量0上的二进制数值
"0"
阿里云:0>getbit muzidao 1    #偏移量1上的二进制数值
"1"
```

**SETBIT key offset value**

对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。

位的设置或清除取决于 value 参数，只能 0 或 1 。其它值异常

offset 参数必须0到 2^32 (bit 映射被限制在 512 MB 之内)。

对使用大的 offset 的 SETBIT 操作来说，内存分配可能造成 Redis 服务器被阻塞。

##### 返回值：字符串值指定偏移量上原来储存的位(bit)。

```
阿里云:0>setbit muzi 0 1
"0"
阿里云:0>setbit muzi 0 2    # 异常,不能设置2
"ERR bit is not an integer or out of range"
阿里云:0>setbit muzi 0 0
"1"
阿里云:0>getbit muzi 0 
"0"
阿里云:0>setbit muzi 0 1
"0"
阿里云:0>getbit muzi 0 
"1"
```

当我们设置多个偏移量的时候，数据就会变成16进制

![img](https://mmbiz.qpic.cn/mmbiz_png/pv2bs6eV9FnXDoEM1y4cD954WIWK0kMicomSPD0dXyg75B9HPibPND1I1Kk3MILltgUMicFbUMZePiaDOAc4gfJAibg/?wx_fmt=png)

通过命令看一下

```
阿里云:0>get muzi
"� "
```

##### **BITPOS key [start end]**

返回字符串里面第一个被设置为 1 或 0 的offset偏移量。

返回一个位置，把字符串当做一个从左到右的字节数组，第一个符合条件的在位置 0、8、16， +8类推

```
阿里云:0>bitpos muzidao 0    #第0位的offset的值
"0"
阿里云:0>bitpos muzidao 1    #第1位的offset的值
"1"
阿里云:0>bitpos muzidao 1 0  #offset值为1并从第0位开始
"1"
阿里云:0>bitpos muzidao 1 2  #offset值为1并从第2位开始
"17"
```

**3.2版本新增功能**

**BITFIELD key    [GET type offset]**

**BITFIELD key    [SET type offset value]**

**BITFIELD key    [INCRBY type offset increment]** 

##### **BITFIELD key    [OVERFLOW WRAP|SAT|FAIL]**

BITFIELD 命令可以在一次调用中同时对多个位范围进行操作： 它接受一系列待执行的操作作为参数， 并返回一个数组作为回复， 数组中的每个元素就是对应操作的执行结果。

BITFIELD 命令最大支持64 位长的有符号整数以及 63 位长的无符号整数， 其中无符号整数的 63 位长度限制是由于 Redis 协议目前还无法返回 64 位长的无符号整数而导致的。

```
阿里云:0>get muzidao
"muzidao"
阿里云:0>bitfield muzidao get i4 0  #i:有符号位 4:连续取4位 0:开始位置
 1)  "6"
阿里云:0>bitfield muzidao get i4 2  #i:有符号位 4:连续取4位 2:开始位置
 1)  "-5"
阿里云:0>bitfield muzidao get u4 0  #u:无符号 4连续取4位 0:开始位置
 1)  "6"
阿里云:0>bitfield muzidao get u4 2
 1)  "11"
阿里云:0>bitfield muzidao get i4 0 get i4 2 get u4 0 get u4 2
 1)  "6"
 2)  "-5"
 3)  "6"
 4)  "11"
阿里云:0>bitfield muzidao get u64 2 1
"ERR Invalid bitfield type. Use something like i16 u8. Note that u64 is not supported but i64 is."
```

**get i4 0****:** 从0开始取4位即0110,有符号/无符号转十进制为6, 1*2^2+1*2^1 = 6, 结果一致

**get i4 2：** 从2开始取4位即1011,有符号位转十进制为-5，由于是有符号第一位为1，所以减一得1010，反转得0101，1*2^2 + 1*2^0 = 5,由于反转，最终结果-5,结果一致

**get u4 2****:** 从2开始取4位即1011,无符号转十进制为11，由于无符号，不需要进行转换，直接算结果1*2^3 + 1 *２^1 + 1*2 ^0 =11 结果一致

```
阿里云:0>bitfield muzidao set u4 8 920 #u无符号 4连续4位 8位开始 920新值
 1)  "7"
阿里云:0>get muzidao
"m�zidao"
阿里云:0>bitfield muzidao set u8 56 119 #u无符号 8连续8位 56位开始 119新值
 1)  "0"
阿里云:0>get muzidao
"m�zidaow"
```

920不知道对应ASCII的什么值，119在ASCII值对应的是w

再看第三个子指令 incrby，它用来对指定范围的位进行自增操作。既然提到自增，就有可能出现溢出。如果增加了正数，会出现上溢，如果增加的是负数，就会出现下溢出。Redis 默认的处理是折返。如果出现了溢出，就将溢出的符号位丢掉。如果是 8 位无符号数 255，加 1 后就会溢出，会全部变零。如果是 8 位有符号数 127，加 1 后就会溢出变成 -128。

```
阿里云:0>set muzidao muzidao
"OK"
阿里云:0>get muzidao
"muzidao"
阿里云:0>bitfield muzidao  incrby u4 2 1  #2位开始连续4位无符号自增
 1)  "12"
阿里云:0>get muzidao
"quzidao"
```

1011 自增为 1100 = 1* 2^3 + 1* 2^2 = 12

WRAP ： 使用回绕（wrap around）方法处理有符号整数和无符号整数的溢出情况。 对于无符号整数来说， 回绕就像使用数值本身与能够被储存的最大无符号整数执行取模计算， 这也是 C 语言的标准行为。 对于有符号整数来说， 上溢将导致数字重新从最小的负数开始计算， 而下溢将导致数字重新从最大的正数开始计算。 比如说， 如果我们对一个值为 127 的 i8 整数执行加一操作， 那么将得到结果 -128 。

SAT ： 使用饱和计算（saturation arithmetic）方法处理溢出， 也即是说， 下溢计算的结果为最小的整数值， 而上溢计算的结果为最大的整数值。 举个例子， 如果我们对一个值为 120 的 i8 整数执行加 10 计算， 那么命令的结果将为 i8 类型所能储存的最大整数值 127 。 与此相反， 如果一个针对 i8 值的计算造成了下溢， 那么这个 i8值将被设置为 -127 。

FAIL ： 在这一模式下， 命令将拒绝执行那些会导致上溢或者下溢情况出现的计算， 并向用户返回空值表示计算未被执行。

![img](https://mmbiz.qpic.cn/mmbiz_png/XQOjSBdpFjkAlxSAzaFQak4gVNlbiakvFBnMHjgn4ljZMv82KXfQJDPEGVotQTUheUnhJw2iazln2xiaNmkhyszvg/?wx_fmt=png) 

**应用场景**

![img](https://mmbiz.qpic.cn/mmbiz_png/XQOjSBdpFjkAlxSAzaFQak4gVNlbiakvFRgqkZ93QBbQFNn7xQ4JsKHBvvOv2nVAb6IVm5xyhcIlQ0otbqoiaYGA/?wx_fmt=png)

我清楚记得的我当时用得到App的时候，我每天都会登录，但是没有签到哦，连续登录7天后，会给你送一本电子书，连续登录30天又会送你一个电子书，有时候还会提示登陆超50天送你一本电子书，我觉得这个场景就非常适合使用位图，某天登录存1，不登录为0。计算登录次数（bitcount）

位图适合存bool数据，当某个业务只有两种结果的时候，位图是不二之选

**为什么能存亿级数据呢？**

考虑在redis中放一个key,它的value很大很大，大到它的二级制位数大于最大的用户id，redis中单个key的最大值是512M，可以达到4,294,967,296bit，足够很多业务的需要了，我们以用户id作为offset,该offset的值作为是否活跃的值即可达到我们的目的。这样只需要一个key就能解决对所有数据的查询问题。假设我们的id最大值是5亿，那么我们需要5亿个bit就行了，相当于只需要5亿/(8*1024*1024)≈59.6M内存。了解内存转换就一目了然, 

单位换算：

　　1Byte = 8 Bit

　　1KB = 1024Byte

　　1MB = 1024KB

　　1GB = 1024MB

　　1TB = 1024GB

我们来看看为何能存42亿之多,单key最大值是512M,通过换算得出

512(M) * 1024 (Kb) * 1024(Byte) * 8(Bit) = **4,294,967,296（bit）****=** **2 ^32**

首先看一下机器内存

```
[root@astali redis-3.2.6]# free       #未执行命令setbit testmax 4294967295 1
              total        used        free      shared  buff/cache   available
Mem:        1883496      699516      650264         436      533716     1022744
Swap:             0           0 
```

```
total：内存总数；
used：已经使用的内存数；
free：空闲的内存数；
shared：当前已经废弃不用；
buffers Buffer：缓存内存数；
cached Page：缓存内存数。
```

执行走一波，超过最大4294967296 异常，offset值[0,4294967296 )

```
阿里云:0>setbit testmax 4294967296 1
"ERR bit offset is not an integer or out of range"
阿里云:0>setbit testmax 4294967295 1
"0"
```

执行setbit testmax 4294967295 1 后的内存如下，空闲内存由650264减少至124984

```
[root@astali redis-3.2.6]# free      #执行命令setbit testmax 4294967295 1 之后
              total        used        free      shared  buff/cache   available
Mem:        1883496     1224796      124984         436      533716      497464
Swap:             0           0           0
```

当我用Redis Desktop Manager 查 testmax 的时候直接查爆了，可怕，最后重启Redis解决问题了,(机器配置低 1cpu,2g内存)

![img](https://mmbiz.qpic.cn/mmbiz_gif/pv2bs6eV9FnaP7cKyCpwuywaAicdotDYQUWtY6mWEktABUcP9licJM4odybRIFxCs18ttd1wbA6l4wyuKAL3dcDQ/?wx_fmt=gif)

花钱买服务，知识付费，真管用，位图是老钱掘金小册中提到，于是了解了一波

请多多指教

![img](https://mmbiz.qpic.cn/mmbiz_jpg/pv2bs6eV9FnohHzjowUSf7EOSalLDWia194PUSV0yltgXZB5Jtn1TbxUJuUiaZl7MIn3N7hXMAzBGQW8Uqiblz4bg/?wx_fmt=jpeg)

参考资料 

老钱：节衣缩食 —— 位图

![img](https://mmbiz.qpic.cn/mmbiz_png/pv2bs6eV9FnaP7cKyCpwuywaAicdotDYQfeKcCFPN9BgmicpLLTt2AuLh8wu0S6CsOhATEnmVwFlsSAgicBrCNhGw/?wx_fmt=png)