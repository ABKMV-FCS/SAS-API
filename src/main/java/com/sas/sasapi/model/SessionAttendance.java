package com.sas.sasapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "session_attendance",schema = "public",uniqueConstraints = @UniqueConstraint(
        name = "session_attendance_unique",
        columnNames = {
                "session_id",
                "user_id"
        }
))
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionAttendance {

    @Id
    @SequenceGenerator(
            name = "session_attendance_sequence",
            sequenceName = "session_attendance_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "session_attendance_sequence"
    )
    @Column(name = "session_attendance_id")
    private Long sessionAttendanceId;
    @ManyToOne(
    )
    @JoinColumn(
            name = "session_id",
            referencedColumnName = "session_id"
    )
    private Session session;

    @ManyToOne(
//            cascade = CascadeType.REMOVE
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id"
    )
    private User user;
}