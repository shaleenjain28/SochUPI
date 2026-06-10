package com.sochupi.app.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
// No @Data on JPA entities — it generates equals/hashCode/toString over ALL fields.
// Fine for simple classes, but risky once @ManyToOne/@OneToMany exist (lazy-load traps).
// @Getter/@Setter: safe. @ToString + @EqualsAndHashCode: only include fields we mark below.
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)            // logs/debugger only print @ToString.Include fields
@EqualsAndHashCode(onlyExplicitlyIncluded = true)  // equals/hash only use @EqualsAndHashCode.Include fields
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include   // entity identity = id (JPA best practice)
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