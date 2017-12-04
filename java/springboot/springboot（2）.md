>  个人积累，请勿私自转载，转载前请联系  
>  代码及文章资源[https://github.com/jedyang/DayDayUp/tree/master/java/springboot](https://github.com/jedyang/DayDayUp/tree/master/java/springboot)  
>  基于SpringBootCookBook的读书笔记，重在个人理解和实践，而非翻译。

## WEB工程
### 创建一个基本的RESTful应用
目前的软件一般都是围绕web服务，或者数据服务等来构建的，下面我们继续前面的代码，构建一个基本的Restful应用。  

1. 首先增加web依赖。  

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
2. 创建一个spring controller 负责处理http request。先创建一个controllers包以存放代码。  
3. 增加一个controller:BookController。  

		@RestController
		@RequestMapping("/books")
		public class BookController {
		    @Autowired
		    private BookRepository bookRepository;
		
		    @RequestMapping(value = "", method = RequestMethod.GET)
		    public Iterable<Book> getAllBooks(){
		        return bookRepository.findAll();
		    }
		
		    @RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
		    public Book getBook(@PathVariable String isbn){
		        return bookRepository.findByIsbn(isbn);
		    }
		}


4. mvn启动工程.mvn spring-boot:run
5. 浏览器http://localhost:8080/books。可以看到结果[]

#### 原理
关注`@RestController`注解。  
这个注解组合了`@Controller`和`@ResponseBody`。所以用这两个注解替换页完全没有问题，只是更方便了。  
剩下的就是spring mvc的东西了。  


### 创建一个spring data rest应用
在上面这个应用中，我们提供一个restful服务，用来获取数据。这种服务非常简单，应用也非常广泛。但是如果要提供很多数据服务接口，也是一项繁重无聊的工作。为了减少模版式工作，spring提供了一个`spring-boot-starter-data-rest`,使我们可以简单的通过在repository接口上添加注解达到暴露数据服务的目的。

1. 添加依赖

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-rest</artifactId>
		</dependency>
2. 新建一个repository接口  
新建一个接口`AuthorRepository`，并使用`@RepositoryRestResource`注解

		/**
		 * 使用注解直接暴露rest服务
		 * @author yunsheng
		 */
		@RepositoryRestResource
		public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {
		}
同样的方式，也给publisher和reviewer提供服务

		@RepositoryRestResource
		public interface PublisherRepository extends
		        PagingAndSortingRepository<Publisher, Long> {
		}


		@RepositoryRestResource
		public interface ReviewerRepository extends
		        PagingAndSortingRepository<Publisher.Reviewer, Long> {
		}
3. Run  
访问http://localhost:8080/authors会看到响应

		{
		  "_embedded" : {
		    "authors" : [ ]
		  },
		  "_links" : {
		    "self" : {
		      "href" : "http://localhost:8080/authors{?page,size,sort}",
		      "templated" : true
		    },
		    "profile" : {
		      "href" : "http://localhost:8080/profile/authors"
		    }
		  },
		  "page" : {
		    "size" : 20,
		    "totalElements" : 0,
		    "totalPages" : 0,
		    "number" : 0
		  }
		}


#### 原理
1. 我们看到响应比之前的BookRepository多了很多额外信息，因为我们的接口是继承自`PagingAndSortingRepository`。`PagingAndSortingRepository`又是继承自`CrudRepository`，多了分页和排序功能。可以改成`CrudRepository`看一下效果。  
2. `RepositoryRestResource`也提供了一些参数。比如可以修改访问的url。  

		@RepositoryRestResource(collectionResourceRel = "writers", path = "writers")
3. 我们依赖了`spring-boot-starter-data-rest`。这同时会引入`spring-hateoas`库，这会给我们提供ALPS支持。（没有太深入研究，是一种数据格式，用来描述应用级别的api接口）
比如可以这样使用： 

		$ curl -l localhost:8080
		  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
		                                 Dload  Upload   Total   Spent    Left  Speed
		100   537    0   537    0     0   1721      0 --:--:-- --:--:-- --:--:--  1721{
		  "_links" : {
		    "writers" : {
		      "href" : "http://localhost:8080/writers{?page,size,sort}",
		      "templated" : true
		    },
		    "books" : {
		      "href" : "http://localhost:8080/books"
		    },
		    "publishers" : {
		      "href" : "http://localhost:8080/publishers{?page,size,sort}",
		      "templated" : true
		    },
		    "reviewers" : {
		      "href" : "http://localhost:8080/reviewers{?page,size,sort}",
		      "templated" : true
		    },
		    "profile" : {
		      "href" : "http://localhost:8080/profile"
		    }
		  }
		}
	查看所有服务。 

		$ curl -l localhost:8080/writers
		  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
		                                 Dload  Upload   Total   Spent    Left  Speed
		100   374    0   374    0     0   3431      0 --:--:-- --:--:-- --:--:--  3978{
		  "_embedded" : {
		    "writers" : [ ]
		  },
		  "_links" : {
		    "self" : {
		      "href" : "http://localhost:8080/writers{?page,size,sort}",
		      "templated" : true
		    },
		    "profile" : {
		      "href" : "http://localhost:8080/profile/writers"
		    }
		  },
		  "page" : {
		    "size" : 20,
		    "totalElements" : 0,
		    "totalPages" : 0,
		    "number" : 0
		  }
		}
	查看writers服务。

### 定制servlet filter
仔细看springboot的启动日志
	
	2017-09-05 13:16:56.528  INFO 14936 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
	2017-09-05 13:16:56.529  INFO 14936 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/8.5.15
	2017-09-05 13:16:56.951  INFO 14936 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
	2017-09-05 13:16:56.951  INFO 14936 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 2713 ms
	2017-09-05 13:16:57.273  INFO 14936 --- [ost-startStop-1] o.s.b.w.servlet.ServletRegistrationBean  : Mapping servlet: 'dispatcherServlet' to [/]
	2017-09-05 13:16:57.276  INFO 14936 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'characterEncodingFilter' to: [/*]
	2017-09-05 13:16:57.277  INFO 14936 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
	2017-09-05 13:16:57.277  INFO 14936 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'httpPutFormContentFilter' to: [/*]
	2017-09-05 13:16:57.277  INFO 14936 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'requestContextFilter' to: [/*]
可以看出，springboot内置使用tomcat做web容器，内置了servlet。  
默认也加载了几个filter。  
同时，我们也可以自定义自己的filter。  
目前，spring已经内置了大量的filter，首先我们看一下如何使用他们。比如我们希望得到的请求的用户真是url，而不是代理的url，可以使用这个filter。`RemoteIpFilter`。  

1. 建一个配置类  
在根路径下新建一个配置类。 

		@Configuration
		public class WebConfiguration {
		    @Bean
		    public RemoteIpFilter remoteIpFilter() {
		        return new RemoteIpFilter();
		    }
		} 

2. run  
可以看到RemoteIpFilter已经加载进来。  

		2017-09-05 13:41:56.814  INFO 13792 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'remoteIpFilter' to: [/*]

#### 原理
在启动类`BookPubApplication`的`@SpringBootApplication`注解中，有包扫描注解。  
所以，在启动时，spring扫描到`@Configuration`注解的类，将其中的bean注入到上下文中。  
然后对其中实现了Filter接口的类，加载到filter chain中。  

所以，我们可以自己实现Filter，然后按照上面的方式，加载到springboot的启动过程中。  


### 配置拦截器
filter过滤器是servlet中的概念，其实和spring没什么关系（除了加到filter chain中）。spring mvc提供了另一种方式处理请求`HandlerInterceptor`。`HandlerInterceptor`允许我们在不同节点处理request，如请求被处理前，请求被处理后，view被渲染前，请求完全处理结束等。拦截器不会改变request本身，是根据逻辑中止请求的执行，可以抛出异常或返回false。  

spring mvc已经预置打很多拦截器，常用的有LocaleChangeInterceptor 和
ThemeChangeInterceptor。已LocaleChangeInterceptor（用于修改国际化参数）为例演示。  

添加拦截器并不像前面添加过滤器一样，通过bean注解就可以了。稍微有点复杂。  

1. 继承WebMvcConfigurerAdapter  
在前面`WebConfiguration`基础上修改。增加继承WebMvcConfigurerAdapter。

2. 通过添加@bean声明一个拦截器。  
3. 只是声明还不行，需要覆写手动添加一个拦截器。  
代码最终如下：  

		@Configuration
		public class WebConfiguration extends WebMvcConfigurerAdapter {
		    @Bean
		    public RemoteIpFilter remoteIpFilter() {
		        return new RemoteIpFilter();
		    }
		
		    @Bean
		    public LocaleChangeInterceptor localeChangeInterceptor() {
		        return new LocaleChangeInterceptor();
		    }
		
		    @Override
		    public void addInterceptors(InterceptorRegistry registry) {
		        registry.addInterceptor(localeChangeInterceptor());
		    }
		}
4. run
浏览器访问http://localhost:8080/books?locale=foo  
（为什么用参数locale？因为默认的修改本地化的参数名就是这个，可以看源码）    
会看到报错：

		java.lang.UnsupportedOperationException: Cannot change HTTP accept header - use a different locale resolution strategy
		at org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver.setLocale(AcceptHeaderLocaleResolver.java:127) ~[spring-webmvc-4.3.9.RELEASE.jar:4.3.9.RELEASE]
		at org.springframework.web.servlet.i18n.LocaleChangeInterceptor.preHandle(LocaleChangeInterceptor.java:148) ~[spring-webmvc-4.3.9.RELEASE.jar:4.3.9.RELEASE]
		at org.springframework.web.servlet.HandlerExecutionChain.applyPreHandle(HandlerExecutionChain.java:133) ~[spring-webmvc-4.3.9.RELEASE.jar:4.3.9.RELEASE]
可以看到异常时来自LocaleChangeInterceptor，证明我们的拦截器已经工作。这个报错的原因是头信息不允许来自浏览器的修改。

#### 原理
你也应该猜到了，很简单。springboot对实现了WebMvcConfigurer的类（WebMvcConfigurerAdapter继承自WebMvcConfigurer），回调其addInterceptors方法。达到添加拦截器的目的。

#### 还没结束
通常其他的文章，到这里已经结束了，但是本篇不是。  
你有没有发现，访问http://localhost:8080/books?locale=foo会报错，但是访问http://localhost:8080/writer?locale=foo是正常的呢？  

为什么呢？  
原来，/books是我们在controller层通过`@RestController`,`@RequestMapping("/books")`暴露的。  
/writers这些事我们在数据层通过`@RepositoryRestResource`直接暴露的。  
看来两种方式还是有区别的。`@RepositoryRestResource`直接暴露的根本不走拦截器。  

