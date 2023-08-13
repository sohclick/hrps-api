package com.sohclick.hrps.hr.service.mapper;

import com.sohclick.hrps.hr.domain.Benefit;
import com.sohclick.hrps.hr.domain.Employee;
import com.sohclick.hrps.hr.service.dto.BenefitDTO;
import com.sohclick.hrps.hr.service.dto.EmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Benefit} and its DTO {@link BenefitDTO}.
 */
@Mapper(componentModel = "spring")
public interface BenefitMapper extends EntityMapper<BenefitDTO, Benefit> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    BenefitDTO toDto(Benefit s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);
}
