package com.sohclick.hrps.payroll.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Deduction.
 */
@Document(collection = "deduction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Deduction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field("tax_deductions")
    private BigDecimal taxDeductions;

    @Field("provident_fund")
    private BigDecimal providentFund;

    @Field("health_insurance")
    private BigDecimal healthInsurance;

    @Field("loan_repayments")
    private BigDecimal loanRepayments;

    @Field("other_deductions")
    private BigDecimal otherDeductions;

    @Field("total_deduction")
    private BigDecimal totalDeduction;

    private Payroll payroll;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Deduction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTaxDeductions() {
        return this.taxDeductions;
    }

    public Deduction taxDeductions(BigDecimal taxDeductions) {
        this.setTaxDeductions(taxDeductions);
        return this;
    }

    public void setTaxDeductions(BigDecimal taxDeductions) {
        this.taxDeductions = taxDeductions;
    }

    public BigDecimal getProvidentFund() {
        return this.providentFund;
    }

    public Deduction providentFund(BigDecimal providentFund) {
        this.setProvidentFund(providentFund);
        return this;
    }

    public void setProvidentFund(BigDecimal providentFund) {
        this.providentFund = providentFund;
    }

    public BigDecimal getHealthInsurance() {
        return this.healthInsurance;
    }

    public Deduction healthInsurance(BigDecimal healthInsurance) {
        this.setHealthInsurance(healthInsurance);
        return this;
    }

    public void setHealthInsurance(BigDecimal healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    public BigDecimal getLoanRepayments() {
        return this.loanRepayments;
    }

    public Deduction loanRepayments(BigDecimal loanRepayments) {
        this.setLoanRepayments(loanRepayments);
        return this;
    }

    public void setLoanRepayments(BigDecimal loanRepayments) {
        this.loanRepayments = loanRepayments;
    }

    public BigDecimal getOtherDeductions() {
        return this.otherDeductions;
    }

    public Deduction otherDeductions(BigDecimal otherDeductions) {
        this.setOtherDeductions(otherDeductions);
        return this;
    }

    public void setOtherDeductions(BigDecimal otherDeductions) {
        this.otherDeductions = otherDeductions;
    }

    public BigDecimal getTotalDeduction() {
        return this.totalDeduction;
    }

    public Deduction totalDeduction(BigDecimal totalDeduction) {
        this.setTotalDeduction(totalDeduction);
        return this;
    }

    public void setTotalDeduction(BigDecimal totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public Payroll getPayroll() {
        return this.payroll;
    }

    public void setPayroll(Payroll payroll) {
        if (this.payroll != null) {
            this.payroll.setDeductions(null);
        }
        if (payroll != null) {
            payroll.setDeductions(this);
        }
        this.payroll = payroll;
    }

    public Deduction payroll(Payroll payroll) {
        this.setPayroll(payroll);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deduction)) {
            return false;
        }
        return id != null && id.equals(((Deduction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deduction{" +
            "id=" + getId() +
            ", taxDeductions=" + getTaxDeductions() +
            ", providentFund=" + getProvidentFund() +
            ", healthInsurance=" + getHealthInsurance() +
            ", loanRepayments=" + getLoanRepayments() +
            ", otherDeductions=" + getOtherDeductions() +
            ", totalDeduction=" + getTotalDeduction() +
            "}";
    }
}
