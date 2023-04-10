package com.evgshul.taskperson.dto;

import com.evgshul.taskperson.model.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotNull
    @Pattern(regexp = "(^[\\p{L}\\s'.-]+$)", message = "Not valid first name and last name")
    private String fullName;

    @NotNull
    @JsonFormat(pattern="dd/MM/yyyy")
    @Past(message = "date of birth must be less than today")
    private LocalDate birthdate;

    private Gender gender;

    @NotNull
    @Pattern(regexp = "^[0-9]*$", message = "Phone number should use only digits")
    private String phoneNumber;

    @NotNull
    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "wrong email value")
    private String email;
}
