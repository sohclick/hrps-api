package com.sohclick.hrps.payroll.repository;

import com.sohclick.hrps.payroll.domain.Deduction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Deduction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeductionRepository extends ReactiveMongoRepository<Deduction, Long> {
    Flux<Deduction> findAllBy(Pageable pageable);
}
