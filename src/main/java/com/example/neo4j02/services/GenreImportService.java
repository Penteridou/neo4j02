package com.example.neo4j02.services;

import com.example.neo4j02.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreImportService {
    @Autowired
    GenreRepository genreRepository;

    public void importGenres(){
        genreRepository.importGenres();
    }
}
