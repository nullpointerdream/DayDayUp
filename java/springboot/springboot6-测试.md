>  个人积累，请勿私自转载，转载前请联系  
>  代码及文章资源[https://github.com/jedyang/DayDayUp/tree/master/java/springboot](https://github.com/jedyang/DayDayUp/tree/master/java/springboot)  
>  基于SpringBootCookBook的读书笔记，重在个人理解和实践，而非翻译。

## 测试
关于单元测试的全面解析请看我的这篇文章，这里只讲springboot相关的。    
[单元测试](http://www.jianshu.com/p/43971f396fb6)

测试这块springboot不同的版本变化较大。这里用的是1.5.*  

在我们创建的工程里，已经存在需要的目录结构BookPubApplicationTests。  

### 测试rest请求

	package org.test.bookpub;
	
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.boot.test.web.client.TestRestTemplate;
	import org.springframework.test.context.junit4.SpringRunner;
	import org.test.bookpub.entity.Book;
	
	import static junit.framework.TestCase.assertNotNull;
	import static org.junit.Assert.assertEquals;
	
	@RunWith(SpringRunner.class)// SpringJUnit支持，由此引入Spring-Test框架支持
	@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
	public class BookPubApplicationTests {
	    @Autowired
	    private TestRestTemplate restTemplate;
	
	    @Test
	    public void webappBookIsbnApi() {
	//        Book book = restTemplate.getForObject("/books/1001", Book.class);
	        Book book = restTemplate.getForObject("http://localhost:8080/books/1001", Book.class);
	        System.out.println(book);
	        assertNotNull(book);
	        assertEquals("yunsheng", book.getPublisher().getName());
	    }
	
	}


1. `webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT`是使用配置的端口。  
还有一个是`webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT`随机端口。  
因为后面`restTemplate.getForObject("http://localhost:8080/books/1001", Book.class);`写死了url，所以这边设置哪个都可以。  
2. `Book book = restTemplate.getForObject("/books/1001", Book.class);`这种使用代码默认的协议和域名。因为我们前面默认配置的是https。这里直接用会有问题。  

### 测试数据库连接
增加一个测试方法

    @Autowired
    private BookRepository bookRepository;

    // 测试数据库可正常连接
    @Test
    public void contextLoads() {
        assertEquals(1, bookRepository.count());
    }

### 服务端测试
TestRestTemplate是模拟从客户端发起请求来测试。还有一种方法是使用`MockMvc`对象在服务端直接测试controller层的方法。就想启动了服务器一样。  

 	@Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void webappBookApi() throws Exception {
        //MockHttpServletRequestBuilder.accept方法是设置客户端可识别的内容类型
        //MockHttpServletRequestBuilder.contentType,设置请求头中的Content-Type字段,表示请求体的内容类型
        mockMvc.perform(get("http://localhost:8080/books/1001")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("yunsheng")))
                .andExpect(jsonPath("$.isbn").value("1001"));
    }


### 数据库插入数据
之前我们把往数据库插入一条记录的代码放在了`StartupRunner`里，其实这不是很合适。现在用单元测试来代替。  
有几种方式来做，  
1. 使用hibernate这类映射工具，它会扫描@Entity注解的类，自动创建对应的表。然后使用import.sql创建数据。  
2. 使用spring jdbc，通过schema.sql文件定义表结构，使用data.sql创建数据。  

#### 做法
使用hibernate方式

1. 注释掉之前在StartupRunner中的代码  

		 @Override
	    public void run(String... strings) throws Exception {
	        logger.info("dataSource:" + ds.toString());
	        // 增加一条记录
		//        Author author = new Author("yunsheng", "yang");
		//        author = authorRepository.save(author);
		//        Publisher publisher = new Publisher("yunsheng");
		//        publisher = publisherRepository.save(publisher);
		//        Book book = new Book("1001", "test1", author, publisher);
		//        Book book1 = bookRepository.save(book);
	        logger.info("number of books:" + bookRepository.count());
	    }
2. 在application.properties中配置

		spring.jpa.hibernate.ddl-auto=create-drop

3. 在resources下创建import.sql文件

		INSERT INTO author (id, first_name, last_name) VALUES (1, 'yunsheng', 'yang');
		INSERT INTO publisher (id, name) VALUES (1, 'yunsheng');
		INSERT INTO book (isbn, title, author_id, publisher_id) VALUES ('1001', 'Spring Boot Recipes', 1,1);

4. 运行测试用例，通过。

使用jdbc方式

1. 在application.properties中配置
	
	spring.jpa.hibernate.ddl-auto=none
即使Hibernate依赖存在也不要做任何事情
2. 创建schema.sql用来创建表
	
		-- Create syntax for TABLE 'author'
		DROP TABLE IF EXISTS `author`;
		CREATE TABLE `author` (
		  `id`         BIGINT(20) NOT NULL AUTO_INCREMENT,
		  `first_name` VARCHAR(255)        DEFAULT NULL,
		  `last_name`  VARCHAR(255)        DEFAULT NULL,
		  PRIMARY KEY (`id`)
		);
		-- CREATE syntax FOR TABLE 'publisher'
		DROP TABLE IF EXISTS `publisher`;
		CREATE TABLE `publisher` (
		  `id`   BIGINT(20) NOT NULL AUTO_INCREMENT,
		  `name` VARCHAR(255)        DEFAULT NULL,
		  PRIMARY KEY (`id`)
		);
		-- CREATE syntax FOR TABLE 'reviewer'
		DROP TABLE IF EXISTS `reviewer`;
		CREATE TABLE `reviewer` (
		  `id`         BIGINT(20) NOT NULL AUTO_INCREMENT,
		  `first_name` VARCHAR(255)        DEFAULT NULL,
		  `last_name`  VARCHAR(255)        DEFAULT NULL,
		  PRIMARY KEY (`id`)
		);
		-- CREATE syntax FOR TABLE 'book'
		DROP TABLE IF EXISTS `book`;
		CREATE TABLE `book` (
		  `id`           BIGINT(20) NOT NULL AUTO_INCREMENT,
		  `description`  VARCHAR(255)        DEFAULT NULL,
		  `isbn`         VARCHAR(255)        DEFAULT NULL,
		  `title`        VARCHAR(255)        DEFAULT NULL,
		  `author_id`    BIGINT(20)          DEFAULT NULL,
		  `publisher_id` BIGINT(20)          DEFAULT NULL,
		  PRIMARY KEY (`id`),
		  CONSTRAINT `FK_publisher` FOREIGN KEY (`publisher_id`) REFERENCES
		    `publisher` (`id`),
		  CONSTRAINT `FK_author` FOREIGN KEY (`author_id`) REFERENCES `author`
		  (`id`)
		);
		-- CREATE syntax FOR TABLE 'book_reviewers'
		DROP TABLE IF EXISTS `book_reviewers`;
		CREATE TABLE `book_reviewers` (
		  `book_id`      BIGINT(20) NOT NULL,
		  `reviewers_id` BIGINT(20) NOT NULL,
		  CONSTRAINT `FK_book` FOREIGN KEY (`book_id`) REFERENCES `book`
		  (`id`),
		  CONSTRAINT `FK_reviewer` FOREIGN KEY (`reviewers_id`) REFERENCES
		    `reviewer` (`id`)
		);

3. 创建data.sql用来插入数据  
和import.sql一样复制一份即可
4. 运行测试用例，通过

默认情况下，Hibernate会使用import.sql。jdbc会使用schema.sql和data.sql。  
如果你希望使用别的名字代替schema.sql或者data.sql，Spring Boot也提供了对应的配置属性，即spring.datasource.schema和spring.datasource.data。

tips，此处我之前有个错误，把reviewer写成了publisher的内部类，这里创建表会失败，已修改。  

### mock数据库操作
在实际工作中，我们写单测更常见的做法是mock掉数据库操作。这里演示使用mockito。  
	
	package org.test.bookpub;
	
	import org.junit.After;
	import org.junit.Before;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.mockito.Mockito;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.boot.test.mock.mockito.MockBean;
	import org.springframework.test.context.junit4.SpringRunner;
	import org.test.bookpub.repository.PublisherRepository;
	
	import static org.junit.Assert.assertEquals;
	
	@RunWith(SpringRunner.class)
	@SpringBootTest
	public class PublisherRepositoryTests {
	    @MockBean
	    private PublisherRepository repository;
	
	    @Before
	    public void setupPublisherRepositoryMock() {
	        Mockito.when(repository.count()).thenReturn(100L);
	    }
	
	    @Test
	    public void publishersExist() {
	        assertEquals(100, repository.count());
	    }
	
	    @After
	    public void resetPublisherRepositoryMock() {
	        Mockito.reset(repository);
	    }
	}

新建一个类演示，mockito更多用法，可以google之。  

这里实在没搞懂一个注解的事，cookbook搞了一堆代码在干嘛

其实写到这里我已经后悔了。cookbook太老了。  
[https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)  
还是直接看官方文档吧，有你想要的全部。

