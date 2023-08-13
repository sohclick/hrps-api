package com.sohclick.hrps.hr.service;

import com.sohclick.hrps.hr.service.dto.WorkHistoryDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.hr.domain.WorkHistory}.
 */
public interface WorkHistoryService {
    /**
     * Save a workHistory.
     *
     * @param workHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<WorkHistoryDTO> save(WorkHistoryDTO workHistoryDTO);

    /**
     * Updates a workHistory.
     *
     * @param workHistoryDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<WorkHistoryDTO> update(WorkHistoryDTO workHistoryDTO);

    /**
     * Partially updates a workHistory.
     *
     * @param workHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<WorkHistoryDTO> partialUpdate(WorkHistoryDTO workHistoryDTO);

    /**
     * Get all the workHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<WorkHistoryDTO> findAll(Pageable pageable);

    /**
     * Returns the number of workHistories available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" workHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<WorkHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" workHistory.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
