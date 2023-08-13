package com.sohclick.hrps.hr.service;

import com.sohclick.hrps.hr.service.dto.EducationHistoryDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.hr.domain.EducationHistory}.
 */
public interface EducationHistoryService {
    /**
     * Save a educationHistory.
     *
     * @param educationHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<EducationHistoryDTO> save(EducationHistoryDTO educationHistoryDTO);

    /**
     * Updates a educationHistory.
     *
     * @param educationHistoryDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<EducationHistoryDTO> update(EducationHistoryDTO educationHistoryDTO);

    /**
     * Partially updates a educationHistory.
     *
     * @param educationHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EducationHistoryDTO> partialUpdate(EducationHistoryDTO educationHistoryDTO);

    /**
     * Get all the educationHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EducationHistoryDTO> findAll(Pageable pageable);

    /**
     * Returns the number of educationHistories available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" educationHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EducationHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" educationHistory.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
