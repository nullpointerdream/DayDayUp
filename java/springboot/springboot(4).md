>  个人积累，请勿私自转载，转载前请联系  
>  代码及文章资源[https://github.com/jedyang/DayDayUp/tree/master/java/springboot](https://github.com/jedyang/DayDayUp/tree/master/java/springboot)  
>  基于SpringBootCookBook的读书笔记，重在个人理解和实践，而非翻译。

## web框架配置
 

### 配置URL匹配
简单来说，springboot允许我们定制自己的url匹配规则。  
比如，我们想使用isbn.revision做匹配，但是默认的情况下，.是界定符。  
####做法
1. 先看下现在的效果  

	http://localhost:8080/books/1001.1  
{"id":1,"isbn":"1001","title":"test1","description":null,"author":{"id":1,"firstName":"yunsheng","lastName":"yang"},"publisher":{"id":1,"name":"yunsheng"},"reviewers":[]}
因为.被做了处理，所以1001.1会被查询来，但实际并不存在。  

2. 配置URL匹配
在WebConfiguration中进行配置

	    // 演示URL匹配
	    @Override
	    public void configurePathMatch(PathMatchConfigurer configurer) {
	        configurer.setUseSuffixPatternMatch(false).
	                setUseTrailingSlashMatch(true);
	    }

3. run  
http://localhost:8080/books/1001.1  
现在就查不到了。  

#### 原理  
- setUseSuffixPatternMatch(false)表明不使用.*匹配。  
区别就是@RequestMapping(value = "/{isbn}", method = RequestMethod.GET)中isbn的值。  
false，isbn值是1001.1。  
true,isbn值是1001。  

- setUseTrailingSlashMatch(true)表明使用/做分割符，就像不存在一样，不会加到参数里。  

在极端特殊情况下，也可以使用PathMatcher 和 UrlPathHelper做特殊定制化。  

### 配置静态资源路径匹配  
上一小节，我们配置了controller方法的路径匹配。也可以配置对静态资源的访问路径。  
比如，我们要通过http://localhost:8080/internal/application.properties对外暴露我们的application.properties配置文件。  

#### 做法
1. 在WebConfiguration中添加配置  

	    // 访问静态资源
	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/internal/**")
	                .addResourceLocations("classpath:/");
	    }

2. run  
访问http://localhost:8080/internal/application.properties会根据你浏览器的设置，下载或直接展示。  

#### 原理
- `addResourceHandler("/internal/**")`添加一个处理匹配`/internal/**`路径的handler，可以多个
- `addResourceLocations("classpath:/")`表明资源从哪里找，可以多个。如果多个，顺序按照添加的顺序查找。  

### 通过EmbeddedServletContainerCustomizer定制tomcat
我们可以在application.properties中配置很多东西，如port、ssl等。但是springboot也通过通过EmbeddedServletContainerCustomizer开放给我们更复杂的程序化配置能力。  

比如通过代码配置session的超时时间。（当然这个也可以通过配置文件配置）

#### 做法
1. 在WebConfiguration中添加配置    
	
	    @Bean
	    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
	        return new EmbeddedServletContainerCustomizer() {
	            @Override
	            public void customize(ConfigurableEmbeddedServletContainer container) {
	                container.setSessionTimeout(1, TimeUnit.MINUTES);
	            }
	        };
	    }

2. 在controller中加一个处理方法  

	    // 演示配置
	    @RequestMapping(value = "/session", method = RequestMethod.GET)
	    public String getSessionId(HttpServletRequest request) {
	        return request.getSession().getId();
	    }
查一下session id  

3. run  
http://localhost:8080/books/session  
一分钟后再试，可以看到session id改变

#### 原理
在应用启动时，springboot会自动发现Customizer实例，并执行其customize方法。将其引用传给servlet容器。  这个Customizer实例有我们对容器的配置。
  



像EmbeddedServletContainerCustomizer这种只有一个方法的接口，对java8 的lamda表达式是非常使用的。所以可以改写成

    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
        return (ConfigurableEmbeddedServletContainer container) -> {
            container.setSessionTimeout(1, TimeUnit.MINUTES);
        };
    }