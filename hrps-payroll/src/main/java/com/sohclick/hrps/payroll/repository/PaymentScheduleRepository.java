package com.sohclick.hrps.payroll.repository;

import com.sohclick.hrps.payroll.domain.PaymentSchedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the PaymentSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentScheduleRepository extends ReactiveMongoRepository<PaymentSchedule, Long> {
    Flux<PaymentSchedule> findAllBy(Pageable pageable);
}
