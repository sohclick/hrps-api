package com.sohclick.hrps.hr.service;

import com.sohclick.hrps.hr.service.dto.RefereeDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.hr.domain.Referee}.
 */
public interface RefereeService {
    /**
     * Save a referee.
     *
     * @param refereeDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<RefereeDTO> save(RefereeDTO refereeDTO);

    /**
     * Updates a referee.
     *
     * @param refereeDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<RefereeDTO> update(RefereeDTO refereeDTO);

    /**
     * Partially updates a referee.
     *
     * @param refereeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<RefereeDTO> partialUpdate(RefereeDTO refereeDTO);

    /**
     * Get all the referees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<RefereeDTO> findAll(Pageable pageable);

    /**
     * Returns the number of referees available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" referee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<RefereeDTO> findOne(Long id);

    /**
     * Delete the "id" referee.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
