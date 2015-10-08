## MongoDB

1. MongoDb放弃关系模型的主要原因就是为了获得更方便的扩展性。MongoDb在设计之初就考虑到了扩展的问题。它所采用的面向文档的数据模型使其可以自动在多台服务器之间分割数据。还可以平衡集群的数据和负载，自动重排文档。需要更大的容量时，只需要在集群中添加新机器，剩下的事交给数据库来处理。

2. 有的和没的功能  
	有的：
	- 索引
	- 存储javascript，不必再使用存储过程了。
	- 聚合，支持MapReduce和其他聚合工具。
	- 固定集合，集合的大小是有上限的。
	- 文件存储  

	没的：没有了关系型数据库的join和多行事务，主要是为了扩展性做的舍弃。

3. 为了性能，mongodb使用MongoDB传输协议所谓与服务器交互的主要方式，这比HTTP/REST协议开销更少。

4. MongoDb的管理理念是让服务器自管理。如果主服务器挂了，自动切换到备份服务器上，并将备份服务器提升为活跃服务器。分布式环境下，集群只要知道增加了新的节点，会自动集成和配置新节点。

### 文档
1. 文档中不能有重复的键。
2. 文档中的键值对是有序的。内容一样，但顺序不一样的文档是完全不同的。
3. 键是区分大小写的。
4. 键不能含有\0，这个字符用来表示键的结尾。
5. .和$有特殊意义
6. _开头的键是保留的，不严格要求。

### 集合
集合是文档的集合。类似于关系数据库无模式的表。
索引是按照集合来定义的。把同种类型的文档放入同一个集合可以使索引更加有效。

- 集合名不能是空字符串""，不能含有\0，不能以system.开头，用户创建的集合不要含有$
- 子集合，如aa.bb,aa.cc，更合理的组织数据

### 数据库
多个集合组成数据库。一个MongoDb实例可以承载多个数据库，他们之间可视为完全独立的。每个数据库都有独立的权限控制。

- 数据库名应全部小写，最多64字节。不能是空，不能含有.$/\和\0
- 数据库最终会变成文件系统里的文件
- 保留的数据库名：
	- admin  从权限角度看，这是root数据库，要是将一个用户添加到这个数据库，那么这个用户将自动继承所有数据库的权限，一些特定的服务器端命令如列出所有数据库或者关闭服务器等，也只能从这个数据库运行。
	- local  这个数据库永远不会被复制，这个数据库可用于存储只与本地单台服务器相关的集合。
	- config 当Mongo用于分片设置时，config数据库在内部使用，用于保存分片的相关信息

数据库名+集合名就是完整的集合限定名，称为命名空间。

### 安装MongoDB
1. 服务器suse11
2. 下载mongodb downloads.mongodb.org/linux/mongodb-linux-x86_64-2.6.4.tgz （3.*版本需要GLIBCXX_3.4.14）
3. 将mongodb-linux-x86_64-2.6.4.tgz下载到mongodb文件夹
4. 解压：tar -zxvf mongodb-linux-x86_64-2.6.4.tgz
5. 重命名：mv mongodb-linux-x86_64-2.6.4 mongodb
6. 设置环境变量：export PATH=<mongodb-install-directory>/bin:$PATH
    例如：export "PATH=$PATH:/home/nim/mongodb/bin/"  >>/etc/profile
7. 启动，Mongodb数据库需要指定存储数据的文件夹，默认存储目录为：/data/db。
创建目录/data/db:mkdir -p /data/db  
进入mongodb/bin 运行` mongod`即可启动mongodb。  
可加参数：  
-port portNo 指定端口，默认27017  
--dbpath dataPath 指定存放数据的目录  
--logpath logPath  指定日志目录  
--fork  后台运行，--fork必须和--logpath一起

		mongo:~/mongodb/mongo/bin # ./mongod --fork --logpath /root/mongodb/logs
		about to fork child process, waiting until server is ready for connections.
		forked process: 19820
		child process started successfully, parent exiting

8. 登陆  
执行mongo  
另外mongo还会启动一个基本的http服务，端口是比主端口高1000（即默认是28017）
这样可以在浏览器登陆mongo了，前提是配置文件里面加入httpinterface=true,然后再创建个用户就可以了

### mongo shell
运行mongo是启动shell，这是一个完备的Javascript解释器。  
开启的时候，shell会自动连接到MongoDB服务器的test数据库，并将这个数据库连接赋值给全局变量db，这个变量是通过shell访问数据库的主要入口点。  
- CRUD基本操作

		> people = {"name":"yunsheng","age":26}
		{ "name" : "yunsheng", "age" : 26 }
		> db.peoples.insert(people)
		WriteResult({ "nInserted" : 1 })
		> db.peoples.find()
		{ "_id" : ObjectId("55fc3d627754b90acf65b39f"), "name" : "yunsheng", "age" : 26 }
		>  
可以看出，peoples集合在insert的时候并不存在，但是会自动创建出来。对插入的文档，mongo会自动加入一个_id字段。

		> people.sex = "male"
		male
		> db.peoples.update({"name":"yunsheng"},people)
		WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
		> db.peoples.find()
		{ "_id" : ObjectId("55fc3d627754b90acf65b39f"), "name" : "yunsheng", "age" : 26, "sex" : "male" }
		>   
可以看到，mongo可以动态的给文档增加一个属性，然后根据参数更新

		> db.peoples.remove({"name":"ss"})
		WriteResult({ "nRemoved" : 0 })
		> db.peoples.remove({"name":"yunsheng"})
		WriteResult({ "nRemoved" : 1 })

删除

### help
通过help查看api帮助  
里面还有再下一层的db.help()查看db级别的api，db.foo.help()查看集合的api等等帮助方法。  

另外，因为这个js的shell，所以可以通过调用方法是不加括号，来查看方法的源码，这样可以看到更具体的用法。如
	
	> db.peoples.update
	function ( query , obj , upsert , multi ){
	    assert( query , "need a query" );
	    assert( obj , "need an object" );
		。。。

### 关于集合名
- 如果使用了数据库自有的属性作为集合名，在使用时，mongo是先去找属性，找不到才会作为集合返回。  
可以使用db.getCollection（）方法强制返回集合。

- 因为在js中，x.y和x['y']等价，所以我们可以使用变量来访问子集合了。如遍历子集合。

### 数据类型
- JSON的6中类型：null,布尔，数字，字符串，数组，对象。
- mongo的改进：  
对象id{"x":ObjectId()},  
日期 从标准纪元开始的毫秒数{"x":new Date()}  
正则表达式：文档中可以包含正则表达式  
代码：文档中可以包含js代码  
undefined：可以使用  
内嵌文档：可以文档包含文档
数字：mongo有三种数字类型（32位整数，64位整数，64位浮点数），但js只有一种数字类型。所以存在一个数据转换的问题。shell中的数字被mongoDB默认是双精度的浮点数。所以在shell下你获得一个文档里面有32位整数，然后更新了文档中的某个字段（没有更新整数），这个整数也会被保存为浮点数。所以明智的做法是不要在shell中更新整个文档。（注意这里谈的是shell下）  
还有一个问题是64位整数不一定能用64位浮点数准确表示。 对不能准确表示的情况，shell会添加两个键，bottom和top分别表示低32位和高32位。  
数组。  
日期：使用js中的new Date()生成一个日期类型。注意不要用Date()，这只是返回date的字符串表示，会造成混乱。

### _id
每个文档都会有一个_id值，来唯一标记一个文档。这个_id可以是任意类型的，默认是ObjectId类型。ObjectId使用12字节的存储空间，每个字节两位16进制数。这样就是24位的字符串。  
ObjectId的生成是前四个字节时间戳，然后三个字节机器码，然后两个字节PID，然后三个字节计数器。因为时间戳在前，所以大致上文档会按照时间排序。  
文档中没有_id时，mongodb会自动生成。

## 文档操作

### 批量插入
批量插入可以明显提高速度，因为一次批量插入是一次tcp请求。可以避免多次请求的开销。  
只能向一个集合批量插入。一次mongo消息的限制是16M，所以批量插入也是有限制的。  
批量插入的原理：驱动程序将数据转换成BSON格式，数据库解析BSON，校验是否含有_id和大小是否大于16M。除此之外不做其他校验，然后数据直接作为文本入库。这样可以避免注入攻击。（在shell中使用Object.bsonsize(doc)可以计算doc文档转成bson格式后的大小）  

### 删除文档
db.users.remove(),删除所有文档，但是不会删除集合，也不会删除索引。可加参数。  

db.drop_collec

### 更新文档
更新是原子操作  

  
修改器的使用  

1. 使用增加值修改器"$inc":{"age":2}  
用来增加已有键的值，键不存在时，创建。只能用于数字值。

		> db.peoples.find()
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "name" : "yunsheng", "age" : 26 }
		> db.peoples.update({"name":"yunsheng"},
		... {"$inc":{"age":2}})
		WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
		> db.peoples.find()
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "name" : "yunsheng", "age" : 28 }
使用修改器时，是不可以修改_id的值的

2. "$set"修改器。  
"$set"修改器用来指定一个键，如果键不存在，创建。

		> db.peoples.find()
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "name" : "yunsheng", "age" : 28 }
		> db.peoples.update({"_id" : ObjectId("560a0981d6aff3c237ce22ed")},{"$set" : {"sex":"male"}})
		WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
		> db.peoples.find()
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "name" : "yunsheng", "age" : 28, "sex" : "male" }
		>
记住修改单个键值对，一定要用"$set"修改器，如果忘加了后果如下

		> db.peoples.update({"_id" : ObjectId("560a0981d6aff3c237ce22ed")},{"sex":"female"})
		WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
		> db.peoples.find()
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female" }
整个文档被修改了  

3. 使用"$unset"可将键删除
4. 数组修改器"$push"，"$pop","$pull"
如果键已经存在，"$push"会向已有的数组末尾添加一个元素。如果数组不存在会创建一个数组。

5. 当前，update操作默认只更新匹配到的第一个文档，如果希望更新所有，要给update方法的第四个参数传true。  
function ( query , obj , upsert , multi )

### 数据库连接
数据库会为每一个MongoDb数据库连接创建一个队列，存放这个连接的请求。注意每个连接都有独立的队列，开两个shell就有两个数据库连接，在一个shell里插入，在另一个shell里面并不一定能立即查到。

## 查询
### find
1. find({...})可加查询参数,key:value形式，多个条件以,间隔，被解释成and关系。
2. find({},{...})用第二个参数可指定返回的列

		> db.peoples.find({},{"_id":0})
		{ "sex" : "female" }

		> db.peoples.find({},{"_id":1})
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed") }
		> 
0：不显示，1：显示。_id列不明确指定不返回，总是被返回。

3. 查询条件  
"$lt","$lte","$gt","$gte"  分别是<,<=,>,>=  

		> db.peoples.find({"age":{"$lt":20,"$gt":10}})
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12 }


"$ne"是不等于

"$in"用来加一个条件数组，条件可以使不同类型的。

		> db.peoples.find({"age":{"$in":[12,66]}})
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "name" : "bb", "age" : 66 }
"$nin"不在

"$or"或条件。用法举例："$or":[条件数组]

		> db.peoples.find()
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "name" : "bb", "age" : 66 }
		> db.peoples.find({"$or":[{"name":"aa"},{"age":66}]})
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "name" : "bb", "age" : 66 }
		> db.peoples.find({"$or":[{"name":"aa"},{"age":{"$gt":60}}]})
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "name" : "bb", "age" : 66 }
		> 

"$mod"模运算。下例是用参数模10，余6的记录

		> db.peoples.find({"age":{"$mod":[10,6]}})
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "name" : "bb", "age" : 66 }
		> 

"$not"非运算。放到前面

		> 
		> db.peoples.find({"age":{"$not":{"$mod":[10,6]}}})
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }
		> 
		
### 查询的条件句是内层文档的键，而修改器则是外层文档的键。一个文档的键可以有多个查询条件，但是不能同时使用多个修改器。

### null
- null 可以匹配值为null的文档，也会匹配出没有这个键的文档

		> db.peoples.find({"hobby":null})
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "name" : "bb", "age" : 66 }
		> 
如果只想匹配存在这个键，只是值为null的文档（"$exists"）：

		> db.peoples.find({"hobby":{"$in":[null], "$exists":true }})
		> 
注意，没有"$eq"操作符

### 正则表达式
mongo的查询支持正则表达式。  

		> db.peoples.find({"name":/BB/i})
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "name" : "bb", "age" : 66 }
		> 

### 查询数组
查询数组时，只使用数组中的一个元素匹配，可以查出整条文档。

		> db.peoples.find()
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "name" : "bb", "age" : 66 }
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming" ] }
		> db.peoples.find({"hobby" : "running"})
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming" ] }
		> 

1. 使用"$all"操作符，查询包含多个值的数组的文档。元素顺序无关。

		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming" ] }
		> db.peoples.find({"hobby" :{"$all" : ["running","swimming"]}})
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming" ] }
		> db.peoples.find({"hobby" :{"$all" : ["swimming","running"]}})
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming" ] }
		> 
		
但是不使用"$all"操作符，直接使用数组精确匹配，就必须元素内容和顺序都一样，即数组一样才匹配。

		> db.peoples.find({"hobby" :["swimming","running"]})
		> db.peoples.find({"hobby" :["running" , "swimming"]})
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming" ] }
		> db.peoples.find({"hobby" :["running"]})
		> 
		
2. "$size"操作符
通过数组长度匹配。

		> db.peoples.find({"hobby" :{"$size" : 3}})
		> db.peoples.find({"hobby" :{"$size" : 2}})
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming" ] }
		> 
但是"$size"不能与"$gt"等连用，即只能匹配精确长度的数组。  
一个替代方法是，为文档加一个自增的size属性，如：

		> db.peoples.find()
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "name" : "bb", "age" : 66 }
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming" ] }
		> db.peoples.update({ "_id" : ObjectId("56132236702bab83edb12602")},
		... {"$push" : {"hobby": "pingpang"}})
		WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
		> db.peoples.find()
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "name" : "bb", "age" : 66 }
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming", "pingpang" ] }
		> 
这个更新并没有增加一个自增键。

		> db.peoples.update({ "_id" : ObjectId("56132236702bab83edb12602")}, {"$inc" : {"size" : 4}})
		WriteResult({ "nMatched" : 1, "nUpserted" : 0, "nModified" : 1 })
		> db.peoples.find()
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "name" : "bb", "age" : 66 }
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming", "pingpang", "basketball" ], "size" : 4 }
下载有了这个"size"键,就可以和"$gt"等连用了。

		> db.peoples.find({"size":{"$gt":4}})
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming", "pingpang", "basketball", "football" ], "size" : 5 }
		> 
		
3. "$slice"指定返回条数
用$slice指定返回的数组的条数。

		> db.peoples.find({ "_id" : ObjectId("56132236702bab83edb12602")},{"hobby" : {"$slice":3 }})
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming", "pingpang" ], "size" : 5 }
		> 
"$slice":3 可以返回前三个，-3可以返回后三个

		> db.peoples.find({ "_id" : ObjectId("56132236702bab83edb12602")},{"hobby" : {"$slice":[2,2] }})
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "pingpang", "basketball" ], "size" : 5 }
		>
调过前两个，返回之后的2个。

		> db.peoples.find({ "_id" : ObjectId("56132236702bab83edb12602")},{"hobby" : {"$slice":-3 }, "_id" : 0})
		{ "hobby" : [ "pingpang", "basketball", "football" ], "size" : 5 }
		> 
可以看到，其他键是默认返回的，不想返回加过滤。


### 查询内嵌文档
使用点号查询。深入一级。

		> db.peoples.find({"name.first" : "yang"})
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "name" : { "first" : "yang", "last" : "yunsheng" }, "age" : 66 }
		> 
		
再比如有属下数据：

		> db.peoples.find()
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }
		{ "_id" : ObjectId("56132236702bab83edb12602"), "hobby" : [ "running", "swimming", "pingpang", "basketball", "football" ], "size" : 5 }
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "score" : { "yuwen" : 99, "shuxue" : 88, "yingyu" : 77 }, "name" : "yunsheng", "age" : 26 }

现在想要查询，数学分数大于90的记录，如果用：

		> db.peoples.find({"score.name" : "shuxue", "score.result":{"$gt":90} })
		{ "_id" : ObjectId("560b9125f566345c78fed34b"), "score" : [ { "name" : "yuwen", "result" : 99 }, { "name" : "shuxue", "result" : 88 } ], "name" : "yunsheng", "age" : 26 }
		
这是不对的，因为这是对整个文档去匹配。score.name和score.result都可以匹配到，但不是我们想要的在同一个内嵌文档中。   
正确写法是这样的：

		> db.peoples.find({"score": {"$elemMatch": {"name":"shuxue","result":{"$gt":90 } }}})
		> 

### where查询
 使用where查询，可以使用js函数进行过滤，当执行函数的结果为true时，该文档会放到结果集中。
 
		 > db.peoples.find({"$where" : "this.name == 'aa'"})
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }

		> db.peoples.find({"$where" : "function() {return this.name == 'aa'}"})
		{ "_id" : ObjectId("560a0981d6aff3c237ce22ed"), "sex" : "female", "age" : 12, "name" : "aa" }
		> 
注意的是，where查询的效率很低，比常规查询慢很多。因为要将BSON转换成js对象，还不能用索引。所以where是在走投无路下用的。


### 游标
查询的结果可以放到游标中，暂时保存。  

		> for(i = 1; i<= 100; i++){ db.myCol.insert({x : i}); }
		WriteResult({ "nInserted" : 1 })
先建一个文档。
然后用游标保存结果。
		> var cursor = db.myCol.find();
		> while(cursor.hasNext()){
		... obj = cursor.next();
		... print(obj.x);
		... }
		1
		2
		3
		4
		5
		。。。。

还可以用foreach方法

		> var cursor = db.myCol.find();
		> cursor.forEach(function(obj){ print(obj.x); }  );
		1
		2
		3。。。
		
使用游标时，find()不会立即执行，而是当真正获取结果时，才发送查询请求。  

### limit，skip，sort
limit用来限制返回结果的数量。 > db.myCol.find().limit(5)  
skip用来跳过指定条数。> db.myCol.find().skip(5);
sort用来指定排序，1是升序，-1是降序。可以指定多个排序的键。 > db.myCol.find().sort({"x" : -1})  

这三个要灵活的组合连用。实现分页等。    
一个键可以有多种类型的值，对不同类型的排序是预定义好的：  
	
	When comparing values of different BSON types, MongoDB uses the following comparison order, from lowest to highest:
	MinKey (internal type)
	Null
	Numbers (ints, longs, doubles)
	Symbol, String
	Object
	Array
	BinData
	ObjectId
	Boolean
	Date
	Timestamp
	Regular Expression
	MaxKey (internal type)

### 分页
要避免对过多数据的skip，会变得很慢。  
所以分页的时候，可以用一些办法代替skip。例如一般分页都会根据某个字段排序，我们可以利用这个，每次保存下最后一个文档，在下一次查询时，加一个查询条件，查大于这个值的。

		> var last = null;
		> var page1 = db.myCol.find().sort({"x" : 1}).limit(5);
		> while(page1.hasNext()){
		... last = page1.next();
		... }
		{ "_id" : ObjectId("5615dd40ea202bacf71960df"), "x" : 5 }
		
		> var page2 = db.myCol.find({"x":{"$gt": last.x}}).sort({"x" : 1}).limit(5);
		> while(page2.hasNext()){ print(page2.next().x); }
		6
		7
		8
		9
		10
		
游标的释放是很快的，page1和page2在完成遍历时已经释放掉了。

## 索引
mongo的索引和关系型数据库的几乎一模一样，所以其技巧同样适用于mongo。

### 创建索引

		> db.myCol.ensureIndex({"x" : 1})
		{
			"createdCollectionAutomatically" : false,
			"numIndexesBefore" : 1,
			"numIndexesAfter" : 2,
			"ok" : 1
		}
		> 
后面的值表示方向，1是升序，-1是降序。  
联合多个属性建立索引时，元素相同，方向不同的索引是不同的。  
也就是说可以建立多个元素一样，但是方向不一样的索引，已加速不同的查询。

mongo会对索引的顺序进行重排序，以便利用更多的索引。

索引{"a":1,"b":1,"c":1}相当于也有了索引{"a":1},{"a":1,"b":1},但是需要使用这些索引{"b":1}，{"c":1},{"a":1,"c":1}的并不能获得加速。就是说多索引，相当于同时有了前面的索引。  

### 为内嵌文档创建索引
一样

### 为排序创建索引
如果对没有索引的键进行排序，mongo需要将所有文档提取到内存中，一旦集合达到不能在内存中进行排序，mongo就会报错。所以按照顺序来索引，以便mongo按照顺序提取数据，可以进行大数据的排序了。
























