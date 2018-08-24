# linux 网络设置/IP重启网卡

**修改网卡命令：**

ifconfig eth0 192.168.0.3 netmask 255.255.255.0 
临时修改IP生效，如果想重启之后依然生效，可以修改网卡配置文件。

**重启网卡命令：**

**centos**： sudo service network restart 
**ubuntu**：sudo /etc/init.d/networking restart

**网卡配置文件：**

- **centos**： 修改 /etc/sysconfig/network-scripts/ifcfg-eth0

```
DEVICE=eth0      #网卡对应的设备别名
BOOTPROTO=static #网卡获得ip地址的方式（默认为dhcp，表示自动获取）
HWADDR=00:07:E9:05:E8:B4        #网卡MAC地址（物理地址）
IPADDR=192.168.100.100          #IP地址
NETMASK=255.255.255.0           #子网掩码 
ONBOOT=yes                      #系统启动时是否激活此设备123456
```

- **ubuntu**： 修改 /etc/network/interfaces

```
auto eth0
iface eth0 inet static
address 192.168.3.90
gateway 192.168.3.1
netmask 255.255.255.012345
```

**修改dns**

```
# vi /etc/resolv.conf
nameserver 8.8.8.8              #google域名服务器
nameserver 114.144.114.114      #国内域名服务器
```
**Linux 网络设置**

```
vi /etc/sysconfig/network-scripts/ifcfg-eth0
```

```
DEVICE=eth0
TYPE=Ethernet
ONBOOT=yes
NM_CONTROLLED=yes
BOOTPROTO=static  //静态ip
IPADDR=192.168.16.199
NETMASK=255.255.255.0
GATEWAY=192.168.16.2

```

**防火墙设置**

1) Linux操作系统中永久性生效，重启后不会复原  
开启： chkconfig iptables on
关闭： chkconfig iptables off
2) 即时生效，重启后复原
开启： service iptables start
关闭： service iptables stop