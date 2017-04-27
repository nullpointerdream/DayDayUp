## 代理模式
为某个对象提供一个代理，以控制对这个对象的访问。 **代理类和委托类有共同的父类或父接口**，这样在任何使用委托类对象的地方都可以用代理对象替代。代理类负责请求的预处理、过滤、将请求分派给委托类处理、以及委托类执行完请求后的后续处理。  
图1：代理模式  
![](http://dl.iteye.com/upload/attachment/562226/cd337e41-6ee8-3619-a1c4-a2c096fb711c.jpg)

从图中可以看出，代理接口（Subject）、代理类（ProxySubject）、委托类（RealSubject）形成一个“品”字结构。
根据代理类的生成时间不同可以将代理分为静态代理和动态代理两种。
代理模式一般涉及到的角色有：  
抽象角色：声明真实对象和代理对象的共同接口，对应代理接口（Subject）；  
真实角色：代理角色所代表的真实对象，是我们最终要引用的对象，对应委托类（RealSubject）；  
代理角色：代理对象角色内部含有对真实对象的引用，从而可以操作真实对象，同时代理对象提供与真实对象相同的接口以便在任何时刻都能代替真实对象。同时，代理对象可以在执行真实对象操作时，附加其他的操作，相当于对真实对象进行封装，对应代理类（ProxySubject）
### 静态代理
由程序员创建或工具生成代理类的源码，再编译代理类。**所谓静态也就是在程序运行前就已经存在代理类的字节码文件，代理类和委托类的关系在运行前就确定了。**  
清单1：代理接口
  
	/**  
	 * 代理接口。 定义一个leader接口，声明一个签署文件的方法。
	 */  
	public interface Leader
	{
	    void sign();
	}

清单2：委托类，具体处理业务。

	/** 
	 * 真正执行任务的类，实现了代理接口。 
	 */  
	public class CEO implements Leader
	{
	
	    @Override
	    public void sign()
	    {
	        System.out.println("====CEO  签署文件====");
	
	    }
	
	} 
清单3：静态代理类
同样实现leader接口

	public class Assistant implements Leader
	{
	    private Leader leader;
	
	    public Assistant(Leader leader)
	    {
	        this.leader = leader;
	    }
	
	    @Override
	    public void sign()
	    {
	        System.out.println("接收文件，交给领导");
	        leader.sign();
	        System.out.println("传回文件");
	    }
	
	}
清单4：生成静态代理类工厂

		// 静态代理测试
        CEO ceo = new CEO();
        Leader leader1 = new Assistant(ceo);
        leader1.sign();
静态代理类优缺点
优点：业务类只需要关注业务逻辑本身，保证了业务类的重用性。这是代理的共有优点。
缺点：
1）代理对象的一个接口只服务于一种类型的对象，如果要代理的方法很多，势必要为每一种方法都进行代理，静态代理在程序规模稍大时就无法胜任了。
2）如果接口增加一个方法，除了所有实现类需要实现这个方法外，所有代理类也需要实现此方法。增加了代码维护的复杂度。  
如在给Leader接口增加了一个reject()方法，那么真实类当然要实现，但是静态代理类也必须实现。  
另外，如果要按照上述的方法使用代理模式，那么真实角色(委托类)必须是事先已经存在的，并将其作为代理对象的内部属性。但是实际使用时，一个真实角色必须对应一个代理角色，如果大量使用会导致类的急剧膨胀；此外，如果事先并不知道真实角色（委托类），该如何使用代理呢？这个问题可以通过Java的动态代理类来解决。
### 动态代理

动态代理类的源码是在程序运行期间由JVM根据反射等机制动态的生成，所以不存在代理类的字节码文件。代理类和委托类的关系是在程序运行时确定。

在java的动态代理机制中，有两个重要的类或接口，一个是 InvocationHandler(Interface)、另一个则是 Proxy(Class)，这一个类和接口是实现我们动态代理所必须用到的。首先我们先来看看java的API帮助文档是怎么样对这两个类进行描述的：  

>   InvocationHandler:	  
InvocationHandler is the interface implemented by the invocation handler of a proxy instance.  
  	
>	Each proxy instance has an associated invocation handler. When a method is invoked on a proxy instance, the method invocation    is encoded and dispatched to the invoke method of its invocation handler.

每一个动态代理类都必须要实现InvocationHandler这个接口，并且每个代理类的实例都关联到了一个handler，当我们通过代理对象调用一个方法的时候，这个方法的调用就会被转发为由InvocationHandler这个接口的 invoke 方法来进行调用。我们来看看InvocationHandler这个接口的唯一一个方法 invoke 方法：

> Object invoke(Object proxy, Method method, Object[] args) throws Throwable  

我们看到这个方法一共接受三个参数，那么这三个参数分别代表什么呢？

- proxy:　　指代我们所代理的那个真实对象
- method:　　指代的是我们所要调用真实对象的某个方法的Method对象
- args:　　指代的是调用真实对象某个方法时接受的参数  


接下来我们来看看Proxy这个类：

> Proxy provides static methods for creating dynamic proxy classes and instances, and it is also the superclass of all dynamic proxy classes created by those methods. 

Proxy这个类的作用就是用来动态创建一个代理对象的类，它提供了许多的方法，

	// 方法 1: 该方法用于获取指定代理对象所关联的调用处理器  
	static InvocationHandler getInvocationHandler(Object proxy)   
	  
	// 方法 2：该方法用于获取关联于指定类装载器和一组接口的动态代理类的类对象  
	static Class getProxyClass(ClassLoader loader, Class[] interfaces)   
	  
	// 方法 3：该方法用于判断指定类对象是否是一个动态代理类  
	static boolean isProxyClass(Class cl)   
	  
	// 方法 4：该方法用于为指定类装载器、一组接口及调用处理器生成动态代理类实例  
	static Object newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h) 

但是我们用的最多的就是 newProxyInstance 这个方法：

>public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces,  InvocationHandler h)  throws IllegalArgumentException
>
Returns an instance of a proxy class for the specified interfaces that dispatches method invocations to the specified invocation handler.

这个方法的作用就是得到一个动态的代理对象，其接收三个参数，我们来看看这三个参数所代表的含义：


- loader:　　一个ClassLoader对象，定义了由哪个ClassLoader对象来对生成的代理对象进行加载

- interfaces:　　一个Interface对象的数组，表示的是我将要给我需要代理的对象提供一组什么接口，如果我提供了一组接口给它，那么这个代理对象就宣称实现了该接口(多态)，这样我就能调用这组接口中的方法了

- h:　　一个InvocationHandler对象，表示的是当我这个动态代理对象在调用方法的时候，会关联到哪一个InvocationHandler对象上

好了，在介绍完这两个接口(类)以后，我们来通过一个实例来看看我们的动态代理模式是什么样的：

首先我们定义了一个Subject类型的接口，为其声明了两个方法：

	public interface Subject
	{
	    public void rent();
	    
	    public void hello(String str);
	}
接着，定义了一个类来实现这个接口，这个类就是我们的真实对象，RealSubject类：


	public class RealSubject implements Subject
	{
	    @Override
	    public void rent()
	    {
	        System.out.println("I want to rent my house");
	    }
	    
	    @Override
	    public void hello(String str)
	    {
	        System.out.println("hello: " + str);
	    }
	}

下一步，我们就要定义一个动态代理类了，前面说个，每一个动态代理类都必须要实现 InvocationHandler 这个接口，因此我们这个动态代理类也不例外：


	public class DynamicProxy implements InvocationHandler
	{
	    //　这个就是我们要代理的真实对象
	    private Object subject;
	    
	    //    构造方法，给我们要代理的真实对象赋初值
	    public DynamicProxy(Object subject)
	    {
	        this.subject = subject;
	    }
	    
	    @Override
	    public Object invoke(Object object, Method method, Object[] args)
	            throws Throwable
	    {
	        //　　在代理真实对象前我们可以添加一些自己的操作
	        System.out.println("before rent house");
	        
	        System.out.println("Method:" + method);
	        
	        //    当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
	        method.invoke(subject, args);
	        
	        //　　在代理真实对象后我们也可以添加一些自己的操作
	        System.out.println("after rent house");
	        
	        return null;
	    }
	
	}

最后，来看看我们的Client类：

	public class Client
	{
	    public static void main(String[] args)
	    {
	        //    我们要代理的真实对象
	        Subject realSubject = new RealSubject();
	
	        //    我们要代理哪个真实对象，就将该对象传进去，最后是通过该真实对象来调用其方法的
	        InvocationHandler handler = new DynamicProxy(realSubject);
	
	        /*
	         * 通过Proxy的newProxyInstance方法来创建我们的代理对象，我们来看看其三个参数
	         * 第一个参数 handler.getClass().getClassLoader() ，我们这里使用handler这个类的ClassLoader对象来加载我们的代理对象
	         * 第二个参数realSubject.getClass().getInterfaces()，我们这里为代理对象提供的接口是真实对象所实行的接口，表示我要代理的是该真实对象，这样我就能调用这组接口中的方法了
	         * 第三个参数handler， 我们这里将这个代理对象关联到了上方的 InvocationHandler 这个对象上
	         */
	        Subject subject = (Subject)Proxy.newProxyInstance(handler.getClass().getClassLoader(), realSubject
	                .getClass().getInterfaces(), handler);
	        
	        System.out.println(subject.getClass().getName());
	        subject.rent();
	        subject.hello("world");
	    }
	}

我们先来看看控制台的输出：


	$Proxy0
	
	before rent house
	Method:public abstract void com.yunsheng.dynamicproxy.Subject.rent()
	I want to rent my house
	after rent house
	
	before rent house
	Method:public abstract void com.yunsheng.dynamicproxy.Subject.hello(java.lang.String)
	hello: world
	after rent house

我们首先来看看``` $Proxy0 ```这东西，我们看到，这个东西是由 ```System.out.println(subject.getClass().getName());``` 这条语句打印出来的，那么为什么我们返回的这个代理对象的类名是这样的呢？

```Subject subject = (Subject)Proxy.newProxyInstance(handler.getClass().getClassLoader(), realSubject
                .getClass().getInterfaces(), handler);```
可能我以为返回的这个代理对象会是Subject类型的对象，或者是InvocationHandler的对象，结果却不是，首先我们解释一下为什么我们这里可以将其转化为Subject类型的对象？原因就是在newProxyInstance这个方法的第二个参数上，我们给这个代理对象提供了一组什么接口，那么我这个代理对象就会实现了这组接口，这个时候我们当然可以将这个代理对象强制类型转化为这组接口中的任意一个，因为这里的接口是Subject类型，所以就可以将其转化为Subject类型了。

同时我们一定要记住，**通过 Proxy.newProxyInstance 创建的代理对象是在jvm运行时动态生成的一个对象，它并不是我们的InvocationHandler类型，也不是我们定义的那组接口的类型，而是在运行是动态生成的一个对象，并且命名方式都是这样的形式，以$开头，proxy为中，最后一个数字表示对象的标号。**   
其中 N 是一个逐一递增的阿拉伯数字，代表 Proxy 类第 N 次生成的动态代理类，值得注意的一点是，并不是每次调用 Proxy 的静态方法创建动态代理类都会使得 N 值增加，原因是如果对同一组接口（包括接口排列的顺序相同）试图重复创建动态代理类，它会很聪明地返回先前已经创建好的代理类的类对象，而不会再尝试去创建一个全新的代理类，这样可以节省不必要的代码重复生成，提高了代理类的创建效率。   
具体来讲，是这样的：  

1. 产生代理类$Proxy0类  
执行了Proxy.newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h)
将产生$Proxy0类，它继承Proxy对象，并根据第二个参数，实现了被代理类的所有接口，自然就可以生成接口要实现的所有方法了（这时候会重写hashcode，toString和equals三个方法），但是还没有具体的实现体；  

2. 将代理类$Proxy0类加载到JVM中  
这时候是根据Proxy.newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h)它的第一个参数----就是被代理类的类加载器，把当前的代理类加载到JVM中  

3. 创建代理类$Proxy0类的对象  
调用的$Proxy0类的$Proxy0（InvocationHandler）构造函数，生成$Proxy0类的对象  
参数就是Proxy.newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h)它的第三个参数
这个参数就是我们自己实现的InvocationHandler对象，$Proxy0对象调用所有要实现的接口的方法，都会调用InvocationHandler对象的invoke（）方法实现； 然后InvocationHandler对象中又组合加入了代理类代理的接口类的实现类；所以在invoke方法中可以调用真实对象的方法。

4. 生成代理类的class byte  
动态代理生成的都是二进制class字节码   


接着我们来看看这两句 
```
	subject.rent();  
	subject.hello("world");
```

这里是通过代理对象来调用实现的那种接口中的方法，这个时候程序就会跳转到由这个代理对象关联到的 handler 中的invoke方法去执行，而我们的这个 handler 对象又接受了一个 RealSubject类型的参数，表示我要代理的就是这个真实对象，所以此时就会调用 handler 中的invoke方法去执行：


	public Object invoke(Object object, Method method, Object[] args)
	            throws Throwable
	    {
	        //　　在代理真实对象前我们可以添加一些自己的操作
	        System.out.println("before rent house");
	        
	        System.out.println("Method:" + method);
	        
	        //    当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
	        method.invoke(subject, args);
	        
	        //　　在代理真实对象后我们也可以添加一些自己的操作
	        System.out.println("after rent house");
	        
	        return null;
	    }

我们看到，在真正通过代理对象来调用真实对象的方法的时候，我们可以在该方法前后添加自己的一些操作，同时我们看到我们的这个 method 对象是这样的：

```public abstract void com.yunsheng.dynamicproxy.Subject.rent()```

```public abstract void com.yunsheng.dynamicproxy.Subject.hello(java.lang.String)```  
正好就是我们的Subject接口中的两个方法，这也就证明了当我通过代理对象来调用方法的时候，起实际就是委托由其关联到的 handler 对象的invoke方法中来调用，并不是自己来真实调用，而是通过代理的方式来调用的。

这就是我们的java动态代理机制

动态代理的目的并不是简单的替代，而是在真实方法的前后增加一些控制逻辑。如拦截器等

动态代理与静态代理相比较，最大的好处是接口中声明的所有方法都被转移到调用处理器一个集中的方法中处理（InvocationHandler.invoke）。这样，在接口方法数量比较多的时候，我们可以进行灵活处理，而不需要像静态代理那样每一个方法进行中转。
就是说在接口中增加方法，动态代理类可以不做修改。
