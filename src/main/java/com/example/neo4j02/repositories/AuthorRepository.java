package com.example.neo4j02.repositories;

import com.example.neo4j02.nodes.Author;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AuthorRepository extends Neo4jRepository<Author,Long> {

    Optional<Author> findById(Long id);


}
