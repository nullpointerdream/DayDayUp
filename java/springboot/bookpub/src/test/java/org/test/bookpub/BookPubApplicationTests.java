package org.test.bookpub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.test.bookpub.entity.Book;
import org.test.bookpub.repository.BookRepository;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)// SpringJUnit支持，由此引入Spring-Test框架支持
@SpringBootTest(classes = BookPubApplication.class) //指定启动类
public class BookPubApplicationTests {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private BookRepository repository;
    @Value("${local.server.port}")
    private int port;
    private MockMvc mockMvc;
    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void contextLoads() {
        assertEquals(1, repository.count());
    }

    @Test
    public void webappBookIsbnApi() {
        Book book = restTemplate.getForObject("http://localhost:" + port +
                "/books/978-1-78528-415-1", Book.class);
        assertNotNull(book);
        assertEquals("Packt", book.getPublisher().getName());
    }

//    @Test
//    public void webappPublisherApi() throws Exception {
//        mockMvc.perform(get("/publishers/1")).andExpect(
//                status().isOk()).andExpect(content()
//                .contentType(MediaType.parseMediaType("application/hal+json")))
//                .andExpect(content().string(containsString("Packt")))
//                .andExpect(jsonPath("$.name").value("Packt"));
//    }
}
