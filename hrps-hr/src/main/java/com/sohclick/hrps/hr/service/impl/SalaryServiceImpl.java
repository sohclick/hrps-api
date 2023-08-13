package com.sohclick.hrps.hr.service.impl;

import com.sohclick.hrps.hr.domain.Salary;
import com.sohclick.hrps.hr.repository.SalaryRepository;
import com.sohclick.hrps.hr.service.SalaryService;
import com.sohclick.hrps.hr.service.dto.SalaryDTO;
import com.sohclick.hrps.hr.service.mapper.SalaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Salary}.
 */
@Service
public class SalaryServiceImpl implements SalaryService {

    private final Logger log = LoggerFactory.getLogger(SalaryServiceImpl.class);

    private final SalaryRepository salaryRepository;

    private final SalaryMapper salaryMapper;

    public SalaryServiceImpl(SalaryRepository salaryRepository, SalaryMapper salaryMapper) {
        this.salaryRepository = salaryRepository;
        this.salaryMapper = salaryMapper;
    }

    @Override
    public Mono<SalaryDTO> save(SalaryDTO salaryDTO) {
        log.debug("Request to save Salary : {}", salaryDTO);
        return salaryRepository.save(salaryMapper.toEntity(salaryDTO)).map(salaryMapper::toDto);
    }

    @Override
    public Mono<SalaryDTO> update(SalaryDTO salaryDTO) {
        log.debug("Request to update Salary : {}", salaryDTO);
        return salaryRepository.save(salaryMapper.toEntity(salaryDTO)).map(salaryMapper::toDto);
    }

    @Override
    public Mono<SalaryDTO> partialUpdate(SalaryDTO salaryDTO) {
        log.debug("Request to partially update Salary : {}", salaryDTO);

        return salaryRepository
            .findById(salaryDTO.getId())
            .map(existingSalary -> {
                salaryMapper.partialUpdate(existingSalary, salaryDTO);

                return existingSalary;
            })
            .flatMap(salaryRepository::save)
            .map(salaryMapper::toDto);
    }

    @Override
    public Flux<SalaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Salaries");
        return salaryRepository.findAllBy(pageable).map(salaryMapper::toDto);
    }

    public Mono<Long> countAll() {
        return salaryRepository.count();
    }

    @Override
    public Mono<SalaryDTO> findOne(Long id) {
        log.debug("Request to get Salary : {}", id);
        return salaryRepository.findById(id).map(salaryMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Salary : {}", id);
        return salaryRepository.deleteById(id);
    }
}
