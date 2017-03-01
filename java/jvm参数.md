## 常用的JVM参数（jdk7）
### 1) 跟 Java 堆大小相关的 JVM 内存参数 

下面三个 JVM 参数用来指定堆的初始大小和最大值以及堆栈大小 

-Xms        设置 Java 堆的初始化大小  
-Xmx        设置最大的 Java 堆大小   
-Xss        设置Java线程堆栈大小   

### 2) 关于打印垃圾收集器详情的 JVM 参数 

-verbose:gc 记录 GC 运行以及运行时间，一般用来查看 GC 是否是应用的瓶颈   

-XX:+PrintGCDetails 记录 GC 运行时的详细数据信息，包括新生成对象的占用内存大小以及耗费时间等   

-XX:-PrintGCTimeStamps  打印垃圾收集的时间戳   



### 3) 设置 Java 垃圾收集器行为的 JVM 参数 

-XX:+UseParallelGC      使用并行垃圾收集   

-XX:-UseConcMarkSweepGC 使用并发标志扫描收集 (Introduced in 1.4.1)   

-XX:-UseSerialGC        使用串行垃圾收集 (Introduced in 5.0.)   

需要提醒的是，但你的应用是非常关键的、交易非常频繁应用时，应该谨慎使用 GC 参数，因为 GC 操作是耗时的，你需要在这之中找到平衡点。   

### 4) JVM 调试参数，用于开启远程调试 

-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000   


### 5) 关于性能诊断的 JVM 参数 

-Xprof 

-Xrunhprof 


### 6) 关于类路径方面的 JVM 参数 

Xbootclasspath 用来指定你需要加载，但不想通过校验的类路径。JVM 会对所有的类在加载前进行校验并为每个类通过一个int数值来应用。这个是保证 JVM 稳定的必要过程，但比较耗时，如果你希望跳过这个过程，就把你的类通过这个参数来指定。 



### 7) 用于修改 Perm Gen 大小的 JVM 参数 

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
