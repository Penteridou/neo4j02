package com.example.neo4j02.services;

public interface Service<T> {



    void test();

    Iterable<T> findAll();

    T find(Long id);

    void delete(Long id);

    T createOrUpdate(T object, Long id);

}
