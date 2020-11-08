package com.example.neo4j02.repositories;

import com.example.neo4j02.nodes.Book;
import com.example.neo4j02.nodes.Genre;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Collection;
import java.util.Optional;

public interface GenreRepository extends Neo4jRepository<Genre,String> {


    Optional<Genre> findById(Long id);


    @Query("LOAD CSV WITH HEADERS FROM \"file:///genres.csv\" AS Line\n" +
            "MERGE (b:Book {title: Line.title})\n" +
            "MERGE (g:Genre {genreName: Line.genre})  \n" +
            "CREATE (b)-[:HAS_CATEGORY]->(g)")
    Collection<Genre> importGenres();

}
