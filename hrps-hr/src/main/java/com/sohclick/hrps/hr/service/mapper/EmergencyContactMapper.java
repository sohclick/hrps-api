package com.sohclick.hrps.hr.service.mapper;

import com.sohclick.hrps.hr.domain.EmergencyContact;
import com.sohclick.hrps.hr.service.dto.EmergencyContactDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmergencyContact} and its DTO {@link EmergencyContactDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmergencyContactMapper extends EntityMapper<EmergencyContactDTO, EmergencyContact> {}
