package com.evgshul.taskperson.service;

import com.evgshul.taskperson.dto.PersonDto;
import com.evgshul.taskperson.dto.PersonMapper;
import com.evgshul.taskperson.model.Gender;
import com.evgshul.taskperson.model.Person;
import com.evgshul.taskperson.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Class for managing on Persons entity
 */
@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<PersonDto> getPersonsList() {
        return personMapper.getPersonsList();
    }

    @Override
    public void savePerson(Person person) {
        if (isFullNameExist(person.getFullName())) {
            log.debug("Person with fulName {} is taken", person.getFullName());
            throw new IllegalStateException("Person firstName and lastName " + person.getFullName() + " taken");
        }
        if (isEmailExist(person.getEmail())) {
            log.debug("This email {} is taken", person.getEmail());
            throw new IllegalStateException("This email " + person.getEmail() + "is taken");
        }
        if (isPhoneNumberExist(person.getPhoneNumber())) {
            log.debug("This phone number {} is taken", person.getPhoneNumber());
            throw new IllegalStateException("This phone number " + person.getPhoneNumber() + "is taken");
        }
        personRepository.save(person);
    }

    @Override
    public PersonDto findPersonByName(String fullName) {
        PersonDto person = personMapper.findPersonFullName(fullName);
        if (person == null) {
            log.debug("Person : {} not found", fullName);
            throw new IllegalStateException("Person " + fullName + " not find");
        } else {
            return person;
        }
    }

    @Override
    public PersonDto findPersonByBirthday(LocalDate bod) {
        PersonDto person = personMapper.findPersonByBirthDay(bod);
        if (person == null) {
            final String birthday = bod.toString();
            log.debug("Person with day of birth : {} does not exist", birthday);
            throw new IllegalStateException("Person with day of birth " + birthday + "does not exist");
        } else {
            return person;
        }
    }

    @Override
    @Transactional
    public void updatePerson(PersonDto person, Long id) {
        Person existPerson = personRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Person with id " + id + " does not exist"
                ));

        final String fullName = person.getFullName();
        if (isFullNameExist(fullName) && !Objects.equals(existPerson.getFullName(), fullName)) {
            existPerson.setFullName(fullName);
        }

        final LocalDate bod = person.getBirthdate();
        if(bod != null && !Objects.equals(existPerson.getBirthdate(), bod)) {
            existPerson.setBirthdate(bod);
        }

        final Gender gender = person.getGender();
        if (!Objects.equals(existPerson.getGender(), gender)) {
            existPerson.setGender(gender);
        }

        final String phoneNumber = person.getPhoneNumber();
        if(!isPhoneNumberExist(phoneNumber) && !Objects.equals(existPerson.getPhoneNumber(), phoneNumber)) {
            existPerson.setPhoneNumber(phoneNumber);
        }

        final String email = person.getEmail();
        if(!isEmailExist(email) && !Objects.equals(existPerson.getEmail(), email)) {
            existPerson.setPhoneNumber(email);
        }
    }

    @Override
    public void deletePerson(Long personId) {
        final boolean existPerson = personRepository.existsById(personId);
        if (!existPerson) {
            throw new IllegalStateException("Person with ID " + personId + " does not exist");
        }
        personRepository.deleteById(personId);
    }

    private boolean isFullNameExist(final String fullName) {
        return personRepository.findByFullName(fullName).isPresent();
    }

    private boolean isEmailExist(final String email) {
        return personRepository.findByEmail(email).isPresent();
    }

    private boolean isPhoneNumberExist(final String phoneNumber) {
        return personRepository.findByPhoneNumber(phoneNumber).isPresent();
    }
}
