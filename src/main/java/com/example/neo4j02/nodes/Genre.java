package com.example.neo4j02.nodes;

import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;

@NodeEntity
public class Genre {

    @Id @GeneratedValue
    private Long id;

    @Property
    private String genreName;

    @Relationship(type = "HAS_CATEGORY", direction = Relationship.INCOMING)
    private ArrayList<Book> books;

    public Genre() {
    }

    public String getGenreName() {
        return genreName;
    }

    public Long getId() {
        return id;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }
}
