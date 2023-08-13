package com.sohclick.hrps.payroll.service.mapper;

import com.sohclick.hrps.payroll.domain.Deduction;
import com.sohclick.hrps.payroll.service.dto.DeductionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Deduction} and its DTO {@link DeductionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeductionMapper extends EntityMapper<DeductionDTO, Deduction> {}
