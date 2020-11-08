package com.example.neo4j02.controllers;


import com.example.neo4j02.nodes.Book;

import com.example.neo4j02.repositories.BookRepository;
import com.example.neo4j02.services.BookImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private final BookImportService bookImportService;
    @Autowired
    private BookRepository repository;


    public BookController(BookImportService bookImportService, BookRepository repository) {
        this.bookImportService = bookImportService;
        this.repository = repository;
    }

    @PostMapping("/addBook")
    public String addBook(@RequestBody Book book) {
        repository.save(book);
        return "Book Added";
    }

    @GetMapping("/getBooks")
    public List<Book> getBooks() {
        return (List<Book>) repository.findAll();
    }



    //MAKE THIS DAO****************************************************************************
    @GetMapping("/getBookById/{id}")
    public Optional<Book> getBook(@PathVariable Long id) {
        return repository.findById(id);
    }

    //import
    @GetMapping("/importbooks")
    public String importBooks() {
        bookImportService.importBooks();
        return "Books Imported";
    }



}
