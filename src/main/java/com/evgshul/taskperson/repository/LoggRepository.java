package com.evgshul.taskperson.repository;

import com.evgshul.taskperson.model.Logg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggRepository extends JpaRepository<Logg, Long > {
}
