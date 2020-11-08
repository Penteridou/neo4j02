package com.example.neo4j02.controllers;

import com.example.neo4j02.nodes.Book;
import com.example.neo4j02.nodes.User;
import com.example.neo4j02.repositories.UserRepository;
import com.example.neo4j02.services.UserImportService;
import com.example.neo4j02.services.UserImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {


    private final UserImportService userService;
    @Autowired
    UserRepository userRepository;

    public UserController(UserImportService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/importusers")
    public String importUsers() {
        userService.importUsers();
        return "Users Imported";
    }

    @GetMapping("/getusers")
    public List<User> getUsers(){ return (List<User>) userRepository.findAll();}

    @GetMapping("/importborrowed")
    public String getBorrowed(){
        userRepository.importBorrowings();
        return "borrowings imported";
    }

    @GetMapping("/importratings")
    public String getRatings(){
        userRepository.importRatings();
        return "ratings imported";
    }



}
