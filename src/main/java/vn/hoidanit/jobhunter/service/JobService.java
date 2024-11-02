package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository){
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public ResCreateJobDTO handleCreateJob(Job job) {
        //check skills
        if(job.getSkills() != null){
            List<Long> reqSkills = job.getSkills()
            .stream().map(x -> x.getId())
            .collect(Collectors.toList());

        List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
        job.setSkills(dbSkills);
        }

        //create job
        Job currentJob = this.jobRepository.save(job);

        //convert response
        ResCreateJobDTO dto = new ResCreateJobDTO();
   
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setLocation(currentJob.getLocation());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLevel(currentJob.getLevel());
        dto.setDescription(currentJob.getDescription());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setCreatedAt(currentJob.getCreatedAt());
        dto.setCreatedBy(currentJob.getCreatedBy());

        if(currentJob.getSkills() != null){
            List<String> skills = currentJob.getSkills()
                .stream().map(item -> item.getName())
                .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return dto;
    }

    public Optional<Job> fetchJobById(long id) {
        return this.jobRepository.findById(id);
    }

    public ResUpdateJobDTO handleUpdateJob(Job job) {
        //check skills
        if(job.getSkills() != null){
            List<Long> reqSkills = job.getSkills()
            .stream().map(x -> x.getId())
            .collect(Collectors.toList());

        List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
        job.setSkills(dbSkills);
        }

        //create job
        Job currentJob = this.jobRepository.save(job);

        //convert response
        ResUpdateJobDTO dto = new ResUpdateJobDTO();
   
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setLocation(currentJob.getLocation());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLevel(currentJob.getLevel());
        dto.setDescription(currentJob.getDescription());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setCreatedAt(currentJob.getCreatedAt());
        dto.setCreatedBy(currentJob.getCreatedBy());

        if(currentJob.getSkills() != null){
            List<String> skills = currentJob.getSkills()
                .stream().map(item -> item.getName())
                .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return dto;
    }

    public void handleDeleteJob(long id) {
        this.skillRepository.deleteById(id);;
    }

    public ResultPaginationDTO fetchAllJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() +1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageJob.getContent());

        return rs;
    }
    
}
