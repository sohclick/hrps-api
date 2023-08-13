package com.sohclick.hrps.hr.service;

import com.sohclick.hrps.hr.service.dto.SkillDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.hr.domain.Skill}.
 */
public interface SkillService {
    /**
     * Save a skill.
     *
     * @param skillDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SkillDTO> save(SkillDTO skillDTO);

    /**
     * Updates a skill.
     *
     * @param skillDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SkillDTO> update(SkillDTO skillDTO);

    /**
     * Partially updates a skill.
     *
     * @param skillDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SkillDTO> partialUpdate(SkillDTO skillDTO);

    /**
     * Get all the skills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SkillDTO> findAll(Pageable pageable);

    /**
     * Returns the number of skills available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" skill.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SkillDTO> findOne(Long id);

    /**
     * Delete the "id" skill.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
