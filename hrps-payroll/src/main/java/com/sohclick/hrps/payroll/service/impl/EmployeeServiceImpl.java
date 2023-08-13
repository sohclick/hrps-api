package com.sohclick.hrps.payroll.service.impl;

import com.sohclick.hrps.payroll.domain.Employee;
import com.sohclick.hrps.payroll.repository.EmployeeRepository;
import com.sohclick.hrps.payroll.service.EmployeeService;
import com.sohclick.hrps.payroll.service.dto.EmployeeDTO;
import com.sohclick.hrps.payroll.service.mapper.EmployeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Mono<EmployeeDTO> save(EmployeeDTO employeeDTO) {
        log.debug("Request to save Employee : {}", employeeDTO);
        return employeeRepository.save(employeeMapper.toEntity(employeeDTO)).map(employeeMapper::toDto);
    }

    @Override
    public Mono<EmployeeDTO> update(EmployeeDTO employeeDTO) {
        log.debug("Request to update Employee : {}", employeeDTO);
        return employeeRepository.save(employeeMapper.toEntity(employeeDTO)).map(employeeMapper::toDto);
    }

    @Override
    public Mono<EmployeeDTO> partialUpdate(EmployeeDTO employeeDTO) {
        log.debug("Request to partially update Employee : {}", employeeDTO);

        return employeeRepository
            .findById(employeeDTO.getId())
            .map(existingEmployee -> {
                employeeMapper.partialUpdate(existingEmployee, employeeDTO);

                return existingEmployee;
            })
            .flatMap(employeeRepository::save)
            .map(employeeMapper::toDto);
    }

    @Override
    public Flux<EmployeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAllBy(pageable).map(employeeMapper::toDto);
    }

    public Mono<Long> countAll() {
        return employeeRepository.count();
    }

    @Override
    public Mono<EmployeeDTO> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id).map(employeeMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        return employeeRepository.deleteById(id);
    }
}
