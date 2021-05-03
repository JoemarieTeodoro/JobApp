package com.job.app.repository;

import com.job.app.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * @author Joemarie Teodoro
 * @since May 2021
 */
public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findById(Long id);

    List<Job> findByTypeAndState(String type, String state);

}
