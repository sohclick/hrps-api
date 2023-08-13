package com.sohclick.hrps.payroll.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.payroll.domain.Payroll} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PayrollDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private BigDecimal basicSalary;

    private BigDecimal bonus;

    private BigDecimal overtimePay;

    private BigDecimal commission;

    private BigDecimal incentives;

    private PaymentScheduleDTO paymentSchedule;

    private DeductionDTO deductions;

    private BenefitDTO benefits;

    private TaxInformationDTO taxInformation;

    private TimeAndAttendanceDTO timeAndAttendance;

    private ReportDTO reportsAndAnalytics;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getOvertimePay() {
        return overtimePay;
    }

    public void setOvertimePay(BigDecimal overtimePay) {
        this.overtimePay = overtimePay;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getIncentives() {
        return incentives;
    }

    public void setIncentives(BigDecimal incentives) {
        this.incentives = incentives;
    }

    public PaymentScheduleDTO getPaymentSchedule() {
        return paymentSchedule;
    }

    public void setPaymentSchedule(PaymentScheduleDTO paymentSchedule) {
        this.paymentSchedule = paymentSchedule;
    }

    public DeductionDTO getDeductions() {
        return deductions;
    }

    public void setDeductions(DeductionDTO deductions) {
        this.deductions = deductions;
    }

    public BenefitDTO getBenefits() {
        return benefits;
    }

    public void setBenefits(BenefitDTO benefits) {
        this.benefits = benefits;
    }

    public TaxInformationDTO getTaxInformation() {
        return taxInformation;
    }

    public void setTaxInformation(TaxInformationDTO taxInformation) {
        this.taxInformation = taxInformation;
    }

    public TimeAndAttendanceDTO getTimeAndAttendance() {
        return timeAndAttendance;
    }

    public void setTimeAndAttendance(TimeAndAttendanceDTO timeAndAttendance) {
        this.timeAndAttendance = timeAndAttendance;
    }

    public ReportDTO getReportsAndAnalytics() {
        return reportsAndAnalytics;
    }

    public void setReportsAndAnalytics(ReportDTO reportsAndAnalytics) {
        this.reportsAndAnalytics = reportsAndAnalytics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayrollDTO)) {
            return false;
        }

        PayrollDTO payrollDTO = (PayrollDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, payrollDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PayrollDTO{" +
            "id=" + getId() +
            ", basicSalary=" + getBasicSalary() +
            ", bonus=" + getBonus() +
            ", overtimePay=" + getOvertimePay() +
            ", commission=" + getCommission() +
            ", incentives=" + getIncentives() +
            ", paymentSchedule=" + getPaymentSchedule() +
            ", deductions=" + getDeductions() +
            ", benefits=" + getBenefits() +
            ", taxInformation=" + getTaxInformation() +
            ", timeAndAttendance=" + getTimeAndAttendance() +
            ", reportsAndAnalytics=" + getReportsAndAnalytics() +
            "}";
    }
}
