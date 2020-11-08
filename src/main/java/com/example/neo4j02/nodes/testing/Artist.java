package com.example.neo4j02.nodes.testing;

import com.example.neo4j02.dto.EntityNode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public class Artist extends EntityNode {

    @Property("name")
    private String name;
    public Artist(){
    }
    public String getName() {
        return name;
    }
}
