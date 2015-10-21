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
