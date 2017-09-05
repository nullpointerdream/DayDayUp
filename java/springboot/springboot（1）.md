>  个人积累，请勿私自转载，转载前请联系  
>  代码及文章资源[https://github.com/jedyang/DayDayUp/tree/master/java/springboot](https://github.com/jedyang/DayDayUp/tree/master/java/springboot)  
>  基于SpringBootCookBook的读书笔记，重在个人理解和实践，而非翻译。

### 入门

在现代快节奏的软件开发中，快速开发和原型设计变得越来越重要。如果你是使用JVM平台语言（不只是java哦），那springboot是加快开发速度的利器。  下面将如何将工程boot化。

#### 使用springboot的template和starter
springboot包含了40多个不同的starter模块，为各种功能提供了即时可用的library。如数据库连接，监控，web服务等等。  不会一个一个介绍，看几个最重要的。  

去https://start.spring.io/生成一个最简单的springboot应用。  
在这个页面上可以选择需要的dependency。在生成的工程中会有对应的starter。比如，我选了`mysql`,`mybatis`。生成的pom中有：

```	

	<dependency>

	<groupId>org.mybatis.spring.boot</groupId>

	<artifactId>mybatis-spring-boot-starter</artifactId>

	<version>1.3.0</version>

	</dependency>

	<dependency>

	<groupId>mysql</groupId>

	<artifactId>mysql-connector-java</artifactId>

	<scope>runtime</scope>

	</dependency>

```

那么，springboot的starter究竟是什么？  
springboot的目标是简化系统的构建。starter就是启动一个特定工程需要的一系列依赖的集合。  
每个starter都有一个特殊的文件spring.provides，如starter-test，https://github.com/spring-projects/spring-boot/blob/master/spring-boot-starters/spring-boot-starter-test/src/main/resources/META-INF/spring.provides
可以看到它集成了provides: spring-test,spring-boot,junit,mockito,hamcrest-library,assertj,jsonassert,json-path。   
这样我们不在需要手动添加这些依赖。  

常用的starter如下：

- spring-boot-starter: 这是核心Spring Boot starter，提供了大部分基础功能，其他starter都依赖于它，因此没有必要显式定义它。
- spring-boot-starter-actuator：主要提供监控、管理和审查应用程序的功能。
- spring-boot-starter-jdbc：该starter提供对JDBC操作的支持，包括连接数据库、操作数据库，以及管理数据库连接等等。
- spring-boot-starter-data-jpa：JPA starter提供使用Java Persistence API(例如Hibernate等)的依赖库。
- spring-boot-starter-data-*：提供对MongoDB、Data-Rest或者Solr的支持。
- spring-boot-starter-security：提供所有Spring-security的依赖库。
- spring-boot-starter-test：这个starter包括了spring-test依赖以及其他测试框架，例如JUnit和Mockito等等。
- spring-boot-starter-web：该starter包括web应用程序的依赖库。

#### 动手建一个demo工程
一个简单的图书管理系统。在spring的创建页面上。  

- Group设置为：org.test；
- Artifact设置为：bookpub；
- Name设置为：BookPub；
- Package Name设置为：org.test.bookpub；
- Packaging代表打包方式，我们选jar；
- Spring Boot Version选择最新的1.3.0；
- 创建Maven工程，当然，对Gradle比较熟悉的同学可以选择Gradle工程。
- 依赖添加H2，JDBC,JPA
- 点击“Generate Project”下载工程包。

打开pom文件，可以看到对应的starter。  
然后看一下主代码。

```

	@SpringBootApplication
	public class BookPubApplication {
	
		public static void main(String[] args) {
			SpringApplication.run(BookPubApplication.class, args);
		}
	}

```

就是这么少，没有配置文件，也没有数据库配置。但是可以运行，实现这个魔法的是注解`@SpringBootApplication`。  

	@Target({ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Inherited
	@SpringBootConfiguration
	@EnableAutoConfiguration
	@ComponentScan(
	    excludeFilters = {@Filter(
	    type = FilterType.CUSTOM,
	    classes = {TypeExcludeFilter.class}
	), @Filter(
	    type = FilterType.CUSTOM,
	    classes = {AutoConfigurationExcludeFilter.class}
	)}
	)
	public @interface SpringBootApplication {...}


注意，@SpringBootConfiguration是被@Configuration注解，@Configuration表示被注解的类包含spring配置定义。  

@ComponentScan，包扫描。默认以该类所在的包为根路径扫描。  
@EnableAutoConfiguration，这个是spring-boot新引入的注解作用是自动配置。  

在main方法中`SpringApplication.run(BookPubApplication.class, args);`读取BookPubApplication的注解，并初始化context。  

启动看下，使用`mvn spring-boot:run`在对应目录启动，观察启动成功。

顺便安利下H2数据库，是一个内存数据库，也支持持久化，个人感觉很好用。请搜索相关文档。  

#### 使用Command-line runners  
让我们加点代码.  
新增一个类。

	
	package org.test.bookpub;

	import org.apache.commons.logging.Log;
	import org.apache.commons.logging.LogFactory;
	import org.springframework.boot.CommandLineRunner;
	
	/**
	 * Created by yangyunsheng on 2017/7/12.
	 */
	
	public class StartupRunner implements CommandLineRunner{
	
	    Log logger = LogFactory.getLog(getClass());
	
	    @Override
	    public void run(String... strings) throws Exception {
	        logger.info("hello");
	    }
	}

在BookPubApplication中增加bean注入：
	
	@Bean
    public StartupRunner schedulerRunner() {
        return new StartupRunner();
    }

可以看到成功的输出：

	2017-07-12 19:10:08.141  INFO 11032 --- [           main] org.test.bookpub.StartupRunner           : hello  


CommandLineRunner适用于那种在程序启动时只执行一次的任务，如各种资源的加载。    

springboot会扫描实现CommandLineRunner的类，执行其中的run方法。  
run方法的参数是启动函数（如main函数）传入的。  

另外，可以使用@Order注解或者实现Order接口，来控制多个Runner的执行顺序。通过设置value的值，值越小，执行越早。  

注意：因为CommandLineRunner是执行在启动阶段，一旦报错将阻断整个应用，所以一定记得用try-catch处理异常。  

#### 建立数据库连接
修改一下代码：

	@Order(value = 1)
	public class StartupRunner implements CommandLineRunner{
	
	    protected  final Log logger = LogFactory.getLog(getClass());
	
	    @Autowired
	    private DataSource ds;
	
	    @Override
	    public void run(String... strings) throws Exception {
	        logger.info("dataSource:" + ds.toString());
	    }
	}

执行，看日志：  

	2017-07-13 09:32:49.464  INFO 8132 --- [           main] org.test.bookpub.StartupRunner           : dataSource:org.apache.tomcat.jdbc.pool.DataSource@f107c50{ConnectionPool[defaultAutoCommit=null; defaultReadOnly=null; defaultTransactionIsolation=-1; defaultCatalog=null; driverClassName=org.h2.Driver; .... }

可以看到，因为H2是一种内存数据库，我们仅需要引入H2的依赖，执行时如果没有H2实例，程序会自动创建一个H2数据库。  但是这样每次结束应用，数据都会丢失。幸运的是，H2支持数据持久化，我们可以将数据保存到文件里。继续修改代码：  
在resources目录下的application.properties中增加配置： 
	
	spring.datasource.url = jdbc:h2:~/test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
	spring.datasource.username = sa
	spring.datasource.password =

再次执行，会在你的用户目录下生产test.mv.db数据库文件。  

连接mysql的代码：

	spring.datasource.driver-class-name: com.mysql.jdbc.Driver
	spring.datasource.url: jdbc:mysql://localhost:3306/springbootcookbook
	spring.datasource.username: root
	spring.datasource.password:

如果使用hibernate自动创建schemal，再加一个：  

	spring.jpa.hibernate.ddl-auto=create-drop 

这里多说一句：  ddl-auto的参数有：validate | update | create | create-drop  

其实这个ddl-auto参数的作用主要用于：自动创建|更新|验证数据库表结构。如果不是此方面的需求建议set value="none"。  

- create：每次加载hibernate时都会删除上一次的生成的表，然后根据你的model类再重新来生成新表，哪怕两次没有任何改变也要这样执行，这就是导致数据库表数据丢失的一个重要原因。  
- create-drop ：每次加载hibernate时根据model类生成表，但是sessionFactory一关闭,表就自动删除。生产环境不要用。  
- update：最常用的属性，第一次加载hibernate时根据model类会自动建立起表的结构（前提是先建立好数据库），以后加载hibernate时根据 model类自动更新表结构，即使表结构改变了但表中的行仍然存在不会删除以前的行。要注意的是当部署到服务器后，表结构是不会被马上建立起来的，是要等 应用第一次运行起来后才会。
- validate ：每次加载hibernate时，验证创建数据库表结构，只会和数据库中的表进行比较，不会创建新表，但是会插入新值。

再说点“废话”：
当我们把hibernate.hbm2ddl.auto=create时hibernate先用hbm2ddl来生成数据库schema。
当我们把hibernate.cfg.xml文件中hbm2ddl属性注释掉，这样我们就取消了在启动时用hbm2ddl来生成数据库schema。通常 只有在不断重复进行单元测试的时候才需要打开它，但再次运行hbm2ddl会把你保存的一切都删除掉（drop）---- create配置的含义是：“在创建SessionFactory的时候，从scema中drop掉所以的表，再重新创建它们”。
注意，很多Hibernate新手在这一步会失败，我们不时看到关于Table not found错误信息的提问。但是，只要你根据上面描述的步骤来执行，就不会有这个问题，因为hbm2ddl会在第一次运行的时候创建数据库schema， 后续的应用程序重启后还能继续使用这个schema。假若你修改了映射，或者修改了数据库schema,你必须把hbm2ddl重新打开一次。

#### 数据操作服务  
当前操作数据一般都是使用ORM框架，这里以hibernate为例。  
代码较多，先建立几个实体对象（省略getset）：
	
	@Entity
	public class Book {
	    @Id
	    @GeneratedValue
	    private Long id;
	    private String isbn;
	    private String title;
	    private String description;
	    @ManyToOne
	    private Author author;
	    @ManyToOne
	    private Publisher publisher;
	    @ManyToMany
	    private List<Publisher.Reviewer> reviewers;
	
	    protected Book() {
	    }
	
	    public Book(String isbn, String title, Author author, Publisher
	            publisher) {
	        this.isbn = isbn;
	        this.title = title;
	        this.author = author;
	        this.publisher = publisher;
	    }


	@Entity
	public class Author {
	    @Id
	    @GeneratedValue
	    private Long id;
	    private String firstName;
	    private String lastName;
	    @OneToMany(mappedBy = "author")
	    private List<Book> books;
	
	    protected Author() {
	    }
	
	    public Author(String firstName, String lastName) {
	        this.firstName = firstName;
	        this.lastName = lastName;
	    }

	@Entity
	public class Publisher {
	    @Id
	    @GeneratedValue
	    private Long id;
	    private String name;
	    @OneToMany(mappedBy = "publisher")
	    private List<Book> books;
	
	    protected Publisher() {
	    }
	
	    public Publisher(String name) {
	        this.name = name;
	    }
	
	
	    @Entity
	    public class Reviewer {
	        @Id
	        @GeneratedValue
	        private Long id;
	        private String firstName;
	        private String lastName;
	
	        protected Reviewer() {
	        }
	
	        public Reviewer(String firstName, String lastName) {
	            this.firstName = firstName;
	            this.lastName = lastName;
	        }

然后创建一个BookRepository接口：

	@Repository
	public interface BookRepository extends CrudRepository<Book, Long> {
	    public Book findByIsbn(String isbn);
	}

修改一下starter：

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... strings) throws Exception {
        logger.info("number of books:" + bookRepository.count());
    }

可以看到，我们并没有写任何sql。只是用了很多注解，做了对象和表结构的映射。然后继承CrudRepository。  

- @Entity：注解类，映射成表。注意类要有一个protect的默认构造器。  
- @Repository 注解的接口，表示提供对数据库的操作服务。同时spring会为这个接口创建对应的bean，以备注入使用。  
- CrudRepository提供了基本的CRUD操作。其他的操做，如findByIsbn。需要根据命名习惯，spring会自动解析，如s findByNameIgnoringCase。  
- @Id @GeneratedValue 自增主键
- @ManyToOne @ManyToMany @OneToMany表示两个实体的对应关系。如book和author是多对一的关系。  

#### 调度执行器
实现每10秒执行一次。  
在BookPubApplication加上@EnableScheduling注解。  
在starter加一个方法

    @Scheduled(initialDelay = 1000, fixedRate = 10000)
    public void run(){
        logger.info("number of books:" + bookRepository.count());
    }

原理可以看下，@EnableScheduling注解，它引入SchedulingConfiguration类。这个类会创建一个ScheduledAnnotationBeanPostProcessor。它会扫描被@Scheduled注解的无参方法。注意方法一定要是无参的。  

 
