package com.sohclick.hrps.payroll.service.impl;

import com.sohclick.hrps.payroll.domain.Benefit;
import com.sohclick.hrps.payroll.repository.BenefitRepository;
import com.sohclick.hrps.payroll.service.BenefitService;
import com.sohclick.hrps.payroll.service.dto.BenefitDTO;
import com.sohclick.hrps.payroll.service.mapper.BenefitMapper;
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
 * Service Implementation for managing {@link Benefit}.
 */
@Service
public class BenefitServiceImpl implements BenefitService {

    private final Logger log = LoggerFactory.getLogger(BenefitServiceImpl.class);

    private final BenefitRepository benefitRepository;

    private final BenefitMapper benefitMapper;

    public BenefitServiceImpl(BenefitRepository benefitRepository, BenefitMapper benefitMapper) {
        this.benefitRepository = benefitRepository;
        this.benefitMapper = benefitMapper;
    }

    @Override
    public Mono<BenefitDTO> save(BenefitDTO benefitDTO) {
        log.debug("Request to save Benefit : {}", benefitDTO);
        return benefitRepository.save(benefitMapper.toEntity(benefitDTO)).map(benefitMapper::toDto);
    }

    @Override
    public Mono<BenefitDTO> update(BenefitDTO benefitDTO) {
        log.debug("Request to update Benefit : {}", benefitDTO);
        return benefitRepository.save(benefitMapper.toEntity(benefitDTO)).map(benefitMapper::toDto);
    }

    @Override
    public Mono<BenefitDTO> partialUpdate(BenefitDTO benefitDTO) {
        log.debug("Request to partially update Benefit : {}", benefitDTO);

        return benefitRepository
            .findById(benefitDTO.getId())
            .map(existingBenefit -> {
                benefitMapper.partialUpdate(existingBenefit, benefitDTO);

                return existingBenefit;
            })
            .flatMap(benefitRepository::save)
            .map(benefitMapper::toDto);
    }

    @Override
    public Flux<BenefitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Benefits");
        return benefitRepository.findAllBy(pageable).map(benefitMapper::toDto);
    }

    /**
     *  Get all the benefits where Payroll is {@code null}.
     *  @return the list of entities.
     */

    public List<BenefitDTO> findAllWherePayrollIsNull() {
        log.debug("Request to get all benefits where Payroll is null");
        return benefitRepository.findAll().filter(benefit -> benefit.getPayroll() == null).map(benefitMapper::toDto);
    }

    public Mono<Long> countAll() {
        return benefitRepository.count();
    }

    @Override
    public Mono<BenefitDTO> findOne(Long id) {
        log.debug("Request to get Benefit : {}", id);
        return benefitRepository.findById(id).map(benefitMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Benefit : {}", id);
        return benefitRepository.deleteById(id);
    }
}
