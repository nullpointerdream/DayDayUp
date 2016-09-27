在Java中，有多种形式可以表示一个资源：

可表示资源的对象	：
### java.io.​File	
可代表文件系统中的文件或目录。例如：
文件系统中的文件：`c:\config.sys`。
文件系统中的目录：`c:\windows\`。
###java.net.​URL	
统一资源定位符。例如：
文件系统中的文件：c:\config.sys，可以表示成URL：`file:///c:/config.sys`。
文件系统中的目录：c:\windows\，可以表示成URL：`file:///c:/windows/`。
远程WEB服务器上的文件：`http://www.springframework.org/schema/beans.xml`。
Jar包中的某个文件，可以表示成URL：`jar:file:///c:/my.jar!/my/file.txt`。
###java.io.​InputStream	
输入流对象,可用来直接访问资源的内容。
例如：
文件系统中的文件：c:\config.sys，可以用下面的代码来转换成输入流：
`new FileInputStream("c:\\config.sys");`
远程WEB服务器上的文件，可以用下面的代码来转换成输入流：
`new URL("http://www.springframework.org/schema/beans.xml")​.openStream();`
Jar包中的某个文件，可以用下面的代码来转换成输入流：
`new URL("jar:file:///c:/my.jar!/my/file.txt")​.openStream();`

然而，并不是所有的资源，都可以表现成上述所有的形式。比如，

- Windows文件系统中的目录，无法表现为输入流。

- 而远程WEB服务器上的文件无法转换成File对象。

- 多数资源都可以表现成URL形式。但也有例外，例如，如果把数据库中的数据看作资源，那么一般来说这种资源无法表示成URL。