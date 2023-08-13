package com.sohclick.hrps.hr.service.impl;

import com.sohclick.hrps.hr.domain.WorkHistory;
import com.sohclick.hrps.hr.repository.WorkHistoryRepository;
import com.sohclick.hrps.hr.service.WorkHistoryService;
import com.sohclick.hrps.hr.service.dto.WorkHistoryDTO;
import com.sohclick.hrps.hr.service.mapper.WorkHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link WorkHistory}.
 */
@Service
public class WorkHistoryServiceImpl implements WorkHistoryService {

    private final Logger log = LoggerFactory.getLogger(WorkHistoryServiceImpl.class);

    private final WorkHistoryRepository workHistoryRepository;

    private final WorkHistoryMapper workHistoryMapper;

    public WorkHistoryServiceImpl(WorkHistoryRepository workHistoryRepository, WorkHistoryMapper workHistoryMapper) {
        this.workHistoryRepository = workHistoryRepository;
        this.workHistoryMapper = workHistoryMapper;
    }

    @Override
    public Mono<WorkHistoryDTO> save(WorkHistoryDTO workHistoryDTO) {
        log.debug("Request to save WorkHistory : {}", workHistoryDTO);
        return workHistoryRepository.save(workHistoryMapper.toEntity(workHistoryDTO)).map(workHistoryMapper::toDto);
    }

    @Override
    public Mono<WorkHistoryDTO> update(WorkHistoryDTO workHistoryDTO) {
        log.debug("Request to update WorkHistory : {}", workHistoryDTO);
        return workHistoryRepository.save(workHistoryMapper.toEntity(workHistoryDTO)).map(workHistoryMapper::toDto);
    }

    @Override
    public Mono<WorkHistoryDTO> partialUpdate(WorkHistoryDTO workHistoryDTO) {
        log.debug("Request to partially update WorkHistory : {}", workHistoryDTO);

        return workHistoryRepository
            .findById(workHistoryDTO.getId())
            .map(existingWorkHistory -> {
                workHistoryMapper.partialUpdate(existingWorkHistory, workHistoryDTO);

                return existingWorkHistory;
            })
            .flatMap(workHistoryRepository::save)
            .map(workHistoryMapper::toDto);
    }

    @Override
    public Flux<WorkHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkHistories");
        return workHistoryRepository.findAllBy(pageable).map(workHistoryMapper::toDto);
    }

    public Mono<Long> countAll() {
        return workHistoryRepository.count();
    }

    @Override
    public Mono<WorkHistoryDTO> findOne(Long id) {
        log.debug("Request to get WorkHistory : {}", id);
        return workHistoryRepository.findById(id).map(workHistoryMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete WorkHistory : {}", id);
        return workHistoryRepository.deleteById(id);
    }
}
