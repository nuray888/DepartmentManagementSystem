package com.example.basicauth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String surname;
    @Column(unique = true)
    String email;
    @JsonIgnore
    String password;
    String address;
    Double salary;
    Boolean isActive;
    Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    UserRole role;

    @CreatedDate
//    @Column(nullable = false,updatable = false)
    LocalDateTime createdDate;
    @LastModifiedDate
    LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    Department department;

    //token
    String resetToken;
    LocalDateTime resetTokenExpiry;
    //oauth2
    String googleId;
}