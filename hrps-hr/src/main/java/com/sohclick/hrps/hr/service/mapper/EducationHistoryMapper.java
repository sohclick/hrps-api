package com.sohclick.hrps.hr.service.mapper;

import com.sohclick.hrps.hr.domain.EducationHistory;
import com.sohclick.hrps.hr.domain.Employee;
import com.sohclick.hrps.hr.service.dto.EducationHistoryDTO;
import com.sohclick.hrps.hr.service.dto.EmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EducationHistory} and its DTO {@link EducationHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface EducationHistoryMapper extends EntityMapper<EducationHistoryDTO, EducationHistory> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    EducationHistoryDTO toDto(EducationHistory s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);
}
