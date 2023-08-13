package com.sohclick.hrps.hr.service.impl;

import com.sohclick.hrps.hr.domain.EmployeeDocument;
import com.sohclick.hrps.hr.repository.EmployeeDocumentRepository;
import com.sohclick.hrps.hr.service.EmployeeDocumentService;
import com.sohclick.hrps.hr.service.dto.EmployeeDocumentDTO;
import com.sohclick.hrps.hr.service.mapper.EmployeeDocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link EmployeeDocument}.
 */
@Service
public class EmployeeDocumentServiceImpl implements EmployeeDocumentService {

    private final Logger log = LoggerFactory.getLogger(EmployeeDocumentServiceImpl.class);

    private final EmployeeDocumentRepository employeeDocumentRepository;

    private final EmployeeDocumentMapper employeeDocumentMapper;

    public EmployeeDocumentServiceImpl(
        EmployeeDocumentRepository employeeDocumentRepository,
        EmployeeDocumentMapper employeeDocumentMapper
    ) {
        this.employeeDocumentRepository = employeeDocumentRepository;
        this.employeeDocumentMapper = employeeDocumentMapper;
    }

    @Override
    public Mono<EmployeeDocumentDTO> save(EmployeeDocumentDTO employeeDocumentDTO) {
        log.debug("Request to save EmployeeDocument : {}", employeeDocumentDTO);
        return employeeDocumentRepository.save(employeeDocumentMapper.toEntity(employeeDocumentDTO)).map(employeeDocumentMapper::toDto);
    }

    @Override
    public Mono<EmployeeDocumentDTO> update(EmployeeDocumentDTO employeeDocumentDTO) {
        log.debug("Request to update EmployeeDocument : {}", employeeDocumentDTO);
        return employeeDocumentRepository.save(employeeDocumentMapper.toEntity(employeeDocumentDTO)).map(employeeDocumentMapper::toDto);
    }

    @Override
    public Mono<EmployeeDocumentDTO> partialUpdate(EmployeeDocumentDTO employeeDocumentDTO) {
        log.debug("Request to partially update EmployeeDocument : {}", employeeDocumentDTO);

        return employeeDocumentRepository
            .findById(employeeDocumentDTO.getId())
            .map(existingEmployeeDocument -> {
                employeeDocumentMapper.partialUpdate(existingEmployeeDocument, employeeDocumentDTO);

                return existingEmployeeDocument;
            })
            .flatMap(employeeDocumentRepository::save)
            .map(employeeDocumentMapper::toDto);
    }

    @Override
    public Flux<EmployeeDocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeDocuments");
        return employeeDocumentRepository.findAllBy(pageable).map(employeeDocumentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return employeeDocumentRepository.count();
    }

    @Override
    public Mono<EmployeeDocumentDTO> findOne(Long id) {
        log.debug("Request to get EmployeeDocument : {}", id);
        return employeeDocumentRepository.findById(id).map(employeeDocumentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete EmployeeDocument : {}", id);
        return employeeDocumentRepository.deleteById(id);
    }
}
