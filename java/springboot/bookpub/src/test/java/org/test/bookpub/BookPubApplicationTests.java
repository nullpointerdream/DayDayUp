package org.test.bookpub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.test.bookpub.entity.Book;
import org.test.bookpub.repository.BookRepository;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)// SpringJUnit支持，由此引入Spring-Test框架支持
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookPubApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void webappBookIsbnApi() {
//        Book book = restTemplate.getForObject("/books/1001", Book.class);
        Book book = restTemplate.getForObject("http://localhost:8080/books/1001", Book.class);
        System.out.println(book);
        assertNotNull(book);
        assertEquals("yunsheng", book.getPublisher().getName());
    }

    @Autowired
    private BookRepository bookRepository;

    // 测试数据库可正常连接
    @Test
    public void contextLoads() {
        assertEquals(1, bookRepository.count());
    }


    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void webappBookApi() throws Exception {
        //MockHttpServletRequestBuilder.accept方法是设置客户端可识别的内容类型
        //MockHttpServletRequestBuilder.contentType,设置请求头中的Content-Type字段,表示请求体的内容类型
        mockMvc.perform(get("http://localhost:8080/books/1001")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("yunsheng")))
                .andExpect(jsonPath("$.isbn").value("1001"));
    }


}
