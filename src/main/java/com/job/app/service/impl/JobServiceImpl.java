package com.job.app.service.impl;

import com.job.app.constant.JobState;
import com.job.app.constant.JobType;
import com.job.app.dto.CreateJobRequest;
import com.job.app.dto.JobResponse;
import com.job.app.dto.UpdateJobRequest;
import com.job.app.exception.JobNotFoundException;
import com.job.app.exception.JobStateNotAllowedException;
import com.job.app.exception.JobStateNotSupportedException;
import com.job.app.exception.JobTypeNotSupportedException;
import com.job.app.model.Job;
import com.job.app.repository.JobRepository;
import com.job.app.service.JobService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Joemarie Teodoro
 * @since May 2021
 * @description Service class for handling job state transactions
 */
@Service
public class JobServiceImpl implements JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    private JobRepository jobRepository;

    private Map<JobType, AbstractJobTypeProcessStrategy> jobTypeProcessStrategyRegistry;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
        this.jobTypeProcessStrategyRegistry = new HashMap<>();

        Arrays.asList(new TypeAJobProcessStrategy(), new TypeBJobProcessStrategy())
              .forEach(strategy -> jobTypeProcessStrategyRegistry.put(strategy.getJobType(), strategy));
    }

    @Override
    public JobResponse createJob(CreateJobRequest createJobRequest) throws JobTypeNotSupportedException {

        validateJobType(createJobRequest);

        Job job = Job.createDefaultJobFromRequest(createJobRequest);

        final Job savedJob = jobRepository.save(job);
        logger.debug("job successfully created [{}].", savedJob);
        return JobResponse.fromJob(savedJob);
    }

    @Override
    public JobResponse updateJobStatus(Long jobId, UpdateJobRequest updateJobRequest)
            throws JobNotFoundException, JobStateNotSupportedException, JobStateNotAllowedException {

        validateJobState(updateJobRequest);

        final Job job = jobRepository.findById(jobId)
                                     .orElseThrow(() -> new JobNotFoundException(String.format("Job [%d] not found!", jobId)));

        final JobType jobType = job.getType();
        final JobState fromJobState = job.getState();
        final JobState toJobState = JobState.valueOf(updateJobRequest.getJobState().toUpperCase());

        final AbstractJobTypeProcessStrategy jobTypeProcessStrategy = jobTypeProcessStrategyRegistry.get(jobType);
        jobTypeProcessStrategy.validateSequentialJobState(fromJobState, toJobState);

        logger.info("Valid sequential state: Type [{}] - [{}] to [{}].", jobType, fromJobState, toJobState);
        job.setState(toJobState);
        job.setDtimeUpdated(LocalDateTime.now());

        final Job updatedJob = jobRepository.save(job);
        logger.info("job successfully updated [{}].", updatedJob);
        return JobResponse.fromJob(updatedJob);
    }

    @Override
    public List<JobResponse> searchAllJobs() {
        return jobRepository.findAll().stream()
                            .map(JobResponse::fromJob)
                            .collect(Collectors.toList());
    }

    @Override
    public JobResponse searchJob(Long jobId) throws JobNotFoundException {
        final Job job = jobRepository.findById(jobId)
                                     .orElseThrow(() -> new JobNotFoundException(String.format("Job with id [%d] not found!", jobId)));
        return JobResponse.fromJob(job);
    }

    private void validateJobType(CreateJobRequest createJobRequest) throws JobTypeNotSupportedException {
        if (StringUtils.isBlank(createJobRequest.getJobType())) {
            throw new JobTypeNotSupportedException(String.format("Job type [%s] not supported!.", createJobRequest.getJobType()));
        }

        try {
            final String jobType = createJobRequest.getJobType().toUpperCase();
            JobType.valueOf(jobType);
        } catch (IllegalArgumentException e) {
            throw new JobTypeNotSupportedException(String.format("Job type [%s] not supported!.", createJobRequest.getJobType()));
        }
    }

    private void validateJobState(UpdateJobRequest updateJobRequest) throws JobStateNotSupportedException {

        if (StringUtils.isBlank(updateJobRequest.getJobState())) {
            throw new JobStateNotSupportedException(String.format("Job type [%s] not supported!.", updateJobRequest.getJobState()));
        }

        try {
            final String jobState = updateJobRequest.getJobState().toUpperCase();
            JobState.valueOf(jobState);
        } catch (IllegalArgumentException e) {
            throw new JobStateNotSupportedException(String.format("Job type [%s] not supported!.", updateJobRequest.getJobState()));
        }
    }

    private abstract class AbstractJobTypeProcessStrategy {

        protected Map<JobState, List<JobState>> commonChronologicalStatesMap;

        public AbstractJobTypeProcessStrategy() {
            this.commonChronologicalStatesMap = new HashMap<>();
            commonChronologicalStatesMap.put(JobState.UNALLOCATED, Arrays.asList(JobState.ALLOCATED, JobState.DELETED));
            commonChronologicalStatesMap.put(JobState.ALLOCATED, Arrays.asList(JobState.STATEA, JobState.DELETED));
            commonChronologicalStatesMap.put(JobState.STATEA, Arrays.asList(JobState.STATEB, JobState.DELETED));
            commonChronologicalStatesMap.put(JobState.STATEB, Arrays.asList(JobState.COMPLETED, JobState.DELETED));
        }

        abstract JobType getJobType();

        abstract void validateSequentialJobState(JobState fromJobState, JobState toJobState) throws JobStateNotAllowedException;

        protected boolean hasNextAllowedStates(JobState fromJobState, JobState toJobState) {
            final List<JobState> allowedNextJobStates = commonChronologicalStatesMap.get(fromJobState);
            return !CollectionUtils.isEmpty(allowedNextJobStates) && allowedNextJobStates.contains(toJobState);
        }

    }

    private class TypeAJobProcessStrategy extends AbstractJobTypeProcessStrategy {

        @Override
        JobType getJobType() {
            return JobType.TYPEA;
        }

        @Override
        void validateSequentialJobState(JobState fromJobState, JobState toJobState) throws JobStateNotAllowedException {

            if (hasNextAllowedStates(fromJobState, toJobState)) {
                logger.info("Allowed Type A Job State");
            } else {
                throw new JobStateNotAllowedException(
                        String.format("Job state [%s] to state [%s] not allowed!.", fromJobState, toJobState));
            }
        }

    }

    private class TypeBJobProcessStrategy extends AbstractJobTypeProcessStrategy {

        private Map<JobState, List<JobState>> chronologicalStatesTypeBMap;

        public TypeBJobProcessStrategy() {
            this.chronologicalStatesTypeBMap = new HashMap<>();
            chronologicalStatesTypeBMap.put(JobState.STATEB, Arrays.asList(JobState.STATEA, JobState.COMPLETED, JobState.DELETED));
        }

        @Override
        JobType getJobType() {
            return JobType.TYPEB;
        }

        @Override
        void validateSequentialJobState(JobState fromJobState, JobState toJobState) throws JobStateNotAllowedException {

            if (hasNextAllowedStates(fromJobState, toJobState) || hasNextAllowedTypeBStates(fromJobState, toJobState)) {
                logger.info("Allowed Type B Job State");
            } else {
                throw new JobStateNotAllowedException(
                        String.format("Job state [%s] to state [%s] not allowed!.", fromJobState, toJobState));
            }
        }

        private boolean hasNextAllowedTypeBStates(JobState fromJobState, JobState toJobState) {
            final List<JobState> allowedNextTypeBStates = chronologicalStatesTypeBMap.get(fromJobState);
            return !CollectionUtils.isEmpty(allowedNextTypeBStates) && allowedNextTypeBStates.contains(toJobState);
        }

    }

}
