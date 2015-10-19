## 网络基础
- 所有现代计算机网络都是包交换网络。

- icmp：traceroute，ping  
tcp：ftp，http，smtp  
udp：dns，nfs  
http：soap，xml-rpc, rest apis

- java不支持ICMP，java支持的协议只有tcp和udp，以及建立在tcp和udp之上的应用层协议。其他的协议在java中只能通过链接到原生代码来实现。

- 1-1023的端口号保留给已知的服务。



## 流
- 基本的流是阻塞的。  
流操作的是字节。  
reader和writer操作的是字符。、
  
- 不管有没有必要，对输出流总是记得flush()一下，总是好的。

- java7引入的带资源的try，以代替try-finally释放资源的固有模式。但是有一个要求是，被释放的对象必须实现了closeable接口。

- inputStream类还有三个不太常用的方法，允许程序备份和重新读取已经读取的数据。用mark()标记位置，在以后的某个时刻，可以reset()回这个位置，再重新进行读取。但是注意不能随意的向前重置，mark有个参数limit。如果超出了这个limit去reset()会抛出IO异常。  
另外，一个流任何时候只能有一个标记，新标记的位置会清楚前一个标记。  
还有，不是所有的流都支持这个，要先用markSupported()方法检查一下。这个设计不好。实际上支持标记的流只有BufferedInputStream和ByteArrayInputStream。

- BufferedInputStream默认缓冲区大小是2048个字节，BufferedOutputStream默认缓冲区大小是512个字节。  
BufferedReader和BufferedWriter的默认缓冲区大小是8192个字符

## 线程
- 单线程程序是在main()方法返回时退出。多线程程序在main()方法以及所有非守护线程都返回时退出。（守护线程完成后台任务，并不会阻止虚拟机退出）。

- 避免在构造函数中启动线程，尤其是线程还会回调该对象的情况。

- Excutors，callable，future组合使用隐藏多线程回调的诸多细节。future的get()会阻塞，知道结果可获取。  

- Collections里有几个对集合包装的方法，得到集合的线程安全视图。

        List<String> srcList = new ArrayList<String>();
        srcList.add("a");
        List<String> synchronizedList = Collections.synchronizedList(srcList);
不过以后对srcList的访问，都必须通过syn版本。

- 线程调度。为了能让其他线程有机会运行，一个线程有10种方式暂停或者指示它准备暂停。包括：  
	1. 可以对IO阻塞。 
	2. 可以对同步对象阻塞。  
不过不管对于io阻塞还是锁阻塞，线程都不会释放线程锁持有的锁。  
	3. 可以放弃。  
Thread.yield()显式让步。不会释放锁。因此在让步时不应该做任何同步。其他线程获取不到锁也执行不了，随后还是回到让步的这个线程，这就没意义了。
	4. 可以休眠。  
Thread.sleep(),更强力的一种放弃。yield是线程表示我愿意暂停，咱们来一起抢。sleep是不管有没有其他线程在等待执行，sleep都会导致线程暂停。记住sleep也不会释放锁。要使线程从sleep状态中醒来，调用该线程的interrupt方法。
	5. 可以连接另一个线程。  
join()允许一个线程在继续执行前等待另一个线程结束。即将另一个线程的执行插入进来。当前join基本可以使用executor+future来代替了。
	6. 可以等待一个对象。  
object.wait会释放对象锁。所以在wait之前，希望暂停的线程应该已经获得了对象锁。线程会释放这个object的对象锁，不会释放该线程拥有的其他对象锁。唤醒wait的三种方式：时间到期，线程被中断，对象得到通知。注意醒来要重新获得锁。通知notify和notifyall也是object的方法，所以notify和notifyall之前也要得到object的锁。notify基本上是随机选择一个等待这个对象的线程，notifyall是通知所有等待这个对象的线程。一定要将wait放在检测状态的代码中，不能假定线程醒来之后，对象的状态就是可用的。
	7. 可以结束。
	8. 可以被更高优先级线程抢占。
	9. 可以被挂起。
	10. 可以停止。  
最后这两种已经废弃不用，可能会让对象处于不一致状态。

### 线程池
        ExecutorService pool = Executors.newFixedThreadPool(4);
        
        // 完成所有等待的任务后结束
        pool.shutdown();
        
        // 立即停止，返回未执行的任务
        pool.shutdownNow();

## IP地址
- ipv4。4个字节的无符号数。点分十进制。
- ipv6。16字节长。冒号分隔8个区块。每块2个字节，作为4个16进制数。  
0的简写形式。  

### InetAddress类
java.net.InetAddress是java对ip地址(包括ipv4和ipv6)的高层表示。

        try
        {
            InetAddress address = InetAddress.getByName("www.baidu.com");
            System.out.println(address);
        }
        catch (UnknownHostException e)
        {
            // TODO Auto-generated catch block
            System.out.println("can't find ...");
        }


	www.baidu.com/112.80.248.74
  
这个方法第一次调用时，会和本地的DNS服务器建立连接，来查名字和地址。然后会缓存起来，下一次查就不会建立网络连接了，直接查缓存。

	InetAddress[] allByName = InetAddress.getAllByName("www.baidu.com");

使用`getAllByName`返回所有服务器的地址。  

使用`InetAddress.getLocalHost()`返回本机地址。  

使用`getByAddress(byte[] paramArrayOfByte)`或者`getByAddress(String paramString,byte[] paramArrayOfByte)`直接设定地址，不与DNS连接查询。  

            InetAddress local = InetAddress.getByAddress(new byte[] {(byte) 192, (byte) 168, 0,
                    (byte) 243});


java对不成功的DNS查询只缓存10秒。

		java.security.Security.setProperty("networkaddress.cache.ttl", "-1");
        System.out.println(java.security.Security.getProperty("networkaddress.cache.ttl"));

        java.security.Security.setProperty("networkaddress.negative.ttl", "10");
        System.out.println(java.security.Security.getProperty("networkaddress.negative.ttl"));

可以通过对系统参数的设置来控制缓存的时间。

当使用`getByName`然后以ip传为参数时，不会查DNS，所以也可以使用实际不存在的ip创建inetaddress对象。域名相对ip地址来说要稳定的多，优先选用域名。  

            InetAddress localHost = InetAddress.getLocalHost();
            // 知道主机名，不查DNS
            localHost.getHostName();
            
            // 知道主机名，也要去查DNS
            localHost.getCanonicalHostName();

`getAddress`方法返回的是byte[]，因为java中没有无符号字节，所以大于127的会被当做负数，要做一下转换：`b >= 0 ? b : b + 256`  

- 使用`isReachable(int timeout)`或者`isReachable(NetworkInterface interface, int ttl , int timeout)`来判断ip是否可达。

- InetAddress类重写的equals和hashcode方法都是只根据ip地址来处理，即ip地址相同的两个InetAddress对象就是相等的，不管主机名是啥。  

- Inet4Address和Inet6Address是InetAddress的子类，一般不用。判断getAddress()返回的字节数组的大小就可以了。  

- 
