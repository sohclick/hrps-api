package com.sohclick.hrps.hr.repository;

import com.sohclick.hrps.hr.domain.EducationHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the EducationHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EducationHistoryRepository extends ReactiveMongoRepository<EducationHistory, Long> {
    Flux<EducationHistory> findAllBy(Pageable pageable);
}
