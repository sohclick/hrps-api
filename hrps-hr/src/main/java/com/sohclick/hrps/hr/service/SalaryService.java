package com.sohclick.hrps.hr.service;

import com.sohclick.hrps.hr.service.dto.SalaryDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.hr.domain.Salary}.
 */
public interface SalaryService {
    /**
     * Save a salary.
     *
     * @param salaryDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SalaryDTO> save(SalaryDTO salaryDTO);

    /**
     * Updates a salary.
     *
     * @param salaryDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SalaryDTO> update(SalaryDTO salaryDTO);

    /**
     * Partially updates a salary.
     *
     * @param salaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SalaryDTO> partialUpdate(SalaryDTO salaryDTO);

    /**
     * Get all the salaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SalaryDTO> findAll(Pageable pageable);

    /**
     * Returns the number of salaries available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" salary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SalaryDTO> findOne(Long id);

    /**
     * Delete the "id" salary.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
