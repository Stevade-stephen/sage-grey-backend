package io.sagegrey.backend.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id", nullable = false)
    private Long id;
    @Size(min = 2, max=64, message = "first name must be at least two letter long")
    @Column(nullable = false)
    private String firstName;

    @Size(min = 2, max=64, message = "last name must be at least two letter long")
    @Column(nullable = false)
    private String lastName;

    @Size(min = 2, max=64, message = "username must be at least two letter long")
    @Column(nullable = false)
    private String username;

    @Size(min = 8, max = 16, message = "password must be at least 8 letters long")
    @Column(nullable = false)
    private String password;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE )
    private String email;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Account> userAccounts;

    public Set<String> getRoles() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        return roles;
    }
}
