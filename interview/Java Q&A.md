问题7: 如何在/usr目录下找出大小超过10MB的文件？

答：find /usr -size +10M

问题8：如何在/home目录下找出120天之前被修改过的文件？

答：find /home -mtime +120

问题9：如何在/var目录下找出90天之内未被访问过的文件？

答：find /var ! -atime -90

**2.JVM中survivor区存在的意义是啥**

在任何时候，总有一个survivor space是empty的，在下一次coping collection时，会将eden和另一个survivor space里的live object copy到这个里面。

live objects在两个survivor space里copy来copy去，直到对象old enough可以放到tenured generation里（copy 过去的）

因为在垃圾收集的时候需要将dead object清理掉，如果只有一个survivor区，那么这个survivor区里的dead object在清理掉之后就会产生内存碎片，为了避免内存碎片那么必须将live object移动来移动去，这样就会损失性能。

如果有两个survivor区，按照上面的说法，就不会存在内存碎片的问题。

**4.mq中的queue和topic有什么区别**

|                      | **Topic**                                                    | **Queue**                                                    |
| -------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **概要**             | Publish  Subscribe messaging 发布订阅消息                    | Point-to-Point  点对点                                       |
| **有无状态**         | topic数据默认不落地，是无状态的。                            | Queue数据默认会在mq服务器上以文件形式保存，比如Active MQ一般保存在$AMQ_HOME\data\kr-store\data下面。也可以配置成DB存储。 |
| **完整性保障**       | 并不保证publisher发布的每条数据，Subscriber都能接受到。      | Queue保证每条数据都能被receiver接收。                        |
| **消息是否会丢失**   | 一般来说publisher发布消息到某一个topic时，只有正在监听该topic地址的sub能够接收到消息；如果没有sub在监听，该topic就丢失了。 | Sender发送消息到目标Queue，receiver可以异步接收这个Queue上的消息。Queue上的消息如果暂时没有receiver来取，也不会丢失。 |
| **消息发布接收策略** | 一对多的消息发布接收策略，监听同一个topic地址的多个sub都能收到publisher发送的消息。Sub接收完通知mq服务器 | 一对一的消息发布接收策略，一个sender发送的消息，只能有一个receiver接收。receiver接收完后，通知mq服务器已接收，mq服务器对queue里的消息采取删除或其他操作。 |
| 消息                 | 可以重复消费                                                 | 不可重复消费                                                 |
|                      | 消息生产者（发布）将消息发布到topic中，同时有多个消息消费者（订阅）消费该消息。 和点对点方式不同，发布到topic的消息会被所有订阅者消费。 **当生产者发布消息，不管是否有消费者。都不会保存消息** | 消息生产者生产消息发送到queue中，然后消息消费者从queue中取出并且消费消息。 **消息被消费以后，queue中不再有存储**，所以消息消费者不可能消费到已经被消费的消息。 Queue支持存在多个消费者，但是对一个消息而言，**只会有一个消费者可以消费**、其它的则不能消费此消息了。 当**消费者不存在时，消息会一直保存，直到有消费消费** |

**1、shutdown（）** JDK1.8

**问**：shutdown()有什么功能？

**答**：阻止新来的任务提交，对已经提交了的任务不会产生任何影响。当已经提交的任务执行完后，它会将那些闲置的线程（idleWorks）进行中断，这个过程是异步的。

**问**：如何阻止新来的任务提交？

**答**：通过将线程池的状态改成SHUTDOWN，当再将执行execute提交任务时，如果测试到状态不为RUNNING，则抛出rejectedExecution，从而达到阻止新任务提交的目的。

**问**：为何对提交的任务不产生任何影响？

**答**：在调用中断任务的方法时，它会检测workers中的任务，如果worker对应的任务没有中断，并且是空闲线程，它才会去中断。另外的话，workQueue中的值，还是按照一定的逻辑顺序不断的往works中进行输送的，这样一来，就可以保证提交的任务按照线程本身的逻辑执行，不受到影响。

 

**2、shutdownNow()** JDK1.8

**问**：shutdownNow()有什么功能？

**答**：阻止新来的任务提交，同时会中断当前正在运行的线程，即workers中的线程。另外它还将workQueue中的任务给移除，并将这些任务添加到列表中进行返回。

**问**：如何阻止新来的任务提交？

**答**：通过将线程池的状态改成STOP，当再将执行execute提交任务时，如果测试到状态不为RUNNING，则抛出rejectedExecution，从而达到阻止新任务提交的目的.

**问**：如果我提交的任务代码块中，正在等待某个资源，而这个资源没到，但此时执行shutdownNow()，会出现什么情况？

**答**：当执行shutdownNow()方法时，如遇已经激活的任务，并且处于阻塞状态时，shutdownNow()会执行1次中断阻塞的操作，此时对应的线程报InterruptedException，如果后续还要等待某个资源，则按正常逻辑等待某个资源的到达。例如，一个线程正在sleep状态中，此时执行shutdownNow()，它向该线程发起interrupt()请求，而sleep()方法遇到有interrupt()请求时，会抛出InterruptedException()，并继续往下执行。在这里要提醒注意的是，在激活的任务中，如果有多个sleep(),该方法只会中断第一个sleep()，而后面的仍然按照正常的执行逻辑进行。

 **在浏览器输入url发生了什么?**

1.浏览器会检查DNS记录缓存，找到ip地址

2.浏览器启动与服务器之间TCP连接

3.发送http请求

4.服务器处理请求并响应

5.服务器发送一个http响应

6.浏览器加载响应，流程结束



能跟我聊聊HTTP/1.1 与 HTTP/1.0 的区别?**
HTTP1.0 无状态、无连接
HTTP1.1 持久连接、请求管道化、增加缓存处理、增加Host字段、支持断点传输
HTTP2.0二进制分帧，多路复用、头部压缩、服务器推送

