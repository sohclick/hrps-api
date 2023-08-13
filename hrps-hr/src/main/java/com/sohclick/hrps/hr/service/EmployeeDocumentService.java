package com.sohclick.hrps.hr.service;

import com.sohclick.hrps.hr.service.dto.EmployeeDocumentDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.hr.domain.EmployeeDocument}.
 */
public interface EmployeeDocumentService {
    /**
     * Save a employeeDocument.
     *
     * @param employeeDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<EmployeeDocumentDTO> save(EmployeeDocumentDTO employeeDocumentDTO);

    /**
     * Updates a employeeDocument.
     *
     * @param employeeDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<EmployeeDocumentDTO> update(EmployeeDocumentDTO employeeDocumentDTO);

    /**
     * Partially updates a employeeDocument.
     *
     * @param employeeDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EmployeeDocumentDTO> partialUpdate(EmployeeDocumentDTO employeeDocumentDTO);

    /**
     * Get all the employeeDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EmployeeDocumentDTO> findAll(Pageable pageable);

    /**
     * Returns the number of employeeDocuments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" employeeDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EmployeeDocumentDTO> findOne(Long id);

    /**
     * Delete the "id" employeeDocument.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
