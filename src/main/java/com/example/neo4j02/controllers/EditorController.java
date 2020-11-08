package com.example.neo4j02.controllers;

import com.example.neo4j02.nodes.Book;
import com.example.neo4j02.nodes.Editor;
import com.example.neo4j02.repositories.EditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EditorController {


    @Autowired
     private EditorRepository repository;


    @GetMapping("/getEditors")
    public List<Editor> getEditors() {
        return (List<Editor>) repository.findAll();
    }

}
