CyclicBarrier在JDK8官方描述如下

```
* A synchronization aid that allows a set of threads to all wait for
* each other to reach a common barrier point.  CyclicBarriers are
* useful in programs involving a fixed sized party of threads that
* must occasionally wait for each other. The barrier is called
* <em>cyclic</em> because it can be re-used after the waiting threads
* are released.
```

翻译过来如下

- **CyclicBarrier**是一个同步辅助类，它允许一组线程相互等待直到所有线程都到达一个公共的屏障点。
- 在程序中有固定数量的线程，这些线程有时候必须等待彼此，这种情况下，使用**CyclicBarrier**很有帮助。
- 这个**屏障**之所以用**循环**修饰，是因为在所有的线程释放彼此之后，这个**屏障**是可以重新使用的。

 简单一点理解就是： **N个线程**相互等待，任何一个线程完成之前（屏障点），所有的线程都必须等待。可重复使用

**执行流程**

1.当线程数未达到`parties`（线程初始化）大小，线程调用`await()`开启Barrier(屏障)拦截，当前线程被阻塞。无法执行后续工作， 

2.Cyclic（循环）第一步直到线程数达到parties（初始化大小） ,循环一次`getNumberWaiting`(等待的线程数量)加1

3.当线程数达到parties（初始化大小），执行Barrier(屏障)里业务逻辑，继续执行后续工作

4.当执行`reset()`开启重置屏障，先进行屏障破坏处理，再设置下一代generation，则进行重用CyclicBarrier对象

**原理**

在CyclicBarrier的内部定义了一个Lock`  final ReentrantLock lock = this.lock;`对象，每当一个线程调用`await`方法时，将拦截的线程数减1` int index = --count;`，然后判断剩余拦截数是否为初始值parties，如果不是，进入Lock对象的条件队列等待。如果是，执行barrierAction对象的Runnable方法，然后将锁的条件队列中的所有线程放入锁等待队列中，这些线程会依次的获取锁、释放锁。

![53572182664](H:\Desktop\1535721826643.png)



```
//初始化相互等待的线程数量以及屏障线程的构造方法。
public CyclicBarrier(int parties, Runnable barrierAction)
//初始化相互等待的线程数量的构造方法。 
public CyclicBarrier(int parties)
//获取CyclicBarrier打开屏障的线程数量
public int getParties()
//获取正在CyclicBarrier上等待的线程数量。
public int getNumberWaiting()
 /**
   * 在CyclicBarrier上进行阻塞等待，直到发生以下情形之一：
     在CyclicBarrier上等待的线程数量达到parties，则所有线程被释放，继续执行。
     当前线程被中断，则抛出InterruptedException异常，并停止等待，继续执行。
     其他等待的线程被中断，则当前线程抛出BrokenBarrierException异常，并停止等待，继续执行。
     其他等待的线程超时，则当前线程抛出BrokenBarrierException异常，并停止等待，继续执行。
     其他线程调用CyclicBarrier.reset()方法，则当前线程抛出BrokenBarrierException异常，并停止等待，继	   续执行
   */
public int await()
/**
   * 在CyclicBarrier上进行限时的阻塞等待，直到发生以下情形之一：
     在CyclicBarrier上等待的线程数量达到parties，则所有线程被释放，继续执行。
     当前线程被中断，则抛出InterruptedException异常，并停止等待，继续执行。
     当前线程等待超时，则抛出TimeoutException异常，并停止等待，继续执行。
     其他等待的线程被中断，则当前线程抛出BrokenBarrierException异常，并停止等待，继续执行。
     其他等待的线程超时，则当前线程抛出BrokenBarrierException异常，并停止等待，继续执行。
     其他线程调用CyclicBarrier.reset()方法，则当前线程抛出BrokenBarrierException异常，并停止等待，继	   续执行
   */	
public int await(long timeout, TimeUnit unit)
/**
   * 获取是否破损标志位broken的值，此值有以下几种情况：
     CyclicBarrier初始化时，broken=false，表示屏障未破损。
     如果正在等待的线程被中断，则broken=true，表示屏障破损。
     如果正在等待的线程超时，则broken=true，表示屏障破损。
     如果有线程调用CyclicBarrier.reset()方法，则broken=false，表示屏障回到未破损状态。
   */
public boolean isBroken() 
/**
  * 使得CyclicBarrier回归初始状态，直观来看它做了两件事：
	如果有正在等待的线程，则会抛出BrokenBarrierException异常，且这些线程停止等待，继续执行。
	将是否破损标志位broken置为false。
*/
public void reset() 
```

核心源码

```java
 private int dowait(boolean timed, long nanos)
        throws InterruptedException, BrokenBarrierException,
               TimeoutException {
        // ReentrantLock:可重入锁就是当前持有该锁的线程能够多次获取该锁，无需等待。
        final ReentrantLock lock = this.lock;
        lock.lock(); //获取锁
        try {
       		 //保存此时的generation
            final Generation g = generation;
 			//判断屏障是否被破坏
            if (g.broken)
                throw new BrokenBarrierException();
//判断线程是否被中断，如果被中断，调用breakBarrier()进行屏障破坏处理，并抛出InterruptedException
            if (Thread.interrupted()) {
                breakBarrier();
                throw new InterruptedException();
            }
			//剩余count递减，并赋值给线程索引，作为方法的返回值	
            int index = --count;
            if (index == 0) {  // tripped
                boolean ranAction = false;
                try {
                    final Runnable command = barrierCommand;
                    if (command != null)
                        command.run();//同步执行barrierCommand
                    ranAction = true;
                    nextGeneration(); //执行成功设置下一个nextGeneration
                    return 0;
                } finally {
                    if (!ranAction)//如果barrierCommand执行失败，进行屏障破坏处理
                        breakBarrier();
                }
            }
			//如果当前线程不是最后一个到达的线程
            // loop until tripped, broken, interrupted, or timed out
            for (;;) {
                try {
                    if (!timed)
                        trip.await(); //调用Condition的await()方法阻塞
                    else if (nanos > 0L)
                        nanos = trip.awaitNanos(nanos); //调用Condition的awaitNanos()方法阻塞
                } catch (InterruptedException ie) {
                  //如果当前线程被中断，则判断是否有其他线程已经使屏障破坏。
                  //若没有则进行屏障破坏处理，并抛出异常；否则再次中断当前线程
                    if (g == generation && ! g.broken) {
                        breakBarrier();
                        throw ie;
                    } else {
                        // We're about to finish waiting even if we had not
                        // been interrupted, so this interrupt is deemed to
                        // "belong" to subsequent execution.
              		//这种捕获了InterruptException之后调用Thread.currentThread().interrupt()					//是一种通用的方式。其实就是为了保存中断状态，从而让其他更高层次的代码注意到这个中断。
                        Thread.currentThread().interrupt();
                    }
                }
				//如果屏障被破坏，当前线程抛BrokenBarrierException
                if (g.broken)
                    throw new BrokenBarrierException();
				//如果已经换代，直接返回index（last thread已经执行的nextGeneration，
                //但当前线程还没有执行到该语句）
                if (g != generation)
                    return index;
				    //超时，进行屏障破坏处理，并抛TimeoutException
                if (timed && nanos <= 0L) {
                    breakBarrier();
                    throw new TimeoutException();
                }
            }
        } finally {
            lock.unlock(); //释放锁
        }
    }
```

**应用场景**

CyclicBarrier可以用于多线程计算数据，最后合并计算结果的应用场景：



#### CountDownLatch和CyclicBarrier的比较

1.CountDownLatch是线程组之间的等待，即一个(或多个)线程等待N个线程完成某件事情之后再执行；而CyclicBarrier则是线程组内的等待，即每个线程相互等待，即N个线程都被拦截之后，然后依次执行。

2.CountDownLatch是**减计数**方式，而CyclicBarrier是**加计数**方式。

3.CountDownLatch计数为0无法重置，而CyclicBarrier计数达到初始值，则可以重置。

4.CountDownLatch**不可以复用**，而CyclicBarrier**可以复用**。

CountDownLatch中的countDown()+await() = CyclicBarrier中的await()。

引用**实战Java高并发程序设计**中案例如下

```java
public class CyclicBarrierDemo {
    public static class Soldier implements Runnable {
      private String soldier;
      private final CyclicBarrier cyclic;

      public Soldier(CyclicBarrier cyclic, String soldier) {
        this.soldier = soldier;
        this.cyclic = cyclic;
      }
      
      @Override
      public void run() {
        try {
          //等待所有士兵到齐
          cyclic.await();
          doWork();
          //等待所有士兵完成工作
          cyclic.await();
        } catch (InterruptedException e) {//在等待过程中,线程被中断
          e.printStackTrace();
        } catch (BrokenBarrierException e) {//表示当前CyclicBarrier已经损坏.系统无法等到所有线程到齐了.
          e.printStackTrace();
        }
      }

      void doWork() {
        try {
          //为了每个线程休眠时间不一致【体现互相等待】
          Thread.sleep(Math.abs(new Random().nextInt() % 10000));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println(soldier + ":任务完成");
      }

    }

    public static class BarrierRun implements Runnable {
      boolean flag;
      int N;

      public BarrierRun(boolean flag, int N) {
        this.flag = flag;
        this.N = N;
      }

      @Override
      public void run() {
        if (flag) {
          System.out.println("司令:[士兵" + N + "个,任务完成!]");
        } else {
          System.out.println("司令:[士兵" + N + "个,集合完毕!]");
          flag = true;
        }
      }
    }

    public static void main(String[] args) {
      final int N = 10;
      Thread[] allSoldier = new Thread[N];
      boolean flag = false;
      CyclicBarrier cyclic = new CyclicBarrier(N, new BarrierRun(flag, N));
      //设置屏障点,主要为了执行这个方法
      System.out.println("集合队伍! ");
      for (int i = 0; i <10; i++) {
        System.out.println("士兵" + i + "报道! ");
        allSoldier[i] = new Thread(new Soldier(cyclic, "士兵" + i));
        allSoldier[i].start();
      }
    }
}
```

结果

```java
集合队伍! 
士兵0报道! 
士兵1报道! 
士兵2报道! 
士兵3报道! 
士兵4报道! 
士兵5报道! 
士兵6报道! 
士兵7报道! 
士兵8报道! 
士兵9报道! 
司令:[士兵10个,集合完毕!]
士兵5:任务完成
士兵8:任务完成
士兵6:任务完成
士兵2:任务完成
士兵7:任务完成
士兵3:任务完成
士兵0:任务完成
士兵9:任务完成
士兵1:任务完成
士兵4:任务完成
司令:[士兵10个,任务完成!]
```







