package com.sohclick.hrps.payroll.service;

import com.sohclick.hrps.payroll.service.dto.TimeAndAttendanceDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.payroll.domain.TimeAndAttendance}.
 */
public interface TimeAndAttendanceService {
    /**
     * Save a timeAndAttendance.
     *
     * @param timeAndAttendanceDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TimeAndAttendanceDTO> save(TimeAndAttendanceDTO timeAndAttendanceDTO);

    /**
     * Updates a timeAndAttendance.
     *
     * @param timeAndAttendanceDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TimeAndAttendanceDTO> update(TimeAndAttendanceDTO timeAndAttendanceDTO);

    /**
     * Partially updates a timeAndAttendance.
     *
     * @param timeAndAttendanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TimeAndAttendanceDTO> partialUpdate(TimeAndAttendanceDTO timeAndAttendanceDTO);

    /**
     * Get all the timeAndAttendances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TimeAndAttendanceDTO> findAll(Pageable pageable);

    /**
     * Get all the TimeAndAttendanceDTO where Payroll is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<TimeAndAttendanceDTO> findAllWherePayrollIsNull();

    /**
     * Returns the number of timeAndAttendances available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" timeAndAttendance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TimeAndAttendanceDTO> findOne(Long id);

    /**
     * Delete the "id" timeAndAttendance.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
