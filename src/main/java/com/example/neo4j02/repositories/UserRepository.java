package com.example.neo4j02.repositories;

import com.example.neo4j02.nodes.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User, Integer> {

    Optional<User> findById(Integer id);

    @Query("MATCH (u:User)<-[r:RATED]-(m:Movie) RETURN u,r,m")
    Collection<User> getAllUsers();

    //IMPORT USERS
    @Query("LOAD CSV WITH HEADERS FROM \"file:///users.csv\" AS Line\n" +
            "    CREATE (u:User {id: toInteger(Line.id), name: Line.Name, surname: Line.Surname , birthday:Line.BirthDate})")
    Collection<User> importUsers();

    //IMPORT BORROWINGS
    @Query("LOAD CSV WITH HEADERS FROM \"file:///borrowed.csv\" AS Line\n" +
            "MERGE (u:User {id:toInteger(Line.userid)})\n" +
            "MERGE (b:Book {name: Line.title})\n" +
            "CREATE (b)-[:BORROWED_BY {date: Line.date}]->(u)")
    Collection <User> importBorrowings();

    //IMPORT RATINGS
    @Query("LOAD CSV WITH HEADERS FROM \"file:///rated.csv\" AS Line\n" +
            "MERGE (u:User {id: toInteger(Line.userid)})\n" +
            "MERGE (b:Book {name: Line.title})\n" +
            "CREATE (b)-[:RATED_BY {rank: toInteger(Line.rank)}]->(u)")
    Collection<User> importRatings();



}
