### RocketMQ
#### 顺序性与重复性
消息系统绕不开的两个问题：如何保证消息的顺序？如何保证消息不重复？  
对于第一个问题，RocketMQ的策略是，你可以自己实现selector方法，保证同一个id的消息放到同一个队列中。  
对于第二个问题，答案是RocketMQ不保证消息不重复。  
需要消费端自己保证幂等。可以使用去重表，消息重复的问题应当是比较少的。  

#### 事务消息
RocketMQ除了支持普通消息，顺序消息，另外还支持事务消息。  
如果一个大事务中涉及的数据分散到多台机器上，那么执行这个事务的时间将会成倍增加，因为会增加多次网络传输时间。  
解决思路是 大事务拆成小事务 + 异步消息。尽可能的减少网络传输次数。  
那么如何保证一个小事务成功了，另一个事务也一定成功？
RocketMQ的做法是：

1. 开始事务A前，先发送Prepared消息。此时会拿到消息的地址。
2. 开始执行事务A。
3. 事务A完成，根据阶段1拿到的消息地址，修改消息状态。

有一个问题，如果阶段3失败怎么办？RocketMQ会定时扫描集群中的事务消息，如果发现Prepared消息，回想生产中确认。生产者需要配置策略，是回滚还是确认发送。  

另一个问题，事务A成功了，事务B一直消费失败怎么办？RocketMQ的策略是不断重试。还是失败？人工介入解决。RocketMQ不会回滚臻哥大的流程，因为如果要实现这个功能，整个RocketMQ复杂度会大大提成，且很容易出bug，RocketMQ认为没有必要为了解决发生几率很小的问题，而花费巨大精力。

#### 发送方负载均衡
Producer轮询某topic下的所有队列的方式来实现发送方的负载均衡

#### 消息存储
RocketMQ的消息存储是由consume queue和commit log配合完成的。  
consume queue 是逻辑队列。用来指定消息在物理文件commit log上的位置。  
commit log 是物理文件。  

每个topic下的每个queue都有一个对应的consume queue文件。  
consume queue的组织结构：
`${rocketmq.home}/store/consumequeue/${topicName}/${queueId}/${fileName}`
`${rocketmq.home}/store/consumequeue/${topicName}/${queueId}/`这是一个文件  
下面的`${fileName}`指向commit log。  

除了正常的消息队列，还有重试队列和死信队列，都是按照分组groupName标记的。  

在consume queue中存储的数据，包含三个信息：在commitLog中的偏移量，消息大小，tag标。  

commitlog的存储位置  
`${user.home} \store\${commitlog}\${fileName}`

#### 消息订阅
RocketMQ消息订阅有两种模式，一种是Push模式，即MQServer主动向消费端推送；另外一种是Pull模式，即消费端在需要时，主动到MQServer拉取。但在具体实现时，Push和Pull模式都是采用消费端主动拉取的方式。

消费端负载均衡，消费端会通过RebalanceService线程，10秒钟做一次基于topic下的所有队列负载。  

消费端的Push模式是通过长轮询的模式来实现的。  

在rocketmq中，push方式的获取消息方式其实是基于对pull方式的封装。为什么这么做的主要原因是要降低broker的负载，因为传统的push方式的实现是broker主动将消息发给client，这样就给broker造成压力。而使用pull封装过的push方式，其实真正发出请求的仍是client端，只是巧妙的给用户造成一种push的假象。





