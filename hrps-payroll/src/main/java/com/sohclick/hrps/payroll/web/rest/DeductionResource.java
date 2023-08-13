package com.sohclick.hrps.payroll.web.rest;

import com.sohclick.hrps.payroll.repository.DeductionRepository;
import com.sohclick.hrps.payroll.service.DeductionService;
import com.sohclick.hrps.payroll.service.dto.DeductionDTO;
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
 * REST controller for managing {@link com.sohclick.hrps.payroll.domain.Deduction}.
 */
@RestController
@RequestMapping("/api")
public class DeductionResource {

    private final Logger log = LoggerFactory.getLogger(DeductionResource.class);

    private static final String ENTITY_NAME = "hrpsPayrollDeduction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeductionService deductionService;

    private final DeductionRepository deductionRepository;

    public DeductionResource(DeductionService deductionService, DeductionRepository deductionRepository) {
        this.deductionService = deductionService;
        this.deductionRepository = deductionRepository;
    }

    /**
     * {@code POST  /deductions} : Create a new deduction.
     *
     * @param deductionDTO the deductionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deductionDTO, or with status {@code 400 (Bad Request)} if the deduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deductions")
    public Mono<ResponseEntity<DeductionDTO>> createDeduction(@RequestBody DeductionDTO deductionDTO) throws URISyntaxException {
        log.debug("REST request to save Deduction : {}", deductionDTO);
        if (deductionDTO.getId() != null) {
            throw new BadRequestAlertException("A new deduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return deductionService
            .save(deductionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/deductions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /deductions/:id} : Updates an existing deduction.
     *
     * @param id the id of the deductionDTO to save.
     * @param deductionDTO the deductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deductionDTO,
     * or with status {@code 400 (Bad Request)} if the deductionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deductions/{id}")
    public Mono<ResponseEntity<DeductionDTO>> updateDeduction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeductionDTO deductionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Deduction : {}, {}", id, deductionDTO);
        if (deductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deductionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return deductionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return deductionService
                    .update(deductionDTO)
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
     * {@code PATCH  /deductions/:id} : Partial updates given fields of an existing deduction, field will ignore if it is null
     *
     * @param id the id of the deductionDTO to save.
     * @param deductionDTO the deductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deductionDTO,
     * or with status {@code 400 (Bad Request)} if the deductionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deductionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deductions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DeductionDTO>> partialUpdateDeduction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeductionDTO deductionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deduction partially : {}, {}", id, deductionDTO);
        if (deductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deductionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return deductionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DeductionDTO> result = deductionService.partialUpdate(deductionDTO);

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
     * {@code GET  /deductions} : get all the deductions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deductions in body.
     */
    @GetMapping(value = "/deductions", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<DeductionDTO>>> getAllDeductions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String filter
    ) {
        if ("payroll-is-null".equals(filter)) {
            log.debug("REST request to get all Deductions where payroll is null");
            return new ResponseEntity<>(deductionService.findAllWherePayrollIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Deductions");
        return deductionService
            .countAll()
            .zipWith(deductionService.findAll(pageable).collectList())
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
     * {@code GET  /deductions/:id} : get the "id" deduction.
     *
     * @param id the id of the deductionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deductionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deductions/{id}")
    public Mono<ResponseEntity<DeductionDTO>> getDeduction(@PathVariable Long id) {
        log.debug("REST request to get Deduction : {}", id);
        Mono<DeductionDTO> deductionDTO = deductionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deductionDTO);
    }

    /**
     * {@code DELETE  /deductions/:id} : delete the "id" deduction.
     *
     * @param id the id of the deductionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deductions/{id}")
    public Mono<ResponseEntity<Void>> deleteDeduction(@PathVariable Long id) {
        log.debug("REST request to delete Deduction : {}", id);
        return deductionService
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
