package com.sohclick.hrps.hr.service.mapper;

import com.sohclick.hrps.hr.domain.Employee;
import com.sohclick.hrps.hr.domain.Referee;
import com.sohclick.hrps.hr.service.dto.EmployeeDTO;
import com.sohclick.hrps.hr.service.dto.RefereeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Referee} and its DTO {@link RefereeDTO}.
 */
@Mapper(componentModel = "spring")
public interface RefereeMapper extends EntityMapper<RefereeDTO, Referee> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    RefereeDTO toDto(Referee s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);
}
