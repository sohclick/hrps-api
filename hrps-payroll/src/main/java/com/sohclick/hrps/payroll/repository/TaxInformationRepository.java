package com.sohclick.hrps.payroll.repository;

import com.sohclick.hrps.payroll.domain.TaxInformation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the TaxInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxInformationRepository extends ReactiveMongoRepository<TaxInformation, Long> {
    Flux<TaxInformation> findAllBy(Pageable pageable);
}
