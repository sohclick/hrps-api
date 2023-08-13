package com.sohclick.hrps.hr.repository;

import com.sohclick.hrps.hr.domain.WorkHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the WorkHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkHistoryRepository extends ReactiveMongoRepository<WorkHistory, Long> {
    Flux<WorkHistory> findAllBy(Pageable pageable);
}
