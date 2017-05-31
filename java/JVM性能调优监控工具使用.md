### JVM性能调优监控工具使用

#### jstack
用于查看某个java进程的线程堆栈信息。  

加 -l 参数，会打印额外的所信息，可以在发生死锁时用来查看锁持有情况。  
-m 参数，不仅会输出java堆栈信息，还会输出c/c++堆栈信息，用来查看本地方法调用。  

tid指Java Thread id。nid指native线程的id。prio是线程优先级。

堆栈信息中的线程id是十六进制表示的。

所以在查询的时候，需要先将top显示的pid转成十六进制表示。  
使用命令 `printf "%x\n" pid`  


### jmap
jmap是查询堆内存信息。排查内存溢出等等。  

使用jmap -heap pid查看进程堆内存使用情况，包括使用的GC算法、堆配置参数和各代中堆内存使用情况  

使用jmap -histo[:live] pid查看堆内存中的对象数目、大小统计直方图，如果带上live则只统计活对象  

使用jmap -dump:format=b,file=/tmp/dump.dat PID。dump下堆快照。  
dump出来的文件可以用MAT、VisualVM等工具查看，这里用jhat查看：jhat -port  /tmp/dump.dat  
注意如果Dump文件太大，可能需要加上-J-Xmx512m这种参数指定最大堆内存，即jhat -J-Xmx512m -port 9998 /tmp/dump.dat。然后就可以在浏览器中输入主机地址:9998查看了。  





