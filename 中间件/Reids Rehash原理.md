**Redis Rehash 内部实现**

在Redis中，键值对（Key-Value Pair）存储方式是由字典（Dict）保存的，而字典底层是通过**哈希表**来实现的。通过哈希表中的节点保存字典中的键值对。类似Java中的HashMap，将Key通过哈希函数映射到哈希表节点位置。

接下来我们一步步来分析Redis Dict Reash的机制和过程。

**(1) Redis 哈希表结构体：**

```c
/* hash表结构定义 */
typedef struct dictht { 
    dictEntry **table;   // 哈希表数组  初始化值大小是4
    unsigned long size;  // 哈希表的大小
    unsigned long sizemask; // 哈希表大小掩码
    unsigned long used;  // 哈希表现有节点的数量
} dictht; 
```

**(2) Redis 哈希桶**

Redis 哈希表中的table数组存放着**哈希桶结构**（dictEntry），里面就是Redis的键值对；类似Java实现的HashMap，Redis的dictEntry也是通过**链表（next指针）方式来解决hash冲突**：

```c
/* 哈希桶 */
typedef struct dictEntry { 
    void *key;     // 键定义
    // 值定义
    union { 
        void *val;    // 自定义类型
        uint64_t u64; // 无符号整形
        int64_t s64;  // 有符号整形
        double d;     // 浮点型
    } v;     
    struct dictEntry *next;  //指向下一个哈希表节点
} dictEntry;
```

**(3) 字典**

Redis Dict 中定义了两张哈希表，是为了后续字典的扩展作Rehash之用：

```c
/* 字典结构定义 */
typedef struct dict { 
    dictType *type;  // 字典类型
    void *privdata;  // 私有数据
    dictht ht[2];    // 哈希表[两个]
    long rehashidx;   // 记录rehash 进度的标志，值为-1表示rehash未进行
    int iterators;   //  当前正在迭代的迭代器数
} dict;

总结：
```

- 在Cluster模式下，一个Redis实例对应一个RedisDB(db0);
- 一个RedisDB对应一个Dict;
- 一个Dict对应2个Dictht，正常情况只用到ht[0]；ht[1] 在Rehash时使用。



我们知道当HashMap中由于Hash冲突（负载因子）超过某个阈值时，出于链表性能的考虑，会进行Resize的操作。Redis也一样【**Redis中通过dictExpand()实现**】。我们看一下Redis中的实现方式：

```c
/* 根据相关触发条件扩展字典 */
static int _dictExpandIfNeeded(dict *d) 
{ 
    if (dictIsRehashing(d)) return DICT_OK;  // 如果正在进行Rehash，则直接返回
    if (d->ht[0].size == 0) return dictExpand(d, DICT_HT_INITIAL_SIZE);  // 如果ht[0]字典为空，则创建并初始化ht[0]  
    /* (ht[0].used/ht[0].size)>=1前提下，
       当满足dict_can_resize=1或ht[0].used/t[0].size>5时，便对字典进行扩展 */
    if (d->ht[0].used >= d->ht[0].size && 
        (dict_can_resize || 
         d->ht[0].used/d->ht[0].size > dict_force_resize_ratio)) 
    { 
        return dictExpand(d, d->ht[0].used*2);   // 扩展字典为原来的2倍
    } 
    return DICT_OK; 
}


...

/* 计算存储Key的bucket的位置 */
static int _dictKeyIndex(dict *d, const void *key) 
{ 
    unsigned int h, idx, table; 
    dictEntry *he; 

    /* 检查是否需要扩展哈希表，不足则扩展 */ 
    if (_dictExpandIfNeeded(d) == DICT_ERR)  
        return -1; 
    /* 计算Key的哈希值 */ 
    h = dictHashKey(d, key); 
    for (table = 0; table <= 1; table++) { 
        idx = h & d->ht[table].sizemask;  //计算Key的bucket位置
        /* 检查节点上是否存在新增的Key */ 
        he = d->ht[table].table[idx]; 
        /* 在节点链表检查 */ 
        while(he) { 
            if (key==he->key || dictCompareKeys(d, key, he->key)) 
                return -1; 
            he = he->next;
        } 
        if (!dictIsRehashing(d)) break;  // 扫完ht[0]后，如果哈希表不在rehashing，则无需再扫ht[1]
    } 
    return idx; 
} 

...

/* 将Key插入哈希表 */
dictEntry *dictAddRaw(dict *d, void *key) 
{ 
    int index; 
    dictEntry *entry; 
    dictht *ht; 

    if (dictIsRehashing(d)) _dictRehashStep(d);  // 如果哈希表在rehashing，则执行单步rehash

    /* 调用_dictKeyIndex() 检查键是否存在，如果存在则返回NULL */ 
    if ((index = _dictKeyIndex(d, key)) == -1) 
        return NULL; 


    ht = dictIsRehashing(d) ? &d->ht[1] : &d->ht[0]; 
    entry = zmalloc(sizeof(*entry));   // 为新增的节点分配内存
    entry->next = ht->table[index];  //  将节点插入链表表头
    ht->table[index] = entry;   // 更新节点和桶信息
    ht->used++;    //  更新ht

    /* 设置新节点的键 */ 
    dictSetKey(d, entry, key); 
    return entry; 
}

...
/* 添加新键值对 */
int dictAdd(dict *d, void *key, void *val) 
{ 
    dictEntry *entry = dictAddRaw(d,key);  // 添加新键

    if (!entry) return DICT_ERR;  // 如果键存在，则返回失败
    dictSetVal(d, entry, val);   // 键不存在，则设置节点值
    return DICT_OK; 
}
```

继续dictExpand的源码实现：

```c
int dictExpand(dict *d, unsigned long size) 
{ 
    dictht n; // 新哈希表
    unsigned long realsize = _dictNextPower(size);  // 计算扩展或缩放新哈希表的大小(调用下面函数_dictNextPower())

    /* 如果正在rehash或者新哈希表的大小小于现已使用，则返回error */ 
    if (dictIsRehashing(d) || d->ht[0].used > size) 
        return DICT_ERR; 

    /* 如果计算出哈希表size与现哈希表大小一样，也返回error */ 
    if (realsize == d->ht[0].size) return DICT_ERR; 

    /* 初始化新哈希表 */ 
    n.size = realsize; 
    n.sizemask = realsize-1; 
    n.table = zcalloc(realsize*sizeof(dictEntry*));  // 为table指向dictEntry 分配内存
    n.used = 0; 

    /* 如果ht[0] 为空，则初始化ht[0]为当前键值对的哈希表 */ 
    if (d->ht[0].table == NULL) { 
        d->ht[0] = n; 
        return DICT_OK; 
    } 

    /* 如果ht[0]不为空，则初始化ht[1]为当前键值对的哈希表，并开启渐进式rehash模式 */ 
    d->ht[1] = n; 
    d->rehashidx = 0; 
    return DICT_OK; 
}
...
static unsigned long _dictNextPower(unsigned long size) { 
    unsigned long i = DICT_HT_INITIAL_SIZE;  // 哈希表的初始值：4


    if (size >= LONG_MAX) return LONG_MAX; 
    /* 计算新哈希表的大小：第一个大于等于size的2的N 次方的数值 */
    while(1) { 
        if (i >= size) 
            return i; 
        i *= 2; 
    } 
}

```

**总结一下具体逻辑实现：**



![https://mmbiz.qpic.cn/mmbiz_png/hEx03cFgUsWhebV9L9U67RaOR0ib5icnmhTSOfhgWWz9yAaMy5etxtSL1ibCQsjQOQmS8nnlS3B9Cn6WNibxluYIdg/640?wx_fmt=png](https://mmbiz.qpic.cn/mmbiz_png/hEx03cFgUsWhebV9L9U67RaOR0ib5icnmhTSOfhgWWz9yAaMy5etxtSL1ibCQsjQOQmS8nnlS3B9Cn6WNibxluYIdg/640?wx_fmt=png)

可以确认当Redis Hash冲突到达某个条件时就会触发dictExpand()函数来扩展HashTable。

DICT_HT_INITIAL_SIZE初始化值为4，通过上述表达式，取当4*2^n >= ht[0].used*2的值作为字典扩展的size大小。即为：ht[1].size 的值等于第一个大于等于ht[0].used*2的2^n的数值。

Redis通过dictCreate()创建词典，在初始化中，table指针为Null，所以两个哈希表ht[0].table和ht[1].table都未真正分配内存空间。只有在dictExpand()字典扩展时才给table分配指向dictEntry的内存。

由上可知，当Redis触发Resize后，就会动态分配一块内存，最终由ht[1].table指向，动态分配的内存大小为：realsize*sizeof(dictEntry*)，table指向dictEntry*的一个指针，大小为8bytes（64位OS），即ht[1].table需分配的内存大小为：8*2*2^n （n大于等于2）。

结论

**当Redis 节点中的Key总量到达临界点后，Redis就会触发Dict的扩展，进行Rehash。申请扩展后相应的内存空间大小。**



[[美团针对Redis Rehash机制的探索和实践](https://www.cnblogs.com/meituantech/p/9376472.html) ](https://www.cnblogs.com/meituantech/p/9376472.html)

