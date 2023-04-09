package com.evgshul.taskperson.controller;

import com.evgshul.taskperson.dto.PersonDto;
import com.evgshul.taskperson.dto.PersonMapper;
import com.evgshul.taskperson.model.Person;
import com.evgshul.taskperson.service.PersonService;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common person controller.
 */
@RestController
@RequestMapping(path = "/api/v1/Persons")
public class PersonController {

    private final PersonService personService;

    private final PersonMapper personMapper;

    @Autowired
    public PersonController(PersonService personService, PersonMapper personMapper) {
        this.personService = personService;
        this.personMapper = personMapper;
    }

    /**
     * Create new Person Entity controller.
     *
     * @param personDto parameters to create new Person entity
     * @return response status
     * @throws ConstraintDeclarationException
     */
    @PostMapping(path = "/save")
    public Person savePerson(@Valid @RequestBody final PersonDto personDto) throws ConstraintDeclarationException {

        return personService.savePerson(personDto);
    }

    /**
     * Provide list of all saved persons.
     * @return List of all saved persons
     */
    @GetMapping
    public List<PersonDto> getAllUserAccounts() {
        return personService.getPersonsList();
    }

    /**
     * Find person by name controller.
     */
    @GetMapping("/name/{fullName}")
    public PersonDto findPersonByName(
            @PathVariable(value = "fullName") final String name) throws ConstraintDeclarationException {

        return personService.findPersonByName(name);
    }

    /**
     * Find person by birth date controller.
     */
    @GetMapping("/date")
    public PersonDto findPersonByBirthDate(@RequestParam(value = "birthdate")
                                           @DateTimeFormat(pattern = "dd/MM/yyyy")
                                               final LocalDate birthdate) throws ConstraintDeclarationException {
        return personService.findPersonByBirthday(birthdate);
    }

    /**
     * Delete person controller.
     *
     * @param id unique identificator.
     * @return response value
     */
    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> deletePerson(
            @PathVariable(value = "id") final Long id) throws ConstraintDeclarationException {
        personService.deletePerson(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Person was deleted successfully", Boolean.TRUE);
        return response;
    }

    /**
     * Update 'Person' entity controller.
     *
     * @param id        unique 'Person' identificator.
     * @param personDto 'Person' entity updated fields values'
     * @return response OK if entiry person updated successfully
     */
    @PutMapping("/updatePerson/{id}")
    public Person updatePerson(@PathVariable(value = "id") final Long id,
                                               @Valid @RequestBody final PersonDto personDto) throws ConstraintDeclarationException {

        return personService.updatePerson(personDto, id);
    }
}
