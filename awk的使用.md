## awk的使用
[参考](http://coolshell.cn/articles/9070.html)
### 内置变量

变量名         | 说明          | 
------------- |:-------------:|
$0            | 当前记录（这个变量中存放着整个行的内容）  |
$1~$n	|当前记录的第n个字段，字段间由FS分隔  |
FS	|输入字段分隔符 默认是空格或Tab|
NF	|当前记录中的字段个数，就是有多少列|
NR	|已经读出的记录数，就是行号，从1开始，如果有多个文件话，这个值也是不断累加中。|
FNR	|当前记录数，与NR不同的是，这个值会是各个文件自己的行号|
RS	|输入的记录分隔符， 默认为换行符|
OFS	|输出字段分隔符， 默认也是空格|
ORS	|输出的记录分隔符，默认为换行符|
FILENAME	|当前输入文件的名字|

### 常见用法
以处理netstat的输出为例。  

        mongo:~ # netstat
        Active Internet connections (w/o servers)
        Proto Recv-Q Send-Q Local Address           Foreign Address         State      
        tcp        0    216 mongo:ssh               192.168.0.245:48976     ESTABLISHED 
        Active UNIX domain sockets (w/o servers)
        Proto RefCnt Flags       Type       State         I-Node Path
        unix  2      [ ]         DGRAM                    3734   @/org/kernel/udev/udevd
        unix  13     [ ]         DGRAM                    8313   /dev/log
        unix  2      [ ]         DGRAM                    10714  @/org/freedesktop/hal/udev_event
        unix  2      [ ]         DGRAM                    7442   /var/run/hook-localserver
        unix  3      [ ]         STREAM     CONNECTED     2366786 @/tmp/gdm-session-jHfDvuam
        unix  3      [ ]         STREAM     CONNECTED     2366212 
        unix  3      [ ]         STREAM     CONNECTED     2364170 /var/run/dbus/system_bus_socket
        unix  3      [ ]         STREAM     CONNECTED     2367959 
        unix  2      [ ]         DGRAM                    2355474 

在内置变量中$1-$n指的是第几列。那么可以这么指定输出的列。

        mongo:~ # netstat | awk '{print $1}'
        Active
        Proto
        tcp
        Active
        Proto
        unix
        unix
        unix
        unix
默认的FS分割符是空格或tab，所以前面几行的描述信息也输出了，所以我们可以用NR行来过滤

        mongo:~ # netstat | awk 'NR > 5 {print $1}'
        Proto
        unix
        unix
        unix
        unix
        unix
        unix

就是说awk的作用是可以指定条件，指定输出。  
比较运算符：!=, >, <, >=, <=,==  
逻辑运算符：!,&&,||  
可以指定输出格式：如{printf "%-20s %-20s %s\n",$4,$5,$6}  
这和C基本是一样的。  

### 指定内置变量
指定分隔符（/etc/passwd 是以：为分隔符的）：
        ```$  awk  'BEGIN{FS=":"} {print $1,$3,$6}' /etc/passwd```   
等价于  ```$ awk  -F: '{print $1,$3,$6}' /etc/passwd```   
如果你要指定多个分隔符，```awk -F '[;:]'```   
指定输出分隔符：```$ awk  -F: '{print $1,$3,$6}' OFS="\t" /etc/passwd```


### 正则匹配

        mongo:~ # netstat | awk '$2 ~ /2|13/'
        unix  2      [ ]         DGRAM                    3734   @/org/kernel/udev/udevd
        unix  13     [ ]         DGRAM                    8313   /dev/log
        unix  2      [ ]         DGRAM                    10714  @/org/freedesktop/hal/udev_event
        unix  2      [ ]         DGRAM                    7442   /var/run/hook-localserver
        unix  2      [ ]         DGRAM                    2355474 
        unix  2      [ ]         DGRAM                    15758  
        unix  2      [ ]         DGRAM                    14435  
        unix  2      [ ]         DGRAM                    13361  
在列后面加~，紧接着开始的//里面开始正则匹配。|表示或者

        mongo:~ # netstat | awk '$2 !~ /2|13/'
        Active Internet connections (w/o servers)
        Proto Recv-Q Send-Q Local Address           Foreign Address         State      
        tcp        0     52 mongo:ssh               192.168.0.245:48976     ESTABLISHED 
        Active UNIX domain sockets (w/o servers)
        Proto RefCnt Flags       Type       State         I-Node Path
        unix  3      [ ]         STREAM     CONNECTED     2383540 @/tmp/gdm-session-bbErrtFs
        unix  3      [ ]         STREAM     CONNECTED     2384090 
        unix  3      [ ]         STREAM     CONNECTED     2384088 /var/run/dbus/system_bus_socket
        unix  3      [ ]         STREAM     CONNECTED     2383539 
        unix  3      [ ]         STREAM     CONNECTED     15217  /var/run/dbus/system_bus_socket
        unix  3      [ ]         STREAM     CONNECTED     15216  
        unix  3      [ ]         STREAM     CONNECTED     13725  @/tmp/dbus-PKZYiwWjts
!~取反

awk 也可以向grep一样去匹配，不指定列，全行匹配。（也可加单引号）

        mongo:~ # netstat | awk /STREAM/
        unix  3      [ ]         STREAM     CONNECTED     2383540 @/tmp/gdm-session-bbErrtFs
        unix  3      [ ]         STREAM     CONNECTED     2384090 
        unix  3      [ ]         STREAM     CONNECTED     2384088 /var/run/dbus/system_bus_socket
        unix  3      [ ]         STREAM     CONNECTED     2383539 
        unix  3      [ ]         STREAM     CONNECTED     15217  /var/run/dbus/system_bus_socket
        unix  3      [ ]         STREAM     CONNECTED     15216  
        unix  3      [ ]         STREAM     CONNECTED     13725  @/tmp/dbus-PKZYiwWjts

