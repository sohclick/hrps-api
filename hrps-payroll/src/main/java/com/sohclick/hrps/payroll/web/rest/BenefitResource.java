package com.sohclick.hrps.payroll.web.rest;

import com.sohclick.hrps.payroll.repository.BenefitRepository;
import com.sohclick.hrps.payroll.service.BenefitService;
import com.sohclick.hrps.payroll.service.dto.BenefitDTO;
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
 * REST controller for managing {@link com.sohclick.hrps.payroll.domain.Benefit}.
 */
@RestController
@RequestMapping("/api")
public class BenefitResource {

    private final Logger log = LoggerFactory.getLogger(BenefitResource.class);

    private static final String ENTITY_NAME = "hrpsPayrollBenefit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BenefitService benefitService;

    private final BenefitRepository benefitRepository;

    public BenefitResource(BenefitService benefitService, BenefitRepository benefitRepository) {
        this.benefitService = benefitService;
        this.benefitRepository = benefitRepository;
    }

    /**
     * {@code POST  /benefits} : Create a new benefit.
     *
     * @param benefitDTO the benefitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new benefitDTO, or with status {@code 400 (Bad Request)} if the benefit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/benefits")
    public Mono<ResponseEntity<BenefitDTO>> createBenefit(@RequestBody BenefitDTO benefitDTO) throws URISyntaxException {
        log.debug("REST request to save Benefit : {}", benefitDTO);
        if (benefitDTO.getId() != null) {
            throw new BadRequestAlertException("A new benefit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return benefitService
            .save(benefitDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/benefits/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /benefits/:id} : Updates an existing benefit.
     *
     * @param id the id of the benefitDTO to save.
     * @param benefitDTO the benefitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated benefitDTO,
     * or with status {@code 400 (Bad Request)} if the benefitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the benefitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/benefits/{id}")
    public Mono<ResponseEntity<BenefitDTO>> updateBenefit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BenefitDTO benefitDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Benefit : {}, {}", id, benefitDTO);
        if (benefitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, benefitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return benefitRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return benefitService
                    .update(benefitDTO)
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
     * {@code PATCH  /benefits/:id} : Partial updates given fields of an existing benefit, field will ignore if it is null
     *
     * @param id the id of the benefitDTO to save.
     * @param benefitDTO the benefitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated benefitDTO,
     * or with status {@code 400 (Bad Request)} if the benefitDTO is not valid,
     * or with status {@code 404 (Not Found)} if the benefitDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the benefitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/benefits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<BenefitDTO>> partialUpdateBenefit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BenefitDTO benefitDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Benefit partially : {}, {}", id, benefitDTO);
        if (benefitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, benefitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return benefitRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<BenefitDTO> result = benefitService.partialUpdate(benefitDTO);

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
     * {@code GET  /benefits} : get all the benefits.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of benefits in body.
     */
    @GetMapping(value = "/benefits", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<BenefitDTO>>> getAllBenefits(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String filter
    ) {
        log.debug("REST request to get a page of Benefits");
        return benefitService
            .countAll()
            .zipWith(benefitService.findAll(pageable).collectList())
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
     * {@code GET  /benefits/:id} : get the "id" benefit.
     *
     * @param id the id of the benefitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the benefitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/benefits/{id}")
    public Mono<ResponseEntity<BenefitDTO>> getBenefit(@PathVariable Long id) {
        log.debug("REST request to get Benefit : {}", id);
        Mono<BenefitDTO> benefitDTO = benefitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(benefitDTO);
    }

    /**
     * {@code DELETE  /benefits/:id} : delete the "id" benefit.
     *
     * @param id the id of the benefitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/benefits/{id}")
    public Mono<ResponseEntity<Void>> deleteBenefit(@PathVariable Long id) {
        log.debug("REST request to delete Benefit : {}", id);
        return benefitService
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
