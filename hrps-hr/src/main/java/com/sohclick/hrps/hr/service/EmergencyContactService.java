package com.sohclick.hrps.hr.service;

import com.sohclick.hrps.hr.service.dto.EmergencyContactDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.hr.domain.EmergencyContact}.
 */
public interface EmergencyContactService {
    /**
     * Save a emergencyContact.
     *
     * @param emergencyContactDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<EmergencyContactDTO> save(EmergencyContactDTO emergencyContactDTO);

    /**
     * Updates a emergencyContact.
     *
     * @param emergencyContactDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<EmergencyContactDTO> update(EmergencyContactDTO emergencyContactDTO);

    /**
     * Partially updates a emergencyContact.
     *
     * @param emergencyContactDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EmergencyContactDTO> partialUpdate(EmergencyContactDTO emergencyContactDTO);

    /**
     * Get all the emergencyContacts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EmergencyContactDTO> findAll(Pageable pageable);

    /**
     * Returns the number of emergencyContacts available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" emergencyContact.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EmergencyContactDTO> findOne(Long id);

    /**
     * Delete the "id" emergencyContact.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
