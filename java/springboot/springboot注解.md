1. @RestController:是springmvc的注解，作用是告诉spring这是一个处理web请求的controller。  
2. @RequestMapping：也是springmvc的注解，作用是路由匹配。
3. @EnableAutoConfiguration：让springboot根据你依赖的jar包，猜测应该怎么配置spring。比如spring-boot-starter-web会添加tomcat和springmvc等的依赖，springboot会认为这是一个web工程。这个注解通常只加在主类上，它所在的包，会被spring默认为包扫描路径。  
如果发现某个自动配置不是想要的，可以使用exclude或者excludeName排除。（也可以在配置文件中用spring.autoconfigure.exclude这个属性排除）     
自动配置是非侵入的，当你定义了自己的配置，默认的自动配置就会被替换掉。至于为什么springboot启用某个配置，可以参考前面的文章（springboot5）。

4. @Import：引入配置类
5. @ImportResource：引入配置文件
6. @ComponentScan：包扫描路径。官方建议是，将主类放在根路径下，加上ComponentScan注解，不需要任何参数，即可将所有组件注入。
7. @SpringBootApplication：一般用于主类上，其实就是@Configuration, @EnableAutoConfiguration and @ComponentScan的组合。
8.   
