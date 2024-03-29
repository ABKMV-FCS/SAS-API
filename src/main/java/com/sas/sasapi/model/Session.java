package com.sas.sasapi.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Table(name = "session",schema = "public")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Session {
    @Id
    @SequenceGenerator(
            name = "session_sequence",
            sequenceName = "session_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "session_sequence"
    )
    @Column(name = "session_id")
    private Long sessionId;
    @ManyToOne(
    )
    @JoinColumn(
            name = "course_batch_id",
            referencedColumnName = "course_batch_id"
    )
    private CourseBatch courseBatch;
    @Column(name = "from_date_time")
    private Date fromDateTime;
    @Column(name = "to_date_time")
    private Date toDateTime;
}