package com.sohclick.hrps.hr.web.rest;

import com.sohclick.hrps.hr.repository.WorkHistoryRepository;
import com.sohclick.hrps.hr.service.WorkHistoryService;
import com.sohclick.hrps.hr.service.dto.WorkHistoryDTO;
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
 * REST controller for managing {@link com.sohclick.hrps.hr.domain.WorkHistory}.
 */
@RestController
@RequestMapping("/api")
public class WorkHistoryResource {

    private final Logger log = LoggerFactory.getLogger(WorkHistoryResource.class);

    private static final String ENTITY_NAME = "hrpsHrWorkHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkHistoryService workHistoryService;

    private final WorkHistoryRepository workHistoryRepository;

    public WorkHistoryResource(WorkHistoryService workHistoryService, WorkHistoryRepository workHistoryRepository) {
        this.workHistoryService = workHistoryService;
        this.workHistoryRepository = workHistoryRepository;
    }

    /**
     * {@code POST  /work-histories} : Create a new workHistory.
     *
     * @param workHistoryDTO the workHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workHistoryDTO, or with status {@code 400 (Bad Request)} if the workHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-histories")
    public Mono<ResponseEntity<WorkHistoryDTO>> createWorkHistory(@RequestBody WorkHistoryDTO workHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save WorkHistory : {}", workHistoryDTO);
        if (workHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new workHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return workHistoryService
            .save(workHistoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/work-histories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /work-histories/:id} : Updates an existing workHistory.
     *
     * @param id the id of the workHistoryDTO to save.
     * @param workHistoryDTO the workHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the workHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-histories/{id}")
    public Mono<ResponseEntity<WorkHistoryDTO>> updateWorkHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkHistoryDTO workHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkHistory : {}, {}", id, workHistoryDTO);
        if (workHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return workHistoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return workHistoryService
                    .update(workHistoryDTO)
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
     * {@code PATCH  /work-histories/:id} : Partial updates given fields of an existing workHistory, field will ignore if it is null
     *
     * @param id the id of the workHistoryDTO to save.
     * @param workHistoryDTO the workHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the workHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WorkHistoryDTO>> partialUpdateWorkHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkHistoryDTO workHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkHistory partially : {}, {}", id, workHistoryDTO);
        if (workHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return workHistoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WorkHistoryDTO> result = workHistoryService.partialUpdate(workHistoryDTO);

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
     * {@code GET  /work-histories} : get all the workHistories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workHistories in body.
     */
    @GetMapping(value = "/work-histories", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<WorkHistoryDTO>>> getAllWorkHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of WorkHistories");
        return workHistoryService
            .countAll()
            .zipWith(workHistoryService.findAll(pageable).collectList())
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
     * {@code GET  /work-histories/:id} : get the "id" workHistory.
     *
     * @param id the id of the workHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-histories/{id}")
    public Mono<ResponseEntity<WorkHistoryDTO>> getWorkHistory(@PathVariable Long id) {
        log.debug("REST request to get WorkHistory : {}", id);
        Mono<WorkHistoryDTO> workHistoryDTO = workHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workHistoryDTO);
    }

    /**
     * {@code DELETE  /work-histories/:id} : delete the "id" workHistory.
     *
     * @param id the id of the workHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-histories/{id}")
    public Mono<ResponseEntity<Void>> deleteWorkHistory(@PathVariable Long id) {
        log.debug("REST request to delete WorkHistory : {}", id);
        return workHistoryService
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
