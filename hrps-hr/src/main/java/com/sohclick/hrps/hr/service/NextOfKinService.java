package com.sohclick.hrps.hr.service;

import com.sohclick.hrps.hr.service.dto.NextOfKinDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.hr.domain.NextOfKin}.
 */
public interface NextOfKinService {
    /**
     * Save a nextOfKin.
     *
     * @param nextOfKinDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<NextOfKinDTO> save(NextOfKinDTO nextOfKinDTO);

    /**
     * Updates a nextOfKin.
     *
     * @param nextOfKinDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<NextOfKinDTO> update(NextOfKinDTO nextOfKinDTO);

    /**
     * Partially updates a nextOfKin.
     *
     * @param nextOfKinDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<NextOfKinDTO> partialUpdate(NextOfKinDTO nextOfKinDTO);

    /**
     * Get all the nextOfKins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<NextOfKinDTO> findAll(Pageable pageable);

    /**
     * Returns the number of nextOfKins available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" nextOfKin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<NextOfKinDTO> findOne(Long id);

    /**
     * Delete the "id" nextOfKin.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
