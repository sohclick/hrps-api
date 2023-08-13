package com.sohclick.hrps.payroll.service.impl;

import com.sohclick.hrps.payroll.domain.TimeAndAttendance;
import com.sohclick.hrps.payroll.repository.TimeAndAttendanceRepository;
import com.sohclick.hrps.payroll.service.TimeAndAttendanceService;
import com.sohclick.hrps.payroll.service.dto.TimeAndAttendanceDTO;
import com.sohclick.hrps.payroll.service.mapper.TimeAndAttendanceMapper;
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
 * Service Implementation for managing {@link TimeAndAttendance}.
 */
@Service
public class TimeAndAttendanceServiceImpl implements TimeAndAttendanceService {

    private final Logger log = LoggerFactory.getLogger(TimeAndAttendanceServiceImpl.class);

    private final TimeAndAttendanceRepository timeAndAttendanceRepository;

    private final TimeAndAttendanceMapper timeAndAttendanceMapper;

    public TimeAndAttendanceServiceImpl(
        TimeAndAttendanceRepository timeAndAttendanceRepository,
        TimeAndAttendanceMapper timeAndAttendanceMapper
    ) {
        this.timeAndAttendanceRepository = timeAndAttendanceRepository;
        this.timeAndAttendanceMapper = timeAndAttendanceMapper;
    }

    @Override
    public Mono<TimeAndAttendanceDTO> save(TimeAndAttendanceDTO timeAndAttendanceDTO) {
        log.debug("Request to save TimeAndAttendance : {}", timeAndAttendanceDTO);
        return timeAndAttendanceRepository.save(timeAndAttendanceMapper.toEntity(timeAndAttendanceDTO)).map(timeAndAttendanceMapper::toDto);
    }

    @Override
    public Mono<TimeAndAttendanceDTO> update(TimeAndAttendanceDTO timeAndAttendanceDTO) {
        log.debug("Request to update TimeAndAttendance : {}", timeAndAttendanceDTO);
        return timeAndAttendanceRepository.save(timeAndAttendanceMapper.toEntity(timeAndAttendanceDTO)).map(timeAndAttendanceMapper::toDto);
    }

    @Override
    public Mono<TimeAndAttendanceDTO> partialUpdate(TimeAndAttendanceDTO timeAndAttendanceDTO) {
        log.debug("Request to partially update TimeAndAttendance : {}", timeAndAttendanceDTO);

        return timeAndAttendanceRepository
            .findById(timeAndAttendanceDTO.getId())
            .map(existingTimeAndAttendance -> {
                timeAndAttendanceMapper.partialUpdate(existingTimeAndAttendance, timeAndAttendanceDTO);

                return existingTimeAndAttendance;
            })
            .flatMap(timeAndAttendanceRepository::save)
            .map(timeAndAttendanceMapper::toDto);
    }

    @Override
    public Flux<TimeAndAttendanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TimeAndAttendances");
        return timeAndAttendanceRepository.findAllBy(pageable).map(timeAndAttendanceMapper::toDto);
    }

    /**
     *  Get all the timeAndAttendances where Payroll is {@code null}.
     *  @return the list of entities.
     */

    public List<TimeAndAttendanceDTO> findAllWherePayrollIsNull() {
        log.debug("Request to get all timeAndAttendances where Payroll is null");
        return timeAndAttendanceRepository
            .findAll()
            .filter(timeAndAttendance -> timeAndAttendance.getPayroll() == null)
            .map(timeAndAttendanceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return timeAndAttendanceRepository.count();
    }

    @Override
    public Mono<TimeAndAttendanceDTO> findOne(Long id) {
        log.debug("Request to get TimeAndAttendance : {}", id);
        return timeAndAttendanceRepository.findById(id).map(timeAndAttendanceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TimeAndAttendance : {}", id);
        return timeAndAttendanceRepository.deleteById(id);
    }
}
