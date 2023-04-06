package com.evgshul.taskperson.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


/**
 * Model object Person.
 */
@ToString
@Entity
@Table
@NoArgsConstructor
public class Person {

    /**
     * object Person unique identification.
     */
    @Id
    @Column(name = "person_id")
    @Getter
    @GeneratedValue
    private Long id;

    /**
     * object Person field first name.
     */
    @Column(name = "fullname")
    @Getter
    @Setter
    @Pattern(regexp = "(^[\\p{L}\\s'.-]+$)", message = "Not valid first name and last name")
    private String fullName;

    /**
     * object Person field gender.
     */
    @Column(name = "gender")
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * object Person field last name.
     */
    @Column(name = "birthdate")
    @Getter
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Past(message = "date of birth must be less than today")
    private LocalDate birthdate;

    /**
     * object Person field phone number.
     */
    @Column(name = "phoneNumber")
    @Getter
    @Setter
    @Pattern(regexp = "^\\d{12}$", message = "Phone number should consist from numbers only")
    private String phoneNumber;

    /**
     * object Person field email.
     */
    @Column(name = "email")
    @Getter
    @Setter
    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "wrong email value")
    private String email;

    /**
     * Object Person constructor.
     *
     * @param fullName    filed value
     * @param birthdate    filed value
     * @param phoneNumber filed value
     * @param email       filed value
     */
    public Person(final String fullName, final Gender gender,final LocalDate birthdate, final String phoneNumber, final String email) {
        this.fullName = fullName;
        this.birthdate = birthdate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}