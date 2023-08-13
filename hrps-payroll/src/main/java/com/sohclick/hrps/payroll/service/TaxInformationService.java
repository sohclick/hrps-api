package com.sohclick.hrps.payroll.service;

import com.sohclick.hrps.payroll.service.dto.TaxInformationDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.payroll.domain.TaxInformation}.
 */
public interface TaxInformationService {
    /**
     * Save a taxInformation.
     *
     * @param taxInformationDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TaxInformationDTO> save(TaxInformationDTO taxInformationDTO);

    /**
     * Updates a taxInformation.
     *
     * @param taxInformationDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TaxInformationDTO> update(TaxInformationDTO taxInformationDTO);

    /**
     * Partially updates a taxInformation.
     *
     * @param taxInformationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TaxInformationDTO> partialUpdate(TaxInformationDTO taxInformationDTO);

    /**
     * Get all the taxInformations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TaxInformationDTO> findAll(Pageable pageable);

    /**
     * Get all the TaxInformationDTO where Payroll is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<TaxInformationDTO> findAllWherePayrollIsNull();

    /**
     * Returns the number of taxInformations available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" taxInformation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TaxInformationDTO> findOne(Long id);

    /**
     * Delete the "id" taxInformation.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
