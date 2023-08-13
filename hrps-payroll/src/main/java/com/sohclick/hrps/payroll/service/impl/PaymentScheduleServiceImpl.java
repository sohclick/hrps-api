package com.sohclick.hrps.payroll.service.impl;

import com.sohclick.hrps.payroll.domain.PaymentSchedule;
import com.sohclick.hrps.payroll.repository.PaymentScheduleRepository;
import com.sohclick.hrps.payroll.service.PaymentScheduleService;
import com.sohclick.hrps.payroll.service.dto.PaymentScheduleDTO;
import com.sohclick.hrps.payroll.service.mapper.PaymentScheduleMapper;
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
 * Service Implementation for managing {@link PaymentSchedule}.
 */
@Service
public class PaymentScheduleServiceImpl implements PaymentScheduleService {

    private final Logger log = LoggerFactory.getLogger(PaymentScheduleServiceImpl.class);

    private final PaymentScheduleRepository paymentScheduleRepository;

    private final PaymentScheduleMapper paymentScheduleMapper;

    public PaymentScheduleServiceImpl(PaymentScheduleRepository paymentScheduleRepository, PaymentScheduleMapper paymentScheduleMapper) {
        this.paymentScheduleRepository = paymentScheduleRepository;
        this.paymentScheduleMapper = paymentScheduleMapper;
    }

    @Override
    public Mono<PaymentScheduleDTO> save(PaymentScheduleDTO paymentScheduleDTO) {
        log.debug("Request to save PaymentSchedule : {}", paymentScheduleDTO);
        return paymentScheduleRepository.save(paymentScheduleMapper.toEntity(paymentScheduleDTO)).map(paymentScheduleMapper::toDto);
    }

    @Override
    public Mono<PaymentScheduleDTO> update(PaymentScheduleDTO paymentScheduleDTO) {
        log.debug("Request to update PaymentSchedule : {}", paymentScheduleDTO);
        return paymentScheduleRepository.save(paymentScheduleMapper.toEntity(paymentScheduleDTO)).map(paymentScheduleMapper::toDto);
    }

    @Override
    public Mono<PaymentScheduleDTO> partialUpdate(PaymentScheduleDTO paymentScheduleDTO) {
        log.debug("Request to partially update PaymentSchedule : {}", paymentScheduleDTO);

        return paymentScheduleRepository
            .findById(paymentScheduleDTO.getId())
            .map(existingPaymentSchedule -> {
                paymentScheduleMapper.partialUpdate(existingPaymentSchedule, paymentScheduleDTO);

                return existingPaymentSchedule;
            })
            .flatMap(paymentScheduleRepository::save)
            .map(paymentScheduleMapper::toDto);
    }

    @Override
    public Flux<PaymentScheduleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentSchedules");
        return paymentScheduleRepository.findAllBy(pageable).map(paymentScheduleMapper::toDto);
    }

    /**
     *  Get all the paymentSchedules where Payroll is {@code null}.
     *  @return the list of entities.
     */

    public List<PaymentScheduleDTO> findAllWherePayrollIsNull() {
        log.debug("Request to get all paymentSchedules where Payroll is null");
        return paymentScheduleRepository
            .findAll()
            .filter(paymentSchedule -> paymentSchedule.getPayroll() == null)
            .map(paymentScheduleMapper::toDto);
    }

    public Mono<Long> countAll() {
        return paymentScheduleRepository.count();
    }

    @Override
    public Mono<PaymentScheduleDTO> findOne(Long id) {
        log.debug("Request to get PaymentSchedule : {}", id);
        return paymentScheduleRepository.findById(id).map(paymentScheduleMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PaymentSchedule : {}", id);
        return paymentScheduleRepository.deleteById(id);
    }
}
