**HashSet与TreeSet比较**

1.TreeSet是二叉树，自动排序，不允许null

2.HashSet是哈希表，无序，可以null



**HashMap和ConcurrentHashMap区别**

1.HashMap不是线程安全，ConcurrentHashMap线程安全

2.ConcurrentHashMap采用锁分段技术，将整个hash桶进行分段segment,

3.ConcurrentHashMap让锁的粒度更精细一些，并发性能更好



**wait和sleep区别**

1.sleep属于Thread类中，wait属于Object

2.sleep线程不会释放对象锁，wait线程会放弃对象锁，通过notify()通知获取



**JVM的内存结构**

1.Java虚拟机栈：线程私有，每个方法在执行的时候会创建一栈帧，存储了局部变量表、操作数栈、动态连接、方法返回地址；每个方法从调用到执行完毕，对应一个栈帧在虚拟机栈中的入栈和出栈。

2.堆：线程共享，用于存放对象实例。

3.方法区：线程共享，用于存储已被虚拟机加载的类信息，常量、静态变量。

4.程序计数器：线程私有，是当前线程执行的字节码的行号指示器。

5.本地方法栈：线程私有，主要在虚拟机使用的Native方法服务



**数组在内存中如何分配**

1.简单的值类型的数组，每个数组成员是一个引用（指针），引用到栈上的空间（因为值类型变量的内存分配在栈上）

2.引用类型，类类型的数组，每个数组成员仍是一个引用（指针），引用到堆上的空间（因为类的实例的内存分配在堆上）



**SpringMVC的核心是什么？请求的路程是怎么处理，控制反转怎么实现的**

核心是控制反转（IOC）和面向切面（AOP）

请求流程：

1.首先用户请求到前端控制器（DispatchServlet）入口。

2.前端控制器调用处理器映射器（HandlerMapping）

3.前端控制器调用处理器适配器（HandlerAdapter）

4.适配器调用处理器（Controller）返回ModelAndView

5.前端控制器将ModelAndView传给视图解析器（ViewReslover）

6.视图解析器解析后返回View

7.前端控制器根据View进行渲染响应用户

控制反转如何实现

每次使用Spring框架都要配置XMl文件，文件中配置了Bean的ID和class

Spring中默认的bean都是为单例模式，通过bean的class引用反射机制可以创建这个实例



**Http和Https协议区别**

Http

是互联网应用最为广泛的一种网络协议，是客户端和服务端请求和应答的标准，用于WWW服务器传输超文本到本地浏览器的传输协议，它可以使浏览器更加高效，使网络传输减少

Https

是以安全为目标的HTTP通道，简单讲是HTTP的安全版，HTTPS=Http+SSl

区别：

1.https协议需要证书，一般免费证书较少

2.http超文本传输协议，信息是明文传输，

3.http默认端口80，https默认端口443

4.http无状态

**说说tcp/ip协议族**

TCP/IP协议族是一个四层协议系统，自底而上分别是数据链路层、网络层、传输层和应用层。每一层完成不同的功能，且通过若干协议来实现，上层协议使用下层协议提供的服务。

1、数据链路层负责帧数据的传递。

2、网络层责数据怎样传递过去。

3、传输层负责传输数据的控制（准确性、安全性）

4、应用层负责数据的展示和获取。

**TCP与UDP的区别**

1、基于连接与无连接

2、TCP要求系统资源较多，UDP较少； 

3、UDP程序结构较简单 

4、流模式（TCP）与数据报模式(UDP); 

5、TCP保证数据正确性，UDP可能丢包 

6、TCP保证数据顺序，UDP不保证 

**说说tcp三次握手，四次挥手**

![信图片_2018032922120](H:\Desktop\微信图片_20180329221203.png)

![信图片_2018032922130](H:\Desktop\微信图片_20180329221306.jpg)![52233357118](C:\Users\jing\AppData\Local\Temp\1522333571182.png)

**TCP和UDP的区别及其适用场景**

**首先说一下什么是TCP和UDP：**

TCP是传输控制协议，提供的是面向连接、可靠的字节流服务。

UDP是用户数据报协议，是一个简单的面向数据报的运输层协议。

**TCP和UDP的区别：**

- TCP面向连接的运输层协议，UDP无连接
- TCP是可靠交付，UDP是尽最大努力交付
- TCP面向字节流，UDP面向报文
- TCP是点对点连接的，UDP一对一，一对多，多对多都可以
- TCP适合用于网页，邮件等，UDP适合用于视频，语音广播等

**TCP和UDP的适用场景：**

整个数据要准确无误的传递给对方，这往往用于一些要求可靠的应用，比如HTTP、HTTPS、FTP等传输文件的协议，POP、SMTP等邮件传输的协议。 

当对网络通讯质量要求不高的时候，要求网络通讯速度能尽量的快，比如视频、广播等，这时就可以使用UDP。

**cookie和session的区别。分布式环境怎么保存用户状态**

1.cookie保存在浏览器，session保存在服务器上

2.cookie不安全，单个cookie保存的数据不能超过4k,浏览器限制最大20cookie

3.session会在一定时间内保存在服务器上，访问过多会占用服务器性能

**分布式环境下的Session**

服务器session复制：session改变，广播其它节点上的session

session共享机制：使用分布式缓存，Redis。需集群。

**为什么要用线程池**

**那先要明白什么是线程池**

线程池是指在初始化一个多线程应用程序过程中创建一个线程集合，然后在需要执行新的任务时重用这些线程而不是新建一个线程。

**使用线程池的好处**

1、线程池改进了一个应用程序的响应时间。由于线程池中的线程已经准备好且等待被分配任务，应用程序可以直接拿来使用而不用新建一个线程。

2、线程池节省了CLR 为每个短生存周期任务创建一个完整的线程的开销并可以在任务完成后回收资源。

3、线程池根据当前在系统中运行的进程来优化线程时间片。

4、线程池允许我们开启多个任务而不用为每个线程设置属性。

5、线程池允许我们为正在执行的任务的程序参数传递一个包含状态信息的对象引用。

6、线程池可以用来解决处理一个特定请求最大线程数量限制问题。

**GC何时开始**

所有的回收器类型都是基于分代技术来实现的。那就必须要清楚对象生命周期是如何划分的。

1.年轻代：划分为三个区域，原始区（Eden）和两个小的存活区(Survivor)，两个存活区按功能分为from和to,绝大多数的对象在原始区分配，超过一个垃圾回收操作仍然存活的对象放到存活区，垃圾回收绝大部分发生在年轻代；

2.年老代：存储年轻代中经过多个回收周期仍然存活的对象，对于一些大的内存分配，也可能分配到永久代。

3.永久代：存储类、方法以及它们的描述信息，这里基本不产生垃圾回收。

原始区（Eden）内存满了之后，开始Minor GC （从年轻代空间回收内存被称为Minor GC）

升到老年代的对象所需空间大于老年代剩余空间开始Full GC(但也可能小于剩余空间时，被HandlePromotionFailure参数强制Full GC)

**类在虚拟机中的加载过程**

**加载Loading：**

通过一个类的全限定名来获取一个二进制字节流、将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构、在内存中生成一个代表这个类的java.lang.Class对象，作为方法区这个类的各种数据的访问入口。

**验证Verification：**

确保Class文件的字节流中包含的信息符合当前虚拟机的要求，并不会危害虚拟机的自身安全。

**准备Preparation：**

正式为类变量分配内存并设置类变量初始值。

**解析Resolution：**

虚拟机将常量池内的符号引用替换为直接引用的过程。

**初始化Initialization：**

类加载过程的最后一步，到了这个阶段才真正开始执行类中定义的Java程序代码。

**使用Using：**

根据你写的程序代码定义的行为执行。

**卸载Unloading：**

GC负责卸载，这部分一般不用讨论。



**Spring中bean的作用域**

**singleton:**

Spring IOC容器中只会存在一个共享的Bean实例，无论有多少个Bean引用它，适中指向同一对象，默认的作用域

**prototype**

每次通过Spring容器获取prototype定义的bean时，容器都将创建一个新的Bean实例，每个Bean的实例都有自己的属性和状态，而singleton全局只有一个对象。

**request**

在一次Http请求中，容器会返回该Bean的同一实例，而对不同的Http请求则会产生新的Bean,而且该bean仅在当前HttpRequest内有效

**session**

在一次Http Session中，容器会返回该Bean的同一实例，而对不同的Session请求则会创建新的实例，该bean实例仅在当前Session有效

**global Session**

在一个全局的Http Session中，容器会返回该Bean的同一个实例，仅在使用protlet context时有效

**说一下spring中Bean的生命周期**

- 实例化一个Bean，也就是我们通常说的new。
- 按照Spring上下文对实例化的Bean进行配置，也就是IOC注入。
- 如果这个Bean实现了BeanNameAware接口，会调用它实现的setBeanName(String beanId)方法，此处传递的是Spring配置文件中Bean的ID。
- 如果这个Bean实现了BeanFactoryAware接口，会调用它实现的setBeanFactory()，传递的是Spring工厂本身（可以用这个方法获取到其他Bean）。
- 如果这个Bean实现了ApplicationContextAware接口，会调用setApplicationContext(ApplicationContext)方法，传入Spring上下文。
- 如果这个Bean关联了BeanPostProcessor接口，将会调用postProcessBeforeInitialization(Object obj, String s)方法，BeanPostProcessor经常被用作是Bean内容的更改，并且由于这个是在Bean初始化结束时调用After方法，也可用于内存或缓存技术。
- 如果这个Bean在Spring配置文件中配置了init-method属性会自动调用其配置的初始化方法。
- 如果这个Bean关联了BeanPostProcessor接口，将会调用postAfterInitialization(Object obj, String s)方法。
- 当Bean不再需要时，会经过清理阶段，如果Bean实现了DisposableBean接口，会调用其实现的destroy方法。
- 最后，如果这个Bean的Spring配置中配置了destroy-method属性，会自动调用其配置的销毁方法。

**对Spring中依赖注入两种方式的认识**

两种注入方式为：构造方法注入和设值注入

1. 设值注入与传统的JavaBean的写法更相似，程序员更容易理解、接受，通过setter方式设定依赖关系显得更加直观、明显;
2. 对于复杂的依赖关系，如果采用构造注入，会导致构造器过于臃肿，难以阅读。Spring在创建Bean实例时，需要同时实例化其依赖的全部实例，因而会产生浪费。而使用设置注入，则避免这下问题;
3. 在某些属性可选的情况下，多参数的构造器更加笨拙，官方更鼓励使用设值注入。
4. 构造注入可以在构造器中决定依赖关系的注入顺序，优先依赖的优先注入。
5. 对于依赖关系无须变化的Bean，构造注入更有用处，因为没有setter方法，所有的依赖关系全部在构造器内设定，因此，不用担心后续代码对依赖关系的破坏。
6. 构造注入使依赖关系只能在构造器中设定，则只有组件的创建者才能改变组件的依赖关系。对组件的调用者而言，组件内部的依赖关系完全透明，更符合高内聚的原则。
7. 设值注入不会重写构造方法的值。如果我们对同一个变量同时使用了构造方法注入又使用了设置方法注入的话，那么构造方法将不能覆盖由设值方法注入的值。
8. 建议采用以设值注入为主，构造注入为辅的注入策略。对于依赖关系无须变化的注入，尽量采用构造注入;而其他的依赖关系的注入，则考虑采用set注入。

**Spring框架中都用到了哪些设计模式？**

- 代理模式：在AOP和remoting中被用的比较多。
- 单例模式：在spring配置文件中定义的bean默认为单例模式。
- 模板方法模式：用来解决代码重复的问题。
- 前端控制器模式：Spring提供了DispatcherServlet来对请求进行分发。
- 依赖注入模式：贯穿于BeanFactory / ApplicationContext接口的核心理念。
- 工厂模式：BeanFactory用来创建对象的实例。

**BeanFactory 和ApplicationContext的区别**

BeanFactory和ApplicationContext都是接口，并且ApplicationContext是BeanFactory的子接口。

BeanFactory是Spring中最底层的接口，提供了最简单的容器的功能，只提供了实例化对象和拿对象的功能。而ApplicationContext是Spring的一个更高级的容器，提供了更多的有用的功能。 

ApplicationContext提供的额外的功能：国际化的功能、消息发送、响应机制、统一加载资源的功能、强大的事件机制、对Web应用的支持等等。

加载方式的区别：BeanFactory采用的是延迟加载的形式来注入Bean；ApplicationContext则相反的，它是在Ioc启动时就一次性创建所有的Bean,好处是可以马上发现Spring配置文件中的错误，坏处是造成浪费。

**60，tomcat容器是如何创建servlet类实例？用到了什么原理？**        

当容器启动时，会读取在webapps目录下所有的web应用中的web.xml文件，然后对xml文件进行解析，并读取servlet注册信息。然后，将每个应用中注册的servlet类都进行加载，并通过反射的方式实例化。（有时候也是在第一次请求时实例化）
        在servlet注册时加上<load-on-startup>1</load-on-startup>如果为正数，则在一开始就实例化，如果不写或为负数，则第一次请求实例化。