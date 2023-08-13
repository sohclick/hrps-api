package com.sohclick.hrps.payroll.service;

import com.sohclick.hrps.payroll.service.dto.PayrollDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.payroll.domain.Payroll}.
 */
public interface PayrollService {
    /**
     * Save a payroll.
     *
     * @param payrollDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PayrollDTO> save(PayrollDTO payrollDTO);

    /**
     * Updates a payroll.
     *
     * @param payrollDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PayrollDTO> update(PayrollDTO payrollDTO);

    /**
     * Partially updates a payroll.
     *
     * @param payrollDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PayrollDTO> partialUpdate(PayrollDTO payrollDTO);

    /**
     * Get all the payrolls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PayrollDTO> findAll(Pageable pageable);

    /**
     * Get all the PayrollDTO where Employee is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<PayrollDTO> findAllWhereEmployeeIsNull();

    /**
     * Returns the number of payrolls available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" payroll.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PayrollDTO> findOne(Long id);

    /**
     * Delete the "id" payroll.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
