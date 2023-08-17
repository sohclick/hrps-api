package com.sohclick.hrps.payroll.web.rest;

import com.sohclick.hrps.payroll.repository.TaxInformationRepository;
import com.sohclick.hrps.payroll.service.TaxInformationService;
import com.sohclick.hrps.payroll.service.dto.TaxInformationDTO;
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
 * REST controller for managing {@link com.sohclick.hrps.payroll.domain.TaxInformation}.
 */
@RestController
@RequestMapping("/api")
public class TaxInformationResource {

    private final Logger log = LoggerFactory.getLogger(TaxInformationResource.class);

    private static final String ENTITY_NAME = "hrpsPayrollTaxInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxInformationService taxInformationService;

    private final TaxInformationRepository taxInformationRepository;

    public TaxInformationResource(TaxInformationService taxInformationService, TaxInformationRepository taxInformationRepository) {
        this.taxInformationService = taxInformationService;
        this.taxInformationRepository = taxInformationRepository;
    }

    /**
     * {@code POST  /tax-informations} : Create a new taxInformation.
     *
     * @param taxInformationDTO the taxInformationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxInformationDTO, or with status {@code 400 (Bad Request)} if the taxInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tax-informations")
    public Mono<ResponseEntity<TaxInformationDTO>> createTaxInformation(@RequestBody TaxInformationDTO taxInformationDTO)
        throws URISyntaxException {
        log.debug("REST request to save TaxInformation : {}", taxInformationDTO);
        if (taxInformationDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return taxInformationService
            .save(taxInformationDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/tax-informations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /tax-informations/:id} : Updates an existing taxInformation.
     *
     * @param id the id of the taxInformationDTO to save.
     * @param taxInformationDTO the taxInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxInformationDTO,
     * or with status {@code 400 (Bad Request)} if the taxInformationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tax-informations/{id}")
    public Mono<ResponseEntity<TaxInformationDTO>> updateTaxInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaxInformationDTO taxInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaxInformation : {}, {}", id, taxInformationDTO);
        if (taxInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return taxInformationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return taxInformationService
                    .update(taxInformationDTO)
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
     * {@code PATCH  /tax-informations/:id} : Partial updates given fields of an existing taxInformation, field will ignore if it is null
     *
     * @param id the id of the taxInformationDTO to save.
     * @param taxInformationDTO the taxInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxInformationDTO,
     * or with status {@code 400 (Bad Request)} if the taxInformationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taxInformationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tax-informations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TaxInformationDTO>> partialUpdateTaxInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaxInformationDTO taxInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaxInformation partially : {}, {}", id, taxInformationDTO);
        if (taxInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return taxInformationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TaxInformationDTO> result = taxInformationService.partialUpdate(taxInformationDTO);

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
     * {@code GET  /tax-informations} : get all the taxInformations.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxInformations in body.
     */
    @GetMapping(value = "/tax-informations", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<TaxInformationDTO>>> getAllTaxInformations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String filter
    ) {

        log.debug("REST request to get a page of TaxInformations");
        return taxInformationService
            .countAll()
            .zipWith(taxInformationService.findAll(pageable).collectList())
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
     * {@code GET  /tax-informations/:id} : get the "id" taxInformation.
     *
     * @param id the id of the taxInformationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxInformationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tax-informations/{id}")
    public Mono<ResponseEntity<TaxInformationDTO>> getTaxInformation(@PathVariable Long id) {
        log.debug("REST request to get TaxInformation : {}", id);
        Mono<TaxInformationDTO> taxInformationDTO = taxInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxInformationDTO);
    }

    /**
     * {@code DELETE  /tax-informations/:id} : delete the "id" taxInformation.
     *
     * @param id the id of the taxInformationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tax-informations/{id}")
    public Mono<ResponseEntity<Void>> deleteTaxInformation(@PathVariable Long id) {
        log.debug("REST request to delete TaxInformation : {}", id);
        return taxInformationService
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
