## 常用的JVM参数（jdk7）

### 堆设置

-Xms:初始堆大小

-Xmx:最大堆大小

-XX:NewSize=n:设置年轻代大小

-XX:NewRatio=n:设置年轻代和年老代的比值。如:为3，表示年轻代与年老代比值为1：3，年轻代占整个年轻代年老代和的1/4

-XX:SurvivorRatio=n:年轻代中Eden区与两个Survivor区的比值。注意Survivor区有两个。如：3，表示Eden：Survivor=3：2，一个Survivor区占整个年轻代的1/5

-XX:MaxPermSize=n:设置持久代大小

### 栈设置
-Xss        设置Java线程堆栈大小 。默认是1M，一般不需要这么大，256k足够。栈小一点可以创建更多的线程。  

### 收集器设置

-XX:+UseSerialGC:设置串行收集器

-XX:+UseParallelGC:设置并行年轻代收集器

-XX:+UseParalledlOldGC:设置并行年老代收集器

-XX:+UseConcMarkSweepGC:设置并发收集器（标记清楚）

### 垃圾回收统计信息

-verbose:gc 记录 GC 运行以及运行时间，一般用来查看 GC 是否是应用的瓶颈

-XX:+PrintGC

-XX:+PrintGCDetails 记录 GC 运行时的详细数据信息，包括新生成对象的占用内存大小以及耗费时间等   

-XX:+PrintGCTimeStamps  打印垃圾收集的时间戳

-Xloggc:filename 日志位置

### 并行收集器设置

-XX:ParallelGCThreads=n:设置并行收集器收集时使用的CPU数。并行收集线程数。

-XX:MaxGCPauseMillis=n:设置并行收集最大暂停时间

-XX:GCTimeRatio=n:设置垃圾回收时间占程序运行时间的百分比。公式为1/(1+n)

### 并发收集器设置

-XX:+CMSIncrementalMode:设置为增量模式。适用于单CPU情况。

-XX:ParallelGCThreads=n:设置并发收集器年轻代收集方式为并行收集时，使用的CPU数。并行收集线程数。
  

### 开启远程调试 

-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000   


### 关于性能诊断的 JVM 参数 

-Xprof 

-Xrunhprof 


### 关于类路径方面的 JVM 参数 

Xbootclasspath 用来指定你需要加载，但不想通过校验的类路径。JVM 会对所有的类在加载前进行校验并为每个类通过一个int数值来应用。这个是保证 JVM 稳定的必要过程，但比较耗时，如果你希望跳过这个过程，就把你的类通过这个参数来指定。 



### 用于修改 Perm Gen 大小的 JVM 参数 

下面的这三个参数主要用来解决 JVM 错误：[url]java.lang.OutOfMemoryError:Perm Gen Space.[/url] 

-XX:PermSize and MaxPermSize   
-XX:NewRatio=2  Ratio of new/old generation sizes.   
-XX:MaxPermSize=64m     Size of the Permanent Generation.   

### 8) 用来跟踪类加载和卸载的信息 

-XX:+TraceClassLoading 和 -XX:+TraceClassUnloading 用来打印类被加载和卸载的过程信息，这个用来诊断应用的内存泄漏问题非常有用。 

### 9) JVM switches related to logging 

-XX:+TraceClassLoading and -XX:+TraceClassUnloading print information class loads and unloads. Useful for investigating if you have a class leak or if old classes (like JITed Ruby methods in JRuby) are getting collected or not. You can read more about logging in Java on my post [url]10 Tips while logging in Java[/url] 


-XX:+PrintCompilation prints out the name of each Java method Hotspot decides to JIT compile. The list will usually show a bunch of core Java class methods initially, and then turn to methods in your application. In JRuby, it eventually starts to show Ruby methods as well 

### 10) 用于调试目的的 JVM 开关参数 

-XX:HeapDumpPath=./java_pid.hprof  Path to directory or file name for heap dump. 

-XX:-PrintConcurrentLocks       Print java.util.concurrent locks in Ctrl-Break thread dump. 

-XX:-PrintCommandLineFlags   Print flags that appeared on the command line. 

