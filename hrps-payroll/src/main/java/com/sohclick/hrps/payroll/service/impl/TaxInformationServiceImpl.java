package com.sohclick.hrps.payroll.service.impl;

import com.sohclick.hrps.payroll.domain.TaxInformation;
import com.sohclick.hrps.payroll.repository.TaxInformationRepository;
import com.sohclick.hrps.payroll.service.TaxInformationService;
import com.sohclick.hrps.payroll.service.dto.TaxInformationDTO;
import com.sohclick.hrps.payroll.service.mapper.TaxInformationMapper;
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
 * Service Implementation for managing {@link TaxInformation}.
 */
@Service
public class TaxInformationServiceImpl implements TaxInformationService {

    private final Logger log = LoggerFactory.getLogger(TaxInformationServiceImpl.class);

    private final TaxInformationRepository taxInformationRepository;

    private final TaxInformationMapper taxInformationMapper;

    public TaxInformationServiceImpl(TaxInformationRepository taxInformationRepository, TaxInformationMapper taxInformationMapper) {
        this.taxInformationRepository = taxInformationRepository;
        this.taxInformationMapper = taxInformationMapper;
    }

    @Override
    public Mono<TaxInformationDTO> save(TaxInformationDTO taxInformationDTO) {
        log.debug("Request to save TaxInformation : {}", taxInformationDTO);
        return taxInformationRepository.save(taxInformationMapper.toEntity(taxInformationDTO)).map(taxInformationMapper::toDto);
    }

    @Override
    public Mono<TaxInformationDTO> update(TaxInformationDTO taxInformationDTO) {
        log.debug("Request to update TaxInformation : {}", taxInformationDTO);
        return taxInformationRepository.save(taxInformationMapper.toEntity(taxInformationDTO)).map(taxInformationMapper::toDto);
    }

    @Override
    public Mono<TaxInformationDTO> partialUpdate(TaxInformationDTO taxInformationDTO) {
        log.debug("Request to partially update TaxInformation : {}", taxInformationDTO);

        return taxInformationRepository
            .findById(taxInformationDTO.getId())
            .map(existingTaxInformation -> {
                taxInformationMapper.partialUpdate(existingTaxInformation, taxInformationDTO);

                return existingTaxInformation;
            })
            .flatMap(taxInformationRepository::save)
            .map(taxInformationMapper::toDto);
    }

    @Override
    public Flux<TaxInformationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaxInformations");
        return taxInformationRepository.findAllBy(pageable).map(taxInformationMapper::toDto);
    }
    public Mono<Long> countAll() {
        return taxInformationRepository.count();
    }

    @Override
    public Mono<TaxInformationDTO> findOne(Long id) {
        log.debug("Request to get TaxInformation : {}", id);
        return taxInformationRepository.findById(id).map(taxInformationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TaxInformation : {}", id);
        return taxInformationRepository.deleteById(id);
    }
}
