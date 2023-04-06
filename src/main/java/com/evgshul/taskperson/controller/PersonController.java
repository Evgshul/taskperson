package com.evgshul.taskperson.controller;

import com.evgshul.taskperson.dto.PersonDto;
import com.evgshul.taskperson.dto.PersonMapperImpl;
import com.evgshul.taskperson.model.Person;
import com.evgshul.taskperson.service.PersonServiceImpl;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 *
 */
@RestController
@RequestMapping(path = "api/v1/Persons")
public class PersonController {

    private final PersonServiceImpl personService;

    @Autowired
    private PersonMapperImpl personMapper;

    @Autowired
    public PersonController(PersonServiceImpl personService) {
        this.personService = personService;
    }

    /**
     * Create new Person Entity controller.
     *
     * @param personDto parameters to create new Person entity
     * @return response status
     * @throws ConstraintDeclarationException
     */
    @PostMapping(path = "/save")
    public ResponseEntity<Object> savePerson(@Valid @RequestBody final PersonDto personDto) throws ConstraintDeclarationException {
        Person person = this.personMapper.personDtoToPerson(personDto);
        personService.savePerson(person);
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK);
        response.put("message", "New Person created successfully!!!");
        return ResponseEntity.ok(response);
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
    public ResponseEntity<Object> findPersonByName(@PathVariable(value = "fullName") final String name) {

        PersonDto person = personService.findPersonByName(name);
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK);
        response.put("message", "Person ".concat(person.getFullName()).concat(" is found!"));
        return ResponseEntity.ok(response);
    }

    /**
     * Find person by birth date controller.
     */
    @GetMapping("/date")
    public PersonDto findPersonByBirthDate(@RequestParam(value = "birthdate")
                                               @DateTimeFormat(pattern = "dd/MM/yyyy") final LocalDate birthdate) {
        return personService.findPersonByBirthday(birthdate);
    }

    /**
     * Delete person controller.
     * @param id unique identificator.
     * @return response value
     */
    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> deletePersone(@PathVariable(value = "id") final Long id) {
        personService.deletePerson(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Person was deleted successfully", Boolean.TRUE);
        return response;
    }

    /**
     * Update 'Person' entity controller.
     * @param id unique 'Person' identificator.
     * @param personDto 'Person' entity updated fields values'
     * @return response OK if entiry person updated successfully
     */
    @PutMapping("/updatePerson/{id}")
    public ResponseEntity<Object> updatePerson(@PathVariable(value = "id")final Long id,
                                                    @Valid @RequestBody final PersonDto personDto) {
        personService.updatePerson(personDto, id);
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK);
        response.put("message", "User Account with id " + id + " updated successful !!!");
        return ResponseEntity.ok(response);
    }
}
