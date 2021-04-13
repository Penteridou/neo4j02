package com.example.neo4j02.services;

import com.example.neo4j02.repositories.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchemaImportService {
//    @Autowired
//NodeRepository nodeRepository;

    @Autowired
    NodeRepository nodeRepository;
    //import books
    public void importSchema(){
        nodeRepository.importSchema();

    }

    public void deleteSchema(){
        nodeRepository.deleteSchema();

    }


}
