package com.sohclick.hrps.payroll.repository;

import com.sohclick.hrps.payroll.domain.Payroll;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Payroll entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayrollRepository extends ReactiveMongoRepository<Payroll, Long> {
    Flux<Payroll> findAllBy(Pageable pageable);
}
