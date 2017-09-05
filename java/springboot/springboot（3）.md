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

