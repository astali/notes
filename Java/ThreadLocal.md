## ThreadLocal为什么会内存泄漏

每个Thread都有一个ThreadLocal.ThreadLocalMap的map，该map的key为ThreadLocal实例，它为一个弱引用，我们知道弱引用有利于GC回收。当ThreadLocal的key == null时，GC就会回收这部分空间，但是value却不一定能够被回收，因为他还与Current Thread存在一个强引用关系

由于存在这个强引用关系，会导致value无法回收。如果这个线程对象不会销毁那么这个强引用关系则会一直存在，就会出现内存泄漏情况。所以说只要这个线程对象能够及时被GC回收，就不会出现内存泄漏。如果碰到线程池，那就更坑了。

那么要怎么避免这个问题呢？

在前面提过，在ThreadLocalMap中的setEntry()、getEntry()，如果遇到key == null的情况，会对value设置为null。当然我们也可以显示调用ThreadLocal的remove()方法进行处理。



ThreadLocal 源码分析（JDK8）

```java
static class ThreadLocalMap{
    //key是弱引用  发生gc必须回收  
      static class Entry extends WeakReference<ThreadLocal<?>> {
            /** The value associated with this ThreadLocal. */
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }
    
}
ThreadLocalMap set方法
 /**
 * Set the value associated with key.
 *
 * @param key the thread local object
 * @param value the value to be set
 */
private void set(ThreadLocal<?> key, Object value) {

	// We don't use a fast path as with get() because it is at
	// least as common to use set() to create new entries as
	// it is to replace existing ones, in which case, a fast
	// path would fail more often than not.

	Entry[] tab = table;
	int len = tab.length;
	// 根据 ThreadLocal 的散列值，查找对应元素在数组中的位置
	int i = key.threadLocalHashCode & (len-1);
	// 采用“线性探测法”，寻找合适位置
	for (Entry e = tab[i];
		 e != null;
		 e = tab[i = nextIndex(i, len)]) {
		ThreadLocal<?> k = e.get();
		// key 存在，直接覆盖
		if (k == key) {
			e.value = value;
			return;
		}
		// key == null，但是存在值（因为此处的e != null），说明之前的ThreadLocal对象已经被回收了
		if (k == null) {                     
			// 用新元素替换陈旧的元素
			replaceStaleEntry(key, value, i);
			return;
		}
	}
	// ThreadLocal对应的key实例不存在也没有陈旧元素，new 一个
	tab[i] = new Entry(key, value);
	int sz = ++size;
	// cleanSomeSlots 清楚陈旧的Entry（key == null）
	// 如果没有清理陈旧的 Entry 并且数组中的元素大于了阈值，则进行 rehash
	if (!cleanSomeSlots(i, sz) && sz >= threshold)
		rehash();
}
```

**ThreadLocal  get方法**

```java
public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {         
		// 从当前线程的ThreadLocalMap获取相对应的Entry
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    return setInitialValue();
}

ThreadLocal set方法
```

```java
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
}
```