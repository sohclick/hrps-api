package com.sohclick.hrps.payroll.service;

import com.sohclick.hrps.payroll.service.dto.DeductionDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.payroll.domain.Deduction}.
 */
public interface DeductionService {
    /**
     * Save a deduction.
     *
     * @param deductionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DeductionDTO> save(DeductionDTO deductionDTO);

    /**
     * Updates a deduction.
     *
     * @param deductionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<DeductionDTO> update(DeductionDTO deductionDTO);

    /**
     * Partially updates a deduction.
     *
     * @param deductionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DeductionDTO> partialUpdate(DeductionDTO deductionDTO);

    /**
     * Get all the deductions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<DeductionDTO> findAll(Pageable pageable);

    /**
     * Get all the DeductionDTO where Payroll is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<DeductionDTO> findAllWherePayrollIsNull();

    /**
     * Returns the number of deductions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" deduction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DeductionDTO> findOne(Long id);

    /**
     * Delete the "id" deduction.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
