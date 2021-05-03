package com.job.app.service.impl.constant;

import com.job.app.constant.JobState;
import com.job.app.constant.JobType;
import com.job.app.dto.CreateJobRequest;
import com.job.app.dto.UpdateJobRequest;
import com.job.app.model.Job;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author Joemarie Teodoro
 * @since April 2021
 * @description Class containing constants for unit test cases
 */
public final class JobTestConstant {

    public static CreateJobRequest createCreateJobRequest() {
        CreateJobRequest createJobRequest = new CreateJobRequest();
        createJobRequest.setJobType("TypeA");
        return createJobRequest;
    }

    public static CreateJobRequest createUnknownJobTypeRequest() {
        CreateJobRequest createJobRequest = new CreateJobRequest();
        createJobRequest.setJobType("unknown");
        return createJobRequest;
    }

    public static Job createUnallocatedJobStateSavedJob() {
        Job job = new Job();
        job.setId(1L);
        job.setType(JobType.TYPEA);
        job.setState(JobState.UNALLOCATED);
        LocalDateTime now = LocalDateTime.now();
        job.setDtimeCreated(now);
        job.setDtimeUpdated(now);
        return job;
    }

    public static Job createAllocatedJobStateSavedJob() {
        Job job = new Job();
        job.setId(2L);
        job.setType(JobType.TYPEA);
        job.setState(JobState.ALLOCATED);
        LocalDateTime now = LocalDateTime.now();
        job.setDtimeCreated(now);
        job.setDtimeUpdated(now);
        return job;
    }

    public static Job createStateAJobStateSavedJob() {
        Job job = new Job();
        job.setId(2L);
        job.setType(JobType.TYPEA);
        job.setState(JobState.STATEA);
        LocalDateTime now = LocalDateTime.now();
        job.setDtimeCreated(now);
        job.setDtimeUpdated(now);
        return job;
    }

    public static Job createStateBJobStateSavedJob() {
        Job job = new Job();
        job.setId(2L);
        job.setType(JobType.TYPEA);
        job.setState(JobState.STATEB);
        LocalDateTime now = LocalDateTime.now();
        job.setDtimeCreated(now);
        job.setDtimeUpdated(now);
        return job;
    }

    public static Job createCompletedJobStateSavedJob() {
        Job job = new Job();
        job.setId(2L);
        job.setType(JobType.TYPEA);
        job.setState(JobState.COMPLETED);
        LocalDateTime now = LocalDateTime.now();
        job.setDtimeCreated(now);
        job.setDtimeUpdated(now);
        return job;
    }

    public static Job createDeletedJobStateSavedJob() {
        Job job = new Job();
        job.setId(2L);
        job.setType(JobType.TYPEA);
        job.setState(JobState.DELETED);
        LocalDateTime now = LocalDateTime.now();
        job.setDtimeCreated(now);
        job.setDtimeUpdated(now);
        return job;
    }

    public static UpdateJobRequest createUnallocatedJobStateUpdateJobRequest() {
        UpdateJobRequest updateJobRequest = new UpdateJobRequest();
        updateJobRequest.setJobState("unallocated");
        return updateJobRequest;
    }

    public static UpdateJobRequest createAllocatedJobStateUpdateJobRequest() {
        UpdateJobRequest updateJobRequest = new UpdateJobRequest();
        updateJobRequest.setJobState("ALLOCATED");
        return updateJobRequest;
    }

    public static UpdateJobRequest createStateAJobStateUpdateJobRequest() {
        UpdateJobRequest updateJobRequest = new UpdateJobRequest();
        updateJobRequest.setJobState("statea");
        return updateJobRequest;
    }

    public static UpdateJobRequest createStateBJobStateUpdateJobRequest() {
        UpdateJobRequest updateJobRequest = new UpdateJobRequest();
        updateJobRequest.setJobState("STATEB");
        return updateJobRequest;
    }

    public static UpdateJobRequest createCompletedJobStateUpdateJobRequest() {
        UpdateJobRequest updateJobRequest = new UpdateJobRequest();
        updateJobRequest.setJobState("completed");
        return updateJobRequest;
    }

    public static UpdateJobRequest createDeletedJobStateUpdateJobRequest() {
        UpdateJobRequest updateJobRequest = new UpdateJobRequest();
        updateJobRequest.setJobState("DELETED");
        return updateJobRequest;
    }

    public static UpdateJobRequest createUnknownJobStateUpdateJobRequest() {
        UpdateJobRequest updateJobRequest = new UpdateJobRequest();
        updateJobRequest.setJobState("unknown");
        return updateJobRequest;
    }

    public static List<Job> createJobs() {
        final LocalDateTime now = LocalDateTime.now();

        Job unallocatedJob = new Job();
        unallocatedJob.setId(1L);
        unallocatedJob.setType(JobType.TYPEA);
        unallocatedJob.setState(JobState.UNALLOCATED);
        unallocatedJob.setDtimeCreated(now);
        unallocatedJob.setDtimeUpdated(now);

        Job allocatedJob = new Job();
        allocatedJob.setId(2L);
        allocatedJob.setType(JobType.TYPEB);
        allocatedJob.setState(JobState.ALLOCATED);
        allocatedJob.setDtimeCreated(now);
        allocatedJob.setDtimeUpdated(now);

        Job stateAJob = new Job();
        stateAJob.setId(3L);
        stateAJob.setType(JobType.TYPEA);
        stateAJob.setState(JobState.STATEA);
        stateAJob.setDtimeCreated(now);
        stateAJob.setDtimeUpdated(now);

        Job stateBJob = new Job();
        stateBJob.setId(4L);
        stateBJob.setType(JobType.TYPEB);
        stateBJob.setState(JobState.STATEB);
        stateBJob.setDtimeCreated(now);
        stateBJob.setDtimeUpdated(now);


        Job completedJob = new Job();
        completedJob.setId(5L);
        completedJob.setType(JobType.TYPEB);
        completedJob.setState(JobState.COMPLETED);
        completedJob.setDtimeCreated(now);
        completedJob.setDtimeUpdated(now);

        Job deletedJob = new Job();
        deletedJob.setId(5L);
        deletedJob.setType(JobType.TYPEA);
        deletedJob.setState(JobState.DELETED);
        deletedJob.setDtimeCreated(now);
        deletedJob.setDtimeUpdated(now);

        return Arrays.asList(unallocatedJob, allocatedJob, stateAJob, stateBJob, completedJob, deletedJob);
    }
}
