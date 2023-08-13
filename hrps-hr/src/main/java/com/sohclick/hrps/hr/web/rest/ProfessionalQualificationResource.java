package com.sohclick.hrps.hr.web.rest;

import com.sohclick.hrps.hr.repository.ProfessionalQualificationRepository;
import com.sohclick.hrps.hr.service.ProfessionalQualificationService;
import com.sohclick.hrps.hr.service.dto.ProfessionalQualificationDTO;
import com.sohclick.hrps.hr.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.sohclick.hrps.hr.domain.ProfessionalQualification}.
 */
@RestController
@RequestMapping("/api")
public class ProfessionalQualificationResource {

    private final Logger log = LoggerFactory.getLogger(ProfessionalQualificationResource.class);

    private static final String ENTITY_NAME = "hrpsHrProfessionalQualification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalQualificationService professionalQualificationService;

    private final ProfessionalQualificationRepository professionalQualificationRepository;

    public ProfessionalQualificationResource(
        ProfessionalQualificationService professionalQualificationService,
        ProfessionalQualificationRepository professionalQualificationRepository
    ) {
        this.professionalQualificationService = professionalQualificationService;
        this.professionalQualificationRepository = professionalQualificationRepository;
    }

    /**
     * {@code POST  /professional-qualifications} : Create a new professionalQualification.
     *
     * @param professionalQualificationDTO the professionalQualificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalQualificationDTO, or with status {@code 400 (Bad Request)} if the professionalQualification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/professional-qualifications")
    public Mono<ResponseEntity<ProfessionalQualificationDTO>> createProfessionalQualification(
        @RequestBody ProfessionalQualificationDTO professionalQualificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ProfessionalQualification : {}", professionalQualificationDTO);
        if (professionalQualificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalQualification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return professionalQualificationService
            .save(professionalQualificationDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/professional-qualifications/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /professional-qualifications/:id} : Updates an existing professionalQualification.
     *
     * @param id the id of the professionalQualificationDTO to save.
     * @param professionalQualificationDTO the professionalQualificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalQualificationDTO,
     * or with status {@code 400 (Bad Request)} if the professionalQualificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalQualificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/professional-qualifications/{id}")
    public Mono<ResponseEntity<ProfessionalQualificationDTO>> updateProfessionalQualification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProfessionalQualificationDTO professionalQualificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProfessionalQualification : {}, {}", id, professionalQualificationDTO);
        if (professionalQualificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalQualificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return professionalQualificationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return professionalQualificationService
                    .update(professionalQualificationDTO)
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
     * {@code PATCH  /professional-qualifications/:id} : Partial updates given fields of an existing professionalQualification, field will ignore if it is null
     *
     * @param id the id of the professionalQualificationDTO to save.
     * @param professionalQualificationDTO the professionalQualificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalQualificationDTO,
     * or with status {@code 400 (Bad Request)} if the professionalQualificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalQualificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalQualificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/professional-qualifications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ProfessionalQualificationDTO>> partialUpdateProfessionalQualification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProfessionalQualificationDTO professionalQualificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProfessionalQualification partially : {}, {}", id, professionalQualificationDTO);
        if (professionalQualificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalQualificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return professionalQualificationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ProfessionalQualificationDTO> result = professionalQualificationService.partialUpdate(professionalQualificationDTO);

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
     * {@code GET  /professional-qualifications} : get all the professionalQualifications.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalQualifications in body.
     */
    @GetMapping(value = "/professional-qualifications", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ProfessionalQualificationDTO>>> getAllProfessionalQualifications(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ProfessionalQualifications");
        return professionalQualificationService
            .countAll()
            .zipWith(professionalQualificationService.findAll(pageable).collectList())
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
     * {@code GET  /professional-qualifications/:id} : get the "id" professionalQualification.
     *
     * @param id the id of the professionalQualificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalQualificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/professional-qualifications/{id}")
    public Mono<ResponseEntity<ProfessionalQualificationDTO>> getProfessionalQualification(@PathVariable Long id) {
        log.debug("REST request to get ProfessionalQualification : {}", id);
        Mono<ProfessionalQualificationDTO> professionalQualificationDTO = professionalQualificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalQualificationDTO);
    }

    /**
     * {@code DELETE  /professional-qualifications/:id} : delete the "id" professionalQualification.
     *
     * @param id the id of the professionalQualificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/professional-qualifications/{id}")
    public Mono<ResponseEntity<Void>> deleteProfessionalQualification(@PathVariable Long id) {
        log.debug("REST request to delete ProfessionalQualification : {}", id);
        return professionalQualificationService
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
