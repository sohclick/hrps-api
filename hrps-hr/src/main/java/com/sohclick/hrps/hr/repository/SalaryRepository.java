package com.sohclick.hrps.hr.repository;

import com.sohclick.hrps.hr.domain.Salary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Salary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalaryRepository extends ReactiveMongoRepository<Salary, Long> {
    Flux<Salary> findAllBy(Pageable pageable);
}
