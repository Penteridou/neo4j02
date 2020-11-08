package com.example.neo4j02.services;

import com.example.neo4j02.nodes.User;
import com.example.neo4j02.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserImportService {
    @Autowired
    UserRepository userRepository;

    public Collection<User> getAll() {
        return userRepository.getAllUsers();
    }

    //import users
    public  void importUsers(){
        userRepository.importUsers();
    }





}
