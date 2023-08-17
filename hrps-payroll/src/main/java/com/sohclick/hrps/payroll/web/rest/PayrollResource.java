package com.sohclick.hrps.payroll.web.rest;

import com.sohclick.hrps.payroll.repository.PayrollRepository;
import com.sohclick.hrps.payroll.service.PayrollService;
import com.sohclick.hrps.payroll.service.dto.PayrollDTO;
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
 * REST controller for managing {@link com.sohclick.hrps.payroll.domain.Payroll}.
 */
@RestController
@RequestMapping("/api")
public class PayrollResource {

    private final Logger log = LoggerFactory.getLogger(PayrollResource.class);

    private static final String ENTITY_NAME = "hrpsPayrollPayroll";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayrollService payrollService;

    private final PayrollRepository payrollRepository;

    public PayrollResource(PayrollService payrollService, PayrollRepository payrollRepository) {
        this.payrollService = payrollService;
        this.payrollRepository = payrollRepository;
    }

    /**
     * {@code POST  /payrolls} : Create a new payroll.
     *
     * @param payrollDTO the payrollDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payrollDTO, or with status {@code 400 (Bad Request)} if the payroll has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payrolls")
    public Mono<ResponseEntity<PayrollDTO>> createPayroll(@Valid @RequestBody PayrollDTO payrollDTO) throws URISyntaxException {
        log.debug("REST request to save Payroll : {}", payrollDTO);
        if (payrollDTO.getId() != null) {
            throw new BadRequestAlertException("A new payroll cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return payrollService
            .save(payrollDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/payrolls/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /payrolls/:id} : Updates an existing payroll.
     *
     * @param id the id of the payrollDTO to save.
     * @param payrollDTO the payrollDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payrollDTO,
     * or with status {@code 400 (Bad Request)} if the payrollDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payrollDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payrolls/{id}")
    public Mono<ResponseEntity<PayrollDTO>> updatePayroll(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PayrollDTO payrollDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Payroll : {}, {}", id, payrollDTO);
        if (payrollDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payrollDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return payrollRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return payrollService
                    .update(payrollDTO)
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
     * {@code PATCH  /payrolls/:id} : Partial updates given fields of an existing payroll, field will ignore if it is null
     *
     * @param id the id of the payrollDTO to save.
     * @param payrollDTO the payrollDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payrollDTO,
     * or with status {@code 400 (Bad Request)} if the payrollDTO is not valid,
     * or with status {@code 404 (Not Found)} if the payrollDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the payrollDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payrolls/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PayrollDTO>> partialUpdatePayroll(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PayrollDTO payrollDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Payroll partially : {}, {}", id, payrollDTO);
        if (payrollDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payrollDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return payrollRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PayrollDTO> result = payrollService.partialUpdate(payrollDTO);

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
     * {@code GET  /payrolls} : get all the payrolls.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payrolls in body.
     */
    @GetMapping(value = "/payrolls", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PayrollDTO>>> getAllPayrolls(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String filter
    ) {

        log.debug("REST request to get a page of Payrolls");
        return payrollService
            .countAll()
            .zipWith(payrollService.findAll(pageable).collectList())
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
     * {@code GET  /payrolls/:id} : get the "id" payroll.
     *
     * @param id the id of the payrollDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payrollDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payrolls/{id}")
    public Mono<ResponseEntity<PayrollDTO>> getPayroll(@PathVariable Long id) {
        log.debug("REST request to get Payroll : {}", id);
        Mono<PayrollDTO> payrollDTO = payrollService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payrollDTO);
    }

    /**
     * {@code DELETE  /payrolls/:id} : delete the "id" payroll.
     *
     * @param id the id of the payrollDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payrolls/{id}")
    public Mono<ResponseEntity<Void>> deletePayroll(@PathVariable Long id) {
        log.debug("REST request to delete Payroll : {}", id);
        return payrollService
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
