package com.sas.sasapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "faculty_assignment",schema = "public",uniqueConstraints = @UniqueConstraint(
        name = "faculty_assignment_unique",
        columnNames = {
                "user_id",
                "course_batch_id"
        }
))
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacultyAssignment {

    @Id
    @SequenceGenerator(
            name = "faculty_assignment_sequence",
            sequenceName = "faculty_assignment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "faculty_assignment_sequence"
    )
    @Column(name = "faculty_assignment_id")
    private Long facultyAssignmentId;


    @ManyToOne(
            cascade = CascadeType.REMOVE
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id"
    )
    private User user;


    @ManyToOne(
            cascade = CascadeType.REMOVE
    )
    @JoinColumn(
            name = "course_batch_id",
            referencedColumnName = "course_batch_id"
    )
    private CourseBatch courseBatch;
}