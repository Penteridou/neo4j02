package com.example.neo4j02.controllers;

import com.example.neo4j02.nodes.Author;
import com.example.neo4j02.nodes.Editor;
import com.example.neo4j02.repositories.AuthorRepository;
import com.example.neo4j02.repositories.EditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthorController {
    
    @Autowired
    private AuthorRepository repository;


    @GetMapping("/getAuthors")
    public List<Author> getAuthors() {
        return (List<Author>) repository.findAll();
    }
}
