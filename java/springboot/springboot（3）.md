>  个人积累，请勿私自转载，转载前请联系  
>  代码及文章资源[https://github.com/jedyang/DayDayUp/tree/master/java/springboot](https://github.com/jedyang/DayDayUp/tree/master/java/springboot)  
>  基于SpringBootCookBook的读书笔记，重在个人理解和实践，而非翻译。

### 配置HttpMessageConverters
在之前的代码中，我们没有写过将java bean转换到http response的代码，实际上是springboot默认配置了转换器，将java bean使用Jackson库转换成json格式，写到http的输出流中。   
当有多个转换器时，会根据请求的类型选择最合适的转换器。  

内置了很多转换器，用来转换不同的类型，例如。  

- MappingJackson2HttpMessageConverter:将java对象转换成application/json  
- ProtobufHttpMessageConverter：只能用来转换谷歌的com.google.protobuf.Message实例，但是可以写成application/json, application/xml, text/plain, application/x-protobuf多种类型。

HttpMessageConverters不仅可以用在转换对象到http输出流中，也可以用在将请求转换成对象。
使用方式有三种：  

1. @Bean注解  
使用`@Bean`注解  

	    @Bean
	    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
	        return new ByteArrayHttpMessageConverter();
	    }
2. 如果继承了WebMvcConfigurerAdapter，可以覆写configureMessageConverters方法

	    @Override
	    public void configureMessageConverters(List<HttpMessageConverter<?>>
	                                                   converters) {
	        converters.add(new ByteArrayHttpMessageConverter());
	    }

3. 如果继承了WebMvcConfigurerAdapter，可以覆写extendMessageConverters方法

		@Override
	    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
	        converters.clear();
	        converters.add(new ByteArrayHttpMessageConverter());
	    }

#### 区别
那么这三种方式有什么区别呢？  
1. 简单。不需要继承WebMvcConfigurerAdapter。只要声明了这个bean。spring会发现它是一个HttpMessageConverter。将它自动加到转换器列表中。  
2. 如果已经继承了WebMvcConfigurerAdapter，使用起来也比较方便。
3. 这种不仅可以添加转换器。而且可以删除，控制转换器。功能比较强大。当然有可能其他类也继承了WebMvcConfigurerAdapter，并且也实现了configureMessageConverters，这种情况要注意，当然这种情况比较少见。  

### 配置PropertyEditors
前面的小节，我们使用converter转换请求和响应，除了这种以外，还有其他的转换，如将参数转换成不同的类型。  
当我们在controller层定义一个处理请求的方法时，方法签名可以使用一个具体的类型。那么http请求过来的应该是字符流，这两种请求参数时如何自动转换的呢？  
答案就是PropertyEditor。  
PropertyEditor时jdk中的api，用来做文本到对象类型的转换。结果spring的开发人员发现正好可以用来使用。  
springmvc已经内置了多种PropertyEditor的实现，如Boolean, Currency, and Class。这一节我们自定义一个Isbn类，演示自定义的PropertyEditor如何使用

1. 数据准备   
在数据库中插入一条数据，使查询时效果如下

	http://localhost:8080/books/1001  
	{"id":1,"isbn":"1001","title":"test1","description":null,"author":{"id":1,"firstName":"yunsheng","lastName":"yang"},"publisher":{"id":1,"name":"yunsheng"},"reviewers":[]}

2. 将上一节的converters.clear()注释掉
3. 创建类Isbn  
因为这个不属于entity，新建一个包utils。  
建Isbn类：  

		package org.test.bookpub.utils;
		
		public class Isbn {
		    private String isbn;
		
		    public Isbn(String isbn) {
		        this.isbn = isbn;
		    }
		
		    public String getIsbn() {
		        return isbn;
		    }
		}

	建IsbnEditor，继承PropertyEditorSupport。用于转换。  

		package org.test.bookpub.utils;
		
		import org.springframework.util.StringUtils;
		
		import java.beans.PropertyEditorSupport;
		
		/**
		 * 演示PropertyEditor
		 */
		public class IsbnEditor extends PropertyEditorSupport {
		    @Override
		    public void setAsText(String text) throws IllegalArgumentException {
		        if (StringUtils.hasText(text)) {
		            setValue(new Isbn(text.trim()));
		        } else {
		            setValue(null);
		        }
		    }
		
		    @Override
		    public String getAsText() {
		        Isbn isbn = (Isbn) getValue();
		        if (isbn != null) {
		            return isbn.getIsbn();
		        } else {
		            return "";
		        }
		    }
		}

4. 在controller中注册  
在BookController中注册这个propertyEditor  

	    @InitBinder
	    public void initBinder(WebDataBinder binder) {
	        binder.registerCustomEditor(Isbn.class, new IsbnEditor());
	    }

5. 修改方法参数为对象类型  
以前我们的`public Book getBook(@PathVariable String isbn) {`方法签名是String类型。现在直接改成Isbn对象类型。

6. run  
可以看到结果依然可以查询出来。 

#### 原理
就是注解`@InitBinder`。spring自动扫描并添加这个注解。注意参数类型一定要是WebDataBinder  

注意。 propertyEditor不是线程安全的，所以每个请求都要创建一个自己的。  
spring已经内建了很多，通常情况下并不需要自己定义一个。   

### 配置type Formatters
如上一节所说，propertyEditor是线程不安全的。所以，spring提供了Formatter以替代。  Formatter是线程安全的，且专注于string和object的相互转换。  

为了演示，假设这样的需求。请求过来的参数是String类型的Isbn号，但是controller中的方法参数是Book对象，需要我们完成其中的转换。  

1. 在utils包下创建formatter实例  

		package org.test.bookpub.utils;
		
		import org.springframework.format.Formatter;
		import org.test.bookpub.entity.Book;
		import org.test.bookpub.repository.BookRepository;
		
		import java.util.Locale;
		
		public class BookFormatter implements Formatter<Book> {
		    private BookRepository repository;
		
		    public BookFormatter(BookRepository repository) {
		        this.repository = repository;
		    }
		
		    @Override
		    public Book parse(String bookIdentifier, Locale locale) {
		        Book book = repository.findByIsbn(bookIdentifier);
		        return book != null ? book :
		                repository.findOne(Long.valueOf(bookIdentifier));
		    }
		
		    @Override
		    public String print(Book book, Locale locale) {
		        return book.getIsbn();
		    }
		}
2. 在WebConfiguration中注册  
	
	 	// 演示Formatter
	    @Autowired
	    private BookRepository bookRepository;
	
	    @Override
	    public void addFormatters(FormatterRegistry registry) {
	        registry.addFormatter(new BookFormatter(bookRepository));
	    }

3. 在controller中新增一个方法

	    // 演示formatter
	    @RequestMapping(value = "/{isbn}/reviewers", method =
	            RequestMethod.GET)
	    public List<Publisher.Reviewer> getReviewers(@PathVariable("isbn") Book book) {
	        return book.getReviewers();
	    }

4. run  

		http://localhost:8080/books/1001/reviewers
		[]

#### 原理
因为formatter是无状态且线程安全的，所以我们只要在WebConfiguration中注册一次，即可在所有请求中生效。  
