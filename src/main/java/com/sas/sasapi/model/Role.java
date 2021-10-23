package com.sas.sasapi.model;

import lombok.*;

import javax.persistence.*;

@Table(name = "roles")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Getter
    @Setter
    private ERole name;

}