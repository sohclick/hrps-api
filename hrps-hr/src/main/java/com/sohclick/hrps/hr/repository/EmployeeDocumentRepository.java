package com.sohclick.hrps.hr.repository;

import com.sohclick.hrps.hr.domain.EmployeeDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the EmployeeDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeDocumentRepository extends ReactiveMongoRepository<EmployeeDocument, Long> {
    Flux<EmployeeDocument> findAllBy(Pageable pageable);
}
