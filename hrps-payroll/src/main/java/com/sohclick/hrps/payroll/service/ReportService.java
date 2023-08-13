package com.sohclick.hrps.payroll.service;

import com.sohclick.hrps.payroll.service.dto.ReportDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.payroll.domain.Report}.
 */
public interface ReportService {
    /**
     * Save a report.
     *
     * @param reportDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ReportDTO> save(ReportDTO reportDTO);

    /**
     * Updates a report.
     *
     * @param reportDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ReportDTO> update(ReportDTO reportDTO);

    /**
     * Partially updates a report.
     *
     * @param reportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ReportDTO> partialUpdate(ReportDTO reportDTO);

    /**
     * Get all the reports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ReportDTO> findAll(Pageable pageable);

    /**
     * Get all the ReportDTO where Payroll is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<ReportDTO> findAllWherePayrollIsNull();

    /**
     * Returns the number of reports available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" report.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ReportDTO> findOne(Long id);

    /**
     * Delete the "id" report.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
