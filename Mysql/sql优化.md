id值不同：id值越大越优先查询

id值有相同，又有不同：id值越大越优先

id值相同：从上往下顺序执行



1.复合索引 最左原则

4.补救。尽量使用索引覆盖（using index）

5 like尽量以常量开头，不要以“%”开头，否则索引失效

6.尽量不要使用类型转换（显示、隐式），否则索引失效

explain select * from teacher where tname = 123 // 123 ->‘123’

7.尽量不要使用or,否则索引失效

提高order by查询策略

a避免select *

b.符合索引 不要跨列使用，避免using filesort

d 保证全部排序字段一致性

SQL慢查询

show variables like "%slow_query_log%"

临时开启：

​	set global slow_query_log = 1; 

​	exit 

​	service mysql restart

​	mysql -uroot -p

永久开启

​	vim /etc/my.cnf

mysqld后面添加

​	 slow_query_log = 1; 

​	slow_query_log_file= /user/目录'



慢查询阈值 show variables like "%slow_query_time%"

