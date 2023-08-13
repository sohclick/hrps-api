package com.sohclick.hrps.payroll.service.mapper;

import com.sohclick.hrps.payroll.domain.Report;
import com.sohclick.hrps.payroll.service.dto.ReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {}
