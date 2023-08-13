package com.sohclick.hrps.payroll.service.mapper;

import com.sohclick.hrps.payroll.domain.PaymentSchedule;
import com.sohclick.hrps.payroll.service.dto.PaymentScheduleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentSchedule} and its DTO {@link PaymentScheduleDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentScheduleMapper extends EntityMapper<PaymentScheduleDTO, PaymentSchedule> {}
