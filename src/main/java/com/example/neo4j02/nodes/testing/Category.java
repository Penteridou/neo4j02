package com.example.neo4j02.nodes.testing;

import com.example.neo4j02.dto.EntityNode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import javax.validation.constraints.NotNull;

@NodeEntity
public class Category extends EntityNode {
    @Property("category")
    private String category;
    public Category() {
    }
    public String getCategory() {
        return category;
    }
}
