Mysql优化

Limit

然后根据需要做一次关联查询再返回所有的列。对于偏移量很大时，这样做的效率会提升非常大。考虑下面的查询：

![img](https://mmbiz.qpic.cn/mmbiz_png/dkwuWwLoRK80m7ee5V5AURFzMYNI0TJta42VeqPkIBlYXYAGQ1q46EkkPYjzlpUgWjJjXaSicgzeviaKRIoNZtkQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1)

如果这张表非常大，那么这个查询最好改成下面的样子：

![img](https://mmbiz.qpic.cn/mmbiz_png/dkwuWwLoRK80m7ee5V5AURFzMYNI0TJtxwnR4iaBb7u09tdqFIFvIfPZokibE6XhzEYymd2zgQqLWLgvY7JcmvHQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1)

这里的延迟关联将大大提升查询效率，让 MySQL 扫描尽可能少的页面，获取需要访问的记录后在根据关联列回原表查询所需要的列。

有时候如果可以使用书签记录上次取数据的位置，那么下次就可以直接从该书签记录的位置开始扫描，这样就可以避免使用 OFFSET，比如下面的查询：

![img](https://mmbiz.qpic.cn/mmbiz_png/dkwuWwLoRK80m7ee5V5AURFzMYNI0TJtLk5CYrGTSegV8LnStp3dqrwJfaLEHNzuMzBLvVAEwDb4s29ZQrTc2g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1)

其他优化的办法还包括使用预先计算的汇总表，或者关联到一个冗余表，冗余表中只包含主键列和需要做排序的列。