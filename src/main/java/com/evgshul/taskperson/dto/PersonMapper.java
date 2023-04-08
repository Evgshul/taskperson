package com.evgshul.taskperson.dto;



import com.evgshul.taskperson.model.Person;

import java.util.List;

public interface PersonMapper {

    Person personDtoToPerson(PersonDto person);

    PersonDto personToPersonDto(Person person);

    List<PersonDto> getPersonsList();
}
