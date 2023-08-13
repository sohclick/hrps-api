package com.sohclick.hrps.hr.repository;

import com.sohclick.hrps.hr.domain.ProfessionalQualification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the ProfessionalQualification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessionalQualificationRepository extends ReactiveMongoRepository<ProfessionalQualification, Long> {
    Flux<ProfessionalQualification> findAllBy(Pageable pageable);
}
