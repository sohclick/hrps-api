package com.sohclick.hrps.hr.web.rest;

import com.sohclick.hrps.hr.repository.RefereeRepository;
import com.sohclick.hrps.hr.service.RefereeService;
import com.sohclick.hrps.hr.service.dto.RefereeDTO;
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
 * REST controller for managing {@link com.sohclick.hrps.hr.domain.Referee}.
 */
@RestController
@RequestMapping("/api")
public class RefereeResource {

    private final Logger log = LoggerFactory.getLogger(RefereeResource.class);

    private static final String ENTITY_NAME = "hrpsHrReferee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RefereeService refereeService;

    private final RefereeRepository refereeRepository;

    public RefereeResource(RefereeService refereeService, RefereeRepository refereeRepository) {
        this.refereeService = refereeService;
        this.refereeRepository = refereeRepository;
    }

    /**
     * {@code POST  /referees} : Create a new referee.
     *
     * @param refereeDTO the refereeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new refereeDTO, or with status {@code 400 (Bad Request)} if the referee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/referees")
    public Mono<ResponseEntity<RefereeDTO>> createReferee(@Valid @RequestBody RefereeDTO refereeDTO) throws URISyntaxException {
        log.debug("REST request to save Referee : {}", refereeDTO);
        if (refereeDTO.getId() != null) {
            throw new BadRequestAlertException("A new referee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return refereeService
            .save(refereeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/referees/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /referees/:id} : Updates an existing referee.
     *
     * @param id the id of the refereeDTO to save.
     * @param refereeDTO the refereeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refereeDTO,
     * or with status {@code 400 (Bad Request)} if the refereeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the refereeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/referees/{id}")
    public Mono<ResponseEntity<RefereeDTO>> updateReferee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RefereeDTO refereeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Referee : {}, {}", id, refereeDTO);
        if (refereeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refereeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return refereeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return refereeService
                    .update(refereeDTO)
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
     * {@code PATCH  /referees/:id} : Partial updates given fields of an existing referee, field will ignore if it is null
     *
     * @param id the id of the refereeDTO to save.
     * @param refereeDTO the refereeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refereeDTO,
     * or with status {@code 400 (Bad Request)} if the refereeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the refereeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the refereeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/referees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RefereeDTO>> partialUpdateReferee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RefereeDTO refereeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Referee partially : {}, {}", id, refereeDTO);
        if (refereeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refereeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return refereeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RefereeDTO> result = refereeService.partialUpdate(refereeDTO);

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
     * {@code GET  /referees} : get all the referees.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referees in body.
     */
    @GetMapping(value = "/referees", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<RefereeDTO>>> getAllReferees(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Referees");
        return refereeService
            .countAll()
            .zipWith(refereeService.findAll(pageable).collectList())
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
     * {@code GET  /referees/:id} : get the "id" referee.
     *
     * @param id the id of the refereeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the refereeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/referees/{id}")
    public Mono<ResponseEntity<RefereeDTO>> getReferee(@PathVariable Long id) {
        log.debug("REST request to get Referee : {}", id);
        Mono<RefereeDTO> refereeDTO = refereeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(refereeDTO);
    }

    /**
     * {@code DELETE  /referees/:id} : delete the "id" referee.
     *
     * @param id the id of the refereeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/referees/{id}")
    public Mono<ResponseEntity<Void>> deleteReferee(@PathVariable Long id) {
        log.debug("REST request to delete Referee : {}", id);
        return refereeService
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
