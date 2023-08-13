package com.sohclick.hrps.hr.web.rest;

import com.sohclick.hrps.hr.repository.NextOfKinRepository;
import com.sohclick.hrps.hr.service.NextOfKinService;
import com.sohclick.hrps.hr.service.dto.NextOfKinDTO;
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
 * REST controller for managing {@link com.sohclick.hrps.hr.domain.NextOfKin}.
 */
@RestController
@RequestMapping("/api")
public class NextOfKinResource {

    private final Logger log = LoggerFactory.getLogger(NextOfKinResource.class);

    private static final String ENTITY_NAME = "hrpsHrNextOfKin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOfKinService nextOfKinService;

    private final NextOfKinRepository nextOfKinRepository;

    public NextOfKinResource(NextOfKinService nextOfKinService, NextOfKinRepository nextOfKinRepository) {
        this.nextOfKinService = nextOfKinService;
        this.nextOfKinRepository = nextOfKinRepository;
    }

    /**
     * {@code POST  /next-of-kins} : Create a new nextOfKin.
     *
     * @param nextOfKinDTO the nextOfKinDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOfKinDTO, or with status {@code 400 (Bad Request)} if the nextOfKin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/next-of-kins")
    public Mono<ResponseEntity<NextOfKinDTO>> createNextOfKin(@Valid @RequestBody NextOfKinDTO nextOfKinDTO) throws URISyntaxException {
        log.debug("REST request to save NextOfKin : {}", nextOfKinDTO);
        if (nextOfKinDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextOfKin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return nextOfKinService
            .save(nextOfKinDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/next-of-kins/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /next-of-kins/:id} : Updates an existing nextOfKin.
     *
     * @param id the id of the nextOfKinDTO to save.
     * @param nextOfKinDTO the nextOfKinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOfKinDTO,
     * or with status {@code 400 (Bad Request)} if the nextOfKinDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOfKinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/next-of-kins/{id}")
    public Mono<ResponseEntity<NextOfKinDTO>> updateNextOfKin(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOfKinDTO nextOfKinDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NextOfKin : {}, {}", id, nextOfKinDTO);
        if (nextOfKinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOfKinDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return nextOfKinRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return nextOfKinService
                    .update(nextOfKinDTO)
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
     * {@code PATCH  /next-of-kins/:id} : Partial updates given fields of an existing nextOfKin, field will ignore if it is null
     *
     * @param id the id of the nextOfKinDTO to save.
     * @param nextOfKinDTO the nextOfKinDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOfKinDTO,
     * or with status {@code 400 (Bad Request)} if the nextOfKinDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextOfKinDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOfKinDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/next-of-kins/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<NextOfKinDTO>> partialUpdateNextOfKin(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOfKinDTO nextOfKinDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NextOfKin partially : {}, {}", id, nextOfKinDTO);
        if (nextOfKinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOfKinDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return nextOfKinRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<NextOfKinDTO> result = nextOfKinService.partialUpdate(nextOfKinDTO);

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
     * {@code GET  /next-of-kins} : get all the nextOfKins.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOfKins in body.
     */
    @GetMapping(value = "/next-of-kins", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<NextOfKinDTO>>> getAllNextOfKins(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String filter
    ) {
        if ("employee-is-null".equals(filter)) {
            log.debug("REST request to get all NextOfKins where employee is null");
            return new ResponseEntity<>(nextOfKinService.findAllWhereEmployeeIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of NextOfKins");
        return nextOfKinService
            .countAll()
            .zipWith(nextOfKinService.findAll(pageable).collectList())
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
     * {@code GET  /next-of-kins/:id} : get the "id" nextOfKin.
     *
     * @param id the id of the nextOfKinDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOfKinDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/next-of-kins/{id}")
    public Mono<ResponseEntity<NextOfKinDTO>> getNextOfKin(@PathVariable Long id) {
        log.debug("REST request to get NextOfKin : {}", id);
        Mono<NextOfKinDTO> nextOfKinDTO = nextOfKinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOfKinDTO);
    }

    /**
     * {@code DELETE  /next-of-kins/:id} : delete the "id" nextOfKin.
     *
     * @param id the id of the nextOfKinDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/next-of-kins/{id}")
    public Mono<ResponseEntity<Void>> deleteNextOfKin(@PathVariable Long id) {
        log.debug("REST request to delete NextOfKin : {}", id);
        return nextOfKinService
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
