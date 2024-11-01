package vn.hoidanit.jobhunter.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Job;

import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    
    private final JobService jobService;

    public JobController(JobService jobService){
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("create a job")
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.handleCreateJob(job));
    }

    @PutMapping("/jobs")
    @ApiMessage("update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException{
        Optional<Job> currentJob = this.jobService.fetchJobById(job.getId());
        if(!currentJob.isPresent()){
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.ok().body(this.jobService.handleUpdateJob(job));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("update a job")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) throws IdInvalidException{
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if(!currentJob.isPresent()){
            throw new IdInvalidException("Job not found");
        }
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.ok().body(null);
    }

    // fetch job by id
    @GetMapping("/jobs/{id}")
    @ApiMessage("Get job by id")
    public ResponseEntity<Job> getJobById(@PathVariable("id") long id) throws IdInvalidException{
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if(!currentJob.isPresent()){
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(currentJob.get());
    }

    // fetch all job
    @GetMapping("/jobs")
    @ApiMessage("fetch all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
        @Filter Specification<Job> spec, Pageable pageable) {
    
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.fetchAllJob(spec, pageable));
    }
}
