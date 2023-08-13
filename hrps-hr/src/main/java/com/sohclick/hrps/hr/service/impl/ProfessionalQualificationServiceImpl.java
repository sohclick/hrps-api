package com.sohclick.hrps.hr.service.impl;

import com.sohclick.hrps.hr.domain.ProfessionalQualification;
import com.sohclick.hrps.hr.repository.ProfessionalQualificationRepository;
import com.sohclick.hrps.hr.service.ProfessionalQualificationService;
import com.sohclick.hrps.hr.service.dto.ProfessionalQualificationDTO;
import com.sohclick.hrps.hr.service.mapper.ProfessionalQualificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ProfessionalQualification}.
 */
@Service
public class ProfessionalQualificationServiceImpl implements ProfessionalQualificationService {

    private final Logger log = LoggerFactory.getLogger(ProfessionalQualificationServiceImpl.class);

    private final ProfessionalQualificationRepository professionalQualificationRepository;

    private final ProfessionalQualificationMapper professionalQualificationMapper;

    public ProfessionalQualificationServiceImpl(
        ProfessionalQualificationRepository professionalQualificationRepository,
        ProfessionalQualificationMapper professionalQualificationMapper
    ) {
        this.professionalQualificationRepository = professionalQualificationRepository;
        this.professionalQualificationMapper = professionalQualificationMapper;
    }

    @Override
    public Mono<ProfessionalQualificationDTO> save(ProfessionalQualificationDTO professionalQualificationDTO) {
        log.debug("Request to save ProfessionalQualification : {}", professionalQualificationDTO);
        return professionalQualificationRepository
            .save(professionalQualificationMapper.toEntity(professionalQualificationDTO))
            .map(professionalQualificationMapper::toDto);
    }

    @Override
    public Mono<ProfessionalQualificationDTO> update(ProfessionalQualificationDTO professionalQualificationDTO) {
        log.debug("Request to update ProfessionalQualification : {}", professionalQualificationDTO);
        return professionalQualificationRepository
            .save(professionalQualificationMapper.toEntity(professionalQualificationDTO))
            .map(professionalQualificationMapper::toDto);
    }

    @Override
    public Mono<ProfessionalQualificationDTO> partialUpdate(ProfessionalQualificationDTO professionalQualificationDTO) {
        log.debug("Request to partially update ProfessionalQualification : {}", professionalQualificationDTO);

        return professionalQualificationRepository
            .findById(professionalQualificationDTO.getId())
            .map(existingProfessionalQualification -> {
                professionalQualificationMapper.partialUpdate(existingProfessionalQualification, professionalQualificationDTO);

                return existingProfessionalQualification;
            })
            .flatMap(professionalQualificationRepository::save)
            .map(professionalQualificationMapper::toDto);
    }

    @Override
    public Flux<ProfessionalQualificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProfessionalQualifications");
        return professionalQualificationRepository.findAllBy(pageable).map(professionalQualificationMapper::toDto);
    }

    public Mono<Long> countAll() {
        return professionalQualificationRepository.count();
    }

    @Override
    public Mono<ProfessionalQualificationDTO> findOne(Long id) {
        log.debug("Request to get ProfessionalQualification : {}", id);
        return professionalQualificationRepository.findById(id).map(professionalQualificationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ProfessionalQualification : {}", id);
        return professionalQualificationRepository.deleteById(id);
    }
}
