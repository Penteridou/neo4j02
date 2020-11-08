package com.example.neo4j02.controllers;

import com.example.neo4j02.nodes.Genre;
import com.example.neo4j02.nodes.User;
import com.example.neo4j02.repositories.GenreRepository;
import com.example.neo4j02.services.GenreImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GenreController {

    private  final GenreImportService genreImportService;
    @Autowired
    GenreRepository genreRepository;

    public GenreController(GenreImportService genreImportService, GenreRepository genreRepository) {
        this.genreImportService = genreImportService;
        this.genreRepository = genreRepository;
    }

    @GetMapping("/importgenres")
    public String importGenres(){
        genreImportService.importGenres();
        return "genres imported";
    }

    @GetMapping("/getgenres")
    public List<Genre> getUsers(){ return (List<Genre>) genreRepository.findAll();}
}
