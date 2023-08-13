package com.sohclick.hrps.payroll.service.impl;

import com.sohclick.hrps.payroll.domain.Payroll;
import com.sohclick.hrps.payroll.repository.PayrollRepository;
import com.sohclick.hrps.payroll.service.PayrollService;
import com.sohclick.hrps.payroll.service.dto.PayrollDTO;
import com.sohclick.hrps.payroll.service.mapper.PayrollMapper;
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
 * Service Implementation for managing {@link Payroll}.
 */
@Service
public class PayrollServiceImpl implements PayrollService {

    private final Logger log = LoggerFactory.getLogger(PayrollServiceImpl.class);

    private final PayrollRepository payrollRepository;

    private final PayrollMapper payrollMapper;

    public PayrollServiceImpl(PayrollRepository payrollRepository, PayrollMapper payrollMapper) {
        this.payrollRepository = payrollRepository;
        this.payrollMapper = payrollMapper;
    }

    @Override
    public Mono<PayrollDTO> save(PayrollDTO payrollDTO) {
        log.debug("Request to save Payroll : {}", payrollDTO);
        return payrollRepository.save(payrollMapper.toEntity(payrollDTO)).map(payrollMapper::toDto);
    }

    @Override
    public Mono<PayrollDTO> update(PayrollDTO payrollDTO) {
        log.debug("Request to update Payroll : {}", payrollDTO);
        return payrollRepository.save(payrollMapper.toEntity(payrollDTO)).map(payrollMapper::toDto);
    }

    @Override
    public Mono<PayrollDTO> partialUpdate(PayrollDTO payrollDTO) {
        log.debug("Request to partially update Payroll : {}", payrollDTO);

        return payrollRepository
            .findById(payrollDTO.getId())
            .map(existingPayroll -> {
                payrollMapper.partialUpdate(existingPayroll, payrollDTO);

                return existingPayroll;
            })
            .flatMap(payrollRepository::save)
            .map(payrollMapper::toDto);
    }

    @Override
    public Flux<PayrollDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payrolls");
        return payrollRepository.findAllBy(pageable).map(payrollMapper::toDto);
    }

    /**
     *  Get all the payrolls where Employee is {@code null}.
     *  @return the list of entities.
     */

    public List<PayrollDTO> findAllWhereEmployeeIsNull() {
        log.debug("Request to get all payrolls where Employee is null");
        return payrollRepository.findAll().filter(payroll -> payroll.getEmployee() == null).map(payrollMapper::toDto);
    }

    public Mono<Long> countAll() {
        return payrollRepository.count();
    }

    @Override
    public Mono<PayrollDTO> findOne(Long id) {
        log.debug("Request to get Payroll : {}", id);
        return payrollRepository.findById(id).map(payrollMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Payroll : {}", id);
        return payrollRepository.deleteById(id);
    }
}
