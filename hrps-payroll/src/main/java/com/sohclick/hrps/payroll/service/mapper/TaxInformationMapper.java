package com.sohclick.hrps.payroll.service.mapper;

import com.sohclick.hrps.payroll.domain.TaxInformation;
import com.sohclick.hrps.payroll.service.dto.TaxInformationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaxInformation} and its DTO {@link TaxInformationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaxInformationMapper extends EntityMapper<TaxInformationDTO, TaxInformation> {}
