package com.sohclick.hrps.hr.service.mapper;

import com.sohclick.hrps.hr.domain.EmergencyContact;
import com.sohclick.hrps.hr.domain.Employee;
import com.sohclick.hrps.hr.domain.NextOfKin;
import com.sohclick.hrps.hr.domain.Salary;
import com.sohclick.hrps.hr.service.dto.EmergencyContactDTO;
import com.sohclick.hrps.hr.service.dto.EmployeeDTO;
import com.sohclick.hrps.hr.service.dto.NextOfKinDTO;
import com.sohclick.hrps.hr.service.dto.SalaryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
    @Mapping(target = "emergencyContact", source = "emergencyContact", qualifiedByName = "emergencyContactId")
    @Mapping(target = "nextOfKin", source = "nextOfKin", qualifiedByName = "nextOfKinId")
    @Mapping(target = "salary", source = "salary", qualifiedByName = "salaryId")
    EmployeeDTO toDto(Employee s);

    @Named("emergencyContactId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmergencyContactDTO toDtoEmergencyContactId(EmergencyContact emergencyContact);

    @Named("nextOfKinId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NextOfKinDTO toDtoNextOfKinId(NextOfKin nextOfKin);

    @Named("salaryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SalaryDTO toDtoSalaryId(Salary salary);
}
