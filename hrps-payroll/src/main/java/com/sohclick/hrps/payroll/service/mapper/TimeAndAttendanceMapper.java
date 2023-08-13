package com.sohclick.hrps.payroll.service.mapper;

import com.sohclick.hrps.payroll.domain.TimeAndAttendance;
import com.sohclick.hrps.payroll.service.dto.TimeAndAttendanceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimeAndAttendance} and its DTO {@link TimeAndAttendanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimeAndAttendanceMapper extends EntityMapper<TimeAndAttendanceDTO, TimeAndAttendance> {}
