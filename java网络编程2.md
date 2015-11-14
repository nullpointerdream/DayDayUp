## HTTP
http说是一种协议，更准确的说是定义了一种数据格式。    

- 每个http1.0请求包含4个步骤。
	1. 客户端在端口80或这指定端口打开一个tcp连接
	2. 客户端向服务器发送消息，请求指定路径上的资源。
	3. 服务器向客户端发送相应。
	4. 服务器关闭连接。

- http1.1之后。可以在一个tcp连接上多次发送请求和响应。
- 格式。   
请求和响应是一样的基本格式： 一个首部行，一个包含元数据的http首部，一个空行\r\n\r\n，一个消息体（get没有）。  
请求格式：  
首部行的第一行的请求行，方法+资源路径+协议版本   
然后一般会在请求行下包含一些信息，以key：value形式，key不区分大小写，value有时区分有时不区分。。ASCII。如果一个值太长，可以在下一行加一个空格或tab继续。  
如host：服务器的名，是web服务器可以区分ip相同但不同名的主机。  
user-agent：浏览器信息  
accept：告诉服务器客户端可以处理的数据类型。但服务器常常忽略这个。mime类型分为两级，类型/子类型。  
服务器看到这个空行，他就开始通过同一个tcp连接发送相应。  
响应先是一个状态行，然后是头部信息，空行，报文主体。  

### http方法
http的请求和响应都是无状态的。  
get 安全且幂等。  
put 不安全但幂等。  
delete 不安全但幂等。  
post 不安全且不幂等。    
如今post在web上大量滥用，不完成提交的所有安全操作应当使用get，而不是post。只有真正提交的操作才应当使用post。    

head：相当于get，只不过他只返回资源的首部，不返回具体数据。这个方法经常用于检查文件的修改日期，查看本地缓存的文件副本是否仍然有效。  

options：客户端可询问服务器可以如何处理一个指定的资源。  

trace：会回显客户端请求来进行调试，特别是在代理服务器工作不正常时。  

java不支持其他的如copy、move等方法。  

  
## Cookie
- 作用：存储持久的客户端状态，指示会话id，登陆凭证，购物车内容，用户首选项等。
- 存在：存在于请求和响应的http头部中，key-value对。他是在服务器端生成的，从服务器端传到客户端，再从客户端传回服务器端。
- 只能是非空白的ASCII文本
- 第一次服务器set-Cookie，以后大家来往用Cookie。服务器可以设置不止一个cookie。
- cookie有多个属性，过期时间，域，路径，语言，端口，版本，安全选项等等。  
Set-Cookie	 lang=zh; Expires=Wed, 21-Oct-15 07:36:47 GMT; Path=/; Domain=.yunsheng.com
这个域包含所有yunsheng.com的子域。就是说浏览器可以把这个cookie会给demo.yunsheng.com。  path指定了cookie可以使用的范围。  
Max-Age=1000，可以设置cookie在多少秒后过期，而不是一个具体的时间。  
secure ： 一个无值的属性，意思是浏览器应拒绝通过非安全通道发送这种cookie，意味着用https替代http  
httponly ： 也是一个无值的属性，告诉浏览器只通过http和https返回cookie，特别强调不能由javascript返回。这针对cookie窃取攻击。  

### java中的cookie

- CookieManager。用来接收cookie。

		CookieManager cookieManager = new CookieManager();
		        // 设置cookie的策略
		        // CookiePolicy.ACCEPT_ORIGINAL_SERVER:只接受正在对话的服务器的cookie
		        //        CookiePolicy.ACCEPT_ALL:全部接受
		        //        CookiePolicy.ACCEPT_NONE:不接受
		//        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
		        cookieManager.setCookiePolicy(new myCookiePolicy());
		        CookieHandler.setDefault(cookieManager);
	
	
	
		/**
		 * 更精细的policy控制
		 * 实现CookiePolicy接口。实现shoudAccept方法
		 */
		class myCookiePolicy implements CookiePolicy
		{
		
		    @Override
		    public boolean shouldAccept(URI paramURI, HttpCookie paramHttpCookie)
		    {
		        if (paramURI.getAuthority().equalsIgnoreCase("yunsheng.com")
		                || paramHttpCookie.getDomain().equalsIgnoreCase("yunsheng.com"))
		        {
		            return true;
		        }
		        return false;
		    }
		
		}


- CookieStore用来存放cookie
`        CookieStore cookieStore = cookieManager.getCookieStore();`
然后就可以增删查HttpCookie。

## URLConnection
URL类和URLConnection之间的区别并不明显，URLConnection提供了对http首部的访问，可以配置发送给服务器的请求参数，除了可以读取服务器数据外，还可以向服务器写入数据。  

- http默认的编码方式是iso-8859-1
- 手动断开连接，`disconnect()` 

            URL myUrl = new URL("http://www.yunsheng.com/cn/");
            URLConnection connection = myUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.disconnect();

## 客户端Socket
Socket使我们可以将网络连接看作是另外一个可以读写字节的流。隐藏了网络实现的细节。  
Socket类是java完成客户端TCP操作的基础类，这个类本身使用原始代码与主机操作系统的本地TCP栈进行通信。

### 使用socket
socket是两台机器之间的一个连接，它完成7个基本操作。

- 连接远程机器
- 发送数据
- 接收数据
- 关闭连接  
这四个是客户端和服务器端socket都可用的
- 绑定端口
- 监听入站数据
- 在绑定端口上接受来自远程机器的连接。  
这三个只有服务器端socket可用。  
连接是全双工的。  

 		try (Socket socket = new Socket("localhost", 8080))
        {
            // 建议为连接设置超时时间单位毫秒
            // 防止服务器接受了连接，但是之后出问题了，一直连接但是又没有响应，又不会断开。
            socket.setSoTimeout(10000);
            // 接受服务器信息
            InputStream in = socket.getInputStream();
            Reader r = new InputStreamReader(in, "UTF-8");
            BufferedReader reader = new BufferedReader(r);
            for (String line = reader.readLine(); !line.equals("."); line = reader
                    .readLine())
            {
                System.out.println(line);
            }

            // 向服务器发送消息
            OutputStream out = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            writer.write("hello,yunsheng");
            writer.flush();

        }
- 前面用的socket构造器是构造对象且连接的。  
还有其他构造器只构造，不连接。分步来。

		Socket s = new Socket();
        // SocketAddress存储socket连接信息
        SocketAddress addr = new InetSocketAddress("localhost", 8888);
        try
        {
            // 第二个参数是超时时间，0表示无限等待
            s.connect(addr, 0);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

可以用SocketAddress存储已经连接的地址信息，以备下次再用。

            SocketAddress localAddr = s.getLocalSocketAddress();
            SocketAddress remoteAddr = s.getRemoteSocketAddress();
###关闭
- socket的close()方法同时关闭socket，以及socket的输入和输出。所以只要在finally中close掉socket就好。
- shutdownInput()方法和shutdownOutput()方法可以只关闭一半，并不会关闭socket。
- 即使shutdown了input和output，仍然需要手动关闭socket。那两个只关闭流。
- 可以用isInputShutdown()和isOutputShutdown()方法确认流的状态。
- 用isClosed()判断socket是否关闭。不过如果一个socket从一开始从未连接，那么isClosed()返回false。
- isConnected()方法，这个方法不是返回当前是否连接着，而是返回是否曾经连接过。。。所以判断一个socket当前是否连接着，应该是isConnected返回true，isClosed()返回false。
- isBound()方法返回当前socket是否成功绑定到本地系统上的出端口，这对server端socket很重要。

### 设置socket选项
指定java所依赖的原生socket如何发送和接受数据。客户端socket支持9个选项。

- `s.setTcpNoDelay(true);`确保包尽可能快的发送，而不管包的大小。包一旦准备好就发，不再去缓冲。
- `s.setSoLinger(true, 0);`设置socket关闭时，如何处理还没发送的数据报。默认的，close()方法将立即返回，但系统仍然会尝试发送剩下的数据。当延迟设置为0时，那么关闭socket时，未发送的会被直接丢弃。设置为正数，阻塞这么多秒去尝试发送。
- `s.setSoTimeout(0);`默认下，从socket读取数据时，read()会阻塞尽可能长的时间。这个方法设置阻塞时间。
- `s.setReceiveBufferSize(1000);s.setSendBufferSize(1000);`设置缓冲区的大小。

- `s.setKeepAlive(true);`打开这个功能后，客户端一般每两个小时会通过一个空闲的连接，发送一个空包，以确认服务器没有挂掉。如果挂掉了，尝试12分钟之后，会干掉这个socket。如果不开启这个功能，那么连接着挂掉的服务器的socket会一直存在下去。

- `s.setOOBInline(true);`设置可以接收处理紧急数据
## ServerSocket
### 基本使用
    // 服务器端socket基本生命周期如下
    public static void main(String[] args)
    {
        ServerSocket serverSocket = null;
        try
        {
            // 1，在特定端口创建一个serverSocket
            serverSocket = new ServerSocket(8888);
            System.out.println("start server socket=================");

            while (true)
            {
                // 2，使用accept()方法监听入站连接，accept()会一直阻塞，直到有一个客户端尝试连接，accept()会返回一个Socket
                Socket clientSocket = null;
                try
                {
                    // 区分server端和client的异常
                    clientSocket = serverSocket.accept();
                    System.out.println("get a connection----------");
                    // 3,根据服务的不同，调用clientSocket的getOutputStream()或getInputStream()得到客户端的输入或输出
                    OutputStream outputStream = clientSocket.getOutputStream();
                    InputStream inputStream = clientSocket.getInputStream();

                    // 4,得到输入输出流之后，开始交互
                    Reader reader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    Writer writer = new OutputStreamWriter(outputStream);
                    StringBuilder content = new StringBuilder();
                    for (String line = bufferedReader.readLine(); !line
                            .equals(""); line = bufferedReader.readLine())
                    {
                        content.append(line);
                    }
                    writer.write("content is : " + content.toString());
                    writer.flush();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if (null != clientSocket)
                    {
                        // 5,双方有一方或者双方都关闭连接
                        // 服务器端不要依赖客户端一定会关闭socket。可以自己控制。
                        clientSocket.close();
                    }
                }
            }
            // 6,服务端返回状态2
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            if (null != serverSocket)
            {
                try
                {
                    serverSocket.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


### 多线程使用
操作系统将指向某个特定端口的入站请求存储在一个FIFO的队列中，java默认的长度是50，但不同的操作系统会有不同。队列长度最大不能超过操作系统支持的最大大小。

    @Test
    public void test1()
    {
        ExecutorService pool = Executors.newFixedThreadPool(50);
        try (ServerSocket serverSocket = new ServerSocket(8888))
        {
            while (true)
            {
                Socket connection = serverSocket.accept();
                ThreadTask task = new ThreadTask(connection);
                pool.submit(task);
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static class ThreadTask implements Callable<Void>
    {

        Socket socket;

        public ThreadTask(Socket connection)
        {
            this.socket = connection;
        }

        @Override
        public Void call() throws Exception
        {
            try
            {
                Writer out = new OutputStreamWriter(socket.getOutputStream());
                out.write("hello,yunsheng");
                out.flush();
            }
            finally
            {
                if (null != socket)
                {
                    try
                    {
                        socket.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            return null;

        }

    }
### 关闭
java7中ServerSocket类实现了AutoCloseable接口。所以可以使用try-with-resource模式。低版本的jdk注意还是要用try-finally中if-not-null模式关闭。  
close()之后，可以使用isClosed()方法检查。  
注意isBound()方法可检查该serverSocket是否已经绑定了端口，但是同socket一样，如果曾经绑定过，但是该serversocket已经关掉了，isBound仍然返回true。  
