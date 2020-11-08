package com.example.neo4j02.services;


import com.example.neo4j02.Neo4jSessionFactory;
import org.neo4j.ogm.session.Session;


public abstract class GenericService<T> implements Service<T> {


    private static final int DEPTH_LIST = 0; // 0, nodes without relationships
    private static final int DEPTH_ENTITY = 1; // 1, loads simple properties of the entity and its immediate relations
    protected Session session = Neo4jSessionFactory.getInstance().getNeo4jSession();



    abstract Class<T> getEntityType();


    @Override
    public void test() {

    }

    @Override
    public Iterable<T> findAll() {
        return session.loadAll(getEntityType(), DEPTH_LIST);
    }

    @Override
    public T find(Long id) {
        return session.load(getEntityType(), id, DEPTH_ENTITY);
    }

    @Override
    public void delete(Long id) {
        session.delete(session.load(getEntityType(), id));
    }

    //to fix --> id
    @Override
    public T createOrUpdate(T entity, Long id) {
        session.save(entity, DEPTH_ENTITY);
       return find(id); //entity.id

    }
}
