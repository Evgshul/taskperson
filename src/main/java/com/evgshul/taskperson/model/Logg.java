package com.evgshul.taskperson.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Model object Logg to store logging parameters.
 */
@ToString
@Entity
@Table
@NoArgsConstructor
public class Logg {

    @Id
    @Column(name = "logg_id")
    @Getter
    @GeneratedValue
    private Long id;

    @Column
    @Getter@Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column
    @Getter@Setter
    private String className;

    @Column
    @Getter@Setter
    private String message;

    public Logg(Date date, String className, String message) {
        this.timestamp = date;
        this.className = className;
        this.message = message;
    }
}
