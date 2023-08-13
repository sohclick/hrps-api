package com.sohclick.hrps.payroll.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Payroll.
 */
@Document(collection = "payroll")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payroll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Field("basic_salary")
    private BigDecimal basicSalary;

    @Field("bonus")
    private BigDecimal bonus;

    @Field("overtime_pay")
    private BigDecimal overtimePay;

    @Field("commission")
    private BigDecimal commission;

    @Field("incentives")
    private BigDecimal incentives;

    @Field("paymentSchedule")
    private PaymentSchedule paymentSchedule;

    @Field("deductions")
    private Deduction deductions;

    @Field("benefits")
    private Benefit benefits;

    @Field("taxInformation")
    private TaxInformation taxInformation;

    @Field("timeAndAttendance")
    private TimeAndAttendance timeAndAttendance;

    @Field("reportsAndAnalytics")
    private Report reportsAndAnalytics;

    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payroll id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBasicSalary() {
        return this.basicSalary;
    }

    public Payroll basicSalary(BigDecimal basicSalary) {
        this.setBasicSalary(basicSalary);
        return this;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public Payroll bonus(BigDecimal bonus) {
        this.setBonus(bonus);
        return this;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getOvertimePay() {
        return this.overtimePay;
    }

    public Payroll overtimePay(BigDecimal overtimePay) {
        this.setOvertimePay(overtimePay);
        return this;
    }

    public void setOvertimePay(BigDecimal overtimePay) {
        this.overtimePay = overtimePay;
    }

    public BigDecimal getCommission() {
        return this.commission;
    }

    public Payroll commission(BigDecimal commission) {
        this.setCommission(commission);
        return this;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getIncentives() {
        return this.incentives;
    }

    public Payroll incentives(BigDecimal incentives) {
        this.setIncentives(incentives);
        return this;
    }

    public void setIncentives(BigDecimal incentives) {
        this.incentives = incentives;
    }

    public PaymentSchedule getPaymentSchedule() {
        return this.paymentSchedule;
    }

    public void setPaymentSchedule(PaymentSchedule paymentSchedule) {
        this.paymentSchedule = paymentSchedule;
    }

    public Payroll paymentSchedule(PaymentSchedule paymentSchedule) {
        this.setPaymentSchedule(paymentSchedule);
        return this;
    }

    public Deduction getDeductions() {
        return this.deductions;
    }

    public void setDeductions(Deduction deduction) {
        this.deductions = deduction;
    }

    public Payroll deductions(Deduction deduction) {
        this.setDeductions(deduction);
        return this;
    }

    public Benefit getBenefits() {
        return this.benefits;
    }

    public void setBenefits(Benefit benefit) {
        this.benefits = benefit;
    }

    public Payroll benefits(Benefit benefit) {
        this.setBenefits(benefit);
        return this;
    }

    public TaxInformation getTaxInformation() {
        return this.taxInformation;
    }

    public void setTaxInformation(TaxInformation taxInformation) {
        this.taxInformation = taxInformation;
    }

    public Payroll taxInformation(TaxInformation taxInformation) {
        this.setTaxInformation(taxInformation);
        return this;
    }

    public TimeAndAttendance getTimeAndAttendance() {
        return this.timeAndAttendance;
    }

    public void setTimeAndAttendance(TimeAndAttendance timeAndAttendance) {
        this.timeAndAttendance = timeAndAttendance;
    }

    public Payroll timeAndAttendance(TimeAndAttendance timeAndAttendance) {
        this.setTimeAndAttendance(timeAndAttendance);
        return this;
    }

    public Report getReportsAndAnalytics() {
        return this.reportsAndAnalytics;
    }

    public void setReportsAndAnalytics(Report report) {
        this.reportsAndAnalytics = report;
    }

    public Payroll reportsAndAnalytics(Report report) {
        this.setReportsAndAnalytics(report);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        if (this.employee != null) {
            this.employee.setPayroll(null);
        }
        if (employee != null) {
            employee.setPayroll(this);
        }
        this.employee = employee;
    }

    public Payroll employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payroll)) {
            return false;
        }
        return id != null && id.equals(((Payroll) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payroll{" +
            "id=" + getId() +
            ", basicSalary=" + getBasicSalary() +
            ", bonus=" + getBonus() +
            ", overtimePay=" + getOvertimePay() +
            ", commission=" + getCommission() +
            ", incentives=" + getIncentives() +
            "}";
    }
}
