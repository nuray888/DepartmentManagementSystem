package com.example.basicauth.model;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    @OneToMany(fetch = FetchType.LAZY,cascade =  {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.DETACH},mappedBy = "department")
    Set<UserInfo> users=new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    UserInfo manager;
}
