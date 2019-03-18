https://maimai.cn/article/detail?fid=1156053364&efid=OAPsP36u_EdRRceoHPwLuA
**1.RabbitMQ有哪些重要的组件？**
1.Server(broker): 接受客户端连接，实现AMQP消息队列和路由功能的进程。
2.Virtual Host:其实是一个虚拟概念，类似于权限控制组，一个Virtual Host里面可以有若干个Exchange和Queue，但是权限控制的最小粒度是Virtual Host
3.Exchange:接受生产者发送的消息，并根据Binding规则将消息路由给服务器中的队列。ExchangeType决定了Exchange路由消息的行为，例如，在RabbitMQ中，ExchangeType有direct、Fanout和Topic三种，不同类型的Exchange路由的行为是不一样的。
4.Message Queue：消息队列，用于存储还未被消费者消费的消息。
5.Message: 由Header和Body组成，Header是由生产者添加的各种属性的集合，包括Message是否被持久化、由哪个Message Queue接受、优先级是多少等。而Body是真正需要传输的APP数据。
6.Binding:Binding联系了Exchange与Message Queue。Exchange在与多个Message Queue发生Binding后会生成一张路由表，路由表中存储着Message Queue所需消息的限制条件即Binding Key。当Exchange收到Message时会解析其Header得到Routing Key，Exchange根据Routing Key与Exchange Type将Message路由到Message Queue。Binding Key由Consumer在Binding Exchange与Message Queue时指定，而Routing Key由Producer发送Message时指定，两者的匹配方式由Exchange Type决定。 
7.Connection:连接，对于RabbitMQ而言，其实就是一个位于客户端和Broker之间的TCP连接。
8.Channel:信道，仅仅创建了客户端到Broker之间的连接后，客户端还是不能发送消息的。需要为每一个Connection创建Channel，AMQP协议规定只有通过Channel才能执行AMQP的命令。一个Connection可以包含多个Channel。之所以需要Channel，是因为TCP连接的建立和释放都是十分昂贵的，如果一个客户端每一个线程都需要与Broker交互，如果每一个线程都建立一个TCP连接，暂且不考虑TCP连接是否浪费，就算操作系统也无法承受每秒建立如此多的TCP连接。RabbitMQ建议客户端线程之间不要共用Channel，至少要保证共用Channel的线程发送消息必须是串行的，但是建议尽量共用Connection。
9.Command:AMQP的命令，客户端通过Command完成与AMQP服务器的交互来实现自身的逻辑。例如在RabbitMQ中，客户端可以通过publish命令发送消息，txSelect开启一个事务，txCommit提交一个事务。
**2.rabbitmq 中 Virtual host 的作用是什么？**
virtual host只是起到一个命名空间的作用，所以可以多个user共同使用一个virtual host，vritual_host = '/'，这个是系统默认的，就是说当我们创建一个到rabbitmq的connection时候，它的命名空间是'/'，需要注意的是不同的命名空间之间的资源是不能访问的，比如 exchang,queue ,bingding等
虚拟主机提供逻辑分组和资源分离

**3.rabbitmq 的消息是怎么发送的？**
1.消息从生产者Producer发送到交换机Exchange
2.交换机根据路由规则将消息转发到相应队列
3.队列将消息进行存储
4.消费者订阅队列消息，并进行消费

a.中间网络断开怎么办？ 
1). 设置信道channel 为事务模式 
2). 设置信道confirm 模式

**4.rabbitmq 怎么避免消息丢失？**
1.消息持久化
2.ACK确认机制
3.设置集群镜像模式
4.消息补偿机制

**5.rabbitmq 有几种广播类型？**
fanout: 所有bind到此exchange的queue都可以接收消息（纯广播，绑定到RabbitMQ的接受者都能收到消息）；
direct: 通过routingKey和exchange决定的那个唯一的queue可以接收消息；
topic:所有符合routingKey(此时可以是一个表达式)的routingKey所bind的queue可以接收消息；

**6.rabbitmq 怎么实现延迟消息队列？**
延迟任务通过消息的TTL和Dead Letter Exchange来实现
"x-message-ttl" 和"x-dead-letter-exchange" 

**7.rabbitmq 持久化有什么缺点？**
持久化的消息在进入队列前会被写到磁盘，这个过程比写到内存慢得多，所以会严重的影响性能，可能导致消息的吞吐量降低10倍不止