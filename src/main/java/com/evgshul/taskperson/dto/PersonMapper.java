package com.evgshul.taskperson.dto;



import com.evgshul.taskperson.model.Person;

import java.time.LocalDate;
import java.util.List;

public interface PersonMapper {

    PersonDto findPersonFullName(String fullName);

    PersonDto findPersonByBirthDay(LocalDate dob);

    Person personDtoToPerson(PersonDto person);

    List<PersonDto> getPersonsList();
}
