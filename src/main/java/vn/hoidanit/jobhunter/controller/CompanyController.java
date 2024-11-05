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
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("create a company")
    public ResponseEntity<?> createCompany(@Valid @RequestBody Company reqCompany){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateCompany(reqCompany));
    }

    @GetMapping("/companies")
    @ApiMessage("fetch companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(
        @Filter Specification<Company> spec,Pageable pageable){
    
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchAllCompany(spec, pageable));
    }

    @PutMapping("/companies")
    @ApiMessage("update a company")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company){
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.handleUpdateCompany(company));
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("delete a company")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id){
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("fetch a company by id")
    public ResponseEntity<Company> fetchCompanyById(@PathVariable("id") long id){
        Optional<Company> cOptional = this.companyService.findById(id);
        return ResponseEntity.ok().body(cOptional.get());
    }
}
