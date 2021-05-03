package com.job.app.service.impl;

import com.job.app.constant.JobState;
import com.job.app.constant.JobType;
import com.job.app.dto.JobResponse;
import com.job.app.exception.JobNotFoundException;
import com.job.app.exception.JobStateNotAllowedException;
import com.job.app.exception.JobStateNotSupportedException;
import com.job.app.exception.JobTypeNotSupportedException;
import com.job.app.repository.JobRepository;
import com.job.app.service.impl.constant.JobTestConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import com.job.app.model.Job;
import java.util.List;
import java.util.Optional;

/**
 * @author Joemarie Teodoro
 * @since April 2021
 */
@RunWith(MockitoJUnitRunner.class)
public class JobServiceImplTest {

    @InjectMocks
    private JobServiceImpl jobServiceImpl;

    @Mock
    private JobRepository jobRepository;

    @Test
    public void whenCreateJobThenJobStateIsUnallocated() throws JobTypeNotSupportedException {
        when(jobRepository.save(any(Job.class))).thenReturn(JobTestConstant.createUnallocatedJobStateSavedJob());
        final JobResponse jobResponse = jobServiceImpl.createJob(JobTestConstant.createCreateJobRequest());
        assertNotNull(jobResponse);
        assertEquals(JobState.UNALLOCATED, jobResponse.getState());
        assertEquals(JobType.TYPEA, jobResponse.getType());
    }

    @Test(expected = JobTypeNotSupportedException.class)
    public void whenCreateJobUnknownJobTypeThenThrowJobTypeNotSupportedException() throws JobTypeNotSupportedException {
        final JobResponse jobResponse = jobServiceImpl.createJob(JobTestConstant.createUnknownJobTypeRequest());
    }

    @Test
    public void whenJobTypeAUpdateJobStatusFromUnallocatedAndUpdateToAllocatedThenUpdateToAllocated()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(JobTestConstant.createUnallocatedJobStateSavedJob()));
        when(jobRepository.save(any(Job.class))).thenReturn(JobTestConstant.createAllocatedJobStateSavedJob());

        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createAllocatedJobStateUpdateJobRequest());

        assertEquals(JobState.ALLOCATED, jobResponse.getState());
        assertEquals(JobType.TYPEA, jobResponse.getType());
    }

    @Test
    public void whenJobTypeAUpdateJobStatusFromAllocatedAndUpdateToStateAThenUpdateToStateA()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(JobTestConstant.createAllocatedJobStateSavedJob()));
        when(jobRepository.save(any(Job.class))).thenReturn(JobTestConstant.createStateAJobStateSavedJob());

        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createStateAJobStateUpdateJobRequest());

        assertEquals(JobState.STATEA, jobResponse.getState());
        assertEquals(JobType.TYPEA, jobResponse.getType());
    }

    @Test
    public void whenJobTypeAUpdateJobStatusFromStateAAndUpdateToStateBThenUpdateToStateB()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(JobTestConstant.createStateAJobStateSavedJob()));
        when(jobRepository.save(any(Job.class))).thenReturn(JobTestConstant.createStateBJobStateSavedJob());

        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createStateBJobStateUpdateJobRequest());

        assertEquals(JobState.STATEB, jobResponse.getState());
        assertEquals(JobType.TYPEA, jobResponse.getType());
    }

    @Test
    public void whenJobTypeAUpdateJobStatusFromStateBAndUpdateToCompletedThenUpdateToCompleted()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(JobTestConstant.createStateBJobStateSavedJob()));
        when(jobRepository.save(any(Job.class))).thenReturn(JobTestConstant.createCompletedJobStateSavedJob());

        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createCompletedJobStateUpdateJobRequest());

        assertEquals(JobState.COMPLETED, jobResponse.getState());
        assertEquals(JobType.TYPEA, jobResponse.getType());
    }

    @Test
    public void whenJobTypeAUpdateJobStatusFromStateBAndUpdateToDeletedThenUpdateToDeleted()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(JobTestConstant.createStateBJobStateSavedJob()));
        when(jobRepository.save(any(Job.class))).thenReturn(JobTestConstant.createDeletedJobStateSavedJob());

        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createDeletedJobStateUpdateJobRequest());

        assertEquals(JobState.DELETED, jobResponse.getState());
        assertEquals(JobType.TYPEA, jobResponse.getType());
    }

    @Test
    public void whenJobTypeBUpdateJobStatusFromStateBAndUpdateToStateAThenUpdateToStateA()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        final Job stateBJobStateSavedJob = JobTestConstant.createStateBJobStateSavedJob();
        stateBJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(stateBJobStateSavedJob));
        final Job stateAJobStateSavedJob = JobTestConstant.createStateAJobStateSavedJob();
        stateAJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.save(any(Job.class))).thenReturn(stateAJobStateSavedJob);

        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createStateAJobStateUpdateJobRequest());

        assertEquals(JobState.STATEA, jobResponse.getState());
        assertEquals(JobType.TYPEB, jobResponse.getType());
    }

    @Test
    public void whenJobTypeBUpdateJobStatusFromUnallocatedAndUpdateToAllocatedThenUpdateToAllocated()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        final Job unallocatedJobStateSavedJob = JobTestConstant.createUnallocatedJobStateSavedJob();
        unallocatedJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(unallocatedJobStateSavedJob));
        final Job allocatedJobStateSavedJob = JobTestConstant.createAllocatedJobStateSavedJob();
        allocatedJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.save(any(Job.class))).thenReturn(allocatedJobStateSavedJob);

        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createAllocatedJobStateUpdateJobRequest());

        assertEquals(JobState.ALLOCATED, jobResponse.getState());
        assertEquals(JobType.TYPEB, jobResponse.getType());
    }

    @Test(expected = JobStateNotAllowedException.class)
    public void whenJobTypeBUpdateJobStatusFromStateAAndUpdateToAllocatedThenThrowJobStateNotAllowedException()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        final Job stateAJobStateSavedJob = JobTestConstant.createStateAJobStateSavedJob();
        stateAJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(stateAJobStateSavedJob));

        jobServiceImpl.updateJobStatus(1L, JobTestConstant.createAllocatedJobStateUpdateJobRequest());
    }

    @Test
    public void whenJobTypeBUpdateJobStatusFromAllocatedAndUpdateToStateAThenUpdateToStateA()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        final Job allocatedJobStateSavedJob = JobTestConstant.createAllocatedJobStateSavedJob();
        allocatedJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(allocatedJobStateSavedJob));
        final Job stateAJobStateSavedJob = JobTestConstant.createStateAJobStateSavedJob();
        stateAJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.save(any(Job.class))).thenReturn(stateAJobStateSavedJob);

        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createStateAJobStateUpdateJobRequest());

        assertEquals(JobState.STATEA, jobResponse.getState());
        assertEquals(JobType.TYPEB, jobResponse.getType());
    }

    @Test
    public void whenJobTypeBUpdateJobStatusFromStateAAndUpdateToStateBThenUpdateToStateB()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        final Job stateAJobStateSavedJob = JobTestConstant.createStateAJobStateSavedJob();
        stateAJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(stateAJobStateSavedJob));
        final Job stateBJobStateSavedJob = JobTestConstant.createStateBJobStateSavedJob();
        stateBJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.save(any(Job.class))).thenReturn(stateBJobStateSavedJob);
        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createStateBJobStateUpdateJobRequest());

        assertEquals(JobState.STATEB, jobResponse.getState());
        assertEquals(JobType.TYPEB, jobResponse.getType());
    }

    @Test
    public void whenJobTypeBUpdateJobStatusFromStateBAndUpdateToCompletedThenUpdateToCompleted()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        final Job stateBJobStateSavedJob = JobTestConstant.createStateBJobStateSavedJob();
        stateBJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(stateBJobStateSavedJob));
        final Job completedJobStateSavedJob = JobTestConstant.createCompletedJobStateSavedJob();
        completedJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.save(any(Job.class))).thenReturn(completedJobStateSavedJob);
        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createCompletedJobStateUpdateJobRequest());

        assertEquals(JobState.COMPLETED, jobResponse.getState());
        assertEquals(JobType.TYPEB, jobResponse.getType());
    }

    @Test
    public void whenJobTypeBUpdateJobStatusFromUnallocatedAndUpdateToDeletedThenUpdateToDeleted()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        final Job unallocatedJobStateSavedJob = JobTestConstant.createUnallocatedJobStateSavedJob();
        unallocatedJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(unallocatedJobStateSavedJob));
        final Job deletedJobStateSavedJob = JobTestConstant.createDeletedJobStateSavedJob();
        deletedJobStateSavedJob.setType(JobType.TYPEB);
        when(jobRepository.save(any(Job.class))).thenReturn(deletedJobStateSavedJob);

        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createDeletedJobStateUpdateJobRequest());

        assertEquals(JobState.DELETED, jobResponse.getState());
        assertEquals(JobType.TYPEB, jobResponse.getType());
    }

    @Test(expected = JobStateNotAllowedException.class)
    public void whenJobTypeAUpdateJobStatusFromUnallocatedAndUpdateToCompletedThenThrowJobStateNotAllowedException()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(JobTestConstant.createUnallocatedJobStateSavedJob()));
        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createCompletedJobStateUpdateJobRequest());
    }

    @Test(expected = JobStateNotAllowedException.class)
    public void whenUpdateJobUnknownJobStateThenThrowJobStateNotSupportedException()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(JobTestConstant.createStateBJobStateSavedJob()));
        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createUnallocatedJobStateUpdateJobRequest());
    }

    @Test(expected = JobStateNotSupportedException.class)
    public void whenTypeAUpdateJobStatusUnknownJobStateThenThrowJobStateNotSupportedException()
            throws JobStateNotAllowedException, JobStateNotSupportedException, JobNotFoundException {
        final JobResponse jobResponse =
                jobServiceImpl.updateJobStatus(1L, JobTestConstant.createUnknownJobStateUpdateJobRequest());
    }

    @Test
    public void searchAllJobs() throws JobNotFoundException {
        when(jobRepository.findAll()).thenReturn(JobTestConstant.createJobs());
        final List<JobResponse> jobResponses = jobServiceImpl.searchAllJobs();
        assertNotNull(jobResponses);
        assertEquals(JobState.UNALLOCATED, jobResponses.get(0).getState());
    }

    @Test
    public void searchJobs() throws JobNotFoundException {
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(JobTestConstant.createCompletedJobStateSavedJob()));
        final JobResponse jobResponse = jobServiceImpl.searchJob(28L);
        assertEquals(JobState.COMPLETED, jobResponse.getState());
        assertEquals(JobType.TYPEA, jobResponse.getType());
    }

}