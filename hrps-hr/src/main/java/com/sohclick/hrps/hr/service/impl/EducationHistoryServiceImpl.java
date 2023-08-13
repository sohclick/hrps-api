package com.sohclick.hrps.hr.service.impl;

import com.sohclick.hrps.hr.domain.EducationHistory;
import com.sohclick.hrps.hr.repository.EducationHistoryRepository;
import com.sohclick.hrps.hr.service.EducationHistoryService;
import com.sohclick.hrps.hr.service.dto.EducationHistoryDTO;
import com.sohclick.hrps.hr.service.mapper.EducationHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link EducationHistory}.
 */
@Service
public class EducationHistoryServiceImpl implements EducationHistoryService {

    private final Logger log = LoggerFactory.getLogger(EducationHistoryServiceImpl.class);

    private final EducationHistoryRepository educationHistoryRepository;

    private final EducationHistoryMapper educationHistoryMapper;

    public EducationHistoryServiceImpl(
        EducationHistoryRepository educationHistoryRepository,
        EducationHistoryMapper educationHistoryMapper
    ) {
        this.educationHistoryRepository = educationHistoryRepository;
        this.educationHistoryMapper = educationHistoryMapper;
    }

    @Override
    public Mono<EducationHistoryDTO> save(EducationHistoryDTO educationHistoryDTO) {
        log.debug("Request to save EducationHistory : {}", educationHistoryDTO);
        return educationHistoryRepository.save(educationHistoryMapper.toEntity(educationHistoryDTO)).map(educationHistoryMapper::toDto);
    }

    @Override
    public Mono<EducationHistoryDTO> update(EducationHistoryDTO educationHistoryDTO) {
        log.debug("Request to update EducationHistory : {}", educationHistoryDTO);
        return educationHistoryRepository.save(educationHistoryMapper.toEntity(educationHistoryDTO)).map(educationHistoryMapper::toDto);
    }

    @Override
    public Mono<EducationHistoryDTO> partialUpdate(EducationHistoryDTO educationHistoryDTO) {
        log.debug("Request to partially update EducationHistory : {}", educationHistoryDTO);

        return educationHistoryRepository
            .findById(educationHistoryDTO.getId())
            .map(existingEducationHistory -> {
                educationHistoryMapper.partialUpdate(existingEducationHistory, educationHistoryDTO);

                return existingEducationHistory;
            })
            .flatMap(educationHistoryRepository::save)
            .map(educationHistoryMapper::toDto);
    }

    @Override
    public Flux<EducationHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EducationHistories");
        return educationHistoryRepository.findAllBy(pageable).map(educationHistoryMapper::toDto);
    }

    public Mono<Long> countAll() {
        return educationHistoryRepository.count();
    }

    @Override
    public Mono<EducationHistoryDTO> findOne(Long id) {
        log.debug("Request to get EducationHistory : {}", id);
        return educationHistoryRepository.findById(id).map(educationHistoryMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete EducationHistory : {}", id);
        return educationHistoryRepository.deleteById(id);
    }
}
