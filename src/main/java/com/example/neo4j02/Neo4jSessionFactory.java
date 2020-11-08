package com.example.neo4j02;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class Neo4jSessionFactory {

    Configuration configuration = new Configuration.Builder()
            .uri("bolt://localhost")
            .credentials("neo4j", "pass")
            .build();

    SessionFactory sessionFactory;

    private static Neo4jSessionFactory factory = new Neo4jSessionFactory();

    public static Neo4jSessionFactory getInstance() {
        return factory;
    }

    // prevent external instantiation
    private Neo4jSessionFactory() {
        sessionFactory = new SessionFactory(configuration, "com.example.neo4j02.nodes");
    }

    public Session getNeo4jSession() {
        return sessionFactory.openSession();
    }
}
