package com.sohclick.hrps.payroll.web.rest;

import com.sohclick.hrps.payroll.repository.TimeAndAttendanceRepository;
import com.sohclick.hrps.payroll.service.TimeAndAttendanceService;
import com.sohclick.hrps.payroll.service.dto.TimeAndAttendanceDTO;
import com.sohclick.hrps.payroll.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.sohclick.hrps.payroll.domain.TimeAndAttendance}.
 */
@RestController
@RequestMapping("/api")
public class TimeAndAttendanceResource {

    private final Logger log = LoggerFactory.getLogger(TimeAndAttendanceResource.class);

    private static final String ENTITY_NAME = "hrpsPayrollTimeAndAttendance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeAndAttendanceService timeAndAttendanceService;

    private final TimeAndAttendanceRepository timeAndAttendanceRepository;

    public TimeAndAttendanceResource(
        TimeAndAttendanceService timeAndAttendanceService,
        TimeAndAttendanceRepository timeAndAttendanceRepository
    ) {
        this.timeAndAttendanceService = timeAndAttendanceService;
        this.timeAndAttendanceRepository = timeAndAttendanceRepository;
    }

    /**
     * {@code POST  /time-and-attendances} : Create a new timeAndAttendance.
     *
     * @param timeAndAttendanceDTO the timeAndAttendanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timeAndAttendanceDTO, or with status {@code 400 (Bad Request)} if the timeAndAttendance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/time-and-attendances")
    public Mono<ResponseEntity<TimeAndAttendanceDTO>> createTimeAndAttendance(@RequestBody TimeAndAttendanceDTO timeAndAttendanceDTO)
        throws URISyntaxException {
        log.debug("REST request to save TimeAndAttendance : {}", timeAndAttendanceDTO);
        if (timeAndAttendanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new timeAndAttendance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return timeAndAttendanceService
            .save(timeAndAttendanceDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/time-and-attendances/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /time-and-attendances/:id} : Updates an existing timeAndAttendance.
     *
     * @param id the id of the timeAndAttendanceDTO to save.
     * @param timeAndAttendanceDTO the timeAndAttendanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeAndAttendanceDTO,
     * or with status {@code 400 (Bad Request)} if the timeAndAttendanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timeAndAttendanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/time-and-attendances/{id}")
    public Mono<ResponseEntity<TimeAndAttendanceDTO>> updateTimeAndAttendance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TimeAndAttendanceDTO timeAndAttendanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TimeAndAttendance : {}, {}", id, timeAndAttendanceDTO);
        if (timeAndAttendanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeAndAttendanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return timeAndAttendanceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return timeAndAttendanceService
                    .update(timeAndAttendanceDTO)
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
     * {@code PATCH  /time-and-attendances/:id} : Partial updates given fields of an existing timeAndAttendance, field will ignore if it is null
     *
     * @param id the id of the timeAndAttendanceDTO to save.
     * @param timeAndAttendanceDTO the timeAndAttendanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeAndAttendanceDTO,
     * or with status {@code 400 (Bad Request)} if the timeAndAttendanceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timeAndAttendanceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timeAndAttendanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/time-and-attendances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TimeAndAttendanceDTO>> partialUpdateTimeAndAttendance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TimeAndAttendanceDTO timeAndAttendanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimeAndAttendance partially : {}, {}", id, timeAndAttendanceDTO);
        if (timeAndAttendanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeAndAttendanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return timeAndAttendanceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TimeAndAttendanceDTO> result = timeAndAttendanceService.partialUpdate(timeAndAttendanceDTO);

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
     * {@code GET  /time-and-attendances} : get all the timeAndAttendances.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timeAndAttendances in body.
     */
    @GetMapping(value = "/time-and-attendances", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<TimeAndAttendanceDTO>>> getAllTimeAndAttendances(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String filter
    ) {
        if ("payroll-is-null".equals(filter)) {
            log.debug("REST request to get all TimeAndAttendances where payroll is null");
            return new ResponseEntity<>(timeAndAttendanceService.findAllWherePayrollIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of TimeAndAttendances");
        return timeAndAttendanceService
            .countAll()
            .zipWith(timeAndAttendanceService.findAll(pageable).collectList())
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
     * {@code GET  /time-and-attendances/:id} : get the "id" timeAndAttendance.
     *
     * @param id the id of the timeAndAttendanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeAndAttendanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/time-and-attendances/{id}")
    public Mono<ResponseEntity<TimeAndAttendanceDTO>> getTimeAndAttendance(@PathVariable Long id) {
        log.debug("REST request to get TimeAndAttendance : {}", id);
        Mono<TimeAndAttendanceDTO> timeAndAttendanceDTO = timeAndAttendanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeAndAttendanceDTO);
    }

    /**
     * {@code DELETE  /time-and-attendances/:id} : delete the "id" timeAndAttendance.
     *
     * @param id the id of the timeAndAttendanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/time-and-attendances/{id}")
    public Mono<ResponseEntity<Void>> deleteTimeAndAttendance(@PathVariable Long id) {
        log.debug("REST request to delete TimeAndAttendance : {}", id);
        return timeAndAttendanceService
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
