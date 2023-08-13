package com.sohclick.hrps.payroll.repository;

import com.sohclick.hrps.payroll.domain.SalaryComponent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the SalaryComponent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalaryComponentRepository extends ReactiveMongoRepository<SalaryComponent, Long> {
    Flux<SalaryComponent> findAllBy(Pageable pageable);
}
