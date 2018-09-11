

ZooKeeper可以保证每个server内的数据完全一致，是如何实现的呢？

ZooKeeper使用的是ZAB协议作为数据一致性的算法， ZAB（ZooKeeper Atomic Broadcast ） 全称为：原子消息广播协议；