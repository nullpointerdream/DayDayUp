package org.test.bookpub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.test.bookpub.entity.Book;
import org.test.bookpub.entity.Publisher;
import org.test.bookpub.repository.BookRepository;
import org.test.bookpub.utils.Isbn;
import org.test.bookpub.utils.IsbnEditor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
    public Book getBook(@PathVariable Isbn isbn) {
        return bookRepository.findByIsbn(isbn.getIsbn());
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Isbn.class, new IsbnEditor());
    }

    // 演示formatter
    @RequestMapping(value = "/{isbn}/reviewers", method =
            RequestMethod.GET)
    public List<Publisher.Reviewer> getReviewers(@PathVariable("isbn") Book book) {
        return book.getReviewers();
    }

    // 演示配置
    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }
}
