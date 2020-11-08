package com.example.neo4j02.services;

import com.example.neo4j02.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BookImportService {
    @Autowired
    BookRepository bookRepository;

    //import books
    public void importBooks(){
         bookRepository.importBooks();

    }
}
