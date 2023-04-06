package com.evgshul.taskperson.dto;

import com.evgshul.taskperson.model.Person;
import com.evgshul.taskperson.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonMapperImpl implements PersonMapper {

    private final PersonRepository personRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    public PersonMapperImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonDto findPersonFullName(final String fullName) {
        final Optional<Person> person = this.personRepository.findByFullName(fullName);
        return person.map(value -> this.mapper.map(value, PersonDto.class)).orElse(null);
    }

    @Override
    public PersonDto findPersonByBirthDay(LocalDate dob) {
        final Optional<Person> person =  this.personRepository.findByBirthdate(dob);
        return person.map(value -> this.mapper.map(value, PersonDto.class)).orElse(null);
    }

    @Override
    public Person personDtoToPerson(PersonDto personDto) {
        return this.mapper.map(personDto , Person.class);
    }

    @Override
    public List<PersonDto> getPersonsList() {
        final List<Person> personList = this.personRepository.findAll();
        return personList.stream().map(person -> mapper.map(person, PersonDto.class)).collect(Collectors.toList());
    }
}
