package com.sochupi.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity                          // Tells JPA: "This class maps to a database table"
@Table(name = "users")           // Table name is "users", not "user" (reserved word in MySQL)
@Data                            // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor               // Lombok: generates empty constructor (JPA requires this)
@AllArgsConstructor              // Lombok: generates constructor with all fields (useful for testing)
public class User {

    @Id                                                     // This field is the PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // Auto-increment: MySQL handles ID generation
    private Long id;

    @Column(nullable = false, length = 100)                 // NOT NULL, max 100 characters
    private String name;

    @Column(nullable = false, unique = true, length = 100)  // NOT NULL + UNIQUE constraint
    private String email;

    @Column(name = "password_hash", nullable = false)       // Maps to column "password_hash" in DB
    private String passwordHash;                            // We NEVER store raw passwords

    @CreationTimestamp                                      // Hibernate auto-sets this when row is INSERT-ed
    @Column(name = "created_at", updatable = false)         // Once set, never changes (updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp                                        // Hibernate auto-sets this on every UPDATE
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}