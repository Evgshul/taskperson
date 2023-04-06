package com.evgshul.taskperson.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;

@ToString
@Entity
@Table
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
    private String level;

    @Column
    @Getter@Setter
    private String message;

    public Logg(Date date, String className, String level, String message) {
        this.timestamp = date;
        this.className = className;
        this.level = level;
        this.message = message;
    }
}
