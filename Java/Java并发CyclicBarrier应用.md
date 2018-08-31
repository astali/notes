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

Cyclic（循环）Barrier（屏障）

原理







![1535704646078](E:\Desktop\1535704646078.png)

