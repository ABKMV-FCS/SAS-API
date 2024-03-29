package com.sas.sasapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "od_event",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(
            name = "event_name_unique",
            columnNames = "event_name"
    )
)
public class ODEvent {
    @Id
    @SequenceGenerator(
            name="odevent_sequence",
            sequenceName = "odevent_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "odevent_sequence"
    )
    @Column(
            name = "od_event_id"
    )
    private Long odEventId;
    @Column(
            name = "from_date"
    )
    private Date fromDate;
    @Column(
            name = "to_date"
    )
    private Date toDate;
    @Column(
            name = "event_name"
    )
    private String eventName;
    private String description;
    @ManyToOne(
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id"
    )
    private User user;
}
