###MySQL高性能使用索引

索引：是存储引擎用于快速找到记录的一种数据结构；
最常用的索引就是B-Tree索引；
为什么要使用索引；
1.索引大大减少了服务器需要的数据量；
2.索引可以帮助服务器避免排序和临时表；
3.索引可以将随机I/O变为顺序I/O;

索引选择性是指不重复的索引值（也称为基数）和数据表记录总数（#T）的比值，范围从1/#T到1之间。索引的选择性越高则查询效率也高；唯一索引的选择性是1，这是最好的索引选择性，性能也是最好的；身份证就可以当做唯一索引；

前缀索引是一种能使索引更小、更快的有效办法；但是无法使用前缀索引做ORDER BY 和GROUP BY 操作，也无法使用前缀索引做覆盖扫描；
###Limit
然后根据需要做一次关联查询再返回所有的列。对于偏移量很大时，这样做的效率会提升非常大。考虑下面的查询：

![img](https://mmbiz.qpic.cn/mmbiz_png/dkwuWwLoRK80m7ee5V5AURFzMYNI0TJta42VeqPkIBlYXYAGQ1q46EkkPYjzlpUgWjJjXaSicgzeviaKRIoNZtkQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1)
如果这张表非常大，那么这个查询最好改成下面的样子：
![img](https://mmbiz.qpic.cn/mmbiz_png/dkwuWwLoRK80m7ee5V5AURFzMYNI0TJtxwnR4iaBb7u09tdqFIFvIfPZokibE6XhzEYymd2zgQqLWLgvY7JcmvHQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1)

这里的延迟关联将大大提升查询效率，让 MySQL 扫描尽可能少的页面，获取需要访问的记录后在根据关联列回原表查询所需要的列。

有时候如果可以使用书签记录上次取数据的位置，那么下次就可以直接从该书签记录的位置开始扫描，这样就可以避免使用 OFFSET，比如下面的查询：

![img](https://mmbiz.qpic.cn/mmbiz_png/dkwuWwLoRK80m7ee5V5AURFzMYNI0TJtLk5CYrGTSegV8LnStp3dqrwJfaLEHNzuMzBLvVAEwDb4s29ZQrTc2g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1)

其他优化的办法还包括使用预先计算的汇总表，或者关联到一个冗余表，冗余表中只包含主键列和需要做排序的列。
###SQL执行器分析
当SQL不需要考虑排序和分组时，将选择性最高的列放在前面（左边）通常是很好的；
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

