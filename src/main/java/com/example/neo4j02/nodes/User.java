package com.example.neo4j02.nodes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
public class User {

    @Id
    private Integer usernum;

    @Property
    private String name;

    @Property
    private String surname;

    @Property
    private String birthday;

    //borrowing rich relationship
    // Needs knowledge about the attribute "title" in the relationship.
    // Applying JsonIgnoreProperties like this ignores properties of the attribute itself.
    @JsonIgnoreProperties("user")
    @Relationship(type = "RATING")
    private ArrayList<Borrowing> borrowedLog;

    //rating rich relationship
    // Needs knowledge about the attribute "title" in the relationship.
    // Applying JsonIgnoreProperties like this ignores properties of the attribute itself.
    @JsonIgnoreProperties("user")
    @Relationship(type = "RATING")
    private ArrayList<Rating> ratedLog;



    public User() {

    }

    public Integer getId() {
        return usernum;
    }

    public String getName() {
        return name;
    }

    public String getSurname() { return surname; }

    public String getBirthday() {
        return birthday;
    }
}
