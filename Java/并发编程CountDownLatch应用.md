CountDownLatch是在Java1.5被引入的，跟它一起被引入的并发工具类还有CyclicBarrier、Semaphore、ConcurrentHashMap和BlockingQueue，它们都存在于java.util.concurrent包下。

本文主要讲讲CountDownLatch;

官方是这样描述CountDownLatch

```
 A synchronization aid that allows one or more threads to wait until
 a set of operations being performed in other threads completes.
```

CountDownLatch是一个同步的辅助类，允许一个或多个线程，等待其他一组线程完成操作，再继续执行。

### CountDownLatch原理

CountDownLatch是通过一个计数器来实现的，计数器的初始化值为线程的数量。每当一个线程完成了自己的任务后，计数器的值就相应得减1。当计数器到达0时，表示所有的线程都已完成任务，然后在闭锁上等待的线程就可以恢复执行任务。

![img](http://incdn1.b0.upaiyun.com/2015/04/f65cc83b7b4664916fad5d1398a36005.png)

![1535620163789](C:\Users\Administrator\AppData\Local\Temp\1535620163789.png)

以上图片就是CountDownLatch结构图

CountDownLatch(int)  初始化计数器

countDown() ：计数器减1

getCount(): 获取当前计数器

await(): 线程会被挂起，它会等待直到计数器值为0才继续执行

await(long,TimeUnit):线程会被挂起某段时间后继续执行



应用场景：给你四张表，你需要从每张表计算出数量,然后计算四张表总数据量

main入口

```java
 	  try {
            CountDownLatch latch = new CountDownLatch(4);
            for (int i = 1; i <= 4; i++) {
                new Thread(new MyThread(latch,i)).start(); //启用四个线程
            }
            latch.await(); //主线程执行完毕
            MyThread.sum(); //最后统计总数
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

```

线程实现类

```java
class MyThread implements Runnable{
   static int tab1 = 0,tab2 = 0,tab3 = 0,tab4 = 0,threadNumber;
    CountDownLatch countDownLatch;
    MyThread(CountDownLatch countDownLatch ,int threadName){
        this.countDownLatch = countDownLatch;
        this.threadNumber = threadName;
    }
    @Override
    public void run(){
        try{
            switch (threadNumber) {
                case 1: tab1 = new Random().nextInt(100);
                    System.out.println("当前线程" + Thread.currentThread().getName()  + ", 表1数量：" +tab1); break;
                case 2: tab2 = new Random().nextInt(100);
                    System.out.println("当前线程" + Thread.currentThread().getName()  + ", 表2数量：" +tab2);break;
                case 3: tab3 = new Random().nextInt(100);
                    System.out.println("当前线程" + Thread.currentThread().getName()  + ", 表3数量："+tab3);break;
                case 4: tab4 = new Random().nextInt(100);
                    System.out.println("当前线程" + Thread.currentThread().getName()  + ", 表4数量：" + tab4);break;
                default:
                    System.out.println("异常了");break;
            }
        }finally {
            if (countDownLatch != null) {
                countDownLatch.countDown();//避免阻塞  计数器减1
            }
        }
    }

    public static void sum(){
        System.out.print("总数据量=");
        System.out.println(tab1 + tab2 + tab3 +tab4);
    }
}
```