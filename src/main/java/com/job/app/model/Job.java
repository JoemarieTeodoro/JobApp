package com.job.app.model;

import com.job.app.constant.JobState;
import com.job.app.constant.JobType;
import com.job.app.dto.CreateJobRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Joemarie Teodoro
 * @since May 2021
 * @description Job Entity class
 */
@ToString
@Getter
@Setter
@Entity
public class Job implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private JobType type;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private JobState state;

    @Column(name = "DATE_TIME_CREATED")
    private LocalDateTime dtimeCreated;

    @Column(name = "DATE_TIME_UPDATED")
    private LocalDateTime dtimeUpdated;

    public static Job createDefaultJobFromRequest(CreateJobRequest createJobRequest) {
        Job job = new Job();
        job.setType(JobType.valueOf(createJobRequest.getJobType().toUpperCase()));
        job.setState(JobState.UNALLOCATED);
        final LocalDateTime now = LocalDateTime.now();
        job.setDtimeCreated(now);
        job.setDtimeUpdated(now);
        return job;
    }
}
