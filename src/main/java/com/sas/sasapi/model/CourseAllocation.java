package com.sas.sasapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "course_allocation",schema = "public",uniqueConstraints = @UniqueConstraint(
        name = "course_allocation_unique",
        columnNames = {
                "course_batch_id",
                "user_id"
        }
))
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseAllocation {

    @Id
    @SequenceGenerator(
            name = "course_allocation_sequence",
            sequenceName = "course_allocation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_allocation_sequence"
    )
    @Column(name = "course_allocation_id")
    private Long courseAllocationId;

    @ManyToOne()
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id"
    )
    private User user;

    @ManyToOne(
    )
    @JoinColumn(
            name = "course_batch_id",
            referencedColumnName = "course_batch_id"
    )
    private CourseBatch courseBatch;


    public CourseAllocation(CourseBatch courseBatchEvent, User user) {
        this.courseBatch = courseBatchEvent;
        this.user = user;
    }
}