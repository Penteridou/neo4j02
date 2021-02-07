package com.example.neo4j02.repositories;


import com.example.neo4j02.dto.EntityNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Collection;
import java.util.Optional;

public interface NodeRepository<T> extends Neo4jRepository<T, Long> {

    Optional<T> findById(Long id);



//---------IMPORT SCHEMA------------------------------------------------------------

    @Query("LOAD CSV WITH HEADERS FROM \"file:///books.csv\" AS Line\n" +
            "MERGE (b:Book {title: Line.title, year: toInteger(Line.year)  , price: Line.price})" +
            "MERGE (e:Editor {publisher: Line.edition })\n" +
            "MERGE (a:Author {writer: Line.author})\n"+
            "MERGE (a)-[:WROTE]->(b)\n" +
            "merge (e)-[:PUBLISHED]->(b)" )
  Collection<EntityNode> importSchema();

//    @Query("LOAD CSV WITH HEADERS FROM 'file:///songs.csv' AS Line\n" +
//            "merge (s:Song{title:Line.title})\n" +
//            "merge (a:Artist{name:Line.artist})\n" +
//            "merge (g:Genre{category:Line.genre})\n" +
//            "create (a)-[:SINGS{ releasedYear: Line.releasedYear }]->(s)-[:IS]->(g)" )
//    Collection<EntityNode> importSchema();






//-----------GET QUERIES-----------------------------------------------------------------------------------------



    //get node labels
    @Query("MATCH (n) \n" +
            "with labels(n) as nested \n" +
            "unwind nested as x \n" +
            "return distinct x")
    Collection<Object> getAllNodes();


    //get relationship types
    @Query("MATCH (a)-[r]-(b) RETURN distinct type(r) as rel_types")
    Collection<Object> showRelTypes();

    //call schema TO FIX
    @Query("call db.schema")
    Collection<Object> callSchema();


    //get node properties (names)
    @Query("match (n) \n" +
            "with keys(n) as nested\n" +
            "unwind nested as x\n" +
            "return distinct x\n")
    Collection<Object> getAllProperties();

  //get relationship properties (names)
  @Query("MATCH ()-[r]-()" +
          " with keys(r) as nested" +
          " unwind nested as x\n" +
          "return distinct x")
  Collection<Object> getAllRelProperties();




}
