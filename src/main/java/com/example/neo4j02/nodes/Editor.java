package com.example.neo4j02.nodes;

import com.example.neo4j02.dto.EntityNode;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;

@NodeEntity
public class Editor extends EntityNode {

//    @Id @GeneratedValue
//    private Long id;

    @Property("publisher")
    private String name;


    @Relationship(type = "PUBLISHED")
    private ArrayList<Book> book;

    public Editor() {
    }

    public String getName() {
        return name;
    }

    public ArrayList<Book> getBook() {
        return book;
    }
}
