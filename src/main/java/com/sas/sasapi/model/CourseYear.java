package com.sas.sasapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "course_year",schema = "public",uniqueConstraints = @UniqueConstraint(
        name = "course_year_unique",
        columnNames = {
                "course_id",
                "semester",
                "year"
        }
))
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseYear {
    @Id
    @SequenceGenerator(
            name = "course_year_sequence",
            sequenceName = "course_year_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_year_sequence"
    )
    @Column(name = "course_year_id")
    private Long courseYearId;
    @ManyToOne(
    )
    @JoinColumn(
            name = "course_id",
            referencedColumnName = "course_id"
    )
    private Course course;
    private Long semester;
    private Long year;

}