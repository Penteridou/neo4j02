package com.example.neo4j02.dto;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.id.UuidStrategy;

@NodeEntity
public class EntityNode {

        @Id
        @GeneratedValue
        private Long id;

        public Long getId() {
            return id;
        }

}
