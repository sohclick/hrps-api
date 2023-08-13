package com.sohclick.hrps.payroll.service;

import com.sohclick.hrps.payroll.service.dto.BenefitDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.payroll.domain.Benefit}.
 */
public interface BenefitService {
    /**
     * Save a benefit.
     *
     * @param benefitDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<BenefitDTO> save(BenefitDTO benefitDTO);

    /**
     * Updates a benefit.
     *
     * @param benefitDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<BenefitDTO> update(BenefitDTO benefitDTO);

    /**
     * Partially updates a benefit.
     *
     * @param benefitDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<BenefitDTO> partialUpdate(BenefitDTO benefitDTO);

    /**
     * Get all the benefits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<BenefitDTO> findAll(Pageable pageable);

    /**
     * Get all the BenefitDTO where Payroll is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<BenefitDTO> findAllWherePayrollIsNull();

    /**
     * Returns the number of benefits available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" benefit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<BenefitDTO> findOne(Long id);

    /**
     * Delete the "id" benefit.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
