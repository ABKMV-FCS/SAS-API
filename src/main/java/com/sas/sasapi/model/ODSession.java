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
        name = "od_session",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(
                name = "UniqueSessionAndODAssignment",
                columnNames = {"session_id", "od_assignment_id"}
        )
)
public class ODSession {
    @Id
    @SequenceGenerator(
            name="od_session_sequence",
            sequenceName = "od_session_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "od_session_sequence"
    )
    private Long odSessionId;
    @ManyToOne
    @JoinColumn(
            name = "session_id",
            referencedColumnName = "session_id"
    )
    private Session session;
    @ManyToOne
    @JoinColumn(
            name = "od_assignment_id",
            referencedColumnName = "odAssignmentId"
    )
    private ODAssignment odAssignment;
}
