### 1.快速插入测试数据

```
insert into user_info(userid,gameid)
	(SELECT userid,gameid FROM user_info WHERE id >=
			(SELECT floor(RAND() * 
				(SELECT MAX(id) FROM user_info)
			)
	) ORDER BY id LIMIT 500000; 

```

### 2. 分页查询优化

```
SELECT * FROM user_infoWHERE ID >=(select id from user_infolimit 0, 1) order by id asc limit 20;
```

### 3.函数使用

1. 字段添加随机值 `floor(1 + rand() * 1000) `
2. concat("字段名","追加值") 字段追加

 创建新表的时候，我是用复制旧表结构的方式去创建的，复制旧表结构的时候，记得使用create table newTableName like oldTableName的方式，不然会没有把旧表的索引复制过来

### 4.添加索引

```
ALTER TABLE  ptlog_login ADD INDEX onlykey_index(onlykey);
```

5.IP存int 无符号

select inet_aton("127.0.0.1");//转换为int
select inet_ntoa(2130706433);//转换为ip




**1、一张表，里面有ID自增主键，当insert了17条记录之后，删除了第15,16,17条记录，再把Mysql重启，再insert一条记录，这条记录的ID是18还是15**

如果表类型是MyISAM，那么是18，因为MyISAM表会把自增主键的最大ID记录到数据文件中，重启MYSQL主键最大ID不会丢失

如果表是INnodb,那么是15，因为Innodb把最大ID记录在内存中，重启或OPTIMISE操作，ID都会丢失

> **25、列设置为AUTO INCREMENT时，如果在表中达到最大值，会发生什么情况？**

它会停止递增，任何进一步的插入都将产生错误，因为密钥已被使用。

> **26、怎样才能找出最后一次插入时分配了哪个自动增量？**

LAST_INSERT_ID将返回由Auto_increment分配的最后一个值，并且不需要指定表名称。

> **27、你怎么看到为表格定义的所有索引？**

索引是通过以下方式为表格定义的：

SHOW INDEX FROM <tablename>;

> **49、什么是通用SQL函数？**

- CONCAT(A, B) - 连接两个字符串值以创建单个字符串输出。通常用于将两个或多个字段合并为一个字段。
- FORMAT(X, D)- 格式化数字X到D有效数字。
- CURRDATE(), CURRTIME()- 返回当前日期或时间。
- NOW（） - 将当前日期和时间作为一个值返回。
- MONTH（），DAY（），YEAR（），WEEK（），WEEKDAY（） - 从日期值中提取给定数据。
- HOUR（），MINUTE（），SECOND（） - 从时间值中提取给定数据。
- DATEDIFF（A，B） - 确定两个日期之间的差异，通常用于计算年龄
- SUBTIMES（A，B） - 确定两次之间的差异。
- FROMDAYS（INT） - 将整数天数转换为日期值。

> **55、Mysql中有哪几种锁？**

- MyISAM支持表锁，InnoDB支持表锁和行锁，默认为行锁
- 表级锁：开销小，加锁快，不会出现死锁。锁定粒度大，发生锁冲突的概率最高，并发量最低
- 行级锁：开销大，加锁慢，会出现死锁。锁力度小，发生锁冲突的概率小，并发度最高