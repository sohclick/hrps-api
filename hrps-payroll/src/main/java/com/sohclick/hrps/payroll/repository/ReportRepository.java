package com.sohclick.hrps.payroll.repository;

import com.sohclick.hrps.payroll.domain.Report;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Report entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportRepository extends ReactiveMongoRepository<Report, Long> {
    Flux<Report> findAllBy(Pageable pageable);
}
