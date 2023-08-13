package com.sohclick.hrps.hr.repository;

import com.sohclick.hrps.hr.domain.Skill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Skill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillRepository extends ReactiveMongoRepository<Skill, Long> {
    Flux<Skill> findAllBy(Pageable pageable);
}
