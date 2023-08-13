package com.sohclick.hrps.hr.web.rest;

import com.sohclick.hrps.hr.repository.EmployeeDocumentRepository;
import com.sohclick.hrps.hr.service.EmployeeDocumentService;
import com.sohclick.hrps.hr.service.dto.EmployeeDocumentDTO;
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
 * REST controller for managing {@link com.sohclick.hrps.hr.domain.EmployeeDocument}.
 */
@RestController
@RequestMapping("/api")
public class EmployeeDocumentResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeDocumentResource.class);

    private static final String ENTITY_NAME = "hrpsHrEmployeeDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeDocumentService employeeDocumentService;

    private final EmployeeDocumentRepository employeeDocumentRepository;

    public EmployeeDocumentResource(
        EmployeeDocumentService employeeDocumentService,
        EmployeeDocumentRepository employeeDocumentRepository
    ) {
        this.employeeDocumentService = employeeDocumentService;
        this.employeeDocumentRepository = employeeDocumentRepository;
    }

    /**
     * {@code POST  /employee-documents} : Create a new employeeDocument.
     *
     * @param employeeDocumentDTO the employeeDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeDocumentDTO, or with status {@code 400 (Bad Request)} if the employeeDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employee-documents")
    public Mono<ResponseEntity<EmployeeDocumentDTO>> createEmployeeDocument(@Valid @RequestBody EmployeeDocumentDTO employeeDocumentDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmployeeDocument : {}", employeeDocumentDTO);
        if (employeeDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return employeeDocumentService
            .save(employeeDocumentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/employee-documents/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /employee-documents/:id} : Updates an existing employeeDocument.
     *
     * @param id the id of the employeeDocumentDTO to save.
     * @param employeeDocumentDTO the employeeDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the employeeDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employee-documents/{id}")
    public Mono<ResponseEntity<EmployeeDocumentDTO>> updateEmployeeDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeDocumentDTO employeeDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeDocument : {}, {}", id, employeeDocumentDTO);
        if (employeeDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return employeeDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return employeeDocumentService
                    .update(employeeDocumentDTO)
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
     * {@code PATCH  /employee-documents/:id} : Partial updates given fields of an existing employeeDocument, field will ignore if it is null
     *
     * @param id the id of the employeeDocumentDTO to save.
     * @param employeeDocumentDTO the employeeDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the employeeDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeeDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employee-documents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EmployeeDocumentDTO>> partialUpdateEmployeeDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeDocumentDTO employeeDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmployeeDocument partially : {}, {}", id, employeeDocumentDTO);
        if (employeeDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return employeeDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EmployeeDocumentDTO> result = employeeDocumentService.partialUpdate(employeeDocumentDTO);

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
     * {@code GET  /employee-documents} : get all the employeeDocuments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeDocuments in body.
     */
    @GetMapping(value = "/employee-documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<EmployeeDocumentDTO>>> getAllEmployeeDocuments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of EmployeeDocuments");
        return employeeDocumentService
            .countAll()
            .zipWith(employeeDocumentService.findAll(pageable).collectList())
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
     * {@code GET  /employee-documents/:id} : get the "id" employeeDocument.
     *
     * @param id the id of the employeeDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employee-documents/{id}")
    public Mono<ResponseEntity<EmployeeDocumentDTO>> getEmployeeDocument(@PathVariable Long id) {
        log.debug("REST request to get EmployeeDocument : {}", id);
        Mono<EmployeeDocumentDTO> employeeDocumentDTO = employeeDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeDocumentDTO);
    }

    /**
     * {@code DELETE  /employee-documents/:id} : delete the "id" employeeDocument.
     *
     * @param id the id of the employeeDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employee-documents/{id}")
    public Mono<ResponseEntity<Void>> deleteEmployeeDocument(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeDocument : {}", id);
        return employeeDocumentService
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
