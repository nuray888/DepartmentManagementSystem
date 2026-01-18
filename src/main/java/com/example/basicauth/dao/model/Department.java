package com.example.basicauth.dao.model;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String name;
    String description;
    @OneToMany(fetch = FetchType.LAZY,cascade =  {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.DETACH},mappedBy = "department")
    Set<UserInfo> users=new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    UserInfo manager;


}
