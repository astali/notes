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

