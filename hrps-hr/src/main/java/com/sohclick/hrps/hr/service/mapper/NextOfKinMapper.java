package com.sohclick.hrps.hr.service.mapper;

import com.sohclick.hrps.hr.domain.NextOfKin;
import com.sohclick.hrps.hr.service.dto.NextOfKinDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NextOfKin} and its DTO {@link NextOfKinDTO}.
 */
@Mapper(componentModel = "spring")
public interface NextOfKinMapper extends EntityMapper<NextOfKinDTO, NextOfKin> {}
