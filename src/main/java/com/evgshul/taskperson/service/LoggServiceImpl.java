package com.evgshul.taskperson.service;

import com.evgshul.taskperson.model.Logg;
import com.evgshul.taskperson.repository.LoggRepository;
import org.springframework.stereotype.Service;

@Service
public class LoggServiceImpl implements LoggService {

    private final LoggRepository loggRepository;

    public LoggServiceImpl(LoggRepository loggRepository) {
        this.loggRepository = loggRepository;
    }

    @Override
    public void saveLogg(Logg logg) {
        loggRepository.save(logg);
    }
}
