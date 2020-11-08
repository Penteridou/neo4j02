package com.example.neo4j02.repositories;

import com.example.neo4j02.nodes.Editor;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface EditorRepository extends Neo4jRepository<Editor,Long> {

    Optional<Editor> findById(Long id);


}
