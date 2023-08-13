package com.sohclick.hrps.payroll.service.mapper;

import com.sohclick.hrps.payroll.domain.Employee;
import com.sohclick.hrps.payroll.domain.Payroll;
import com.sohclick.hrps.payroll.service.dto.EmployeeDTO;
import com.sohclick.hrps.payroll.service.dto.PayrollDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
    @Mapping(target = "payroll", source = "payroll", qualifiedByName = "payrollId")
    EmployeeDTO toDto(Employee s);

    @Named("payrollId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PayrollDTO toDtoPayrollId(Payroll payroll);
}
