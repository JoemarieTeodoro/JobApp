package com.job.app.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author Joemarie Teodoro
 * @since May 2021
 */
@Data
public class ErrorResponse {

    private Integer responseCode;
    private String message;
    private LocalDateTime timeStamp;

}
