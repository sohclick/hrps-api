package com.sohclick.hrps.hr.service.mapper;

import com.sohclick.hrps.hr.domain.Employee;
import com.sohclick.hrps.hr.domain.ProfessionalQualification;
import com.sohclick.hrps.hr.service.dto.EmployeeDTO;
import com.sohclick.hrps.hr.service.dto.ProfessionalQualificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalQualification} and its DTO {@link ProfessionalQualificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalQualificationMapper extends EntityMapper<ProfessionalQualificationDTO, ProfessionalQualification> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    ProfessionalQualificationDTO toDto(ProfessionalQualification s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);
}
