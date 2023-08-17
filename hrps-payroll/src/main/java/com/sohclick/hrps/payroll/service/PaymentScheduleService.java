package com.sohclick.hrps.payroll.service;

import com.sohclick.hrps.payroll.service.dto.PaymentScheduleDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.sohclick.hrps.payroll.domain.PaymentSchedule}.
 */
public interface PaymentScheduleService {
    /**
     * Save a paymentSchedule.
     *
     * @param paymentScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PaymentScheduleDTO> save(PaymentScheduleDTO paymentScheduleDTO);

    /**
     * Updates a paymentSchedule.
     *
     * @param paymentScheduleDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PaymentScheduleDTO> update(PaymentScheduleDTO paymentScheduleDTO);

    /**
     * Partially updates a paymentSchedule.
     *
     * @param paymentScheduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PaymentScheduleDTO> partialUpdate(PaymentScheduleDTO paymentScheduleDTO);

    /**
     * Get all the paymentSchedules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PaymentScheduleDTO> findAll(Pageable pageable);


    /**
     * Returns the number of paymentSchedules available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" paymentSchedule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PaymentScheduleDTO> findOne(Long id);

    /**
     * Delete the "id" paymentSchedule.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
