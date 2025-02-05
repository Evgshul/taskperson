package com.evgshul.taskperson.service;

import com.evgshul.taskperson.dto.PersonDto;
import com.evgshul.taskperson.dto.PersonMapper;
import com.evgshul.taskperson.model.Gender;
import com.evgshul.taskperson.model.Logg;
import com.evgshul.taskperson.model.Person;
import com.evgshul.taskperson.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Class for managing on Persons entity
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    private final LoggService loggService;

    private static final String DNE = " does not exist";

    /**
     * Find all saved 'Persons'.
     *
     * @return Persons list
     */
    @Override
    public List<PersonDto> getPersonsList() {
        return personMapper.getPersonsList();
    }

    /**
     * Create a new entity Person.
     *
     * @param personDto parameters to save new entity PersonDto
     */
    @Override
    public Person savePerson(PersonDto personDto) {
        Person person = this.personMapper.personDtoToPerson(personDto);
        if (isFullNameExist(person.getFullName())) {
            throw new IllegalStateException("Person firstName and lastName " + person.getFullName() + " taken");
        }
        if (isEmailExist(person.getEmail())) {
            throw new IllegalStateException("This email " + person.getEmail() + " is taken");
        }
        if (isPhoneNumberExist(person.getPhoneNumber())) {
            throw new IllegalStateException("This phone number " + person.getPhoneNumber() + " is taken");
        }

        final String loggMessage = String.format("New Person %s was created", person.getFullName());
        log.debug(loggMessage);
        loggService.saveLogg(saveLogToDatabase(loggMessage));
        return personRepository.save(person);
    }

    /**
     * Find existing entity Person by name.
     *
     * @param fullName incoming parameter 'fullName to find Person
     * @return person to PersonDto
     */
    @Override
    public PersonDto findPersonByName(String fullName) {
        return personRepository.findByFullName(fullName)
                .map(personMapper::personToPersonDto)
                .orElseThrow(() -> {
                    log.debug("Person : {} not found", fullName);
                    return new IllegalStateException("Person " + fullName + " not find");
                });
    }

    /**
     * Find existing entity Person by birthdate.
     *
     * @param dob incoming parameter 'birthdate' to find Person
     * @return person to PersonDto
     */
    @Override
    public PersonDto findPersonByBirthday(LocalDate dob) {
        return this.personRepository.findByBirthdate(dob)
                .map(personMapper::personToPersonDto)
                .orElseThrow(() -> {
                    String birthday = dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    log.debug("Person with date of birth:{} {}", DNE, birthday);
                    return new IllegalStateException("Person with date of birth " + birthday + DNE);
                });
    }

    /**
     * Update person data method.
     *
     * @param person personDto incoming field to update Person
     * @param id     unique Person identification
     */
    @Override
    @Transactional
    public Person updatePerson(PersonDto person, Long id) {
        Person existPerson = personRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Person with id " + id + DNE
                ));

        final String fullName = person.getFullName();
        if (!Objects.equals(existPerson.getFullName(), fullName) && !isFullNameExist(fullName)) {
            existPerson.setFullName(fullName);
            final String loggMessage = String.format("Person id: %s was changed fullName from %s to %s ",
                    id, existPerson.getFullName(), fullName);
            log.info(loggMessage);
            loggService.saveLogg(saveLogToDatabase(loggMessage));
        }

        final LocalDate bod = person.getBirthdate();
        if (bod != null && !Objects.equals(existPerson.getBirthdate(), bod)) {
            existPerson.setBirthdate(bod);
            final String loggMessage = String.format("Person id: %s was changed birthdate from %s to %s ",
                    id, existPerson.getBirthdate(), bod);
            log.info(loggMessage);
            loggService.saveLogg(saveLogToDatabase(loggMessage));
        }

        final Gender gender = person.getGender();
        if (!Objects.equals(existPerson.getGender(), gender)) {
            existPerson.setGender(gender);
            final String loggMessage = String.format("Person id: %s was changed Gender from %s to %s ",
                    id, existPerson.getGender(), gender.toString());
            log.info(loggMessage);
            loggService.saveLogg(saveLogToDatabase(loggMessage));
        }

        final String phoneNumber = person.getPhoneNumber();
        if (!Objects.equals(existPerson.getPhoneNumber(), phoneNumber) && !isPhoneNumberExist(phoneNumber)) {
            existPerson.setPhoneNumber(phoneNumber);
            final String loggMessage = String.format("Person id: %s was changed phoneNumber from %s to %s ",
                    id, existPerson.getPhoneNumber(), phoneNumber);
            log.info(loggMessage);
            loggService.saveLogg(saveLogToDatabase(loggMessage));
        }

        final String email = person.getEmail();
        if (!Objects.equals(existPerson.getEmail(), email) && !isEmailExist(email)) {
            existPerson.setEmail(email);
            final String loggMessage = String.format("Person id: %s was changed email from %s to %s ",
                    id, existPerson.getEmail(), email);
            log.info(loggMessage);
            loggService.saveLogg(saveLogToDatabase(loggMessage));
        }
        return existPerson;
    }

    /**
     * Remove existing Person entity from a database.
     *
     * @param personId unique Person identification
     */
    @Override
    public void deletePerson(Long personId) {
        final boolean existPerson = personRepository.existsById(personId);
        if (!existPerson) {
            throw new IllegalStateException("Person with ID " + personId + DNE);
        }
        personRepository.deleteById(personId);
    }

    /**
     * Check if fullName exist in database.
     *
     * @param fullName checked parameter fullName
     * @return bool true ore false
     */
    private boolean isFullNameExist(final String fullName) {
        return personRepository.findByFullName(fullName).isPresent();
    }

    /**
     * Check if email exists in a database.
     *
     * @param email checked parameter email
     * @return bool true ore false
     */
    private boolean isEmailExist(final String email) {
        return personRepository.findByEmail(email).isPresent();
    }

    /**
     * Check if phoneNumber exist in a database.
     *
     * @param phoneNumber checked parameter phoneNumber
     * @return bool true ore false
     */
    private boolean isPhoneNumberExist(final String phoneNumber) {
        return personRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

    /**
     * Preparing new log parameters to save in a database.
     *
     * @param message logg message
     * @return new log object
     */
    private Logg saveLogToDatabase(final String message) {
        return new Logg(new Date(), log.getName(), message);
    }
}
