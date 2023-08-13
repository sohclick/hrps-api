package com.sohclick.hrps.payroll.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.payroll.domain.Deduction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeductionDTO implements Serializable {

    private Long id;

    private BigDecimal taxDeductions;

    private BigDecimal providentFund;

    private BigDecimal healthInsurance;

    private BigDecimal loanRepayments;

    private BigDecimal otherDeductions;

    private BigDecimal totalDeduction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTaxDeductions() {
        return taxDeductions;
    }

    public void setTaxDeductions(BigDecimal taxDeductions) {
        this.taxDeductions = taxDeductions;
    }

    public BigDecimal getProvidentFund() {
        return providentFund;
    }

    public void setProvidentFund(BigDecimal providentFund) {
        this.providentFund = providentFund;
    }

    public BigDecimal getHealthInsurance() {
        return healthInsurance;
    }

    public void setHealthInsurance(BigDecimal healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    public BigDecimal getLoanRepayments() {
        return loanRepayments;
    }

    public void setLoanRepayments(BigDecimal loanRepayments) {
        this.loanRepayments = loanRepayments;
    }

    public BigDecimal getOtherDeductions() {
        return otherDeductions;
    }

    public void setOtherDeductions(BigDecimal otherDeductions) {
        this.otherDeductions = otherDeductions;
    }

    public BigDecimal getTotalDeduction() {
        return totalDeduction;
    }

    public void setTotalDeduction(BigDecimal totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeductionDTO)) {
            return false;
        }

        DeductionDTO deductionDTO = (DeductionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deductionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeductionDTO{" +
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
