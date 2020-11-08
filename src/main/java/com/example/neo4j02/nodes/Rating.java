package com.example.neo4j02.nodes;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "RATING")
public class Rating {

    @StartNode
    User user;

    @EndNode
    Book book;

    private double rate;

    public Rating() {
    }

    public double getRate() {
        return rate;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }
}
