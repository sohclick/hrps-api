package com.sohclick.hrps.hr.service.impl;

import com.sohclick.hrps.hr.domain.Skill;
import com.sohclick.hrps.hr.repository.SkillRepository;
import com.sohclick.hrps.hr.service.SkillService;
import com.sohclick.hrps.hr.service.dto.SkillDTO;
import com.sohclick.hrps.hr.service.mapper.SkillMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Skill}.
 */
@Service
public class SkillServiceImpl implements SkillService {

    private final Logger log = LoggerFactory.getLogger(SkillServiceImpl.class);

    private final SkillRepository skillRepository;

    private final SkillMapper skillMapper;

    public SkillServiceImpl(SkillRepository skillRepository, SkillMapper skillMapper) {
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

    @Override
    public Mono<SkillDTO> save(SkillDTO skillDTO) {
        log.debug("Request to save Skill : {}", skillDTO);
        return skillRepository.save(skillMapper.toEntity(skillDTO)).map(skillMapper::toDto);
    }

    @Override
    public Mono<SkillDTO> update(SkillDTO skillDTO) {
        log.debug("Request to update Skill : {}", skillDTO);
        return skillRepository.save(skillMapper.toEntity(skillDTO)).map(skillMapper::toDto);
    }

    @Override
    public Mono<SkillDTO> partialUpdate(SkillDTO skillDTO) {
        log.debug("Request to partially update Skill : {}", skillDTO);

        return skillRepository
            .findById(skillDTO.getId())
            .map(existingSkill -> {
                skillMapper.partialUpdate(existingSkill, skillDTO);

                return existingSkill;
            })
            .flatMap(skillRepository::save)
            .map(skillMapper::toDto);
    }

    @Override
    public Flux<SkillDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Skills");
        return skillRepository.findAllBy(pageable).map(skillMapper::toDto);
    }

    public Mono<Long> countAll() {
        return skillRepository.count();
    }

    @Override
    public Mono<SkillDTO> findOne(Long id) {
        log.debug("Request to get Skill : {}", id);
        return skillRepository.findById(id).map(skillMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Skill : {}", id);
        return skillRepository.deleteById(id);
    }
}
