## Suse11安装Mysql

### 安装过程
1. 下载。我下的版本是5.1.73
2. 解压。
3. 安装server。

		'''
		mongo:~/mysqldb # rpm -ivh MySQL-server-community-5.1.73-1.sles11.x86_64.rpm 
		Preparing...                ########################################### [100%]
		   1:MySQL-server-community ########################################### [100%]
		insserv: Service network is missed in the runlevels 4 to use service mysql
		mysql                     0:off  1:off  2:on   3:on   4:on   5:on   6:off
		
		PLEASE REMEMBER TO SET A PASSWORD FOR THE MySQL root USER !
		To do so, start the server, then issue the following commands:
		
		/usr/bin/mysqladmin -u root password 'new-password'
		/usr/bin/mysqladmin -u root -h mongo password 'new-password'
		
		Alternatively you can run:
		/usr/bin/mysql_secure_installation
		
		which will also give you the option of removing the test
		databases and anonymous user created by default.  This is
		strongly recommended for production servers.
		
		See the manual for more instructions.
		
		Please report any problems with the /usr/bin/mysqlbug script!
		
		Starting MySQL.                                                       done
		Giving mysqld 2 seconds to start
		'''

4. 查看mysql服务已启用。

		mongo:~/mysqldb # netstat -nat
		Active Internet connections (servers and established)
		Proto Recv-Q Send-Q Local Address           Foreign Address         State      
		tcp        0      0 0.0.0.0:27017           0.0.0.0:*               LISTEN      
		tcp        0      0 0.0.0.0:5801            0.0.0.0:*               LISTEN      
		tcp        0      0 0.0.0.0:3306            0.0.0.0:*               LISTEN      
3306端口已经监听

5. 安装客户端

		mongo:~/mysqldb # rpm -ivh  MySQL-client-community-5.1.73-1.sles11.x86_64.rpm 
		Preparing...                ########################################### [100%]
		   1:MySQL-client-community ########################################### [100%]

6. 登陆  
登录MySQL的命令是mysql， mysql 的使用语法如下：  
mysql [-u username] [-h host] [-p[password]] [dbname]  
MySQL默认用户是root，由于 初始没有密码，第一次进时只需键入mysql即可。

		mongo:~/mysqldb # mysql
		Welcome to the MySQL monitor.  Commands end with ; or \g.
		Your MySQL connection id is 9
		Server version: 5.1.73-community MySQL Community Server (GPL)
	
		Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.

		Oracle is a registered trademark of Oracle Corporation and/or its
		affiliates. Other names may be trademarks of their respective
		owners.
	
		Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
	
		mysql> 

7. 修改密码  
因为开始时root没有密码，所以-p旧密码一项就可以省略了。

		mongo:/etc/rc.d # /usr/bin/mysqladmin -u root password 123456
		mongo:/etc/rc.d # mysql
		ERROR 1045 (28000): Access denied for user 'root'@'localhost' (using password: NO)
		mongo:/etc/rc.d # mysql -u root -p
		Enter password: 
		Welcome to the MySQL monitor.  Commands end with ; or \g.
		Your MySQL connection id is 12
		Server version: 5.1.73-community MySQL Community Server (GPL)

		Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.

		Oracle is a registered trademark of Oracle Corporation and/or its
		affiliates. Other names may be trademarks of their respective
		owners.

		Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

		mysql> 

### 目录
MySQL安装完成后不象SQL Server默认安装在一个目录，它的数据库文件、配置文件和命令文件分别在不同的目录。  
　　下面就介绍一下这几个目录。  
　　1. 数据库目录  
　　/var/lib/mysql/  
　　2. 配置文件  
　　/usr/share/mysql（mysql.server命令及配置文件）  
　　3. 相关命令    
　　/usr/bin(mysqladmin mysqldump等命令)  
　　4. 启动脚本  
　　/etc/rc.d/rcX.d/（启动脚本文件mysql的目录）  

### 启动与停止
1. 启动  
MySQL安装完成后启动文件mysql在/etc/init.d目录下，在需要启动时运行下面命令即可。  

		mongo:/etc/init.d # ./mysql start  
		Starting MySQL 

2. 停止

		mongo:/etc/init.d # /usr/bin/mysqladmin -u root -p shutdown
		Enter password: 
		mongo:/etc/init.d # netstat -nat
	查看端口3306的状态已经变成FIN_WAIT2

3. 自动启动  

   1. 察看mysql是否在自动启动列表中  
   
			mongo:/etc/init.d # /sbin/chkconfig -list | grep mysql
			mysql                     0:off  1:off  2:on   3:on   4:on   5:on   6:off 

　　 2. 把MySQL添加到你系统的启动服务组里面去

		mongo:/etc/init.d # /sbin/chkconfig -add mysql
		insserv: Service network is missed in the runlevels 4 to use service mysql
		mysql                     0:off  1:off  2:on   3:on   4:on   5:on   6:off

　　 3. 把MySQL从启动服务组里面删除。
			
			mongo:/etc/init.d # /sbin/chkconfig -del mysql
			mysql                     0:off  1:off  2:off  3:off  4:off  5:off  6:off




### 更改MySQL目录
　　MySQL默认的数据文件存储目录为/var/lib/mysql。假如要把目录移到/data下需要进行下面几步：  
1. 建立data目录   
　  	cd /  
　　	mkdir data  
2. 把MySQL服务进程停掉：  
　　mysqladmin -u root -p shutdown  
3. 把/var/lib/mysql整个目录移到/data  
　　mv /var/lib/mysql　/home/data/  
　　这样就把MySQL的数据文件移动到了/home/data/mysql下   
  记得赋予权限呀否则启动不了的：  
  我这里就直接赋予权限：chmod 777 mysql -R   
4. 找到my.cnf配置文件  
　　如果/etc/目录下没有my.cnf配置文件，请到/usr/share/mysql/下找到*.cnf文件，拷贝其中一个到/etc/并改名为my.cnf)中。命令如下：  
　　 [root@test1 mysql]# cp /usr/share/mysql/my-medium.cnf　/etc/my.cnf
5. 编辑MySQL的配置文件/etc/my.cnf  
　　为保证MySQL能够正常工作，需要指明mysql.sock文件的产生位置。 修改socket=/var/lib/mysql/mysql.sock一行中等号右边的值为：/data/mysql/mysql.sock 。  
操作如下：  
　　 vi　 my.cnf　　　 (用vi工具编辑my.cnf文件，找到下列数据修改之)  
　　 # The MySQL server  
　　　 [mysqld]    
　　　 port　　　= 3306  
　　　#socket　 = /var/lib/mysql/mysql.sock（原内容，为了更稳妥用“#”注释此行）  
　　　 socket　 = /data/mysql/mysql.sock　　　（加上此行）  
default_character_set=utf8（加上默认字符集设置，防止乱码）  
[client]  
default_character_set=utf8（加上默认字符集设置，防止乱码）  
6. 修改SUSE下MySQL启动脚本/etc/init.d/mysql  
　　最后，需要修改MySQL启动脚本/etc/nit.d/mysql，把其中datadir=/var/lib/mysql一行中，等号右边的路径改成你现在的实际存放路径：/data/mysql。  
　　[root@test1 etc]# vi　/etc/init.d/mysql  
　　#datadir=/var/lib/mysql　　　　（注释此行）  
　　datadir=/data/mysql　　 （加上此行）  
注意：有一个默认的datadir=  的设定，在那个地方设定没用  
7. 重新启动MySQL服务  
　　/etc/init.d/mysql　start  
　　或用reboot命令重启Linux  
　　如果工作正常移动就成功了，否则对照前面的7步再检查一下。  


### 常用操作

1. 显示库  

		mysql> show databases;
		+--------------------+
		| Database           |
		+--------------------+
		| information_schema |
		| mysql              |
		| test               |
		+--------------------+
		3 rows in set (0.00 sec)  
mysql库非常重要，它里面有MySQL的系统信息，我们改密码和新增用户，实际上就是用这个库中的相关表进行操作。

2. 显示表

		mysql> use mysql;
		Reading table information for completion of table and column names
		You can turn off this feature to get a quicker startup with -A

		Database changed
		mysql> show tables;
		+---------------------------+
		| Tables_in_mysql           |
		+---------------------------+
		| columns_priv              |
		| db                        |
		| event                     |
		| func                      |
		| general_log               |
		| help_category             |
		| help_keyword              |
		| help_relation             |
		| help_topic                |
		| host                      |
		| ndb_binlog_index          |
		| plugin                    |
		| proc                      |
		| procs_priv                |
		| servers                   |
		| slow_log                  |
		| tables_priv               |
		| time_zone                 |
		| time_zone_leap_second     |
		| time_zone_name            |
		| time_zone_transition      |
		| time_zone_transition_type |
		| user                      |
		+---------------------------+
		23 rows in set (0.00 sec)

3. 显示表结构   
describe + 表名  
mysql> describe user;
4. 其他基本操作略
5. 增加mysql用户  
格式：grant 哪些操作（select,insert,update,delete） on 数据库.* to 用户名@登录主机 identified by "密码"  
例1. 增加一个用户user_1密码为123，让他可以在任何主机上登录，并对所有数据库有查询、插入、修改、删除的权限。首先用以root用户连入MySQL，然后键入以下命令：  
　　mysql> grant select,insert,update,delete on *.* to me@"%" identified by "123";  
此处要注意一个问题，因为mysql匿名用户的存在。  

		mysql> select User,Host  from mysql.user;
		+-----------+-----------+
		| User      | Host      |
		+-----------+-----------+
		| me        | %         |
		| yunsheng1 | %         |
		| root      | 127.0.0.1 |
		|           | localhost |
		| root      | localhost |
		| yunsheng2 | localhost |
		|           | mongo     |
		| root      | mongo     |
		+-----------+-----------+
		8 rows in set (0.00 sec)
此时，用me用户在本机登录会失败，在其他机器可以成功，这是因为在本机登陆是去匹配me@localhost，此时会先匹配上''@localhost这个匿名账户，所以失败。  
所以，先删除这些匿名用户。delete from mysql.user where user = '';   
尝试一下，还不行的话，重启一下mysql。  
网上看到一段话：
> mysql参考手册。发现其中增加用户部分有这么一段话： 
其中两个账户有相同的用户名monty和密码some_pass。两个账户均为超级用户账户，具有完全的权限可以做任何事情。一个账户 ('monty'@'localhost')只用于从本机连接时。另一个账户('monty'@'%')可用于从其它主机连接。请注意monty的两个账户必须能从任何主机以monty连接。没有localhost账户，当monty从本机连接时，mysql_install_db创建的localhost的匿名用户账户将占先。结果是，monty将被视为匿名用户。原因是匿名用户账户的Host列值比'monty'@'%'账户更具体，这样在user表排序顺序中排在前面。


例1增加的用户是十分危险的，如果知道了user_1的密码，那么他就可以在网上的任何一台电脑上登录你的MySQL数据库并对你的数据为所欲为了，解决办法见例2。  
例2. 增加一个用户user_2密码为123,让此用户只可以在localhost上登录，并可以对数据库aaa进行查询、插入、修改、删除的 操作（localhost指本地主机，即MySQL数据库所在的那台主机），这样用户即使用知道user_2的密码，他也无法从网上直接访问数据库，只能 通过MYSQL主机来操作aaa库。  
　　mysql>grant select,insert,update,delete on aaa.* to user_2@localhost identified by "123";   
　　用新增的用户如果登录不了MySQL，在登录时用如下命令：  
　　mysql -u user_1 -p　-h 192.168.111.118　（-h后跟的是要登录主机的ip地址）
  
权限有： 
  
	  Alter 　　　　　　修改表和索引 
	　　Create 　　　　 创建数据库和表 
	　　Delete 　　　　 删除表中已有的记录 
	　　Drop 　　 抛弃（删除）数据库和表 
	　　INDEX 　　　　 创建或抛弃索引 
	　　Insert 　　　　 向表中插入新行 
	　　REFERENCE 　　未用 
	　　Select　　　　 检索表中的记录 
	　　Update 　　　　 修改现存表记录 
	　　FILE 　　　　　　读或写服务器上的文件 
	　　PROCESS 　　 查看服务器中执行的线程信息或杀死线程 
	　　RELOAD 　　　　重载授权表或清空日志、主机缓存或表缓存。 
	　　SHUTDOWN　　 关闭服务器 
	　　ALL 　　　　　　所有；ALL PRIVILEGES同义词 
	　　USAGE 　　　　特殊的“无权限”权限
	
