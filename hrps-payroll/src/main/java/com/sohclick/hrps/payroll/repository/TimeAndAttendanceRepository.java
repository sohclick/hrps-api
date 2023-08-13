package com.sohclick.hrps.payroll.repository;

import com.sohclick.hrps.payroll.domain.TimeAndAttendance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the TimeAndAttendance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeAndAttendanceRepository extends ReactiveMongoRepository<TimeAndAttendance, Long> {
    Flux<TimeAndAttendance> findAllBy(Pageable pageable);
}
