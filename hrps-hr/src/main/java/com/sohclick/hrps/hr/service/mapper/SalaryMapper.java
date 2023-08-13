package com.sohclick.hrps.hr.service.mapper;

import com.sohclick.hrps.hr.domain.Salary;
import com.sohclick.hrps.hr.service.dto.SalaryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Salary} and its DTO {@link SalaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface SalaryMapper extends EntityMapper<SalaryDTO, Salary> {}
