package com.sohclick.hrps.payroll.service.mapper;

import com.sohclick.hrps.payroll.domain.Benefit;
import com.sohclick.hrps.payroll.domain.Deduction;
import com.sohclick.hrps.payroll.domain.PaymentSchedule;
import com.sohclick.hrps.payroll.domain.Payroll;
import com.sohclick.hrps.payroll.domain.Report;
import com.sohclick.hrps.payroll.domain.TaxInformation;
import com.sohclick.hrps.payroll.domain.TimeAndAttendance;
import com.sohclick.hrps.payroll.service.dto.BenefitDTO;
import com.sohclick.hrps.payroll.service.dto.DeductionDTO;
import com.sohclick.hrps.payroll.service.dto.PaymentScheduleDTO;
import com.sohclick.hrps.payroll.service.dto.PayrollDTO;
import com.sohclick.hrps.payroll.service.dto.ReportDTO;
import com.sohclick.hrps.payroll.service.dto.TaxInformationDTO;
import com.sohclick.hrps.payroll.service.dto.TimeAndAttendanceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payroll} and its DTO {@link PayrollDTO}.
 */
@Mapper(componentModel = "spring")
public interface PayrollMapper extends EntityMapper<PayrollDTO, Payroll> {
    @Mapping(target = "paymentSchedule", source = "paymentSchedule", qualifiedByName = "paymentScheduleId")
    @Mapping(target = "deductions", source = "deductions", qualifiedByName = "deductionId")
    @Mapping(target = "benefits", source = "benefits", qualifiedByName = "benefitId")
    @Mapping(target = "taxInformation", source = "taxInformation", qualifiedByName = "taxInformationId")
    @Mapping(target = "timeAndAttendance", source = "timeAndAttendance", qualifiedByName = "timeAndAttendanceId")
    @Mapping(target = "reportsAndAnalytics", source = "reportsAndAnalytics", qualifiedByName = "reportId")
    PayrollDTO toDto(Payroll s);

    @Named("paymentScheduleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentScheduleDTO toDtoPaymentScheduleId(PaymentSchedule paymentSchedule);

    @Named("deductionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeductionDTO toDtoDeductionId(Deduction deduction);

    @Named("benefitId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BenefitDTO toDtoBenefitId(Benefit benefit);

    @Named("taxInformationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaxInformationDTO toDtoTaxInformationId(TaxInformation taxInformation);

    @Named("timeAndAttendanceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TimeAndAttendanceDTO toDtoTimeAndAttendanceId(TimeAndAttendance timeAndAttendance);

    @Named("reportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDTO toDtoReportId(Report report);
}
