package com.evgshul.taskperson.dto;

import com.evgshul.taskperson.model.Person;
import com.evgshul.taskperson.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonMapperImpl implements PersonMapper {

    private final PersonRepository personRepository;

    private final ModelMapper mapper;

    @Autowired
    public PersonMapperImpl(PersonRepository personRepository, ModelMapper mapper) {
        this.personRepository = personRepository;
        this.mapper = mapper;
    }

    @Override
    public Person personDtoToPerson(PersonDto personDto) {
        return this.mapper.map(personDto, Person.class);
    }

    @Override
    public PersonDto personToPersonDto(Person person) {
        return this.mapper.map(person, PersonDto.class);
    }

    @Override
    public List<PersonDto> getPersonsList() {
        final List<Person> personList = this.personRepository.findAll();
        return personList.stream().map(person -> mapper.map(person, PersonDto.class)).collect(Collectors.toList());
    }
}
