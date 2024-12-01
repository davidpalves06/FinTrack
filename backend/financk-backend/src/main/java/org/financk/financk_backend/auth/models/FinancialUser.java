package org.financk.financk_backend.auth.models;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.*;

@Entity
@Table(name = "FinancialUser")
@Getter
@Setter
@NoArgsConstructor
public class FinancialUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(unique = true, length = 100)
    private String email;

    @Column(unique = true, length = 100)
    private String username;

    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    public FinancialUser(String name, String email, String password, String username) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = username;
    }


}
