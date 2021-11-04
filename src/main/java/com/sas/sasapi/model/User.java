package com.sas.sasapi.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "user",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(
                name = "username_unique",
                columnNames = "username"
                ),
                @UniqueConstraint(
                name = "email_unique",
                columnNames = "email"
                )
        }
)
public class User {
    @Id
    @SequenceGenerator(
            name="user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(
            name = "user_id"
    )
    private Long userId;
    private String name;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Getter
    @Setter
    private Set<Role> roles = new HashSet<>();
    @Column(
            name = "email"
    )
    private String email;
    private String address;
    private String coordinates;

    public User(String name, String username, String password, String email, String address, String coordinates) {
        this.name=name;
        this.username=username;
        this.password=password;
        this.email=email;
        this.address=address;
        this.coordinates=coordinates;
    }
}
