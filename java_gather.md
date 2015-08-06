## java积累

### 1，list的toArray方法
        public <T> T[] toArray(T[] paramArrayOfT)
        {
            int i = size();
            if (paramArrayOfT.length < i)
                return Arrays.copyOf(this.a, i, paramArrayOfT.getClass());
            System.arraycopy(this.a, 0, paramArrayOfT, 0, i);
            if (paramArrayOfT.length > i)
                paramArrayOfT[i] = null;
            return paramArrayOfT;
        }
根据源码来看，  
如果toArray的入参的size比list小，会以入参数组的类型重新创建一个数组返回。  
如果相等，返回入参数组，此时其实可以不接受返回值，直接用入参。  
如果入参数组size大，大的以null填充。

### 2，数组转list
要将数组转成list，我们都知道用**Arrays.asList(arr)**;  
但是，这里有一个要注意的问题，**这个arr不能是基本类型数组**，  
比如对一个int[],进行Arrays.asList(),返回值是List<int[]>。asList()只能识别对象类型。

### 3，数组的查找
Arrays提供了binarySearch方法进行查找。  
但是，注意这只能用于对已经排好序的数组进行查找，对一个乱序数组使用binarySearch会导致不可预料的后果。

### 4，jdk源码
安装java之后，在安装路径如D:\Program Files (x86)\Java\jdk1.7.0_79下会有，src.zip。解压就是源码。

### 5, 内部类
内部类（特指非静态内部类）可以访问外部类的所有成员。这是如何做到的呢？  
其实是当某个外部类的实例对象创建一个内部类对象时，该内部类对象会秘密的捕获一个指向该外部类对象的引用，以后当要访问外部类的成员时，就通过这个引用来获取。这些都是编译器处理的，不需要程序员操心。

### 6，java循环变量的定义位置
今天看代码，关于java中，循环变量的定位位置，究竟是定义在循环内还是循环外好呢？

有以下几点争论：  
1. 定义在循环外，这样只申请一次栈空间。  
当在一段代码块定义一个变量时，Java在栈中为这个变量分配内存空间，当该变量退出其作用域后，Java会自动释放掉为该变量所分配的内存空间，该内存空间可以立即被另作他用。
但是栈空间的申请和释放是非常快的，仅次于寄存器，基本可以忽略不计。  
2. 放在循环内定义，可以使变量的作用域最小化。  
3. 放在循环外定义，可以使用取到这个值来做其他操作。
其实2和3这个根本就需要看具体的场景了。没什么对错。

另外通过javap -c 反汇编来看，在循环内和循环外的代码差别只有，在循环外定义时，只是多了  ```     8: aconst_null   
       9: astore_2   ```
这两条指令，其余任何区别。

总上来看，根本不需要纠结于这个问题。差别就是栈空间的申请和释放，不会多消耗栈空间的，因为立即释放了嘛。


### 7，java中字符串的存储
对于字符串：其对象的引用都是存储在栈中的，如果是 编译期已经创建好(直接用双引号定义的)的就存储在常量池中，如果是运行期（new出来的）才能确定的就存储在堆中 。对于equals相等的字符串，在常量池中永远只有一份，在堆中有多份。
 
new String("china");
对于通过new产生一个字符串（假设为”china”）时，会先去常量池中查找是否已经有了”china”对象，如果没有则在常量池中创建一个此字符串对象，然后堆中再创建一个常量池中此”china”对象的拷贝对象。这也就是有道面试题：String s = new String(“xyz”);产生几个对象？一个或两个，如果常量池中原来没有”xyz”,就是两个。每次new都至少在堆中创建一个。

### 8,常量池
常量池 (constant pool) 

　　常量池指的是**在编译期被确定，并被保存在已编译的.class文件中的一些数据。**除了包含代码中所定义的各种基本类型（如int、long等等）和对象型（如String及数组）的常量值(final)还包含一些以文本形式出现的符号引用，比如： 

　　类和接口的全限定名； 

　　字段的名称和描述符； 

　　方法和名称和描述符。 

　　虚拟机必须为每个被装载的类型维护一个常量池。常量池就是该类型所用到常量的一个有序集和，包括直接常量（string,integer和 floating point常量）和对其他类型，字段和方法的符号引用。 

　　对于String常量，它的值是在常量池中的。而JVM中的常量池在内存当中是以表的形式存在的， 对于String类型，有一张固定长度的CONSTANT_String_info表用来存储文字字符串值，注意：该表只存储文字字符串值，不存储符号引 用。说到这里，对常量池中的字符串值的存储位置应该有一个比较明了的理解了。在程序执行的时候,常量池 会储存在Method Area,而不是堆中。

总之，常量池就是存放类中的不变的信息的。如类名啊，方法名啊，常量啊，字符串等等。就是class文件中的常量。

### 9,关于栈中的数据可以共享的理解
栈有一个很重要的特殊性，就是存在栈中的数据可以共享。假设我们同时定义：   
　　int a = 3;   int b = 3；   
 编译器先处理int a = 3；首先它会在栈中创建一个变量为a的引用，然后查找栈中是否有3这个值，如果没找到，就将3存放进来，然后将a指向3。接着处理int b = 3；在创建完b的引用变量后，因为在栈中已经有3这个值，便将b直接指向3。这样，就出现了a与b同时均指向3的情况。 

　　这时，如果再令 a=4；那么编译器会重新搜索栈中是否有4值，如果没有，则将4存放进来，并令a指向4；如果已经有了，则直接将a指向这个地址。因此a值的改变不会影响 到b的值。 

　　要注意这种数据的共享与两个对象的引用同时指向一个对象的这种共享是不同的，因为这种情况a的修改并不会影响到b, 它是由编译器完成的，它有利于节省空间。而一个对象引用变量修改了这个对象的内部状态，会影响到另一个对象引用变量。 

### 10，关于final
final只对引用的"值"(即内存地址)有效，它迫使引用只能指向初始指向的那个对象，改变它的指向会导致编译期错误。至于它所指向的对象 的变化，final是不负责的。 
通俗的将，就是指向了这个地址的对象，地址不能变，地址上的对象可以变。

### 11，关于string + 的编译器优化
下面是几个常见例子的比较分析和理解： 

	　　String a = "a1"; 
	　　String b = "a" + 1; 
	　　System.out.println((a == b)); //result = true 
	　　String a = "atrue"; 
	　　String b = "a" + "true"; 
	　　System.out.println((a == b)); //result = true 
	　　String a = "a3.4"; 
	　　String b = "a" + 3.4; 
	　　System.out.println((a == b)); //result = true 

　　分析：JVM对于字符串常量的"+"号连接，将程序编译期，JVM就将常量字符串的"+"连接优化为连接后的值，拿"a" + 1来说，经编译器优化后在class中就已经是a1。在编译期其字符串常量的值就确定下来，故上面程序最终的结果都为true。 

	　　String a = "ab"; 
	　　String bb = "b"; 
	　　String b = "a" + bb; 
	　　System.out.println((a == b)); //result = false 

　　分析：JVM对于字符串引用，由于在字符串的"+"连接中，有字符串引用存在，而引用的值在程序编译期是无法确定的，即"a" + bb无法被编译器优化，只有在程序运行期来动态分配并将连接后的新地址赋给b。所以上面程序的结果也就为false。 

	　　String a = "ab"; 
	　　final String bb = "b"; 
	　　String b = "a" + bb; 
	　　System.out.println((a == b)); //result = true 

　　分析：和[3]中唯一不同的是bb字符串加了final修饰，对于final修饰的变量，它在编译时被解析为常量值的一个本地拷贝存储到自己的常量池中或嵌入到它的字节码流中。所以此时的"a" + bb和"a" + "b"效果是一样的。故上面程序的结果为true。 
	
	　　String a = "ab"; 
	　　final String bb = getBB(); 
	　　String b = "a" + bb; 
	　　System.out.println((a == b)); 
	　　   //result = false 
	　　private static String getBB() { 
	　　     return "b"; 
	　　} 

　　分析：JVM对于字符串引用bb，它的值在编译期无法确定，只有在程序运行期调用方法后，将方法的返回值和"a"来动态连接并分配地址为b，故上面 程序的结果为false。 

　　通过上面4个例子可以得出得知： 
　　```String s = "a" + "b" + "c";```  就等价于```String s = "abc";```

	　　String a = "a"; 
	　　String b = "b"; 
	　　String c = "c"; 
	　　String s = a + b + c; 
　　这个就不一样了，最终结果等于： 

	　　StringBuffer temp = new StringBuffer(); 
	　　temp.append(a).append(b).append(c); 
	　　String s = temp.toString(); 

### 12,String的intern（）
存在于.class文件中的常量池，在运行期被JVM装载，并且可以扩充。String的 intern()方法就是扩充常量池的 一个方法；当一个String实例str调用intern()方法时，Java 查找常量池中 是否有相同Unicode的字符串常量，如果有，则返回其的引用，如果没有，则在常 量池中增加一个Unicode等于str的字符串并返回它的引用；
