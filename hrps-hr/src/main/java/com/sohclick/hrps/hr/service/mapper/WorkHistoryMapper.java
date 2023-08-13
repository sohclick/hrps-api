package com.sohclick.hrps.hr.service.mapper;

import com.sohclick.hrps.hr.domain.Employee;
import com.sohclick.hrps.hr.domain.WorkHistory;
import com.sohclick.hrps.hr.service.dto.EmployeeDTO;
import com.sohclick.hrps.hr.service.dto.WorkHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkHistory} and its DTO {@link WorkHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkHistoryMapper extends EntityMapper<WorkHistoryDTO, WorkHistory> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    WorkHistoryDTO toDto(WorkHistory s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);
}
