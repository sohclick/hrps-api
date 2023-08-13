package com.sohclick.hrps.payroll.service.mapper;

import com.sohclick.hrps.payroll.domain.SalaryComponent;
import com.sohclick.hrps.payroll.service.dto.SalaryComponentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SalaryComponent} and its DTO {@link SalaryComponentDTO}.
 */
@Mapper(componentModel = "spring")
public interface SalaryComponentMapper extends EntityMapper<SalaryComponentDTO, SalaryComponent> {}
