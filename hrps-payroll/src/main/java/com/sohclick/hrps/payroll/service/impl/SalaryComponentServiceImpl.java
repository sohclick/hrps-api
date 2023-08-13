package com.sohclick.hrps.payroll.service.impl;

import com.sohclick.hrps.payroll.domain.SalaryComponent;
import com.sohclick.hrps.payroll.repository.SalaryComponentRepository;
import com.sohclick.hrps.payroll.service.SalaryComponentService;
import com.sohclick.hrps.payroll.service.dto.SalaryComponentDTO;
import com.sohclick.hrps.payroll.service.mapper.SalaryComponentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link SalaryComponent}.
 */
@Service
public class SalaryComponentServiceImpl implements SalaryComponentService {

    private final Logger log = LoggerFactory.getLogger(SalaryComponentServiceImpl.class);

    private final SalaryComponentRepository salaryComponentRepository;

    private final SalaryComponentMapper salaryComponentMapper;

    public SalaryComponentServiceImpl(SalaryComponentRepository salaryComponentRepository, SalaryComponentMapper salaryComponentMapper) {
        this.salaryComponentRepository = salaryComponentRepository;
        this.salaryComponentMapper = salaryComponentMapper;
    }

    @Override
    public Mono<SalaryComponentDTO> save(SalaryComponentDTO salaryComponentDTO) {
        log.debug("Request to save SalaryComponent : {}", salaryComponentDTO);
        return salaryComponentRepository.save(salaryComponentMapper.toEntity(salaryComponentDTO)).map(salaryComponentMapper::toDto);
    }

    @Override
    public Mono<SalaryComponentDTO> update(SalaryComponentDTO salaryComponentDTO) {
        log.debug("Request to update SalaryComponent : {}", salaryComponentDTO);
        return salaryComponentRepository.save(salaryComponentMapper.toEntity(salaryComponentDTO)).map(salaryComponentMapper::toDto);
    }

    @Override
    public Mono<SalaryComponentDTO> partialUpdate(SalaryComponentDTO salaryComponentDTO) {
        log.debug("Request to partially update SalaryComponent : {}", salaryComponentDTO);

        return salaryComponentRepository
            .findById(salaryComponentDTO.getId())
            .map(existingSalaryComponent -> {
                salaryComponentMapper.partialUpdate(existingSalaryComponent, salaryComponentDTO);

                return existingSalaryComponent;
            })
            .flatMap(salaryComponentRepository::save)
            .map(salaryComponentMapper::toDto);
    }

    @Override
    public Flux<SalaryComponentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SalaryComponents");
        return salaryComponentRepository.findAllBy(pageable).map(salaryComponentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return salaryComponentRepository.count();
    }

    @Override
    public Mono<SalaryComponentDTO> findOne(Long id) {
        log.debug("Request to get SalaryComponent : {}", id);
        return salaryComponentRepository.findById(id).map(salaryComponentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SalaryComponent : {}", id);
        return salaryComponentRepository.deleteById(id);
    }
}
