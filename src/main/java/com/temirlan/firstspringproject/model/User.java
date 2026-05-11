package com.temirlan.firstspringproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;  // add this import

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor   // add this line
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;
}