#### buffer
使用 Buffer 读写数据一般遵循以下四个步骤：

- 写入数据到 Buffer；
- 调用 flip() 方法；
- 从 Buffer 中读取数据；
- 调用 clear() 方法或者 compact() 方法。

当向 Buffer 写入数据时，Buffer 会记录下写了多少数据。一旦要读取数据，需要通过 flip() 方法将 Buffer 从写模式切换到读模式。在读模式下，可以读取之前写入到 Buffer 的所有数据。

一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。有两种方式能清空缓冲区：调用 clear() 或 compact() 方法。clear() 方法会清空整个缓冲区。compact() 方法只会清除已经读过的数据。任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。


#### channel
Channel是一个对象，可以通过它读取和写入数据。可以把它看做IO中的流。但是它和流相比还有一些不同：

- Channel是双向的，既可以读又可以写，而流是单向的
- Channel可以进行异步的读写
- 对Channel的读写必须通过buffer对象

在Java NIO中Channel主要有如下几种类型：

- FileChannel：从文件读取数据的，没有异步模式
- DatagramChannel：读写UDP网络协议数据
- SocketChannel：读写TCP网络协议数据
- ServerSocketChannel：可以监听TCP连接

