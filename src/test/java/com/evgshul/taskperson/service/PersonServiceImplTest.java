package com.evgshul.taskperson.service;


import com.evgshul.taskperson.dto.PersonDto;
import com.evgshul.taskperson.dto.PersonMapper;
import com.evgshul.taskperson.dto.PersonMapperImpl;
import com.evgshul.taskperson.model.Gender;
import com.evgshul.taskperson.model.Person;
import com.evgshul.taskperson.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class PersonServiceImplTest {

    @Autowired
    private PersonRepository personRepository;

    private PersonService underTest;

    @Mock
    private LoggService loggService;

    @Autowired
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        PersonMapper personMapper = new PersonMapperImpl(personRepository, mapper);
        underTest = new PersonServiceImpl(personRepository, personMapper, loggService);
    }

    @Test
    void savePersonTest_success() {
        PersonDto person = createValidPersonDto();

        underTest.savePerson(person);

        Optional<Person> checkPerson = personRepository.findByEmail(person.getEmail());
        assertTrue(checkPerson.isPresent(), "Person should exist");
        assertEquals("John Silver", checkPerson.get().getFullName(), "Invalid Person fullName value");
        assertEquals("MALE", checkPerson.get().getGender().name(), "Invalid Person gender value");
        assertEquals("14/01/2000", convertLocalDateToString(checkPerson.get().getBirthdate())
                , "Invalid Person birthdate value");
        assertEquals("037127090911", checkPerson.get().getPhoneNumber(), "Invalid Person phoneNumber value");
        assertEquals("test@mail.org", checkPerson.get().getEmail(), "Invalid Person email value");

    }

    @Test
    void savePersonTest_FullNameExist() {
        PersonDto person = createValidPersonDto();
        underTest.savePerson(person);

        PersonDto newPerson = new PersonDto();
        newPerson.setFullName("John Silver");
        newPerson.setGender(Gender.FEMALE);
        newPerson.setBirthdate(convertStringToLocalDate("18/09/1980"));
        newPerson.setEmail("test2@mail.com");
        newPerson.setPhoneNumber("097256987421");


        Exception exception = assertThrows(IllegalStateException.class,
                () -> underTest.savePerson(newPerson));
        assertTrue(exception.getMessage().contains("Person firstName and lastName " + newPerson.getFullName() + " taken"));
    }

    @Test
    void testSavePerson_EmailExist() {
        PersonDto person = createValidPersonDto();
        underTest.savePerson(person);

        PersonDto newPerson = new PersonDto();
        newPerson.setFullName("Yangya Satpath");
        newPerson.setGender(Gender.FEMALE);
        newPerson.setBirthdate(convertStringToLocalDate("18/09/1980"));
        newPerson.setEmail("test@mail.org");
        newPerson.setPhoneNumber("097256987421");


        Exception exception = assertThrows(IllegalStateException.class,
                () -> underTest.savePerson(newPerson));
        assertTrue(exception.getMessage().contains("This email " + newPerson.getEmail() + " is taken"));
    }

    @Test
    void testSavePerson_PhoneNumberExist() {
        PersonDto person = createValidPersonDto();
        underTest.savePerson(person);

        PersonDto newPerson = new PersonDto();
        newPerson.setFullName("Yangya Satpath");
        newPerson.setGender(Gender.FEMALE);
        newPerson.setBirthdate(convertStringToLocalDate("18/09/1980"));
        newPerson.setEmail("test@gmail.com");
        newPerson.setPhoneNumber("037127090911");


        Exception exception = assertThrows(IllegalStateException.class,
                () -> underTest.savePerson(newPerson));
        assertTrue(exception.getMessage().contains("This phone number " + newPerson.getPhoneNumber() + " is taken"));
    }

    @Test
    void testFindPirsonByName_success() {
        Person person = createValidPerson();
        personRepository.save(person);

        PersonDto checkPerson = underTest.findPersonByName("John Silver");
        assertNotNull(checkPerson, "Person should exist");
        assertEquals("John Silver", checkPerson.getFullName(), "Invalid Person fullName value");
        assertEquals("MALE", checkPerson.getGender().name(), "Invalid Person gender value");
        assertEquals("14/01/2000", convertLocalDateToString(checkPerson.getBirthdate())
                , "Invalid Person birthdate value");
        assertEquals("037127090911", checkPerson.getPhoneNumber(), "Invalid Person phoneNumber value");
        assertEquals("test@mail.org", checkPerson.getEmail(), "Invalid Person email value");

    }

    @Test
    void testFindPirsonByName_FullNameNotExist() {
        Person person = createValidPerson();
        personRepository.save(person);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> underTest.findPersonByName("Den Philips"));
        assertTrue(exception.getMessage().contains("Person Den Philips not find"));
    }

    @Test
    void testFindPirsonByBirthdate_success() {
        LocalDate testedBirthdate = convertStringToLocalDate("18/06/1999");
        Person person = createValidPerson();
        person.setBirthdate(testedBirthdate);

        personRepository.save(person);

        PersonDto checkPerson = underTest.findPersonByBirthday(testedBirthdate);
        assertNotNull(checkPerson, "Person should exist");
        assertEquals("John Silver", checkPerson.getFullName(), "Invalid Person fullName value");
        assertEquals("MALE", checkPerson.getGender().name(), "Invalid Person gender value");
        assertEquals("18/06/1999", convertLocalDateToString(checkPerson.getBirthdate())
                , "Invalid Person birthdate value");
        assertEquals("037127090911", checkPerson.getPhoneNumber(), "Invalid Person phoneNumber value");
        assertEquals("test@mail.org", checkPerson.getEmail(), "Invalid Person email value");
    }

    @Test
    void testFindPirsonByBirthdate_InvalidBirthdate() {
        LocalDate testedBirthdate = convertStringToLocalDate("18/06/1999");
        Person person = createValidPerson();
        person.setBirthdate(testedBirthdate);
        personRepository.save(person);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> underTest.findPersonByBirthday(convertStringToLocalDate("10/02/1982")));
        assertTrue(exception.getMessage().contains("Person with day of birth 10/02/1982 does not exist"));
    }

    @Test
    void testDeletePerson_success() {
        Person person1 = createValidPerson();
        personRepository.save(person1);

        Person person2 = new Person();
        person2.setFullName("Yangya Satpath");
        person2.setGender(Gender.FEMALE);
        person2.setBirthdate(convertStringToLocalDate("18/09/1980"));
        person2.setEmail("test2@mail.com");
        person2.setPhoneNumber("097256987421");
        personRepository.save(person2);

        final List<PersonDto> createdPersons = underTest.getPersonsList();
        assertEquals(2, createdPersons.size(), "Should be two Persons created");

        underTest.deletePerson(person2.getId());
        final List<PersonDto> finalPersonList = underTest.getPersonsList();
        assertEquals(1, finalPersonList.size(), "Should be only one Persons");

        assertTrue(finalPersonList.stream().noneMatch(p -> person2.getFullName().equals(p.getFullName())
                        && person2.getBirthdate().equals(p.getBirthdate()) && person2.getEmail().equals(p.getEmail())),
                "Person with name 'Yangya Satpath' should not exist");
    }

    @Test
    void testDeletePerson_DuchPersonIsNotExist() {
        Person person = createValidPerson();
        personRepository.save(person);
        long personId = 2L;

        Exception exception = assertThrows(IllegalStateException.class,
                () -> underTest.deletePerson(personId));
        assertTrue(exception.getMessage().contains("Person with ID " + personId + " does not exist"));
    }

    @Test
    void testUpdatePerson_success() {
        Person person = createValidPerson();
        personRepository.save(person);


        PersonDto personNewValus = new PersonDto();
        personNewValus.setFullName("Yangya Satpath");
        personNewValus.setGender(Gender.FEMALE);
        personNewValus.setBirthdate(convertStringToLocalDate("18/09/1980"));
        personNewValus.setEmail("test2@mail.com");
        personNewValus.setPhoneNumber("097256987421");

        final Long personId = person.getId();
        underTest.updatePerson(personNewValus, personId);
        Optional<Person> updatedPerson = personRepository.findById(personId);
        assertTrue(updatedPerson.isPresent(), "Person should exist");

        assertEquals("Yangya Satpath", updatedPerson.get().getFullName(), "Invalid Person fullName value");
        assertEquals("FEMALE", updatedPerson.get().getGender().name(), "Invalid Person gender value");
        assertEquals("18/09/1980", convertLocalDateToString(updatedPerson.get().getBirthdate())
                , "Invalid Person birthdate value");
        assertEquals("097256987421", updatedPerson.get().getPhoneNumber(), "Invalid Person phoneNumber value");
        assertEquals("test2@mail.com", updatedPerson.get().getEmail(), "Invalid Person email value");

    }

    @Test
    void testUpdatePerson_InvalidParametrsToUpdate_1() {
        Person person = createValidPerson();
        personRepository.save(person);

        Person person2 = new Person();
        person2.setFullName("Yangya Satpath");
        person2.setGender(Gender.FEMALE);
        person2.setBirthdate(convertStringToLocalDate("18/09/1980"));
        person2.setEmail("test2@mail.com");
        person2.setPhoneNumber("097256987421");
        personRepository.save(person2);


        PersonDto personNewValues = new PersonDto();
        personNewValues.setFullName("Yangya Satpath");
        personNewValues.setGender(Gender.FEMALE);
        personNewValues.setBirthdate(convertStringToLocalDate("18/09/1980"));
        personNewValues.setEmail("test2@mail.com");
        personNewValues.setPhoneNumber("097256987421");

        final Long personId = person2.getId();
        underTest.updatePerson(personNewValues, personId);
        Optional<Person> updatedPerson = personRepository.findById(personId);
        assertTrue(updatedPerson.isPresent(), "Person should exist");

        assertEquals(person2, updatedPerson.get(), "The 'Person2' entity should not be changed if the parameters for update equal existing params");
    }

    @Test
    void testUpdatePerson_InvalidParametrsToUpdate_2() {
        Person person = createValidPerson();
        personRepository.save(person);

        Person person2 = new Person();
        person2.setFullName("Yangya Satpath");
        person2.setGender(Gender.FEMALE);
        person2.setBirthdate(convertStringToLocalDate("18/09/1980"));
        person2.setEmail("test2@mail.com");
        person2.setPhoneNumber("097256987421");
        personRepository.save(person2);


        PersonDto personNewValues = new PersonDto();
        personNewValues.setFullName("John Silver");
        personNewValues.setGender(Gender.MALE);
        personNewValues.setBirthdate(null);
        personNewValues.setEmail("test@mail.org");
        personNewValues.setPhoneNumber("037127090911");

        final Long personId = person2.getId();
        underTest.updatePerson(personNewValues, personId);
        Optional<Person> updatedPerson = personRepository.findById(personId);
        assertTrue(updatedPerson.isPresent(), "Person should exist");

        assertEquals(person2, updatedPerson.get(), "The 'Person2' entity should not be changed if the parameters was taken");
    }

    private PersonDto createValidPersonDto() {
        return new PersonDto(
                "John Silver",
                convertStringToLocalDate("14/01/2000"),
                Gender.MALE,
                "037127090911",
                "test@mail.org");
    }

    private Person createValidPerson() {
        return new Person(
                "John Silver",
                Gender.MALE,
                convertStringToLocalDate("14/01/2000"),
                "037127090911",
                "test@mail.org");
    }

    private String convertLocalDateToString(final LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private LocalDate convertStringToLocalDate(final String dateValue) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateValue, formatter);
    }
}