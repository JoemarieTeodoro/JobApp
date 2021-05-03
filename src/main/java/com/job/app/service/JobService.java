package com.job.app.service;

import com.job.app.dto.CreateJobRequest;
import com.job.app.dto.JobResponse;
import com.job.app.dto.UpdateJobRequest;
import com.job.app.exception.JobNotFoundException;
import com.job.app.exception.JobStateNotAllowedException;
import com.job.app.exception.JobStateNotSupportedException;
import com.job.app.exception.JobTypeNotSupportedException;
import java.util.List;

/**
 * @author Joemarie Teodoro
 * @since May 2021
 * @description Contains methods for handling job state transactions
 */
public interface JobService {

    /**
     * Handles job creation
     *
     * @param createJobRequest create request object
     * @return created Job details
     * @throws JobTypeNotSupportedException if job type not supported
     */
    JobResponse createJob(CreateJobRequest createJobRequest) throws JobTypeNotSupportedException;

    /**
     * Update existing job type
     *
     * @param jobId unique job id
     * @param updateJobRequest update request object
     * @return updated Job details
     * @throws JobNotFoundException if Job for specific id not found
     * @throws JobStateNotSupportedException if job state not supported encountered
     * @throws JobStateNotAllowedException if job state not allowed
     */
    JobResponse updateJobStatus(Long jobId, UpdateJobRequest updateJobRequest) throws JobNotFoundException,
            JobStateNotSupportedException, JobStateNotAllowedException;

    /**
     * Query all jobs
     * @return all saved jobs
     */
    List<JobResponse> searchAllJobs();

    /**
     * Query specific job
     * @param jobId unique job id
     * @return saved job details
     * @throws JobNotFoundException if unable to find job
     */
    JobResponse searchJob(Long jobId) throws JobNotFoundException;

}
