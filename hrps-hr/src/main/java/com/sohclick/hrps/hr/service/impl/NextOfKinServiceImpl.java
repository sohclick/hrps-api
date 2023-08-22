package com.sohclick.hrps.hr.service.impl;

import com.sohclick.hrps.hr.domain.NextOfKin;
import com.sohclick.hrps.hr.repository.NextOfKinRepository;
import com.sohclick.hrps.hr.service.NextOfKinService;
import com.sohclick.hrps.hr.service.dto.NextOfKinDTO;
import com.sohclick.hrps.hr.service.mapper.NextOfKinMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link NextOfKin}.
 */
@Service
public class NextOfKinServiceImpl implements NextOfKinService {

    private final Logger log = LoggerFactory.getLogger(NextOfKinServiceImpl.class);

    private final NextOfKinRepository nextOfKinRepository;

    private final NextOfKinMapper nextOfKinMapper;

    public NextOfKinServiceImpl(NextOfKinRepository nextOfKinRepository, NextOfKinMapper nextOfKinMapper) {
        this.nextOfKinRepository = nextOfKinRepository;
        this.nextOfKinMapper = nextOfKinMapper;
    }

    @Override
    public Mono<NextOfKinDTO> save(NextOfKinDTO nextOfKinDTO) {
        log.debug("Request to save NextOfKin : {}", nextOfKinDTO);
        return nextOfKinRepository.save(nextOfKinMapper.toEntity(nextOfKinDTO)).map(nextOfKinMapper::toDto);
    }

    @Override
    public Mono<NextOfKinDTO> update(NextOfKinDTO nextOfKinDTO) {
        log.debug("Request to update NextOfKin : {}", nextOfKinDTO);
        return nextOfKinRepository.save(nextOfKinMapper.toEntity(nextOfKinDTO)).map(nextOfKinMapper::toDto);
    }

    @Override
    public Mono<NextOfKinDTO> partialUpdate(NextOfKinDTO nextOfKinDTO) {
        log.debug("Request to partially update NextOfKin : {}", nextOfKinDTO);

        return nextOfKinRepository
            .findById(nextOfKinDTO.getId())
            .map(existingNextOfKin -> {
                nextOfKinMapper.partialUpdate(existingNextOfKin, nextOfKinDTO);

                return existingNextOfKin;
            })
            .flatMap(nextOfKinRepository::save)
            .map(nextOfKinMapper::toDto);
    }

    @Override
    public Flux<NextOfKinDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NextOfKins");
        return nextOfKinRepository.findAllBy(pageable).map(nextOfKinMapper::toDto);
    }

    /**
     *  Get all the nextOfKins where Employee is {@code null}.
     *  @return the list of entities.
     */

    public Mono<Long> countAll() {
        return nextOfKinRepository.count();
    }

    @Override
    public Mono<NextOfKinDTO> findOne(Long id) {
        log.debug("Request to get NextOfKin : {}", id);
        return nextOfKinRepository.findById(id).map(nextOfKinMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete NextOfKin : {}", id);
        return nextOfKinRepository.deleteById(id);
    }
}
