package com.sochupi.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * User Entity - Maps to the 'users' table in MySQL.
 */
@Entity
@Table(name = "users") // 'user' is a reserved keyword in some databases, so we use 'users'
@Data // Lombok: Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: Required by JPA to instantiate the entity via reflection
@AllArgsConstructor // Lombok: Generates a constructor with all arguments
public class User {

    @Id // Marks this field as the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Relies on MySQL's auto-increment feature
    private Long id;

    @Column(nullable = false, length = 100) // Maps to a NOT NULL column with a max length of 100
    private String name;

    @Column(nullable = false, unique = true, length = 100) // Ensures no two users can have the same email
    private String email;

    @Column(name = "password_hash", nullable = false) // Custom column name in DB for clarity, though camelCase would map to password_hash by default
    private String passwordHash;

    @CreationTimestamp // Hibernate handles setting this when the record is created
    @Column(name = "created_at", updatable = false) // Once created, this should never be modified
    private LocalDateTime createdAt;

    @UpdateTimestamp // Hibernate handles updating this every time the record is modified
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
