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