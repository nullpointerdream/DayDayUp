#Zookeeper

###什么是zookeeper？
一句话描述：zookeeper是一个开源的高可靠的分布式系统**协调器**。  
主要提供：配置管理，命名，分布式锁，组服务。这些服务如果在自己的分布式应用中实现和维护起来会有很大工作量。有了zookeeper，我们可以直接拿来用它的一致性，组管理，leader选举等功能，也可以在此基础上构建自己的应用。  

## 基础知识
- zookeeper提供的是分布式应用所需的分布式协同服务。它提供了一系列基本要件供分布式应用去构建自己的高级分布式服务。zookeeper被设计的容易对其进行编程，使用的是类似于目录树的一种数据模型。  
众所周知，构建正确的分布式协同服务很难，很容易产生竞态条件和死锁，zookeeper的目标就是将我们从构建自己的分布式应用的协同器中解放出来。  

### 设计目标
1. 简单。 zookeeper允许各分布式进程间通过共享一个类似于文件目录树结构的有层级的命名空间来实现彼此间的协同，该命名空间是有一系列注册数据点组成，称为znode。和文件系统很像，所不同的是，znodes是保存在内存中，使zookeeper可以保持高性能低延迟。  
zookeeper实现高性能，高可用性，严格的顺序访问，使它可以应用于大型分布式系统。
2. 备份。zookeeper服务本身也是由多台server组成的整体。各台server互相知道彼此，他们在自己的内存中放了各server的状态，只要server集群中一半以上的服务可用，整个zookeeper服务就是可用的。  
![](https://zookeeper.apache.org/doc/r3.4.6/images/zkservice.jpg)  
client只会连到一台server，client和server间保持tcp连接，以发送请求，获取响应，发送心跳，得到watch事件，一旦tcp连接断掉，client会同另一台server建立tcp连接。
3. 有序。ZooKeeper会在每一个更新上打上数字标记，以表示这个ZooKeeper事务的顺序。这样以后的操作可以根据这个标记实现更高级的抽象，如同步原语。
4. 快速。尤其是在读操作占大多数的场景下。ZooKeeper的读写速度相差大约10:1。

###数据模型
前面讲了，非常像文件模型。每一个node都以路径来唯一标识。  
![](https://zookeeper.apache.org/doc/r3.4.6/images/zknamespace.jpg)  
ZooKeeper的Node可以有子Node，也可以存储数据，存储的数据用来处理协同，主要有状态信息，配置，位置信息等，通常很小。一般将node称为znode。  
znode存储的数据中有个版本号，一旦数据有更新，这个版本号会自增。client在此获取数据时，会得到新的版本号。  
znode中数据的读写都是原子的。每个node都有访问控制列表acl来严格控制谁可以做什么。  
还有一种临时node（ephemeral nodes),这个临时node的生存周期是创建它的session的生存周期，一旦这个session断掉，这个临时node就会被删除，这是很有用的一个特性，可以用来实现分布式锁。  

### 状态更新和监视
client可以在znode上设置watch，当znode的状态变化时将触发watch。当client和server的连接断掉，client会收到本地提醒。  

### 保证
- 一致性。按照client发送请求的顺序执行。
- 原子性。
- 单系统视图。不管client连到哪台server，它看到的服务视图都是一致的。
- 可靠性。
- 实时性。

### api
ZooKeeper的设计目标有一个是简单性，所以它提供的api也设计的很简单。
 
- create。创建一个node  
- delete。
- exists。判断在这个location是否存在node
- get data。从node获取数据。
- set data。向node写数据。
- get children。获取该node的子列表。
- sync。等待数据传播完成。

### 实现
![](https://zookeeper.apache.org/doc/r3.4.6/images/zkcomponents.jpg)  
上图为ZooKeeper服务的抽象组件。每个server都会有一份自己的组件。  
replicated database是内存数据库，存放着整个service数据。也就是说每个server都有全部的信息。更新操作会记日志到硬盘中已备恢复。写操作是先序列化到硬盘中，再应用到内存数据库。  
从上图可以看出，读操作是直接从该client连的server 执行。  
写操作要通过一致性协议来执行。  
所有的写操作都会被转发到leader，由leader进行转发。所以ZooKeeper的读能力比写强大很多。  
信息转发这层称为messaging层，负责维持leader和同步follower与leader的信息。messaging层使用原子性的信息协议，messaging层是原子性的，所以他可以保证每一个本地的信息复制都是可靠地。当leader收到一个写请求，计算写请求提交时的系统状态，然后将其转为一个投票事务，再计算新的状态（后面详细讲解）。  
原子化广播（atomic broadcast）保证了每一个进程按照相同的顺序投递同一个消息。  


## 安装小试
1. [下载一个稳定版](https://zookeeper.apache.org/),我下面的操作以3.4.6为例
2. 解压，找到conf/zoo_simple.cfg,一般将其改个名字，如zoo.cfg。  
[参数解释(转帖-感谢)](http://www.cnblogs.com/sunddenly/articles/4071730.html)  
3. 启动ZooKeeper的Server：bin/zkServer.sh start；  
关闭ZooKeeper的Server：bin/zkServer.sh stop

4. 连接。sh zkCli.sh -server 127.0.0.1:2181  
连接成功，会看到如
> [zk: 127.0.0.1:2181(CONNECTED) 0] 

在这个视图下，可以输入zookeeper的命令了。  
help：显示所有命令。  
如：

	[zk: 127.0.0.1:2181(CONNECTED) 2] ls /  
	[zookeeper]

然后创建一个自己的znode，

	[zk: 127.0.0.1:2181(CONNECTED) 3] create /my_node my_data
	Created /my_node
	[zk: 127.0.0.1:2181(CONNECTED) 4] ls /
	[zookeeper, my_node]

获取一下my_node的信息，

	[zk: 127.0.0.1:2181(CONNECTED) 5] get /my_node
	my_data
	cZxid = 0x6
	ctime = Thu Jan 21 15:53:08 CST 2016
	mZxid = 0x6
	mtime = Thu Jan 21 15:53:08 CST 2016
	pZxid = 0x6
	cversion = 0
	dataVersion = 0
	aclVersion = 0
	ephemeralOwner = 0x0
	dataLength = 7
	numChildren = 0

我们也可以用set修改关联的data，

	[zk: 127.0.0.1:2181(CONNECTED) 6] set /my_node yunsheng
	cZxid = 0x6
	ctime = Thu Jan 21 15:53:08 CST 2016
	mZxid = 0x7
	mtime = Thu Jan 21 15:56:32 CST 2016
	pZxid = 0x6
	cversion = 0
	dataVersion = 1
	aclVersion = 0
	ephemeralOwner = 0x0
	dataLength = 8
	numChildren = 0
	[zk: 127.0.0.1:2181(CONNECTED) 7] get /my_node
	yunsheng
	cZxid = 0x6
	ctime = Thu Jan 21 15:53:08 CST 2016
	mZxid = 0x7
	mtime = Thu Jan 21 15:56:32 CST 2016
	pZxid = 0x6
	cversion = 0
	dataVersion = 1
	aclVersion = 0
	ephemeralOwner = 0x0
	dataLength = 8
	numChildren = 0

最后，删除，

	[zk: 127.0.0.1:2181(CONNECTED) 8] delete /my_node
	[zk: 127.0.0.1:2181(CONNECTED) 9] ls /
	[zookeeper]

5.集群  

	tickTime=2000
	dataDir=/var/lib/zookeeper
	clientPort=2181
	initLimit=5//5个tickTime内，允许其他server连接并初始化数据，如果zooKeeper管理的数据较大，则应相应增大这个值。
	syncLimit=2//2个tickTime内，允许follower同步，如果follower落后太多，则会被丢弃。
	server.1=zoo1:2888:3888
	server.2=zoo2:2888:3888
	server.3=zoo3:2888:3888
	// server.x的x要写在myid文件中，决定当前机器的id
	// 第一个port用于连接leader
	// 第二个用于leader选举。
	// hostname也可以填ip

比如配一个三台的集群，可以搞在一台机器上起三个zkServer做假集群，也可以找三台机器，起三个zkServer。我手头没有那么多机器，以假集群演示，其实真集群配起来更简单。  
例子：
配置conf
- 
	mongo:~/zookeeper/zookeeper-3.4.6/conf # ll
	total 20
	-rw-rw-r-- 1 test yunsheng  535 Feb 20  2014 configuration.xsl
	-rw-rw-r-- 1 test yunsheng 2161 Feb 20  2014 log4j.properties
	-rw-rw-r-- 1 test yunsheng 1073 Jan 21 17:21 zoo1.cfg
	-rw-r--r-- 1 root root     1073 Jan 21 17:21 zoo2.cfg
	-rw-r--r-- 1 root root     1073 Jan 21 17:22 zoo3.cfg
	mongo:~/zookeeper/zookeeper-3.4.6/conf # cat zoo1.cfg 
	# The number of milliseconds of each tick
	tickTime=2000
	# The number of ticks that the initial 
	# synchronization phase can take
	initLimit=10
	# The number of ticks that can pass between 
	# sending a request and getting an acknowledgement
	syncLimit=5
	# the directory where the snapshot is stored.
	# do not use /tmp for storage, /tmp here is just 
	# example sakes.
	dataDir=/root/zookeeper/dataDir1
	
	dataLogDir=/root/zookeeper/logs1
	# the port at which the clients will connect
	clientPort=2181
	# the maximum number of client connections.
	# increase this if you need to handle more clients
	#maxClientCnxns=60
	#
	# Be sure to read the maintenance section of the 
	# administrator guide before turning on autopurge.
	#
	# http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
	#
	# The number of snapshots to retain in dataDir
	#autopurge.snapRetainCount=3
	# Purge task interval in hours
	# Set to "0" to disable auto purge feature
	#autopurge.purgeInterval=1
	server.1=192.168.111.118:2881:3881
	server.2=192.168.111.118:2882:3882
	server.3=192.168.111.118:2883:3883
	 
zoo2和zoo3的配置大同小异，注意分开各自的dataDir和dataLogDir
配myid
-
然后去各自的dataDir下创建myid，如：

	mongo:~/zookeeper/dataDir1 # cat myid 
	1	

创建一个名叫myid的文件，里面的内容就是server.x的x  

启动server
-
分别启动三个

	mongo:~/zookeeper/zookeeper-3.4.6/bin # sh zkServer.sh start zoo1.cfg
	JMX enabled by default
	Using config: /root/zookeeper/zookeeper-3.4.6/bin/../conf/zoo1.cfg
	Starting zookeeper ... STARTED
	mongo:~/zookeeper/zookeeper-3.4.6/bin # sh zkServer.sh start zoo2.cfg
	JMX enabled by default
	Using config: /root/zookeeper/zookeeper-3.4.6/bin/../conf/zoo2.cfg
	Starting zookeeper ... STARTED
	mongo:~/zookeeper/zookeeper-3.4.6/bin # sh zkServer.sh start zoo3.cfg
	JMX enabled by default
	Using config: /root/zookeeper/zookeeper-3.4.6/bin/../conf/zoo3.cfg
	Starting zookeeper ... STARTED

查看状态
-
	mongo:~/zookeeper/zookeeper-3.4.6/bin # sh zkServer.sh status zoo3.cfg
	JMX enabled by default
	Using config: /root/zookeeper/zookeeper-3.4.6/bin/../conf/zoo3.cfg
	Mode: leader
	mongo:~/zookeeper/zookeeper-3.4.6/bin # sh zkServer.sh status zoo2.cfg
	JMX enabled by default
	Using config: /root/zookeeper/zookeeper-3.4.6/bin/../conf/zoo2.cfg
	Mode: follower
	mongo:~/zookeeper/zookeeper-3.4.6/bin # sh zkServer.sh status zoo1.cfg
	JMX enabled by default
	Using config: /root/zookeeper/zookeeper-3.4.6/bin/../conf/zoo1.cfg
	Mode: follower

可以看到，选举zoo3为leader，zoo1和zoo2为follower。

