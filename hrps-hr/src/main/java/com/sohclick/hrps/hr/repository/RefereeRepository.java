package com.sohclick.hrps.hr.repository;

import com.sohclick.hrps.hr.domain.Referee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Referee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RefereeRepository extends ReactiveMongoRepository<Referee, Long> {
    Flux<Referee> findAllBy(Pageable pageable);
}
