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
