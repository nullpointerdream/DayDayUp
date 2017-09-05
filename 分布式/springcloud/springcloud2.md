## Zuul
[代码地址：https://github.com/jedyang/springCloud](https://github.com/jedyang/springCloud)

先来看一个简单的微服务架构图。
![](6.png)
用户请求通过负载均衡nginx，网关路由zuul，到达服务层。服务统一注册在注册中心Eureka，配置项同意在配置中心管理，配置托管在git仓库。  

Zuul的主要功能是路由转发和过滤。路由转发可以类比spring的servlet-mapping功能。zuul默认已集成ribbon负载均衡。    

1. 新建一个工程zuul  
创建时勾选web、eureka discover、zuul  

2. 代码开启zuul功能 

		@EnableZuulProxy
		@EnableEurekaClient
		@SpringBootApplication
		public class ZuulApplication {
		
			public static void main(String[] args) {
				SpringApplication.run(ZuulApplication.class, args);
			}
		}
	@EnableZuulProxy注解开启zuul功能

3. 改造之前的服务  
在学习ribbon和feign时，我在一个工程里做了演示。现在想要演示zuul路由分发功能，最好将之前的工程拆分一下。我这里就不拆了，通过修改端口号提供两个服务出来。一个service-ribbon在端口8764，另一个service-feign在8765。
这是之前的配置文件：

		eureka:
		  client:
		    serviceUrl:
		      defaultZone: http://localhost:8761/eureka/
		server:
		  port: 8764
		#  port: 8765
		# 切换端口，启动不同服务
		spring:
		  application:
		    name: service-ribbon
		#    name: service-feign

4. 以此启动之前的服务
注册中心8761。  
服务provider在8762。（只启动一个好了）  
服务consumer service-ribbon在8764  
服务consumer service-feign在8765
![](7.png)

5. 配置下zuul工程

		eureka:
		  client:
		    serviceUrl:
		      defaultZone: http://localhost:8761/eureka/
		server:
		  port: 8766
		spring:
		  application:
		    name: service-zuul
		zuul:
		  routes:
		    api-a:
		      path: /api-ribbon/**
		      serviceId: service-ribbon
		    api-b:
		      path: /api-feign/**
		      serviceId: service-feign

	以/api-ribbon/ 开头的请求都转发给service-ribbon服务；以/api-feign/开头的请求都转发给service-feign服务；

6. 测试  
浏览器请求http://localhost:8766/api-feign/feignHi?name=yunsheng  
返回`hi yunsheng,i am from port:8762`证明zuul将请求转发给了8765。  
改一下请求http://localhost:8766/api-ribbon/feignHi?name=yunsheng   
返回错误。因为api-ribbon会转发给8764,没有对应的服务。  

### 过滤器功能
除了请求路由以外，zuul另一个重要功能就是filter了。  
代码
		
		@Component
		public class MyFilter extends ZuulFilter {
		    @Override
		    public String filterType() {
		        /* 过滤时机
		        pre：路由之前
		        routing：路由之时
		        post： 路由之后
		        error：发送错误调用
		        */
		        return "pre";
		    }
		
		    /**
		     * 过滤器的优先级
		     * 数字越大优先级越低
		     *
		     * @return
		     */
		    @Override
		    public int filterOrder() {
		        return 0;
		    }
		
		    /**
		     * 该过滤器开关
		     *
		     * @return
		     */
		    @Override
		    public boolean shouldFilter() {
		        return true;
		    }
		
		    /**
		     * 过滤器方法
		     *
		     * @return
		     */
		    @Override
		    public Object run() {
		        RequestContext ctx = RequestContext.getCurrentContext();
		        HttpServletRequest request = ctx.getRequest();
		
		        String username = request.getParameter("name");// 获取请求的参数
		        if (null != username && username.equals("yunsheng")) {// 如果请求的参数不为空，且值为chhliu时，则通过
		            ctx.setSendZuulResponse(true);// 对该请求进行路由
		            ctx.setResponseStatusCode(200);
		            ctx.set("isSuccess", true);// 设值，让下一个Filter看到上一个Filter的状态
		            return null;
		        } else {
		            ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
		            ctx.setResponseStatusCode(401);// 返回错误码
		            ctx.setResponseBody("{\"result\":\"name is not correct!\"}");// 返回错误内容
		            ctx.set("isSuccess", false);
		            return null;
		        }
		    }
		}

测试一下，http://localhost:8766/api-ribbon/ribbonHi?name=yunshen  
返回{"result":"name is not correct!"}  

## 配置中心config
spring cloud的配置项管理是以托管在git仓库中的文件为存储介质。  
配置中心分为两部分：server端负责连接git仓库，得到配置文件。client端负责对外提供api，从server端查询具体配置信息。

### server端
1. 新建工程  
勾选eureka discovert、config server  

2. 代码

		@SpringBootApplication
		@EnableConfigServer
		public class ConfigApplication {
		
			public static void main(String[] args) {
				SpringApplication.run(ConfigApplication.class, args);
			}
		}
还是注解开启对应的功能  

3. 配置  
配置application.properties

		#spring.application.name=config-server
		server.port=8767
		
		
		# 配置git仓库地址
		spring.cloud.config.server.git.uri=https://github.com/jedyang/springCloud/
		# 仓库路径
		spring.cloud.config.server.git.searchPaths=configRepo
		# 仓库的分支
		spring.cloud.config.label=master
		# 用户名和密码，例子的是公开仓库，不需要
		#spring.cloud.config.server.git.username=your username
		#spring.cloud.config.server.git.password=your password

4. 创建配置文件  
这是我创建的配置文件  
![](8.png)

5. 测试  
 http://localhost:8767/thekey/dev/master  
看到响应：    
{"name":"thekey","profiles":["dev"],"label":"master","version":"d6a477aa1da5862e9e42553d644e136efbea9296","state":null,"propertySources":[]}  
但是。。其实这只代表能访问到仓库的某个分之下，并不是真的配置内容。  
比如访问http://localhost:8767/a/a/  
依然能得到响应：  
{"name":"a","profiles":["a"],"label":null,"version":"d6a477aa1da5862e9e42553d644e136efbea9296","state":null,"propertySources":[]}  
从响应中我们也可以看出，比如访问http://localhost:8767/a/b/c时，a代表应用名（在client中配置，现在是在server工程，不要急），b是环境如dev、test，c代表git上的分支，现在只有master（所以要么不写，写错会报错）。  

还有其他访问方式：  

	/{application}/{profile}[/{label}]
	/{application}-{profile}.yml
	/{label}/{application}-{profile}.yml
	/{application}-{profile}.properties
	/{label}/{application}-{profile}.properties、

### client端
1. 新建工程  
勾选web、config client
2. 配置  
这里是配置bootstrap.properties。一定要注意啊。坑。

		spring.application.name=config-client
		spring.cloud.config.label=master
		spring.cloud.config.profile=dev
		spring.cloud.config.uri= http://localhost:8767/
		server.port=8768
	最终对应的文件是spring.application.name-spring.cloud.config.profile.properties

3. 代码

		@SpringBootApplication
		@RestController
		public class ConfigClientApplication {
		
		    public static void main(String[] args) {
		        SpringApplication.run(ConfigClientApplication.class, args);
		    }
		
		    @Value("${thekey}")
		    String theValue;
		
		    @RequestMapping(value = "/getValue")
		    public String getValue() {
		        return theValue;
		    }
		}
	做一个rest服务查询下配置的值。  
	原理就是通过@Value注解取值
4. 测试
访问http://localhost:8768/getValue  
得到配置的值

### 集群化
到现在为止，我们的配置服务是单点的，在生产环境要求高可用的情况下，是存在风险的。现在我们将其集群化。  
集群化的方法也很简单，将服务注册到eureka，通过eureka服务调用。  

这里我们还是复用之前的注册中心8761。

1. 改造server端  
在配置文件中加上  
`eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/`  
指定服务注册地址  

	启动类加上`@EnableEurekaClient`注解

2. 改造client端  
	添加依赖  	

		<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>

	修改配置  

		spring.application.name=config-client
		spring.cloud.config.label=master
		spring.cloud.config.profile=dev
		#spring.cloud.config.uri= http://localhost:8767/
		server.port=8768
		
		eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
		spring.cloud.config.discovery.enabled=true
		spring.cloud.config.discovery.serviceId=config-server

	可以看到，现在修改为通过serviceId来获取服务，如果有多个服务，可以实现集群化和负载均衡。  

3.测试  
tips：client端一定要在server启动成功后再起，因为需要从server获取配置，如果解析不到，会报错。  

## 服务链路追踪
在复杂业务系统中，排查问题是一件痛苦的事情，尤其是超时问题。你必须知道全链路的服务调用关系，以及服务花费的时间。  

针对服务化应用全链路追踪的问题，Google发表了Dapper论文，介绍了他们如何进行服务追踪分析。其基本思路是在服务调用的请求和响应中加入ID，标明上下游请求的关系。利用这些信息，可以可视化地分析服务调用链路和服务间的依赖关系。  

Zipkin是对Dapper论文的开源实现，Spring Cloud Sleuth对Zipkin进行了封装，以便加入spring cloud全家桶。  

Spring Cloud Sleuth（以下简称sleuth）借用了Dapper的术语。 
 
- span。简单的理解就是一个最小的服务单元。例如发送一个RPC请求是一个span，发送一个响应给RPC请求也是一个span。每个span用64bit的唯一id标示。span上会包含其他信息，如描述、注解（理解成标签），触发这个span的上一个span的id，最重要的时间信息。  

- trace。就是有一个个span组成的调用链路。
- annotion。我们javaer习惯称为注解，但是这里理解成标签比较合适。常用的核心标签：  
	- cs，client sent
	- sr，server received
	- ss，server sent
	- cr，client received
	
![](9.png) 
可以理解，sr-cs=网络传输时间。ss-sr=服务处理时间。cr-cs得到整个服务需要的时间。  

开始代码
### zipkin server
1. 新建一个工程zipkin-server  
我的依赖如下：  
	
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-starter-zipkin</artifactId>
			</dependency>
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-web</artifactId>
			</dependency>
	
			<dependency>
				<groupId>io.zipkin.java</groupId>
				<artifactId>zipkin-server</artifactId>
			</dependency>
	
			<dependency>
				<groupId>io.zipkin.java</groupId>
				<artifactId>zipkin-autoconfigure-ui</artifactId>
				<scope>runtime</scope>
			</dependency>
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-test</artifactId>
				<scope>test</scope>
			</dependency>
		</dependencies>

里面的zipkin-server这个依赖，在idea创建的时候选不到，但是又是必须的。有知道的麻烦告诉我一下。  

2. 代码  
启动器加注解`@EnableZipkinServer`

3. 配置  
加一下端口server.port=9411  
最好使用这个端口，应该是有依赖关系。我换成其他的会报错。还不清楚具体怎么依赖的。  

### 创建相互调用的服务  
我创建了两个工程。app1和app2。

1. 依赖  

		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-starter-zipkin</artifactId>
			</dependency>
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-web</artifactId>
			</dependency>
	
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-test</artifactId>
				<scope>test</scope>
			</dependency>
		</dependencies>

2. 代码

	App1：

		@SpringBootApplication
		@RestController
		public class App1Application {
		
		    public static void main(String[] args) {
		        SpringApplication.run(App1Application.class, args);
		    }
		
		    @Autowired
		    private RestTemplate restTemplate;
		
		    @Bean
		    public RestTemplate getRestTemplate() {
		        return new RestTemplate();
		    }
		
		    @RequestMapping("/hi1")
		    public String callHi1() {
		        return restTemplate.getForObject("http://localhost:9002/hi2", String.class);
		    }
		
		    @RequestMapping("/hi3")
		    public String callHi3() {
		
		        return "i'm hi 33333";
		
		    }
		}

	App2：
		
		@SpringBootApplication
		@RestController
		public class ZipkinApp2Application {
		
			public static void main(String[] args) {
				SpringApplication.run(ZipkinApp2Application.class, args);
			}
		
			@Autowired
			private RestTemplate restTemplate;
		
			@Bean
			public RestTemplate getRestTemplate(){
				return new RestTemplate();
			}
		
			@RequestMapping("/hi2")
			public String callHi2(){
				return restTemplate.getForObject("http://localhost:9001/hi3", String.class);
			}
		}

3. 配置

	APP1：

		server.port=9001
		spring.zipkin.base-url=http://localhost:9411
		spring.application.name=service-app1

	APP2：
	
		server.port=9002
		spring.zipkin.base-url=http://localhost:9411
		spring.application.name=service-app2

就是hi1-->hi2-->hi3

依次启动服务。  

查看http://localhost:9411/zipkin/  
![](10.png)

看一条链路  
![](11.png)
可以看到时间和标签