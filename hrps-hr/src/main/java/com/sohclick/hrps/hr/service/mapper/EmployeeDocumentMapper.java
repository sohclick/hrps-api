package com.sohclick.hrps.hr.service.mapper;

import com.sohclick.hrps.hr.domain.Employee;
import com.sohclick.hrps.hr.domain.EmployeeDocument;
import com.sohclick.hrps.hr.service.dto.EmployeeDTO;
import com.sohclick.hrps.hr.service.dto.EmployeeDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmployeeDocument} and its DTO {@link EmployeeDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeDocumentMapper extends EntityMapper<EmployeeDocumentDTO, EmployeeDocument> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    EmployeeDocumentDTO toDto(EmployeeDocument s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);
}
