## java模拟http请求

### 下载jersy相关的jar包
[http://www.java2s.com/Code/Jar/j/Downloadjerseyserver117jar.htm](http://www.java2s.com/Code/Jar/j/Downloadjerseyserver117jar.htm)  
或者[mvnrepository.com/artifact/com.sun.jersey](mvnrepository.com/artifact/com.sun.jersey)   
我这边下载了：  
jersey-client-1.17.jar  
jersey-server-1.17.jar  
jersey-servlet-1.17.jar  
jersey-json-1.17.jar  
jersey-core-1.17.jar 
asm-3.3.1.jar

### 使用
1. 将jar加入classpath
2. web.xml  

		<?xml version="1.0" encoding="UTF-8"?>
		<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" id="WebApp_ID" version="2.5">
		  <display-name>sample</display-name>
		  <servlet>
		    <servlet-name>Jersey REST Service</servlet-name>
		    <servlet-class>com.sun.jersey.spi.container.servlet.ServletContainer</servlet-class>
		    <init-param>
		      <param-name>com.sun.jersey.config.property.packages</param-name>
				<!-- 响应代码所在的包 -->
		      <param-value>sample</param-value>
		    </init-param>
		    <load-on-startup>1</load-on-startup>
		  </servlet>
		  <servlet-mapping>
		    <servlet-name>Jersey REST Service</servlet-name>
			 <!-- url:http://ip:port/RestfulDemo(项目名) + 下面的参数 -->
		    <url-pattern>/rest/*</url-pattern>
		  </servlet-mapping>
		</web-app> 

3. 响应代码  

		package sample;

		import javax.ws.rs.GET;
		import javax.ws.rs.Path;
		import javax.ws.rs.Produces;
		import javax.ws.rs.core.MediaType;
		
		//Sets the path to base URL + /hello
		@Path("/hello")
		public class Hello {

		// This method is called if TEXT_PLAIN is request
		@GET
		@Produces(MediaType.TEXT_PLAIN)
		public String sayPlainTextHello() {
			return "Hello Jersey";
		}

		// This method is called if XML is request
		@GET
		@Produces(MediaType.TEXT_XML)
		public String sayXMLHello() {
			return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
		}
	
		// This method is called if HTML is request
		@GET
		@Produces(MediaType.TEXT_HTML)
		public String sayHtmlHello() {
			return "<html> " + "<title>" + "Hello Jersey" + "</title>"
					+ "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
		}

		}

4. 测试代码  

		package sample;
		
		import java.net.URI;
		
		import javax.ws.rs.core.MediaType;
		import javax.ws.rs.core.UriBuilder;
		
		import com.sun.jersey.api.client.Client;
		import com.sun.jersey.api.client.ClientResponse;
		import com.sun.jersey.api.client.WebResource;
		import com.sun.jersey.api.client.config.ClientConfig;
		import com.sun.jersey.api.client.config.DefaultClientConfig;

		public class Test {
			public static void main(String[] args) {
				ClientConfig config = new DefaultClientConfig();
				Client client = Client.create(config);
				WebResource service = client.resource(getBaseURI());
				// Fluent interfaces
				System.out.println(service.path("rest").path("hello")
						.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class)
						.toString());
				// Get plain text
				System.out.println(service.path("rest").path("hello")
						.accept(MediaType.TEXT_PLAIN).get(String.class));
				// Get XML
				System.out.println(service.path("rest").path("hello")
						.accept(MediaType.TEXT_XML).get(String.class));
				// The HTML
				System.out.println(service.path("rest").path("hello")
						.accept(MediaType.TEXT_HTML).get(String.class));

		}

	    /*
	    public static void main(String[] args)
	    {
	        Client client = Client.create();
	        //
	        String url = "http://localhost:8080/RestfulDemo/rest/hello";
	        WebResource webResource = client.resource(url);
	
	        ClientResponse response = webResource.type("application/json").get(
	                ClientResponse.class);
	
	        //如果调用查询不成功
	        if (200 != response.getStatus())
	        {
	            System.out.println("query failed:"
	                    + response.getEntity(String.class));
	        }
	        System.out.println(response.getEntity(String.class));
	    }
	    */

		private static URI getBaseURI() {
			return UriBuilder.fromUri("http://localhost:8080/RestfulDemo").build();
		}

		}

5. 发布服务

tomcat
 

### 问题
1. java.lang.ClassNotFoundException: com.sun.jersey.spi.container.servlet.ServletContainer  
解决：要将jar包，放到WEB-INF的lib下，然后project -> properties -> development assembly -> add -> java build path entries.关联发布


2. asm缺包  
报错：  

		java.lang.NoClassDefFoundError: org/objectweb/asm/ClassVisitor
		    com.sun.jersey.api.core.ScanningResourceConfig.init(ScanningResourceConfig.java:79)
		    com.sun.jersey.api.core.servlet.WebAppResourceConfig.init(WebAppResourceConfig.java:102)
		    com.sun.jersey.api.core.servlet.WebAppResourceConfig.<init>(WebAppResourceConfig.java:89)
		    com.sun.jersey.api.core.servlet.WebAppResourceConfig.<init>(WebAppResourceConfig.java:74)
		    com.sun.jersey.spi.container.servlet.WebComponent.getWebAppResourceConfig(WebComponent.java:672)
		    com.sun.jersey.spi.container.servlet.ServletContainer.getDefaultResourceConfig(ServletContainer.java:415)
		    com.sun.jersey.spi.container.servlet.ServletContainer.getDefaultResourceConfig(ServletContainer.java:582)
		    com.sun.jersey.spi.container.servlet.WebServletConfig.getDefaultResourceConfig(WebServletConfig.java:87)
		    com.sun.jersey.spi.container.servlet.WebComponent.createResourceConfig(WebComponent.java:703)
		    com.sun.jersey.spi.container.servlet.WebComponent.createResourceConfig(WebComponent.java:678)
		    com.sun.jersey.spi.container.servlet.WebComponent.init(WebComponent.java:203)
		    com.sun.jersey.spi.container.servlet.ServletContainer.init(ServletContainer.java:374)
		    com.sun.jersey.spi.container.servlet.ServletContainer.init(ServletCont
原因：  
  	缺包，[asm-3.3.1](www.java2s.com/Code/Jar/a/Downloadasm331jar.htm)


3. 发布服务后，登陆不了  
将"Deploy Path"的默认值"wtpwebapps"改为"webapps",也就是tomcat中发布项目所在的文件夹名字。  
[解决](http://bbs.itbookstudy.com/t/41011/1/1)
