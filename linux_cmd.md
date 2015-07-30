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
