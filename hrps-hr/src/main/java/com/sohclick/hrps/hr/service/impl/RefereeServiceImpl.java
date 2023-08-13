package com.sohclick.hrps.hr.service.impl;

import com.sohclick.hrps.hr.domain.Referee;
import com.sohclick.hrps.hr.repository.RefereeRepository;
import com.sohclick.hrps.hr.service.RefereeService;
import com.sohclick.hrps.hr.service.dto.RefereeDTO;
import com.sohclick.hrps.hr.service.mapper.RefereeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Referee}.
 */
@Service
public class RefereeServiceImpl implements RefereeService {

    private final Logger log = LoggerFactory.getLogger(RefereeServiceImpl.class);

    private final RefereeRepository refereeRepository;

    private final RefereeMapper refereeMapper;

    public RefereeServiceImpl(RefereeRepository refereeRepository, RefereeMapper refereeMapper) {
        this.refereeRepository = refereeRepository;
        this.refereeMapper = refereeMapper;
    }

    @Override
    public Mono<RefereeDTO> save(RefereeDTO refereeDTO) {
        log.debug("Request to save Referee : {}", refereeDTO);
        return refereeRepository.save(refereeMapper.toEntity(refereeDTO)).map(refereeMapper::toDto);
    }

    @Override
    public Mono<RefereeDTO> update(RefereeDTO refereeDTO) {
        log.debug("Request to update Referee : {}", refereeDTO);
        return refereeRepository.save(refereeMapper.toEntity(refereeDTO)).map(refereeMapper::toDto);
    }

    @Override
    public Mono<RefereeDTO> partialUpdate(RefereeDTO refereeDTO) {
        log.debug("Request to partially update Referee : {}", refereeDTO);

        return refereeRepository
            .findById(refereeDTO.getId())
            .map(existingReferee -> {
                refereeMapper.partialUpdate(existingReferee, refereeDTO);

                return existingReferee;
            })
            .flatMap(refereeRepository::save)
            .map(refereeMapper::toDto);
    }

    @Override
    public Flux<RefereeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Referees");
        return refereeRepository.findAllBy(pageable).map(refereeMapper::toDto);
    }

    public Mono<Long> countAll() {
        return refereeRepository.count();
    }

    @Override
    public Mono<RefereeDTO> findOne(Long id) {
        log.debug("Request to get Referee : {}", id);
        return refereeRepository.findById(id).map(refereeMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Referee : {}", id);
        return refereeRepository.deleteById(id);
    }
}
