package com.example.neo4j02.nodes.testing;

import com.example.neo4j02.dto.EntityNode;
import com.example.neo4j02.nodes.Book;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;

@NodeEntity
public class Song extends EntityNode {

    @Property("title")
    private String title;
    public Song() {
    }
    public String getTitle() {
        return title;
    }
}
