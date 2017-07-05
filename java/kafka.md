#Kafka简明教程
## 1.入门
### 1.1 简介
**kafka是一个分布式流平台。**这究竟代表什么？  
我们认为一个流平台，应该主要有以下三方面的关键能力：  
	1. 发布和订阅基于流的消息的能力，在这方面类似于消息队列或企业级消息系统。  
	2. 存储流消息时具有容错能力。  
	3. 在消息产生时及时得到处理。   

kafka的使用场景？  
主要应用于2大类场景：  
1. 构建系统或者应用间可靠地实时的数据流管道。  
2. 构建对数据流进行实时处理的应用。  

为了理解为什么kafka可以做这些，让我们什么了解一下kafka的能力。  

一些概念：  
1. kafka一般集群部署，一台或多台server。  
2. kafka存储数据，是以被称为topic的字段分类存储。   
3. 每一个record（消息），都会包含key，vakue和时间戳。  

kafka有四个核心API：  

- Producer API ：应用发布一个record到一个或多个kafka topic。
- Consumer API ：应用订阅topics，然后可以处理发给这些topic的record。
- Streams API：可以看做一个中间的流处理器。从一个或多个topic接受输入流，处理，然后将输出流发给一个或多个topic。
- Connector API ：可用来构建重复使用的producer或consumer，将topic连接到已有的系统。如一个关系型数据库的connecotr可以捕获数据表的所有改变。  

![](kafka-apis.png)

在kafka里，client和server建的通信是基于一个简单的，高性能的，语言无关的TCP协议。协议是向后兼容的。并且提供了包括java在内的多种语言的客户端。  

### 1.2 术语
#### Topics and Logs
topic是kafka中一个核心抽象概念。topic是一类消息的类别名。在kafaka中一个topic总是会被0个、1个或多个consumer订阅。  

对每一个topic，kafka是通过分区log来存储的。  

![](topic_log.png)

每一个分区log都是一个有序的、不可变的序列，结构化的commit log会连续的在队尾追加。  
每一个分区log会被标记上一个连续的唯一性id，称为offset（偏移量）。以此来标记一条log。  

kafka集群会保留所有发布的消息，不管是否已经被消费过。通过一个可配置的保留时间进行删除。 例如，保留策略设置为2天，那么2天之内发布的消息可以被消费，超过2天的会被删除以回收空间。kafka对不同数据量性能是常数，所以长时间保存数据不是问题。  

![](log_consumer.png)  

事实上，每个consumer存储的元信息只有偏移量（offset），即该consumer在log中的位置。通常，consumer是按线性顺序，递增的消费消息。但是，consumer也可以自己控制，可以调到前面某个位置重新消费，也可以向后跳过一些消息消费。  

可以看出，kafka consumer的操作是非常干净的，一个consumer的操作不会受到集群或其他consumer的影响。  

log分区可以，1，突破单机容量限制，一个分区只能在一个server上，但是一个topic可以有多个分区，所以可以支持任意量数据。2是每个分布可以作为一个并行的处理单元，下面细讲。  

#### 分布式
log的分区是在kafka集群中分布式部署的，每个分区可以根据配置备份到其他server上以容错。  
每个分区有一个server作为leader，其他server上的备份做follower。  
leader处理所有的读写请求，follower只是被动的同步。一旦leader挂掉，某个follower会自动成为leader。每个server都会作为某些分区的leader，而作为另外某些分区的follower，已达到负载均衡的效果。  

#### Producers
生产者向topic发送消息，并且决定发送到那个分区。可以使用简单的轮流选中，以负载均衡。也可以根据自己特定的选择算法。  

#### Consumers
consumer以consumer group进行标记，即consumer使分组的。topic下的每一个消息会被分发到每个组下的一个consumer实例。  
如果所有的consumer都在一个组下，那么消息会在这些consumer下进行充分的负载均衡。  
如果每一个consumer都是不同的组，那么消息就是广播模式，一个消息会被所有consumer都消费一次。  
![](consumer-groups.png)
这是一个由两个server组成的kafka集群。每个server有两个分区，一共4个分区。  
下面挂了6个consumer，分成2组，group A有2个consumer，group B有4个consumer。  



