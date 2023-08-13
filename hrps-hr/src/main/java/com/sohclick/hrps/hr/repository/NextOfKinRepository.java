package com.sohclick.hrps.hr.repository;

import com.sohclick.hrps.hr.domain.NextOfKin;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the NextOfKin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextOfKinRepository extends ReactiveMongoRepository<NextOfKin, Long> {
    Flux<NextOfKin> findAllBy(Pageable pageable);
}
