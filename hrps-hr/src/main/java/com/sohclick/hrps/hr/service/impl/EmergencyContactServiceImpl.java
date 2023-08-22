package com.sohclick.hrps.hr.service.impl;

import com.sohclick.hrps.hr.domain.EmergencyContact;
import com.sohclick.hrps.hr.repository.EmergencyContactRepository;
import com.sohclick.hrps.hr.service.EmergencyContactService;
import com.sohclick.hrps.hr.service.dto.EmergencyContactDTO;
import com.sohclick.hrps.hr.service.mapper.EmergencyContactMapper;
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
 * Service Implementation for managing {@link EmergencyContact}.
 */
@Service
public class EmergencyContactServiceImpl implements EmergencyContactService {

    private final Logger log = LoggerFactory.getLogger(EmergencyContactServiceImpl.class);

    private final EmergencyContactRepository emergencyContactRepository;

    private final EmergencyContactMapper emergencyContactMapper;

    public EmergencyContactServiceImpl(
        EmergencyContactRepository emergencyContactRepository,
        EmergencyContactMapper emergencyContactMapper
    ) {
        this.emergencyContactRepository = emergencyContactRepository;
        this.emergencyContactMapper = emergencyContactMapper;
    }

    @Override
    public Mono<EmergencyContactDTO> save(EmergencyContactDTO emergencyContactDTO) {
        log.debug("Request to save EmergencyContact : {}", emergencyContactDTO);
        return emergencyContactRepository.save(emergencyContactMapper.toEntity(emergencyContactDTO)).map(emergencyContactMapper::toDto);
    }

    @Override
    public Mono<EmergencyContactDTO> update(EmergencyContactDTO emergencyContactDTO) {
        log.debug("Request to update EmergencyContact : {}", emergencyContactDTO);
        return emergencyContactRepository.save(emergencyContactMapper.toEntity(emergencyContactDTO)).map(emergencyContactMapper::toDto);
    }

    @Override
    public Mono<EmergencyContactDTO> partialUpdate(EmergencyContactDTO emergencyContactDTO) {
        log.debug("Request to partially update EmergencyContact : {}", emergencyContactDTO);

        return emergencyContactRepository
            .findById(emergencyContactDTO.getId())
            .map(existingEmergencyContact -> {
                emergencyContactMapper.partialUpdate(existingEmergencyContact, emergencyContactDTO);

                return existingEmergencyContact;
            })
            .flatMap(emergencyContactRepository::save)
            .map(emergencyContactMapper::toDto);
    }

    @Override
    public Flux<EmergencyContactDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmergencyContacts");
        return emergencyContactRepository.findAllBy(pageable).map(emergencyContactMapper::toDto);
    }

    /**
     *  Get all the emergencyContacts where Employee is {@code null}.
     *  @return the list of entities.
     */

    public Mono<Long> countAll() {
        return emergencyContactRepository.count();
    }

    @Override
    public Mono<EmergencyContactDTO> findOne(Long id) {
        log.debug("Request to get EmergencyContact : {}", id);
        return emergencyContactRepository.findById(id).map(emergencyContactMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete EmergencyContact : {}", id);
        return emergencyContactRepository.deleteById(id);
    }
}
