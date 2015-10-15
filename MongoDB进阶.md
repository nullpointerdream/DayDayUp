### 固定集合
mongo支持创建大小固定的集合，当向已满的集合中插入文档时，最早的文档会被删除。默认情况下，固定集合是没有索引的，即使_id也没有。  
文档存储的顺序就是插入的顺序。这样固定集合的插入和按照插入顺序的查询速度都很快。  
很适合存储日志的场景。  

        > db.createCollection("fixedCollection", {capped : true, size : 10000, max : 10})
        { "ok" : 1 }
size是集合大小，字节单位。 max是最大文档数，可以不指定。         

### GridFS
GridFS是mongo中存储大的二进制文件的机制。  
使用，mongofiles  

        mongo:~/mongodb/mongo/bin # echo "hello yunsheng" > test.txt
        mongo:~/mongodb/mongo/bin # l
        total 282976
        drwxr-xr-x 2 root root     4096 Oct 15 10:40 ./
        drwxr-xr-x 3 root root     4096 Sep 29 11:36 ../
        -rwxr-xr-x 1 1046 1046 23520056 Aug  9  2014 bsondump*
        -rwxr-xr-x 1 1046 1046 11896064 Aug  9  2014 mongo*
        -rwxr-xr-x 1 1046 1046 23662856 Aug  9  2014 mongod*
        -rwxr-xr-x 1 1046 1046 23589784 Aug  9  2014 mongodump*
        -rwxr-xr-x 1 1046 1046 23536408 Aug  9  2014 mongoexport*
        -rwxr-xr-x 1 1046 1046 23582272 Aug  9  2014 mongofiles*
        -rwxr-xr-x 1 1046 1046 23561016 Aug  9  2014 mongoimport*
        -rwxr-xr-x 1 1046 1046 23528248 Aug  9  2014 mongooplog*
        -rwxr-xr-x 1 1046 1046 23339944 Aug  9  2014 mongoperf*
        -rwxr-xr-x 1 1046 1046 23626872 Aug  9  2014 mongorestore*
        -rwxr-xr-x 1 1046 1046 18412576 Aug  9  2014 mongos*
        -rwxr-xr-x 1 1046 1046 23578744 Aug  9  2014 mongostat*
        -rwxr-xr-x 1 1046 1046 23524120 Aug  9  2014 mongotop*
        -rw-r--r-- 1 root root       15 Oct 15 10:40 test.txt
        
        mongo:~/mongodb/mongo/bin # ./mongofiles put test.txt 
        connected to: 127.0.0.1
        added file: { _id: ObjectId('561f127fcf61ae7dcc32f7b4'), filename: "test.txt", chunkSize: 261120, uploadDate: new Date(1444876927045), md5: "ac7afa3f84478f5a583ba61c7ac986c4", length: 15 }
        done!
        mongo:~/mongodb/mongo/bin # ./mongofiles list
        connected to: 127.0.0.1
        test.txt	15
        mongo:~/mongodb/mongo/bin # rm test.txt 
        mongo:~/mongodb/mongo/bin # l
        total 282972
        drwxr-xr-x 2 root root     4096 Oct 15 10:42 ./
        drwxr-xr-x 3 root root     4096 Sep 29 11:36 ../
        -rwxr-xr-x 1 1046 1046 23520056 Aug  9  2014 bsondump*
        -rwxr-xr-x 1 1046 1046 11896064 Aug  9  2014 mongo*
        -rwxr-xr-x 1 1046 1046 23662856 Aug  9  2014 mongod*
        -rwxr-xr-x 1 1046 1046 23589784 Aug  9  2014 mongodump*
        -rwxr-xr-x 1 1046 1046 23536408 Aug  9  2014 mongoexport*
        -rwxr-xr-x 1 1046 1046 23582272 Aug  9  2014 mongofiles*
        -rwxr-xr-x 1 1046 1046 23561016 Aug  9  2014 mongoimport*
        -rwxr-xr-x 1 1046 1046 23528248 Aug  9  2014 mongooplog*
        -rwxr-xr-x 1 1046 1046 23339944 Aug  9  2014 mongoperf*
        -rwxr-xr-x 1 1046 1046 23626872 Aug  9  2014 mongorestore*
        -rwxr-xr-x 1 1046 1046 18412576 Aug  9  2014 mongos*
        -rwxr-xr-x 1 1046 1046 23578744 Aug  9  2014 mongostat*
        -rwxr-xr-x 1 1046 1046 23524120 Aug  9  2014 mongotop*
        mongo:~/mongodb/mongo/bin # ./mongofiles get test.txt
        connected to: 127.0.0.1
        done write to: test.txt
        mongo:~/mongodb/mongo/bin # l
        total 282976
        drwxr-xr-x 2 root root     4096 Oct 15 10:43 ./
        drwxr-xr-x 3 root root     4096 Sep 29 11:36 ../
        -rwxr-xr-x 1 1046 1046 23520056 Aug  9  2014 bsondump*
        -rwxr-xr-x 1 1046 1046 11896064 Aug  9  2014 mongo*
        -rwxr-xr-x 1 1046 1046 23662856 Aug  9  2014 mongod*
        -rwxr-xr-x 1 1046 1046 23589784 Aug  9  2014 mongodump*
        -rwxr-xr-x 1 1046 1046 23536408 Aug  9  2014 mongoexport*
        -rwxr-xr-x 1 1046 1046 23582272 Aug  9  2014 mongofiles*
        -rwxr-xr-x 1 1046 1046 23561016 Aug  9  2014 mongoimport*
        -rwxr-xr-x 1 1046 1046 23528248 Aug  9  2014 mongooplog*
        -rwxr-xr-x 1 1046 1046 23339944 Aug  9  2014 mongoperf*
        -rwxr-xr-x 1 1046 1046 23626872 Aug  9  2014 mongorestore*
        -rwxr-xr-x 1 1046 1046 18412576 Aug  9  2014 mongos*
        -rwxr-xr-x 1 1046 1046 23578744 Aug  9  2014 mongostat*
        -rwxr-xr-x 1 1046 1046 23524120 Aug  9  2014 mongotop*
        -rw-r--r-- 1 root root       15 Oct 15 10:43 test.txt
        mongo:~/mongodb/mongo/bin # cat test.txt 
        hello yunsheng

上面使用了mongofiles的put，list，get方法，很容易使用。  

还有search和delete

        mongo:~/mongodb/mongo/bin # ./mongofiles search test
        connected to: 127.0.0.1
        test.txt	15
        mongo:~/mongodb/mongo/bin # ./mongofiles delete test
        connected to: 127.0.0.1
        done!
        mongo:~/mongodb/mongo/bin # ./mongofiles list
        connected to: 127.0.0.1
        test.txt	15
        mongo:~/mongodb/mongo/bin # ./mongofiles delete test.txt
        connected to: 127.0.0.1
        done!
        mongo:~/mongodb/mongo/bin # ./mongofiles list
        connected to: 127.0.0.1
        
可以通过mongo各个版本的驱动来使用gridfs

### 服务器端脚本
db.eval来执行javascript脚本。  

        > db.eval("return 1")
        1
        > db.eval("function(){return 1}")
        1
        > 
可以封装成函数，也可以不封装。等价的。但是如果需要传递参数，那就只能用函数形式了。参数所谓eval的第二个参数，写成数组的形式。 

在mongo数据库中有一个特殊的集合system.js用来存储全局js变量或者脚本。用insert加入。


## mongo的管理


