package com.sas.sasapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
                name = "email_id_unique",
                columnNames = "email_id"
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
    private String role;
    @Column(
            name = "email_id"
    )
    private String emailId;
    private String address;
}
