## windows定位java程序cpu占用过高

1. 任务管理器中找到cpu占用过高的程序A

2. 运用微软的Process Explorer查看占用cpu高的进程的具体信息。  
在A上右键properties，得到程序的进程ID，打开Threads标签，看到线程占用cpu的情况。

3. 用jdk的jstack工具打印threaddump信息。  
cmd下执行，jstack.exe -l A的进程ID > 目标文件XX.txt

4. 到XX.txt根据线程ID搜索具体的代码行。  
注意，在Threads标签中看到的tid要转换成16进制的在txt中搜

5. 最后反编译class文件，看提示的代码是否有问题。
