## Linux 常用命令

### mv
既可以用于移动。又可以改名。注意如果目标位置已经存在相同名字的文件，将会覆盖。
> mv logtool1.sh opensmb.sh 

### 修改系统日期和时间
- date：显示当前时间<br>
date –s：按字符串方式修改时间<br>
 1. 可以只修改日期,不修改时间,输入: date -s 2007-08-03
 2. 只修改时间,输入:date -s 14:15:00
 3. 同时修改日期时间,注意要加双引号,日期与时间之间有一空格，输入:
date -s "2007-08-03 14:15:00"

- hwclock:查看硬件时间<br>
 设置硬件时间:
 hwclock -set -date="07/07/06 10:19" （月/日/年 时:分:秒）

- 硬件时间和系统时间的同步<br>
  重新启动系统，硬件时间会读取系统时间，实现同步，但是在不重新启动的时候，需要用hwclock命令实现同步。
  1. 硬件时钟与系统时钟同步：大家都同步到硬件时间<br>
  hwclock --hctosys（hc代表硬件时间，sys代表系统时间）
  2. 系统时钟和硬件时钟同步：大家都同步到系统时间<br>
  hwclock --systohc

### tar
- tar 其实是打包命令，只是加上压缩的可选参数，使他具备了压缩解压缩的能力。他没有压缩能力，只是调用压缩功能。  
- 参数，很多，备查。

    命令参数：  
	必要参数有如下：  
		-A 新增压缩文件到已存在的压缩    
		-B 设置区块大小  
	    -c 建立新的压缩文件  
		-d 记录文件的差别  
		-r 添加文件到已经压缩的文件  
		-u 添加改变了和现有的文件到已经存在的压缩文件  
		**-x 从压缩的文件中提取文件**  
		-t 显示压缩文件的内容  
		**-z 支持gzip解压文件**  
		-j 支持bzip2解压文件  
		-Z 支持compress解压文件  
		**-v 显示操作过程**  
		-l 文件系统边界设置  
		-k 保留原有文件不覆盖  
		-m 保留文件不被覆盖  
		-W 确认压缩文件的正确性  
	可选参数如下：  
		-b 设置区块数目  
		-C 切换到指定目录  
		**-f 指定压缩文件**  
		--help 显示帮助信息  
		--version 显示版本信息  

- 常用命令  
	tar 
	解包：tar xvf FileName.tar  
	打包：tar cvf FileName.tar DirName  
	（注：tar是打包，不是压缩！）  
	
	.gz  
	解压1：gunzip FileName.gz  
	解压2：gzip -d FileName.gz  
	压缩：gzip FileName  

	**.tar.gz 和 .tgz**  
	**解压：tar zxvf FileName.tar.gz**  
	**压缩：tar zcvf FileName.tar.gz DirName**  
	
	.bz2  
	解压1：bzip2 -d FileName.bz2  
	解压2：bunzip2 FileName.bz2  
	压缩： bzip2 -z FileName  

	.tar.bz2  
	解压：tar jxvf FileName.tar.bz2  
	压缩：tar jcvf FileName.tar.bz2 DirName  
	
	.bz  
	解压1：bzip2 -d FileName.bz  
	解压2：bunzip2 FileName.bz  
	压缩：未知  

	.tar.bz  
	解压：tar jxvf FileName.tar.bz  
	压缩：未知  
	
	.Z  
	解压：uncompress FileName.Z  
	压缩：compress FileName  
	
    .tar.Z  
	解压：tar Zxvf FileName.tar.Z  
	压缩：tar Zcvf FileName.tar.Z DirName  
	
    .zip  
	解压：unzip FileName.zip  
	压缩：zip FileName.zip DirName  
	
	.rar  
	解压：rar x FileName.rar  
	压缩：rar a FileName.rar DirName 

### 重启网络服务
suse ：  service network restart  
ubuntu ：  /etc/init.d/networking restart

### chmod修改文件权限
chmod命令用于改变linux系统文件或目录的访问权限。用它控制文件或目录的访问权限。该命令有两种用法。一种是包含字母和操作符表达式的文字设定法；另一种是包含数字的数字设定法。

Linux系统中的每个文件和目录都有访问许可权限，用它来确定谁可以通过何种方式对文件和目录进行访问和操作。
　　文件或目录的访问权限分为只读，只写和可执行三种。以文件为例，只读权限表示只允许读其内容，而禁止对其做任何的更改操作。可执行权限表示允许将该文件作为一个程序执行。文件被创建时，文件所有者自动拥有对该文件的读、写和可执行权限，以便于对文件的阅读和修改。用户也可根据需要把访问权限设置为需要的任何组合。
　　有三种不同类型的用户可对文件或目录进行访问：文件所有者，同组用户、其他用户。所有者一般是文件的创建者。所有者可以允许同组用户有权访问文件，还可以将文件的访问权限赋予系统中的其他用户。在这种情况下，系统中每一位用户都能访问该用户拥有的文件或目录。
　　每一文件或目录的访问权限都有三组，每组用三位表示，分别为文件属主的读、写和执行权限；与属主同组的用户的读、写和执行权限；系统中其他用户的读、写和执行权限。当用ls -l命令显示文件或目录的详细信息时，最左边的一列为文件的访问权限。 例如：

命令： 

ls -al

输出：

[root@localhost test]# ll -al

总计 316lrwxrwxrwx 1 root root     11 11-22 06:58 linklog.log -> log2012.log

-rw-r--r-- 1 root root 302108 11-13 06:03 log2012.log

-rw-r--r-- 1 root root     61 11-13 06:03 log2013.log

-rw-r--r-- 1 root root      0 11-13 06:03 log2014.log

-rw-r--r-- 1 root root      0 11-13 06:06 log2015.log

-rw-r--r-- 1 root root      0 11-16 14:41 log2016.log

-rw-r--r-- 1 root root      0 11-16 14:43 log2017.log

我们以log2012.log为例：

-rw-r--r-- 1 root root 296K 11-13 06:03 log2012.log

第一列共有10个位置，第一个字符指定了文件类型。在通常意义上，一个目录也是一个文件。如果第一个字符是横线，表示是一个非目录的文件。如果是d，表示是一个目录。从第二个字符开始到第十个共9个字符，3个字符一组，分别表示了3组用户对文件或者目录的权限。权限字符用横线代表空许可，r代表只读，w代表写，x代表可执行。

例如：
　　- rw- r-- r--
　　表示log2012.log是一个普通文件；log2012.log的属主有读写权限；与log2012.log属主同组的用户只有读权限；其他用户也只有读权限。

　　确定了一个文件的访问权限后，用户可以利用Linux系统提供的chmod命令来重新设定不同的访问权限。也可以利用chown命令来更改某个文件或目录的所有者。利用chgrp命令来更改某个文件或目录的用户组。 

chmod命令是非常重要的，用于改变文件或目录的访问权限。用户用它控制文件或目录的访问权限。chmod命令详细情况如下。

1. 命令格式:

chmod [-cfvR] [--help] [--version] mode file   

2. 命令功能：

用于改变文件或目录的访问权限，用它控制文件或目录的访问权限。

3. 命令参数：

必要参数：
-c 当发生改变时，报告处理信息
-f 错误信息不输出
-R 处理指定目录以及其子目录下的所有文件
-v 运行时显示详细处理信息

选择参数：
--reference=<目录或者文件> 设置成具有指定目录或者文件具有相同的权限
--version 显示版本信息
<权限范围>+<权限设置> 使权限范围内的目录或者文件具有指定的权限
<权限范围>-<权限设置> 删除权限范围的目录或者文件的指定权限
<权限范围>=<权限设置> 设置权限范围内的目录或者文件的权限为指定的值

权限范围：
u ：目录或者文件的当前的用户
g ：目录或者文件的当前的群组
o ：除了目录或者文件的当前用户或群组之外的用户或者群组
a ：所有的用户及群组

权限代号：
r ：读权限，用数字4表示
w ：写权限，用数字2表示
x ：执行权限，用数字1表示
- ：删除权限，用数字0表示
s ：特殊权限 

该命令有两种用法。一种是包含字母和操作符表达式的文字设定法；另一种是包含数字的数字设定法。
　　1）. 文字设定法:
　　	chmod ［who］ ［+ | - | =］ ［mode］ 文件名
　　2）. 数字设定法
　　我们必须首先了解用数字表示的属性的含义：0表示没有权限，1表示可执行权限，2表示可写权限，4表示可读权限，然后将其相加。所以数字属性的格式应为3个从0到7的八进制数，其顺序是（u）（g）（o）。
　　例如，如果想让某个文件的属主有“读/写”二种权限，需要把4（可读）+2（可写）＝6（读/写）。
　　数字设定法的一般形式为：
　　	chmod ［mode］ 文件名

数字与字符对应关系如下：

r=4，w=2，x=1
若要rwx属性则4+2+1=7
若要rw-属性则4+2=6；
若要r-x属性则4+1=7。 

4. 使用实例：
实例1：增加文件所有用户组可执行权限

命令：

chmod a+x log2012.log

输出：

[root@localhost test]# ls -al log2012.log 

-rw-r--r-- 1 root root 302108 11-13 06:03 log2012.log

[root@localhost test]# chmod a+x log2012.log 

[root@localhost test]# ls -al log2012.log 

-rwxr-xr-x 1 root root 302108 11-13 06:03 log2012.log

[root@localhost test]#

说明：
　　即设定文件log2012.log的属性为：文件属主（u） 增加执行权限；与文件属主同组用户（g） 增加执行权限；其他用户（o） 增加执行权限。
　

实例2：同时修改不同用户权限

命令：

chmod ug+w,o-x log2012.log

输出：

[root@localhost test]# ls -al log2012.log 

-rwxr-xr-x 1 root root 302108 11-13 06:03 log2012.log

[root@localhost test]# chmod ug+w,o-x log2012.log 

[root@localhost test]# ls -al log2012.log 

-rwxrwxr-- 1 root root 302108 11-13 06:03 log2012.log


说明：
　　即设定文件text的属性为：文件属主（u） 增加写权限;与文件属主同组用户（g） 增加写权限;其他用户（o） 删除执行权限

实例3：删除文件权限

命令：

chmod a-x log2012.log

输出：

[root@localhost test]# ls -al log2012.log 

-rwxrwxr-- 1 root root 302108 11-13 06:03 log2012.log

[root@localhost test]# chmod a-x log2012.log 

[root@localhost test]# ls -al log2012.log 

-rw-rw-r-- 1 root root 302108 11-13 06:03 log2012.log

说明：
　	删除所有用户的可执行权限 
　	

实例4：使用“=”设置权限 

命令：

chmod u=x log2012.log

输出：

[root@localhost test]# ls -al log2012.log 

-rw-rw-r-- 1 root root 302108 11-13 06:03 log2012.log

[root@localhost test]# chmod u=x log2012.log 

[root@localhost test]# ls -al log2012.log 

---xrw-r-- 1 root root 302108 11-13 06:03 log2012.log

说明：

撤销原来所有的权限，然后使拥有者具有可读权限 

实例5：对一个目录及其子目录所有文件添加权限 

命令：

chmod -R u+x test4

输出：

[root@localhost test]# cd test4

[root@localhost test4]# ls -al

总计 312drwxrwxr-x 2 root root   4096 11-13 05:50 .

drwxr-xr-x 5 root root   4096 11-22 06:58 ..

-rw-r--r-- 1 root root 302108 11-12 22:54 log2012.log

-rw-r--r-- 1 root root     61 11-12 22:54 log2013.log

-rw-r--r-- 1 root root      0 11-12 22:54 log2014.log

[root@localhost test4]# cd ..

[root@localhost test]# chmod -R u+x test4

[root@localhost test]# cd test4

[root@localhost test4]# ls -al

总计 312drwxrwxr-x 2 root root   4096 11-13 05:50 .

drwxr-xr-x 5 root root   4096 11-22 06:58 ..

-rwxr--r-- 1 root root 302108 11-12 22:54 log2012.log

-rwxr--r-- 1 root root     61 11-12 22:54 log2013.log

-rwxr--r-- 1 root root      0 11-12 22:54 log2014.log

说明：

递归地给test4目录下所有文件和子目录的属主分配权限 

其他一些实例：

1）. 

命令：

chmod 751 file   

说明：

给file的属主分配读、写、执行(7)的权限，给file的所在组分配读、执行(5)的权限，给其他用户分配执行(1)的权限

2）. 

命令：

chmod u=rwx,g=rx,o=x file 

说明：

上例的另一种形式

3）. 

命令

chmod =r file 

说明：                　　　　

为所有用户分配读权限

3）. 

命令：

chmod 444 file 

说明： 

   	同上例

4）. 

命令：

chmod a-wx,a+r   file

说明：

同上例
