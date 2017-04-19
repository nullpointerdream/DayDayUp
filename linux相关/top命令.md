## top命令

top命令是Linux下常用的性能分析工具，能够实时显示系统中各个进程的资源占用状况，类似于Windows的任务管理器。top是一个动态显示过程,自动刷新（默认3秒），也可以通过用户按键来不断刷新当前状态.如果在前台执行该命令,它将独占前台,直到用户终止该程序为止.比较准确的说,top命令提供了实时的对系统处理器的状态监视.它将显示系统中CPU最“敏感”的任务列表.该命令可以按CPU使用.内存使用和执行时间对任务进行排序。

### top   
输出：

		 top - 14:52:42 up 4 days, 21:28,  2 users,  load average: 0.91, 1.50, 2.13
		Tasks: 168 total,   1 running, 165 sleeping,   0 stopped,   2 zombie
		Cpu(s):  1.6%us,  2.1%sy,  0.0%ni, 96.3%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
		Mem:     15953M total,    10881M used,     5072M free,      546M buffers
		Swap:     2053M total,       15M used,     2038M free,     4756M cached
		
		  PID USER      PR  NI  VIRT  RES  SHR S   %CPU %MEM    TIME+  COMMAND                                                                                                                                                                     
		20074 controll  20   0 11.1g 3.6g  18m S      9 22.9  22:01.95 java                                                                                                                                                                         
		 8131 omm       20   0 91476 8412 1628 S      1  0.1  14:53.10 ha.bin                                                                                                                                                                       
		11868 mongodb   20   0 12.9g  38m 7620 S      1  0.2  36:52.68 mongod                                                                                                                                                                       
		 1352 root      20   0 19308 1116  832 S      0  0.0   0:33.45 syslog-ng                                                                                                                                                                    
		 2900 root      20   0  130m  924  788 S      0  0.0   1:31.15 nscd                                                                                                                                                                         
		 3098 root      20   0  187m  16m 3864 S      0  0.1  17:28.66 X                                                                                                                                                                            
		 7273 controll  20   0 4443m 135m  10m S      0  0.8   3:16.95 java                                                                                                                                                                         
		 9622 omm       20   0  671m 187m  11m S      0  1.2  11:13.91 java                                                                                                                                                                         
		 9639 omm       20   0 4669m 227m  11m S      0  1.4  11:12.22 java                                                                                                                                                                         
		10354 omm       20   0 4754m 235m  11m S      0  1.5   5:38.90 java                                                                                                                                                                         
		11839 omm       20   0  103m  11m 3324 S      0  0.1  10:03.19 omm_agent.bin                                                                                                                                                                
		19262 mongodb   20   0  384m  56m  26m S      0  0.4  19:07.57 mongod                                                                                                                                                                       
		22360 mongodb   20   0  134m 9240 3808 S      0  0.1   9:02.65 mongos                                                                                                                                                                       
		28769 root      20   0     0    0    0 S      0  0.0   0:01.85 kworker/2:2                                                                                                                                                                  
		    1 root      20   0 10540  736  700 S      0  0.0   0:27.31 init                                                                                                                                                                         
		    2 root      20   0     0    0    0 S      0  0.0   0:02.48 kthreadd                                                                                                                                                                     
		    3 root      20   0     0    0    0 S      0  0.0   0:13.02 ksoftirqd/0                                                                                                                                                                  
		    6 root      RT   0     0    0    0 S      0  0.0   1:02.22 migration/0                                                                                                                                                                  
		    7 root      RT   0     0    0    0 S      0  0.0   1:04.67 watchdog/0                                                                                                                                                                   
		    8 root      RT   0     0    0    0 S      0  0.0   1:10.59 migration/1                                                                                                                                                                  
		   10 root      20   0     0    0    0 S      0  0.0   0:06.69 ksoftirqd/1                                                                                                                                                                  
		   12 root      RT   0     0    0    0 S      0  0.0   0:21.46 watchdog/1                                                                                                                                                                   
		   13 root      RT   0     0    0    0 S      0  0.0   1:21.14 migration/2                                                                                                                                                                  
		   15 root      20   0     0    0    0 S      0  0.0   0:05.63 ksoftirqd/2                                                                                                                                                                  
		   16 root      RT   0     0    0    0 S      0  0.0   0:24.26 watchdog/2                                                                                                                                                                   
		   17 root      RT   0     0    0    0 S      0  0.0   1:12.33 migration/3                                                                                                                                                                  
		   19 root      20   0     0    0    0 S      0  0.0   0:06.10 ksoftirqd/3                                                                                                                                                                  
		   20 root      RT   0     0    0    0 S      0  0.0   0:52.91 watchdog/3        

参数说明：  
第一行：  
     14:52:42 ： 当前系统时间  
	 up 4 days, 21:28, ：系统持续运行4天21小时28分，没有重启过  
     2 users,：当前有两个用户登录  
     load average: 0.91, 1.50, 2.13   ：1分钟，5分钟，15分钟的负载情况。load average数据是每隔5秒钟检查一次活跃的进程数，然后按特定算法计算出的数值。如果这个数除以逻辑CPU的数量，**结果高于5的时候就表明系统在超负荷运转了。**     

第二行：  
当前有168个进程，及状态。（zambie是僵尸状态）  

第三行：  
cpu信息。  
5.9%us — 用户空间占用CPU的百分比。

3.4% sy — 内核空间占用CPU的百分比。

0.0% ni — 改变过优先级的进程占用CPU的百分比

90.4% id — 空闲CPU百分比

0.0% wa — IO等待占用CPU的百分比

0.0% hi — 硬中断（Hardware IRQ）占用CPU的百分比

0.2% si — 软中断（Software Interrupts）占用CPU的百分比

备注：在这里CPU的使用比率和windows概念不同，需要理解linux系统用户空间和内核空间的相关知识！

第四行,内存状态，具体信息如下：

32949016k total — 物理内存总量（32GB）

14411180k used — 使用中的内存总量（14GB）

18537836k free — 空闲内存总量（18GB）

169884k buffers — 缓存的内存量 （169M）

第五行，swap交换分区信息，具体信息说明如下：

32764556k total — 交换区总量（32GB）

0k used — 使用的交换区总量（0K）

32764556k free — 空闲交换区总量（32GB）

3612636k cached — 缓冲的交换区总量（3.6GB）

备注：

**第四行中使用中的内存总量（used）指的是现在系统内核控制的内存数，空闲内存总量（free）是内核还未纳入其管控范围的数量。纳入内核管理的内存不见得都在使用中，还包括过去使用过的现在可以被重复利用的内存，内核并不把这些可被重新使用的内存交还到free中去，因此在linux上free内存会越来越少，但不用为此担心。**

如果出于习惯去计算可用内存数，这里有个近似的计算公式：第四行的free + 第四行的buffers + 第五行的cached，按这个公式此台服务器的可用内存：18537836k +169884k +3612636k = 22GB左右。

对于内存监控，在top里我们要时刻监控第五行swap交换分区的used，如果这个数值在不断的变化，说明内核在不断进行内存和swap的数据交换，这是真正的内存不够用了。

第六行，空行。

第七行以下：各进程（任务）的状态监控，项目列信息说明如下：

PID — 进程id

USER — 进程所有者

PR — 进程优先级

NI — nice值。负值表示高优先级，正值表示低优先级

VIRT — 进程使用的虚拟内存总量，单位kb。VIRT=SWAP+RES

RES — 进程使用的、未被换出的物理内存大小，单位kb。RES=CODE+DATA

SHR — 共享内存大小，单位kb

S — 进程状态。D=不可中断的睡眠状态 R=运行 S=睡眠 T=跟踪/停止 Z=僵尸进程

%CPU — 上次更新到现在的CPU时间占用百分比

%MEM — 进程使用的物理内存百分比

TIME+ — 进程使用的CPU时间总计，单位1/100秒

COMMAND — 进程名称（命令名/命令行）	    

### 在top基本视图中，按键盘数字“1”，可监控每个逻辑CPU的状况 

	top - 15:04:23 up 4 days, 21:39,  2 users,  load average: 9.77, 7.73, 5.35
	Tasks: 173 total,   5 running, 166 sleeping,   0 stopped,   2 zombie
	Cpu0  :  4.7%us,  9.4%sy,  0.0%ni, 85.9%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
	Cpu1  :  1.3%us,  2.7%sy,  0.0%ni, 96.0%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
	Cpu2  :  0.4%us, 14.8%sy,  0.0%ni, 84.4%id,  0.0%wa,  0.0%hi,  0.4%si,  0.0%st
	Cpu3  :  0.0%us, 50.5%sy,  0.0%ni, 49.5%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
	Cpu4  :  3.1%us,  2.4%sy,  0.0%ni, 94.2%id,  0.0%wa,  0.0%hi,  0.3%si,  0.0%st
	Cpu5  :  2.4%us,  2.7%sy,  0.0%ni, 94.6%id,  0.0%wa,  0.0%hi,  0.3%si,  0.0%st
	Cpu6  :  2.0%us, 98.0%sy,  0.0%ni,  0.0%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
	Cpu7  :  0.3%us,  0.3%sy,  0.0%ni, 98.7%id,  0.0%wa,  0.0%hi,  0.7%si,  0.0%st
	Mem:     15953M total,    13559M used,     2394M free,      532M buffers
	Swap:     2053M total,       15M used,     2038M free,     3664M cached
	
	  PID USER      PR  NI  VIRT  RES  SHR S   %CPU %MEM    TIME+  COMMAND       

如图，显示当前8个cpu的使用情况。  
再按1回到基本视图。

### 基本视图下，按b高亮run状态的进程。其实和y有点重复，在高亮状态下，y是显示run的进程，按x，高亮当前排序的列，默认是按cpu使用率排序的。按shift+>或者<,改变排序列。

### top -c 显示完整的command  

### top -b 批处理显示，不方便看，适合导出转存。   

### top -n 数字  ：设置刷新次数

### top -d 数字  : 设置刷新周期

### top -p 进程号 ： 显示特定进程     

### top –H –p <pid> 查看单个线程的信息
