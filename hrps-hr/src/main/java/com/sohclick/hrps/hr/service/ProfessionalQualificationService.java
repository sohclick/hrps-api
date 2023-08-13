package com.sohclick.hrps.hr.service;

import com.sohclick.hrps.hr.service.dto.ProfessionalQualificationDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.hr.domain.ProfessionalQualification}.
 */
public interface ProfessionalQualificationService {
    /**
     * Save a professionalQualification.
     *
     * @param professionalQualificationDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ProfessionalQualificationDTO> save(ProfessionalQualificationDTO professionalQualificationDTO);

    /**
     * Updates a professionalQualification.
     *
     * @param professionalQualificationDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ProfessionalQualificationDTO> update(ProfessionalQualificationDTO professionalQualificationDTO);

    /**
     * Partially updates a professionalQualification.
     *
     * @param professionalQualificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ProfessionalQualificationDTO> partialUpdate(ProfessionalQualificationDTO professionalQualificationDTO);

    /**
     * Get all the professionalQualifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ProfessionalQualificationDTO> findAll(Pageable pageable);

    /**
     * Returns the number of professionalQualifications available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" professionalQualification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ProfessionalQualificationDTO> findOne(Long id);

    /**
     * Delete the "id" professionalQualification.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
