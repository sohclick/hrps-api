package com.sohclick.hrps.hr.web.rest;

import com.sohclick.hrps.hr.repository.EmergencyContactRepository;
import com.sohclick.hrps.hr.service.EmergencyContactService;
import com.sohclick.hrps.hr.service.dto.EmergencyContactDTO;
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
 * REST controller for managing {@link com.sohclick.hrps.hr.domain.EmergencyContact}.
 */
@RestController
@RequestMapping("/api")
public class EmergencyContactResource {

    private final Logger log = LoggerFactory.getLogger(EmergencyContactResource.class);

    private static final String ENTITY_NAME = "hrpsHrEmergencyContact";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmergencyContactService emergencyContactService;

    private final EmergencyContactRepository emergencyContactRepository;

    public EmergencyContactResource(
        EmergencyContactService emergencyContactService,
        EmergencyContactRepository emergencyContactRepository
    ) {
        this.emergencyContactService = emergencyContactService;
        this.emergencyContactRepository = emergencyContactRepository;
    }

    /**
     * {@code POST  /emergency-contacts} : Create a new emergencyContact.
     *
     * @param emergencyContactDTO the emergencyContactDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emergencyContactDTO, or with status {@code 400 (Bad Request)} if the emergencyContact has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/emergency-contacts")
    public Mono<ResponseEntity<EmergencyContactDTO>> createEmergencyContact(@Valid @RequestBody EmergencyContactDTO emergencyContactDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmergencyContact : {}", emergencyContactDTO);
        if (emergencyContactDTO.getId() != null) {
            throw new BadRequestAlertException("A new emergencyContact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return emergencyContactService
            .save(emergencyContactDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/emergency-contacts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /emergency-contacts/:id} : Updates an existing emergencyContact.
     *
     * @param id the id of the emergencyContactDTO to save.
     * @param emergencyContactDTO the emergencyContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emergencyContactDTO,
     * or with status {@code 400 (Bad Request)} if the emergencyContactDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emergencyContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/emergency-contacts/{id}")
    public Mono<ResponseEntity<EmergencyContactDTO>> updateEmergencyContact(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmergencyContactDTO emergencyContactDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmergencyContact : {}, {}", id, emergencyContactDTO);
        if (emergencyContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emergencyContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return emergencyContactRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return emergencyContactService
                    .update(emergencyContactDTO)
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
     * {@code PATCH  /emergency-contacts/:id} : Partial updates given fields of an existing emergencyContact, field will ignore if it is null
     *
     * @param id the id of the emergencyContactDTO to save.
     * @param emergencyContactDTO the emergencyContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emergencyContactDTO,
     * or with status {@code 400 (Bad Request)} if the emergencyContactDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emergencyContactDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emergencyContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/emergency-contacts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EmergencyContactDTO>> partialUpdateEmergencyContact(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmergencyContactDTO emergencyContactDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmergencyContact partially : {}, {}", id, emergencyContactDTO);
        if (emergencyContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emergencyContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return emergencyContactRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EmergencyContactDTO> result = emergencyContactService.partialUpdate(emergencyContactDTO);

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
     * {@code GET  /emergency-contacts} : get all the emergencyContacts.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emergencyContacts in body.
     */
    @GetMapping(value = "/emergency-contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<EmergencyContactDTO>>> getAllEmergencyContacts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String filter
    ) {
        if ("employee-is-null".equals(filter)) {
            log.debug("REST request to get all EmergencyContacts where employee is null");
            return new ResponseEntity<>(emergencyContactService.findAllWhereEmployeeIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of EmergencyContacts");
        return emergencyContactService
            .countAll()
            .zipWith(emergencyContactService.findAll(pageable).collectList())
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
     * {@code GET  /emergency-contacts/:id} : get the "id" emergencyContact.
     *
     * @param id the id of the emergencyContactDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emergencyContactDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/emergency-contacts/{id}")
    public Mono<ResponseEntity<EmergencyContactDTO>> getEmergencyContact(@PathVariable Long id) {
        log.debug("REST request to get EmergencyContact : {}", id);
        Mono<EmergencyContactDTO> emergencyContactDTO = emergencyContactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emergencyContactDTO);
    }

    /**
     * {@code DELETE  /emergency-contacts/:id} : delete the "id" emergencyContact.
     *
     * @param id the id of the emergencyContactDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/emergency-contacts/{id}")
    public Mono<ResponseEntity<Void>> deleteEmergencyContact(@PathVariable Long id) {
        log.debug("REST request to delete EmergencyContact : {}", id);
        return emergencyContactService
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
