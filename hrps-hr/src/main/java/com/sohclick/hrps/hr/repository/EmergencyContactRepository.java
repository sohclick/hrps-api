package com.sohclick.hrps.hr.repository;

import com.sohclick.hrps.hr.domain.EmergencyContact;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the EmergencyContact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmergencyContactRepository extends ReactiveMongoRepository<EmergencyContact, Long> {
    Flux<EmergencyContact> findAllBy(Pageable pageable);
}
