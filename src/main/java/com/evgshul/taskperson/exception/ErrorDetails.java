package com.evgshul.taskperson.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Collect error details class.
 */

@Getter@Setter
@AllArgsConstructor
public class ErrorDetails {
    private Date timestamp;
    private String status;
    private String details;
}
