package com.job.app.controller;

import com.job.app.dto.CreateJobRequest;
import com.job.app.dto.JobResponse;
import com.job.app.dto.UpdateJobRequest;
import com.job.app.exception.JobNotFoundException;
import com.job.app.exception.JobStateNotAllowedException;
import com.job.app.exception.JobStateNotSupportedException;
import com.job.app.exception.JobTypeNotSupportedException;
import com.job.app.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Joemarie Teodoro
 * @since May 2021
 * @description Request entry point for Job requests
 */
@RequestMapping("/app/v1")
@RestController
public class JobController {

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @Operation(summary = "Create job entity", tags = "jobs")
    @PostMapping(value = "/jobs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobResponse> createJob(@RequestBody CreateJobRequest createJobRequest) throws JobTypeNotSupportedException {
        return new ResponseEntity<>(jobService.createJob(createJobRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Update job specific job entity", tags = "jobs")
    @PutMapping(value = "/jobs/{jobId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobResponse> updateJobStatus(
            @Parameter(description = "job id to update", example = "1") @PathVariable Long jobId,
            @RequestBody UpdateJobRequest updateJobRequest)
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        return new ResponseEntity<>(jobService.updateJobStatus(jobId, updateJobRequest), HttpStatus.OK);
    }

    @Operation(summary = "Query all job entities", tags = "jobs")
    @GetMapping(value = "/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JobResponse>> searchAllJob() {
        return new ResponseEntity<>(jobService.searchAllJobs(), HttpStatus.OK);
    }

    @Operation(summary = "Query job entity by unique id", tags = "jobs")
    @GetMapping(value = "/jobs/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobResponse> searchJob(
            @Parameter(description = "job id to search", example = "1") @PathVariable("jobId") Long jobId)
            throws JobNotFoundException {
        return new ResponseEntity<>(jobService.searchJob(jobId), HttpStatus.OK);
    }

}
