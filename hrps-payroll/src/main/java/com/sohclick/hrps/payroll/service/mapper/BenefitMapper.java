package com.sohclick.hrps.payroll.service.mapper;

import com.sohclick.hrps.payroll.domain.Benefit;
import com.sohclick.hrps.payroll.service.dto.BenefitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Benefit} and its DTO {@link BenefitDTO}.
 */
@Mapper(componentModel = "spring")
public interface BenefitMapper extends EntityMapper<BenefitDTO, Benefit> {}
