package com.sas.sasapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "od_assignment",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(
                name = "UniqueEventAndUser",
                columnNames = {"od_event_id", "user_id"}
        )
)
public class ODAssignment {
    @Id
    @SequenceGenerator(
            name="od_assignment_sequence",
            sequenceName = "od_assignment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "od_assignment_sequence"
    )
    private Long odAssignmentId;
    @ManyToOne(
            cascade = CascadeType.REMOVE
    )
    @JoinColumn(
            name = "od_event_id",
            referencedColumnName = "od_event_id"
    )
    private ODEvent odEvent;
    @ManyToOne(
            cascade = CascadeType.REMOVE
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id"
    )
    private User user;
}
