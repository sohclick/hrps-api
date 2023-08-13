package com.sohclick.hrps.payroll.repository;

import com.sohclick.hrps.payroll.domain.Benefit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Benefit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BenefitRepository extends ReactiveMongoRepository<Benefit, Long> {
    Flux<Benefit> findAllBy(Pageable pageable);
}
