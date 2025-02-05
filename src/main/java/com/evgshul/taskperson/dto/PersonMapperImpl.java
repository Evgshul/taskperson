package com.evgshul.taskperson.dto;

import com.evgshul.taskperson.model.Person;
import com.evgshul.taskperson.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class PersonMapperImpl implements PersonMapper {

    private final PersonRepository personRepository;

    private final ModelMapper mapper;

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
