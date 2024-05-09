package com.evgshul.taskperson.dto;

import com.evgshul.taskperson.model.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    @NotNull
    @Pattern(regexp = "(^[\\p{L}\\s'.-]+$)", message = "Not valid first name and last name")
    @Schema(name = "fullname", example = "Name Surname", requiredMode = REQUIRED)
    private String fullName;

    @NotNull
    @JsonFormat(pattern="dd/MM/yyyy")
    @Past(message = "date of birth must be less than today")
    @Schema(name = "birthdate", type = "string", pattern = "dd/MM/yyyy", example = "12/02/1998", requiredMode = REQUIRED)
    private LocalDate birthdate;

    private Gender gender;

    @NotNull
    @Pattern(regexp = "^[0-9]*$", message = "Phone number should use only digits")
    @Schema(name = "phonenumber", example = "00371256412", requiredMode = REQUIRED)
    private String phoneNumber;

    @NotNull
    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "wrong email value")
    @Schema(name = "email", example = "nickname@mail.com", requiredMode = REQUIRED)
    private String email;
}
