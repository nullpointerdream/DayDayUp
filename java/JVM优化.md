## JVM优化

java性能优化，首先要做的一点是判断性能瓶颈在哪？  
一般我们会对系统进行压测，以找到性能瓶颈。  

这里只谈java系统优化。  
一般来讲，默认的jvm可以工作的挺好，但是经过特定优化的jvm才可以发挥最大的性能。  
调优希望达到的目标是： 

1. 尽量少的GC次数
2. 尽量短的GC时间（每次）
3. 尽量长的GC间隔时间

所以，要达到1应该是设置一个尽量大的堆，但是要达到目标2应该是设置小一点的堆。这就需要取得一个平衡的值。  

一些优化建议：

1. 可以将堆的-Xmx最大值和—Xmx最小值设置成一样的，是为了避免堆重新分配大小的开销。  
同样的道理，对于年轻代，也可以把-XX:newSize -XX:MaxNewSize设置为同样大小。 

2. 如何控制年轻代与老年代的比例。  
默认是1：2的。那么：  
如果更大的年轻代，更小的老年代。那么minor GC必然会次数会减少，每次时间会变长。FullGC会更频繁，每次时间会更短。  
如果更小的年轻代，更大的老年代。那么minor GC必然会次数会变多，每次时间会变短。FullGC会减少，每次时间会更长。  
本着FullGC应更少的原则，大的老年代是合理的，这也是默认1：2的原因。  
但有时也应分析具体的应用，如果应用存在大量朝生夕死的临时对象，可以考虑加大年轻代。又或者系统有大量的持久对象，可以考虑加大老年代。  

3. 老年代的垃圾收集算法默认是单线程Serial的。可以考虑优化为并行算法。 -XX:+UseParallelOldGC
4. jvm的参数加一下-XX:+HeapDumpOnOutOfMemoryError，以便堆溢出时dump日志。  
5. 对响应要求非常高的应用，可以考虑将年轻大设置打一下，减少到老年代的对象。
6. 过大的老年代，可能导致一次FullGC过长。

参考配置（4核8G）：

```
-server -Xms3G -Xmx3G -Xss256k -XX:PermSize=128m -XX:MaxPermSize=128m -XX:+UseParallelOldGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/usr/aaa/dump -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:/usr/aaa/dump/heap_trace.txt -XX:NewSize=1G -XX:MaxNewSize=1G
```

```
-server -Xms4g -Xmx4g -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -Xmn2g -XX:MaxDirectMemorySize=1g -XX:SurvivorRatio=10 -XX:+UseConcMarkSweepGC -XX:CMSMaxAbortablePrecleanTime=5000 -XX:+CMSClassUnloadingEnabled -XX:CMSInitiatingOccupancyFraction=80 -XX:+UseCMSInitiatingOccupancyOnly -XX:+ExplicitGCInvokesConcurrent -Dsun.rmi.dgc.server.gcInterval=2592000000 -Dsun.rmi.dgc.client.gcInterval=2592000000 -XX:ParallelGCThreads=4 -Xloggc:/home/admin/logs/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/home/admin/logs/java.hprof
```


###1、对象优先在Eden分配
-XX:SurvivorRatio=8，默认的Eden和Survivor比为8:1
###2、大对象直接进入老年代
通过-XX:PretenureSizeThreshold令大于这个设置值的对象直接在老年代分配，
可以避免在Eden和两个Survivor区域之间发生大量的内存复制操作
###3、长期存活的对象将进入老年代
通过参数-XX:MaxTenuringThreshold （默认为15岁）设置对象晋升老年代的年龄阈值。
###4、动态对象年龄判定
为了更好地适应不同程序的内存状况，虚拟机并不是永远的要求对象的年龄必须达到了MaxTenuringThreshold才能晋升老年代；如果在Survivor空间中相同年龄所有对象的大小总和大于Survivor空间的一半，年龄大于或等于该年龄的对象就可以直接进入老年代，无需等到MaxTenuringThreshold中要求的年龄。
###5、控件分配担保
在Jdk1.6 update24之前，通过-XX:+HandlePromotionFailure设置，避免频繁的FullGC；  
在Jdk1.6 update24之后，只要老年代最大连续空间大于新生代对象总和或者大于历次晋升平均大小，都将进行MinorGC，否则将进行Full GC。

###

