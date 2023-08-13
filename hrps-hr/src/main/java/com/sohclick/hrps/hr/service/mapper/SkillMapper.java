package com.sohclick.hrps.hr.service.mapper;

import com.sohclick.hrps.hr.domain.Employee;
import com.sohclick.hrps.hr.domain.Skill;
import com.sohclick.hrps.hr.service.dto.EmployeeDTO;
import com.sohclick.hrps.hr.service.dto.SkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    SkillDTO toDto(Skill s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);
}
