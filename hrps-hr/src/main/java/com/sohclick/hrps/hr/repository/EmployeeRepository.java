package com.sohclick.hrps.hr.repository;

import com.sohclick.hrps.hr.domain.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Employee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, Long> {
    Flux<Employee> findAllBy(Pageable pageable);
}
