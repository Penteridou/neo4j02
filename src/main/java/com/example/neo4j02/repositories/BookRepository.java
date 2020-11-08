package com.example.neo4j02.repositories;

import com.example.neo4j02.nodes.Book;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface BookRepository extends Neo4jRepository<Book, Long> {

    //Book findByName(String name);

    Optional<Book> findById(Long id);



    @Query("LOAD CSV WITH HEADERS FROM \"file:///books.csv\" AS Line\n" +
            "MERGE (b:Book {title: Line.title, year: toInteger(Line.year)  , price: Line.price})" +
            "MERGE (e:Editor {publisher: Line.edition })\n" +  //checked ok --> csv works fine , the problem is the entity
            "MERGE (a:Author {writer: Line.author})\n"+
            "MERGE (a)-[:WROTE]->(b)\n" +
            "merge (e)-[:PUBLISHED]->(b)" )
    Collection<Book> importBooks();



}
