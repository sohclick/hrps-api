package com.sohclick.hrps.payroll.service.impl;

import com.sohclick.hrps.payroll.domain.Deduction;
import com.sohclick.hrps.payroll.repository.DeductionRepository;
import com.sohclick.hrps.payroll.service.DeductionService;
import com.sohclick.hrps.payroll.service.dto.DeductionDTO;
import com.sohclick.hrps.payroll.service.mapper.DeductionMapper;
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
 * Service Implementation for managing {@link Deduction}.
 */
@Service
public class DeductionServiceImpl implements DeductionService {

    private final Logger log = LoggerFactory.getLogger(DeductionServiceImpl.class);

    private final DeductionRepository deductionRepository;

    private final DeductionMapper deductionMapper;

    public DeductionServiceImpl(DeductionRepository deductionRepository, DeductionMapper deductionMapper) {
        this.deductionRepository = deductionRepository;
        this.deductionMapper = deductionMapper;
    }

    @Override
    public Mono<DeductionDTO> save(DeductionDTO deductionDTO) {
        log.debug("Request to save Deduction : {}", deductionDTO);
        return deductionRepository.save(deductionMapper.toEntity(deductionDTO)).map(deductionMapper::toDto);
    }

    @Override
    public Mono<DeductionDTO> update(DeductionDTO deductionDTO) {
        log.debug("Request to update Deduction : {}", deductionDTO);
        return deductionRepository.save(deductionMapper.toEntity(deductionDTO)).map(deductionMapper::toDto);
    }

    @Override
    public Mono<DeductionDTO> partialUpdate(DeductionDTO deductionDTO) {
        log.debug("Request to partially update Deduction : {}", deductionDTO);

        return deductionRepository
            .findById(deductionDTO.getId())
            .map(existingDeduction -> {
                deductionMapper.partialUpdate(existingDeduction, deductionDTO);

                return existingDeduction;
            })
            .flatMap(deductionRepository::save)
            .map(deductionMapper::toDto);
    }

    @Override
    public Flux<DeductionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Deductions");
        return deductionRepository.findAllBy(pageable).map(deductionMapper::toDto);
    }

    /**
     *  Get all the deductions where Payroll is {@code null}.
     *  @return the list of entities.
     */

    public List<DeductionDTO> findAllWherePayrollIsNull() {
        log.debug("Request to get all deductions where Payroll is null");
        return deductionRepository.findAll().filter(deduction -> deduction.getPayroll() == null).map(deductionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return deductionRepository.count();
    }

    @Override
    public Mono<DeductionDTO> findOne(Long id) {
        log.debug("Request to get Deduction : {}", id);
        return deductionRepository.findById(id).map(deductionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Deduction : {}", id);
        return deductionRepository.deleteById(id);
    }
}
