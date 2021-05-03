package com.job.app.dto;

import com.job.app.constant.JobState;
import com.job.app.constant.JobType;
import com.job.app.model.Job;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author Joemarie Teodoro
 * @since May 2021
 */
@Builder
@Data
public class JobResponse {

    private Long id;

    private JobType type;

    private JobState state;

    private LocalDateTime dtimeCreated;

    private LocalDateTime dtimeUpdated;

    public static JobResponse fromJob(Job job) {
        return JobResponse.builder()
                          .id(job.getId())
                          .type(job.getType())
                          .state(job.getState())
                          .dtimeCreated(job.getDtimeCreated())
                          .dtimeUpdated(job.getDtimeUpdated())
                          .build();
    }
}
