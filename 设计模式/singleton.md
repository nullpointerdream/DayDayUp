# 设计模式

## 单例模式
单实例Singleton设计模式可能是被讨论和使用的最广泛的一个设计模式了，这可能也是面试中问得最多的一个设计模式了。这个设计模式主要目的是想在整个系统中只能出现一个类的实例。这样做当然是有必要的，比如你的软件的全局配置信息，或者是一个Factory，或是一个主控类，等等。你希望这个类在整个系统中只能出现一个实例。  
实现单例的核心在于private私有化类中的构造方法。

### 单例的反面教材(线程不安全的懒汉式)

	// version 1.0
	public class Singleton {
	    private static Singleton singleton = null;
	    private Singleton() {  }
	    public static Singleton getInstance() {
	        if (singleton== null) {
	            singleton= new Singleton();
	        }
	        return singleton;
	    }
	}

在上面的实例中，我想说明下面几个Singleton的特点：（下面这些东西可能是尽人皆知的，没有什么新鲜的）

- 私有（private）的构造函数，表明这个类是不可能形成实例了。这主要是怕这个类会有多个实例。
- 即然这个类是不可能形成实例，那么，我们需要一个静态的方式让其形成实例：getInstance()。注意这个方法是在new自己，因为其可以访问私有的构造函数，所以他是可以保证实例被创建出来的。
- 在getInstance()中，先做判断是否已形成实例，如果已形成则直接返回，否则创建实例。
- 所形成的实例保存在自己类中的私有成员中。
- 我们取实例时，只需要使用Singleton.getInstance()就行了。  

这段代码简单明了，而且使用了懒加载模式，但是却存在致命的问题。当有多个线程并行调用 getInstance() 的时候，同时进入singleton== null判断，就会创建多个实例。也就是说在多线程下不能正常工作。

### 线程安全的懒汉式
为了解决并发问题，加锁。   
 
    public static synchronized Singleton getInstance() 
	{
	    if (instance == null) {
	        instance = new Singleton();
	    }
	    return instance;
	}

这样虽然可以解决并发问题，但是效率比较低。因为我们本来只需要在第一次创建的时候进行加锁判断，现在却搞成了每次判断都要加锁。  
所以进一步改进。

### 双重检查锁 DCL
双重检验锁模式（double checked locking pattern），是一种使用同步块加锁的方法。程序员称其为双重检查锁，因为会有两次检查 instance == null，一次是在同步块外，一次是在同步块内。为什么在同步块内还要再检验一次？因为可能会有多个线程一起进入同步块外的 if，如果在同步块内不进行二次检验的话就会生成多个实例了。

	public static Singleton getSingleton() {
	    if (instance == null) {                         //Single Checked
	        synchronized (Singleton.class) {
	            if (instance == null) {                 //Double Checked
	                instance = new Singleton();
	            }
	        }
	    }
	    return instance ;
	}
这段代码看起来很完美，很可惜，它是有问题。主要在于instance = new Singleton()这句，这并非是一个原子操作，事实上在 JVM 中这句话大概做了下面 3 件事情。

1. 给 instance 分配内存
2. 调用 Singleton 的构造函数来初始化成员变量
3. 将instance对象指向分配的内存空间（执行完这步 instance 就为非 null 了）  

但是在 JVM 的即时编译器中存在指令重排序的优化。也就是说上面的第二步和第三步的顺序是不能保证的，最终的执行顺序可能是 1-2-3 也可能是 1-3-2。如果是后者，则在 3 执行完毕、2 未执行之前，被线程二抢占了，这时 instance 已经是非 null 了（但却没有初始化），所以线程二会直接返回 instance，然后使用，然后顺理成章地报错。

解决：我们只需要将 instance 变量声明成 volatile 就可以了。

	public class Singleton {
	    private volatile static Singleton instance; //声明成 volatile
	    private Singleton (){}
	
	    public static Singleton getSingleton() {
	        if (instance == null) {                         
	            synchronized (Singleton.class) {
	                if (instance == null) {       
	                    instance = new Singleton();
	                }
	            }
	        }
	        return instance;
	    }
	   
	}
有些人认为使用 volatile 的原因是可见性，也就是可以保证线程在本地不会存有 instance 的副本，每次都是去主内存中读取。但其实是不对的。使用 volatile 的主要原因是其另一个特性：禁止指令重排序优化。也就是说，在 volatile 变量的赋值操作后面会有一个内存屏障（生成的汇编代码上），读操作不会被重排序到内存屏障之前。比如上面的例子，取操作必须在执行完 1-2-3 之后或者 1-3-2 之后，不存在执行到 1-3 然后取到值的情况。从「先行发生原则」的角度理解的话，就是对于一个 volatile 变量的写操作都先行发生于后面对这个变量的读操作（这里的“后面”是时间上的先后顺序）。

但是特别注意在 Java 5 以前的版本使用了 volatile 的双检锁还是有问题的。其原因是 Java 5 以前的 JMM （Java 内存模型）是存在缺陷的，即时将变量声明成 volatile 也不能完全避免重排序，主要是 volatile 变量前后的代码仍然存在重排序问题。这个 volatile 屏蔽重排序的问题在 Java 5 中才得以修复，所以在这之后才可以放心使用 volatile。

相信你不会喜欢这种复杂又隐含问题的方式，当然我们有更好的实现线程安全的单例模式的办法。

### 饿汉式 static final field（我们正在用的）
这种方法非常简单，因为单例的实例被声明成 static 和 final 变量了，在第一次加载类到内存中时就会初始化，所以创建实例本身是线程安全的。

	public class Singleton{
	    //类加载时就初始化
	    private static final Singleton instance = new Singleton();
	    
	    private Singleton(){}
	
	    public static Singleton getInstance(){
	        return instance;
	    }
	}
这种写法如果完美的话，就没必要在啰嗦那么多双检锁的问题了。缺点是它不是一种懒加载模式（lazy initialization），单例会在加载类后一开始就被初始化，即使客户端没有调用 getInstance()方法。饿汉式的创建方式在一些场景中将无法使用：譬如 Singleton 实例的创建是依赖参数或者配置文件的，在 getInstance() 之前必须调用某个方法设置参数给它，那样这种单例写法就无法使用了。

### 静态内部类 static nested class
我比较倾向于使用静态内部类的方法，这种方法也是《Effective Java》上所推荐的。

	public class Singleton {  
	    private static class SingletonHolder {  
	        private static final Singleton INSTANCE = new Singleton();  
	    }  
	    private Singleton (){}  
	    public static final Singleton getInstance() {  
	        return SingletonHolder.INSTANCE; 
	    }  
	}
这种写法仍然使用JVM本身机制保证了线程安全问题；由于 SingletonHolder 是私有的，除了 getInstance() 之外没有办法访问它，因此它是懒汉式的；同时读取实例的时候不会进行同步，没有性能缺陷；也不依赖 JDK 版本。

### 枚举 Enum
实现单例的核心在于private私有化类中的构造方法，在枚举中的构造方法必须是私有的，这就为枚举来实现单例奠定了基础。  
有这么几个原因可以用来说服你使用枚举单例：
1. 安全性。上面的写法貌似已经没有问题了，但是，还是存在一点点安全风险的，因为我们可以通过反射，通过设置访问权限，来执行私有的构造器，从而获得更多对象，打破单例。   
枚举写法没有这个问题，原因见番外篇。
2. 而且用枚举写单例实在太简单了！这也是它最大的优点。下面这段代码就是声明枚举实例的通常做法。

	public enum EasySingleton{
	    INSTANCE;
	}
我们可以通过EasySingleton.INSTANCE来访问实例，这比调用getInstance()方法简单多了。  
3. 创建枚举默认就是线程安全的，所以不需要担心double checked locking  
4. 枚举自己处理序列化。传统单例存在的另外一个问题是一旦你实现了序列化接口，那么它们不再保持单例了，因为readObject()方法一直返回一个新的对象就像java的构造方法一样，你可以通过使用readResolve()方法来避免此事发生，看下面的例子：

	//readResolve to prevent another instance of Singleton
	    private Object readResolve(){
	        return INSTANCE;
	    }
这样甚至还可以更复杂，如果你的单例类维持了其他对象的状态的话，因此你需要使他们成为transient的对象。但是枚举单例，JVM对序列化有保证。所以可以防止反序列化导致重新创建新的对象。

### 总结
一般情况下直接使用饿汉式就好了，如果明确要求要懒加载（lazy initialization）会倾向于使用静态内部类，如果涉及到反序列化创建对象时会试着使用枚举的方式来实现单例。

## 番外篇之枚举单例是如何防止反射创建的
一个饿汉式单例：

	public class CommonSingleton
	{
	    private static final CommonSingleton sin = new CommonSingleton();
	
	    private CommonSingleton(){}
	
	    public static CommonSingleton getInstance()
	    {
	        return sin;
	    }
	}

测试代码：

	@Test
    public void testCommon() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        CommonSingleton instance1 = CommonSingleton.getInstance();
        CommonSingleton instance2 = CommonSingleton.getInstance();


        Constructor<CommonSingleton> cons = CommonSingleton.class.getDeclaredConstructor();
        cons.setAccessible(true);
        CommonSingleton instance3 = cons.newInstance();

        System.out.println("instance1:"+instance1);
        System.out.println("instance2:"+instance2);
        System.out.println("instance3:"+instance3);
        System.out.println(instance1 == instance2);
        System.out.println(instance1 == instance3);
    }

测试结果：

	instance1:singleton.CommonSingleton@53bbfa
	instance2:singleton.CommonSingleton@53bbfa
	instance3:singleton.CommonSingleton@15a4a77
	true
	false

结果证明：完全可以通过反射，打破单例。

下面验证枚举单例。
代码：
	public enum EnumSingleton
	{
	    INSTANCE;
	
	    public void hello()
	    {
	        System.out.println("Hello,singleton");
	    }
	}
测试代码：

	@Test
	public void testEnum() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	    {
	        EnumSingleton instance1 = EnumSingleton.INSTANCE;
	        EnumSingleton instance2 = EnumSingleton.INSTANCE;
	
	
	//        Constructor<EnumSingleton> cons = EnumSingleton.class.getDeclaredConstructor();
	//        Constructor<EnumSingleton>[] conss = (Constructor<EnumSingleton>[]) EnumSingleton.class.getDeclaredConstructors();
	//        for(Constructor i : conss)
	//        {
	//            System.out.println(i);
	//        }
	        Constructor<EnumSingleton> cons = EnumSingleton.class.getDeclaredConstructor(java.lang.String.class,int.class);
	        cons.setAccessible(true);
	        EnumSingleton instance3 = cons.newInstance();
	
	        System.out.println("instance1:"+instance1);
	        System.out.println("instance2:"+instance2);
	        System.out.println("instance3:"+instance3);
	        System.out.println(instance1 == instance2);
	        System.out.println(instance1 == instance3);
	    }

测试结果：直接报错。java.lang.IllegalArgumentException: Cannot reflectively create enum objects

原因：通过查看java源代码Constructor.java发现：

	@CallerSensitive
    public T newInstance(Object ... initargs)
        throws InstantiationException, IllegalAccessException,
               IllegalArgumentException, InvocationTargetException
    {
        if (!override) {
            if (!Reflection.quickCheckMemberAccess(clazz, modifiers)) {
                Class<?> caller = Reflection.getCallerClass();
                checkAccess(caller, clazz, null, modifiers);
            }
        }
        if ((clazz.getModifiers() & Modifier.ENUM) != 0)
            throw new IllegalArgumentException("Cannot reflectively create enum objects");
        ConstructorAccessor ca = constructorAccessor;   // read volatile
        if (ca == null) {
            ca = acquireConstructorAccessor();
        }
        return (T) ca.newInstance(initargs);
    }

在通过反射创建的时候，会进行判断。禁止通过反射创建enum类型的示例。

## 番外篇之内部类
