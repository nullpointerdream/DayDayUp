package org.test.bookpub;

import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.test.bookpub.repository.BookRepository;
import org.test.bookpub.utils.BookFormatter;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    // 使用转换器的3种方式

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        return new ByteArrayHttpMessageConverter();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 为了下面的测试，注释掉
        // 否则缺少converter报错
        // converters.clear();

        converters.add(new ByteArrayHttpMessageConverter());
    }

    // 演示Formatter
    @Autowired
    private BookRepository bookRepository;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new BookFormatter(bookRepository));
    }

    // 演示URL匹配
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false).
                setUseTrailingSlashMatch(true);
    }

    // 访问静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/internal/**")
                .addResourceLocations("classpath:/");
    }

    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
//        return new EmbeddedServletContainerCustomizer() {
//            @Override
//            public void customize(ConfigurableEmbeddedServletContainer container) {
//                container.setSessionTimeout(1, TimeUnit.MINUTES);
//            }
//        };

        return (ConfigurableEmbeddedServletContainer container) -> {
            container.setSessionTimeout(1, TimeUnit.MINUTES);
        };
    }


}