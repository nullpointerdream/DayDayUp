## Dubbo服务遇到服务暴露地址错误的问题

今天有同事找过来，遇到一个见鬼的问题。  
他们的服务器找网管改过ip，之后发布服务。在dubbo admin上查到服务的地址竟然是旧的ip。  
看了dubbo的日志，发现服务export时，暴露的ip确实是旧ip。排除是zk方面的问题。  
然后看了dubbo查询本地ip的源码。  
类`com.alibaba.dubbo.common.utils.NetUtils`

	/**
     * 遍历本地网卡，返回第一个合理的IP。
     * 
     * @return 本地网卡IP
     */
    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null)
            return LOCAL_ADDRESS;
        InetAddress localAddress = getLocalAddress0();
        LOCAL_ADDRESS = localAddress;
        return localAddress;
    }

此时怀疑，难道是多网卡？  
idconfig -a后发现只有一个网卡，ip也是对的。  

继续看代码，发现是直接调用java.net.InetAddress  
我模仿dubbo写了个简单代码，在服务器上一跑，还真是得到了旧ip。  
现在基本确定是服务器配置问题。  
网络或者DNS相关的配置，无非/etc/hosts和/etc/resolv.conf。  
打开/etc/hosts一看，果然是域名解析忘改了。  
	
	收获：  
		1. 以后服务器要注意是不是多网卡  
		2. hosts配置要对	

继续看代码。  
http://ju.outofmemory.cn/entry/111943 讲的非常好。  
java.net.InetAddress#getLocalHost。我是jdk1.8。这个版本应该是已经没有上面文章说的cache是forever问题。只是5秒的cache有效期。  
一步步看下去，最好是调用本地方法，查/etc/hosts文件。  


  




