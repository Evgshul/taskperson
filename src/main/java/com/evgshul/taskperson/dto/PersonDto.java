package com.evgshul.taskperson.dto;

import com.evgshul.taskperson.model.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    @Pattern(regexp = "(^[\\p{L}\\s'.-]+$)", message = "Not valid first name and last name")
    private String fullName;

    @JsonFormat(pattern="dd/MM/yyyy")
    @Past(message = "date of birth must be less than today")
    private LocalDate birthdate;

    private Gender gender;

    @Pattern(regexp = "^\\d{12}$", message = "Phone number should use no more then 12 digits")
    private String phoneNumber;

    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "wrong email value")
    private String email;
}
