package com.sohclick.hrps.payroll.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.payroll.domain.Benefit} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BenefitDTO implements Serializable {

    private Long id;

    private BigDecimal paidLeave;

    private BigDecimal unpaidLeave;

    private BigDecimal maternityLeave;

    private Boolean medicalBenefits;

    private Boolean dentalAndVisionBenefits;

    private Boolean lifeInsurance;

    private Boolean retirementBenefits;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPaidLeave() {
        return paidLeave;
    }

    public void setPaidLeave(BigDecimal paidLeave) {
        this.paidLeave = paidLeave;
    }

    public BigDecimal getUnpaidLeave() {
        return unpaidLeave;
    }

    public void setUnpaidLeave(BigDecimal unpaidLeave) {
        this.unpaidLeave = unpaidLeave;
    }

    public BigDecimal getMaternityLeave() {
        return maternityLeave;
    }

    public void setMaternityLeave(BigDecimal maternityLeave) {
        this.maternityLeave = maternityLeave;
    }

    public Boolean getMedicalBenefits() {
        return medicalBenefits;
    }

    public void setMedicalBenefits(Boolean medicalBenefits) {
        this.medicalBenefits = medicalBenefits;
    }

    public Boolean getDentalAndVisionBenefits() {
        return dentalAndVisionBenefits;
    }

    public void setDentalAndVisionBenefits(Boolean dentalAndVisionBenefits) {
        this.dentalAndVisionBenefits = dentalAndVisionBenefits;
    }

    public Boolean getLifeInsurance() {
        return lifeInsurance;
    }

    public void setLifeInsurance(Boolean lifeInsurance) {
        this.lifeInsurance = lifeInsurance;
    }

    public Boolean getRetirementBenefits() {
        return retirementBenefits;
    }

    public void setRetirementBenefits(Boolean retirementBenefits) {
        this.retirementBenefits = retirementBenefits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BenefitDTO)) {
            return false;
        }

        BenefitDTO benefitDTO = (BenefitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, benefitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BenefitDTO{" +
            "id=" + getId() +
            ", paidLeave=" + getPaidLeave() +
            ", unpaidLeave=" + getUnpaidLeave() +
            ", maternityLeave=" + getMaternityLeave() +
            ", medicalBenefits='" + getMedicalBenefits() + "'" +
            ", dentalAndVisionBenefits='" + getDentalAndVisionBenefits() + "'" +
            ", lifeInsurance='" + getLifeInsurance() + "'" +
            ", retirementBenefits='" + getRetirementBenefits() + "'" +
            "}";
    }
}
