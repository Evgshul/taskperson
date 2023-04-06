package com.evgshul.taskperson.repository;

import com.evgshul.taskperson.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Person repository
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByFullName(String fullName);

    Optional<Person> findByEmail(String email);

    Optional<Person> findByPhoneNumber(String phoneNumber);

    /**
     */
    Optional<Person> findByBirthdate(LocalDate birthdate);
}
