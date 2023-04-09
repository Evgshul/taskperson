package com.evgshul.taskperson.service;



import com.evgshul.taskperson.dto.PersonDto;
import com.evgshul.taskperson.model.Person;

import java.time.LocalDate;
import java.util.List;

public interface PersonService {

    List<PersonDto> getPersonsList();

    Person savePerson(PersonDto personDto);

    PersonDto findPersonByName(String fullName);

    PersonDto findPersonByBirthday(LocalDate bod);

    Person updatePerson(PersonDto person, Long id);

    void deletePerson(Long personId);

}
