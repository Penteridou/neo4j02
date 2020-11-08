package com.example.neo4j02.nodes;

import com.example.neo4j02.dto.EntityNode;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;

@NodeEntity
public class Author extends EntityNode {


    @Property("writer")
    private String name;

    @Relationship(type = "WROTE")
    private ArrayList<Book> book;

    public Author() {
    }


    public String getName() {
        return name;
    }
    public ArrayList<Book> getBook() {
        return book;
    }


}
