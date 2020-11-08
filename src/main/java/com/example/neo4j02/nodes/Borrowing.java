package com.example.neo4j02.nodes;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import java.util.Date;

@RelationshipEntity(type = "BORROWING")
public class Borrowing {

    @StartNode
    User user;

    @EndNode
    Book book;

    @DateLong
    private Date date;

    public Borrowing() {
    }

    public Date getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }
}
