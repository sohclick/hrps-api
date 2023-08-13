package com.sohclick.hrps.hr.web.rest;

import com.sohclick.hrps.hr.repository.SalaryRepository;
import com.sohclick.hrps.hr.service.SalaryService;
import com.sohclick.hrps.hr.service.dto.SalaryDTO;
import com.sohclick.hrps.hr.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.sohclick.hrps.hr.domain.Salary}.
 */
@RestController
@RequestMapping("/api")
public class SalaryResource {

    private final Logger log = LoggerFactory.getLogger(SalaryResource.class);

    private static final String ENTITY_NAME = "hrpsHrSalary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalaryService salaryService;

    private final SalaryRepository salaryRepository;

    public SalaryResource(SalaryService salaryService, SalaryRepository salaryRepository) {
        this.salaryService = salaryService;
        this.salaryRepository = salaryRepository;
    }

    /**
     * {@code POST  /salaries} : Create a new salary.
     *
     * @param salaryDTO the salaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salaryDTO, or with status {@code 400 (Bad Request)} if the salary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/salaries")
    public Mono<ResponseEntity<SalaryDTO>> createSalary(@Valid @RequestBody SalaryDTO salaryDTO) throws URISyntaxException {
        log.debug("REST request to save Salary : {}", salaryDTO);
        if (salaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new salary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return salaryService
            .save(salaryDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/salaries/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /salaries/:id} : Updates an existing salary.
     *
     * @param id the id of the salaryDTO to save.
     * @param salaryDTO the salaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaryDTO,
     * or with status {@code 400 (Bad Request)} if the salaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/salaries/{id}")
    public Mono<ResponseEntity<SalaryDTO>> updateSalary(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SalaryDTO salaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Salary : {}, {}", id, salaryDTO);
        if (salaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return salaryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return salaryService
                    .update(salaryDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /salaries/:id} : Partial updates given fields of an existing salary, field will ignore if it is null
     *
     * @param id the id of the salaryDTO to save.
     * @param salaryDTO the salaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaryDTO,
     * or with status {@code 400 (Bad Request)} if the salaryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the salaryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the salaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/salaries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SalaryDTO>> partialUpdateSalary(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SalaryDTO salaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Salary partially : {}, {}", id, salaryDTO);
        if (salaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return salaryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SalaryDTO> result = salaryService.partialUpdate(salaryDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /salaries} : get all the salaries.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salaries in body.
     */
    @GetMapping(value = "/salaries", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SalaryDTO>>> getAllSalaries(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Salaries");
        return salaryService
            .countAll()
            .zipWith(salaryService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /salaries/:id} : get the "id" salary.
     *
     * @param id the id of the salaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/salaries/{id}")
    public Mono<ResponseEntity<SalaryDTO>> getSalary(@PathVariable Long id) {
        log.debug("REST request to get Salary : {}", id);
        Mono<SalaryDTO> salaryDTO = salaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salaryDTO);
    }

    /**
     * {@code DELETE  /salaries/:id} : delete the "id" salary.
     *
     * @param id the id of the salaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/salaries/{id}")
    public Mono<ResponseEntity<Void>> deleteSalary(@PathVariable Long id) {
        log.debug("REST request to delete Salary : {}", id);
        return salaryService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
