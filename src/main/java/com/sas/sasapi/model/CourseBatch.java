package com.sas.sasapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "course_batch",schema = "public",uniqueConstraints = @UniqueConstraint(
        name = "course_batch_unique",
        columnNames = {
                "course_year_id",
                "batch"
        }
))
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseBatch {
    @Id
    @SequenceGenerator(
            name = "course_batch_sequence",
            sequenceName = "course_batch_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_batch_sequence"
    )
    @Column(name = "course_batch_id")
    private Long courseBatchId;
    @ManyToOne()
    @JoinColumn(
            name = "course_year_id",
            referencedColumnName = "course_year_id"
    )
    private CourseYear courseYear;
    private String batch;
}