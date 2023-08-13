package com.sohclick.hrps.payroll.service;

import com.sohclick.hrps.payroll.service.dto.SalaryComponentDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.payroll.domain.SalaryComponent}.
 */
public interface SalaryComponentService {
    /**
     * Save a salaryComponent.
     *
     * @param salaryComponentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SalaryComponentDTO> save(SalaryComponentDTO salaryComponentDTO);

    /**
     * Updates a salaryComponent.
     *
     * @param salaryComponentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SalaryComponentDTO> update(SalaryComponentDTO salaryComponentDTO);

    /**
     * Partially updates a salaryComponent.
     *
     * @param salaryComponentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SalaryComponentDTO> partialUpdate(SalaryComponentDTO salaryComponentDTO);

    /**
     * Get all the salaryComponents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SalaryComponentDTO> findAll(Pageable pageable);

    /**
     * Returns the number of salaryComponents available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" salaryComponent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SalaryComponentDTO> findOne(Long id);

    /**
     * Delete the "id" salaryComponent.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
