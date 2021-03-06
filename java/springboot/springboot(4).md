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

### 选择内嵌的servlet容器
tomcat是springboot默认的servlet容器，但是我们也可以选择其他的容器，例如jetty。  
#### 做法
1. 排除tomcat的依赖，增加jetty依赖  

		<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-web</artifactId>
				<exclusions>
					<exclusion>
						<groupId>org.springframework.boot</groupId>
						<artifactId>spring-boot-starter-tomcat</artifactId>
					</exclusion>
				</exclusions>
			</dependency>
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-jetty</artifactId>
			</dependency>  

2. 会发现有代码报错。因为RemoteIpFilter是tomcat的包里的。将其注释掉。  

3. run  

		2017-09-06 15:47:25.468  INFO 8580 --- [           main] org.eclipse.jetty.server.Server          : jetty-9.4.5.v20170502
测试一切正常。  

### 配置连接支持https
关于htts这块，原文档已经过时，下面是自己摸索的。 

#### 做法
1. 为了使用https。首先需要创建一个keysotre。用来加解密同浏览器的SSL通信。   
keytool是jdk的工具。我是jdk8，命令如下：    
keytool -genkeypair -alias tomcat -keyalg RSA
![](rsa.png)
成功之后，在用户目录下会创建.keystore文件。

2. 修改配置文件application.properties

		server.port = 8443
		server.ssl.key-store = ${user.home}/.keystore
		server.ssl.key-store-password = 123456
		server.ssl.key-password = 123456
密码改成你自己的。。
3. run
这样就需要使用https访问8443端口了
![](https.png)



### 配置连接同时支持http和https
企业应用中经常有这种场景，需要同时开两个端口，分别支持http和https。  
但是上面的方法只能使用一个。不可同时配置，如果两个都启动，至少有一个要以编程的方式配置。Spring Boot官方文档建议在application.properties中配置HTTPS，因为HTTPS比HTTP更复杂一些。  

继续上面操作。
#### 做法
1. 用代码给tomcat配置上一个http连接器  

	    @Bean
	    public EmbeddedServletContainerFactory servletContainer() {
	        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
	        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
	        return tomcat;
	    }
	
	    private Connector createStandardConnector() {
	        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	        connector.setPort(8080);
	        return connector;
	    }
我是放在WebConfiguration中，其实并不是一定要放在这。只要启动时加载就行。

2. run  
可以看到http也可以了。  
![](http.png)

综合起来看比原文简单的多。  
唉，springbootcookbook现在看有些过时了，还是坚持看完吧，开卷有益，遇坑填坑吧。
