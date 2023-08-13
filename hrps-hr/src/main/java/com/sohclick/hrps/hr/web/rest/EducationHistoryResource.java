package com.sohclick.hrps.hr.web.rest;

import com.sohclick.hrps.hr.repository.EducationHistoryRepository;
import com.sohclick.hrps.hr.service.EducationHistoryService;
import com.sohclick.hrps.hr.service.dto.EducationHistoryDTO;
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
 * REST controller for managing {@link com.sohclick.hrps.hr.domain.EducationHistory}.
 */
@RestController
@RequestMapping("/api")
public class EducationHistoryResource {

    private final Logger log = LoggerFactory.getLogger(EducationHistoryResource.class);

    private static final String ENTITY_NAME = "hrpsHrEducationHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EducationHistoryService educationHistoryService;

    private final EducationHistoryRepository educationHistoryRepository;

    public EducationHistoryResource(
        EducationHistoryService educationHistoryService,
        EducationHistoryRepository educationHistoryRepository
    ) {
        this.educationHistoryService = educationHistoryService;
        this.educationHistoryRepository = educationHistoryRepository;
    }

    /**
     * {@code POST  /education-histories} : Create a new educationHistory.
     *
     * @param educationHistoryDTO the educationHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new educationHistoryDTO, or with status {@code 400 (Bad Request)} if the educationHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/education-histories")
    public Mono<ResponseEntity<EducationHistoryDTO>> createEducationHistory(@RequestBody EducationHistoryDTO educationHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save EducationHistory : {}", educationHistoryDTO);
        if (educationHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new educationHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return educationHistoryService
            .save(educationHistoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/education-histories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /education-histories/:id} : Updates an existing educationHistory.
     *
     * @param id the id of the educationHistoryDTO to save.
     * @param educationHistoryDTO the educationHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the educationHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the educationHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/education-histories/{id}")
    public Mono<ResponseEntity<EducationHistoryDTO>> updateEducationHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EducationHistoryDTO educationHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EducationHistory : {}, {}", id, educationHistoryDTO);
        if (educationHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, educationHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return educationHistoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return educationHistoryService
                    .update(educationHistoryDTO)
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
     * {@code PATCH  /education-histories/:id} : Partial updates given fields of an existing educationHistory, field will ignore if it is null
     *
     * @param id the id of the educationHistoryDTO to save.
     * @param educationHistoryDTO the educationHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the educationHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the educationHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the educationHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/education-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EducationHistoryDTO>> partialUpdateEducationHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EducationHistoryDTO educationHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EducationHistory partially : {}, {}", id, educationHistoryDTO);
        if (educationHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, educationHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return educationHistoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EducationHistoryDTO> result = educationHistoryService.partialUpdate(educationHistoryDTO);

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
     * {@code GET  /education-histories} : get all the educationHistories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of educationHistories in body.
     */
    @GetMapping(value = "/education-histories", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<EducationHistoryDTO>>> getAllEducationHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of EducationHistories");
        return educationHistoryService
            .countAll()
            .zipWith(educationHistoryService.findAll(pageable).collectList())
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
     * {@code GET  /education-histories/:id} : get the "id" educationHistory.
     *
     * @param id the id of the educationHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the educationHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/education-histories/{id}")
    public Mono<ResponseEntity<EducationHistoryDTO>> getEducationHistory(@PathVariable Long id) {
        log.debug("REST request to get EducationHistory : {}", id);
        Mono<EducationHistoryDTO> educationHistoryDTO = educationHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(educationHistoryDTO);
    }

    /**
     * {@code DELETE  /education-histories/:id} : delete the "id" educationHistory.
     *
     * @param id the id of the educationHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/education-histories/{id}")
    public Mono<ResponseEntity<Void>> deleteEducationHistory(@PathVariable Long id) {
        log.debug("REST request to delete EducationHistory : {}", id);
        return educationHistoryService
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
