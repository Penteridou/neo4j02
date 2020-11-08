package com.example.neo4j02.nodes;

import com.example.neo4j02.dto.EntityNode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;

@NodeEntity
public class Book extends EntityNode {


   // @NotNull
    @Property("title")
    private String title;
    @Property
    private Integer year;

    @Property
    private String price;

    @JsonIgnoreProperties("book")
    @Relationship (type = "WROTE", direction = Relationship.INCOMING)
    private Author author;

    @JsonIgnoreProperties("book")
    @Relationship (type = "PUBLISHED", direction = Relationship.INCOMING)
    private Editor editor;


    @JsonIgnoreProperties("books")
    @Relationship(type = "HAS_CATEGORY")
    private  ArrayList<Genre> genre;


    public Book() {
    }


   // public Long getId() { return id;}

    public ArrayList<Genre>  getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }


    public Integer getYear() {
        return year;
    }

    public String getPrice() {return price; }

    public Editor getEditor() {
        return editor;
    }


    public Author getAuthor() {return author;}

}