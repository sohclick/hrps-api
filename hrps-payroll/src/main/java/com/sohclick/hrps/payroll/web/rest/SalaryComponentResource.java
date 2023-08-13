package com.sohclick.hrps.payroll.web.rest;

import com.sohclick.hrps.payroll.repository.SalaryComponentRepository;
import com.sohclick.hrps.payroll.service.SalaryComponentService;
import com.sohclick.hrps.payroll.service.dto.SalaryComponentDTO;
import com.sohclick.hrps.payroll.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.sohclick.hrps.payroll.domain.SalaryComponent}.
 */
@RestController
@RequestMapping("/api")
public class SalaryComponentResource {

    private final Logger log = LoggerFactory.getLogger(SalaryComponentResource.class);

    private static final String ENTITY_NAME = "hrpsPayrollSalaryComponent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalaryComponentService salaryComponentService;

    private final SalaryComponentRepository salaryComponentRepository;

    public SalaryComponentResource(SalaryComponentService salaryComponentService, SalaryComponentRepository salaryComponentRepository) {
        this.salaryComponentService = salaryComponentService;
        this.salaryComponentRepository = salaryComponentRepository;
    }

    /**
     * {@code POST  /salary-components} : Create a new salaryComponent.
     *
     * @param salaryComponentDTO the salaryComponentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salaryComponentDTO, or with status {@code 400 (Bad Request)} if the salaryComponent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/salary-components")
    public Mono<ResponseEntity<SalaryComponentDTO>> createSalaryComponent(@Valid @RequestBody SalaryComponentDTO salaryComponentDTO)
        throws URISyntaxException {
        log.debug("REST request to save SalaryComponent : {}", salaryComponentDTO);
        if (salaryComponentDTO.getId() != null) {
            throw new BadRequestAlertException("A new salaryComponent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return salaryComponentService
            .save(salaryComponentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/salary-components/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /salary-components/:id} : Updates an existing salaryComponent.
     *
     * @param id the id of the salaryComponentDTO to save.
     * @param salaryComponentDTO the salaryComponentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaryComponentDTO,
     * or with status {@code 400 (Bad Request)} if the salaryComponentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salaryComponentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/salary-components/{id}")
    public Mono<ResponseEntity<SalaryComponentDTO>> updateSalaryComponent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SalaryComponentDTO salaryComponentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SalaryComponent : {}, {}", id, salaryComponentDTO);
        if (salaryComponentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salaryComponentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return salaryComponentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return salaryComponentService
                    .update(salaryComponentDTO)
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
     * {@code PATCH  /salary-components/:id} : Partial updates given fields of an existing salaryComponent, field will ignore if it is null
     *
     * @param id the id of the salaryComponentDTO to save.
     * @param salaryComponentDTO the salaryComponentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaryComponentDTO,
     * or with status {@code 400 (Bad Request)} if the salaryComponentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the salaryComponentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the salaryComponentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/salary-components/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SalaryComponentDTO>> partialUpdateSalaryComponent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SalaryComponentDTO salaryComponentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SalaryComponent partially : {}, {}", id, salaryComponentDTO);
        if (salaryComponentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salaryComponentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return salaryComponentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SalaryComponentDTO> result = salaryComponentService.partialUpdate(salaryComponentDTO);

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
     * {@code GET  /salary-components} : get all the salaryComponents.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salaryComponents in body.
     */
    @GetMapping(value = "/salary-components", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SalaryComponentDTO>>> getAllSalaryComponents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of SalaryComponents");
        return salaryComponentService
            .countAll()
            .zipWith(salaryComponentService.findAll(pageable).collectList())
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
     * {@code GET  /salary-components/:id} : get the "id" salaryComponent.
     *
     * @param id the id of the salaryComponentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salaryComponentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/salary-components/{id}")
    public Mono<ResponseEntity<SalaryComponentDTO>> getSalaryComponent(@PathVariable Long id) {
        log.debug("REST request to get SalaryComponent : {}", id);
        Mono<SalaryComponentDTO> salaryComponentDTO = salaryComponentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salaryComponentDTO);
    }

    /**
     * {@code DELETE  /salary-components/:id} : delete the "id" salaryComponent.
     *
     * @param id the id of the salaryComponentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/salary-components/{id}")
    public Mono<ResponseEntity<Void>> deleteSalaryComponent(@PathVariable Long id) {
        log.debug("REST request to delete SalaryComponent : {}", id);
        return salaryComponentService
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
