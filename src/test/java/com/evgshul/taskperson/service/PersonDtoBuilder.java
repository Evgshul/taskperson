package com.evgshul.taskperson.service;

import com.evgshul.taskperson.dto.PersonDto;
import com.evgshul.taskperson.model.Gender;

import java.time.LocalDate;

public final class PersonDtoBuilder {

    private String fullName;

    private LocalDate birthdate;

    private Gender gender;

    private String phoneNumber;

    private String email;

    private PersonDtoBuilder() {
    }

    public static PersonDtoBuilder personDtoBuilder() {
        return new PersonDtoBuilder();
    }

    public PersonDtoBuilder withFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public PersonDtoBuilder withBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public PersonDtoBuilder withGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public PersonDtoBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public PersonDtoBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public PersonDto build() {
        return new PersonDto(fullName, birthdate, gender, phoneNumber, email);
    }
}
