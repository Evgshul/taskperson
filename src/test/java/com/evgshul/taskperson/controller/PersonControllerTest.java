package com.evgshul.taskperson.controller;

import com.evgshul.taskperson.dto.PersonDto;
import com.evgshul.taskperson.dto.PersonMapper;
import com.evgshul.taskperson.model.Gender;
import com.evgshul.taskperson.model.Person;
import com.evgshul.taskperson.repository.PersonRepository;
import com.evgshul.taskperson.service.LoggService;
import com.evgshul.taskperson.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private PersonRepository personRepository;

    @MockBean
    private PersonMapper personMapper;

    @MockBean
    private LoggService loggService;

    @Test
    void testSavePersonController() throws Exception {

        Person person = createValidPerson();
        given(personService.savePerson(any(PersonDto.class))).willReturn(person);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/Persons/save")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON);


        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String responseBody = "\"fullName\":\"John Silver\",\"gender\":\"MALE\",\"birthdate\":\"14/01/2000\",\"phoneNumber\":\"037127090911\",\"email\":\"test@mail.org\"";
        assertTrue(response.getContentAsString().contains(responseBody));
    }

    @Test
    void testGetAllUserAccountsController() throws Exception {
        PersonDto personDto = createPersonDto();
        PersonDto personDto1 = new PersonDto();
        personDto1.setFullName("Yangya Satpath");
        personDto1.setGender(Gender.FEMALE);
        personDto1.setBirthdate(convertStringToLocalDate("18/09/1980"));
        personDto1.setEmail("test2@mail.com");
        personDto1.setPhoneNumber("097256987421");
        List<PersonDto> personsList = new ArrayList<>();
        personsList.add(personDto);
        personsList.add(personDto1);

        given(personService.getPersonsList()).willReturn(personsList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/Persons")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fullName").value(personsList.get(0).getFullName()))
                .andExpect(jsonPath("$[1].fullName").value(personsList.get(1).getFullName()))
                .andExpect(jsonPath("$[0].gender").value(personsList.get(0).getGender().name()))
                .andExpect(jsonPath("$[1].gender").value(personsList.get(1).getGender().name()))
                .andExpect(jsonPath("$[0].phoneNumber").value(personsList.get(0).getPhoneNumber()))
                .andExpect(jsonPath("$[1].phoneNumber").value(personsList.get(1).getPhoneNumber()))
                .andExpect(jsonPath("$[0].email").value(personsList.get(0).getEmail()))
                .andExpect(jsonPath("$[1].email").value(personsList.get(1).getEmail()));
    }

    @Test
    void testFindPersonByNameController() throws Exception {
        PersonDto personDto = createPersonDto();
        String fullName = "John Silver";

        given(personService.findPersonByName(any(String.class))).willReturn(personDto);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/Persons/name/{fullName}", fullName)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(personDto.getFullName()))
                .andExpect(jsonPath("$.gender").value(personDto.getGender().name()))
                .andExpect(jsonPath("$.birthdate").value(convertLocalDateToString(personDto.getBirthdate())))
                .andExpect(jsonPath("$.phoneNumber").value(personDto.getPhoneNumber()))
                .andExpect(jsonPath("$.email").value(personDto.getEmail()));
    }

    @Test
    void testFindPersonByBirthDateController() throws Exception {
        PersonDto personDto = createPersonDto();
        String birthDate = "14/01/2000";

        given(personService.findPersonByBirthday(any(LocalDate.class))).willReturn(personDto);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/Persons/date")
                .queryParam("birthdate", birthDate)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(personDto.getFullName()))
                .andExpect(jsonPath("$.gender").value(personDto.getGender().name()))
                .andExpect(jsonPath("$.birthdate").value(convertLocalDateToString(personDto.getBirthdate())))
                .andExpect(jsonPath("$.phoneNumber").value(personDto.getPhoneNumber()))
                .andExpect(jsonPath("$.email").value(personDto.getEmail()));
    }

    @Test
    void testDeletePersoneController() throws Exception {
        Long id = 1L;

        doNothing().when(personService).deletePerson(id);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/Persons/delete/{id}", id)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        String responseBody = "Person was deleted successfully";
        assertTrue(response.getContentAsString().contains(responseBody));
    }

    @Test
    void testUpdatePersonController() throws Exception {
        Person updatedPerson = new Person();
        updatedPerson.setFullName("Yangya Satpath");
        updatedPerson.setGender(Gender.FEMALE);
        updatedPerson.setBirthdate(convertStringToLocalDate("18/09/1980"));
        updatedPerson.setEmail("test2@mail.com");
        updatedPerson.setPhoneNumber("097256987421");

        long id = 1L;
        given(personService.updatePerson(any(PersonDto.class), anyLong())).willReturn(updatedPerson);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/Persons/updatePerson/{id}", Long.toString(id))
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson))
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Yangya Satpath"))
                .andExpect(jsonPath("$.gender").value("FEMALE"))
                .andExpect(jsonPath("$.birthdate").value("18/09/1980"))
                .andExpect(jsonPath("$.phoneNumber").value("097256987421"))
                .andExpect(jsonPath("$.email").value("test2@mail.com"));


    }

    private Person createValidPerson() {
        return new Person(
                "John Silver",
                Gender.MALE,
                convertStringToLocalDate("14/01/2000"),
                "037127090911",
                "test@mail.org");
    }

    private PersonDto createPersonDto() {
        return new PersonDto(
                "John Silver",
                convertStringToLocalDate("14/01/2000"),
                Gender.MALE,
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