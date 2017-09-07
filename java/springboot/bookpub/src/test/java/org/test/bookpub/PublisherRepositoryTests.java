package org.test.bookpub;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.test.bookpub.repository.PublisherRepository;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PublisherRepositoryTests {
    @MockBean
    private PublisherRepository repository;

    @Before
    public void setupPublisherRepositoryMock() {
        Mockito.when(repository.count()).thenReturn(100L);
    }

    @Test
    public void publishersExist() {
        assertEquals(100, repository.count());
    }

    @After
    public void resetPublisherRepositoryMock() {
        Mockito.reset(repository);
    }
}