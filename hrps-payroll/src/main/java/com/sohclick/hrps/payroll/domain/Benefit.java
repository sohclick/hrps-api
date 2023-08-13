package com.sohclick.hrps.payroll.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Benefit.
 */
@Document(collection = "benefit")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Benefit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field("paid_leave")
    private BigDecimal paidLeave;

    @Field("unpaid_leave")
    private BigDecimal unpaidLeave;

    @Field("maternity_leave")
    private BigDecimal maternityLeave;

    @Field("medical_benefits")
    private Boolean medicalBenefits;

    @Field("dental_and_vision_benefits")
    private Boolean dentalAndVisionBenefits;

    @Field("life_insurance")
    private Boolean lifeInsurance;

    @Field("retirement_benefits")
    private Boolean retirementBenefits;

    private Payroll payroll;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Benefit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPaidLeave() {
        return this.paidLeave;
    }

    public Benefit paidLeave(BigDecimal paidLeave) {
        this.setPaidLeave(paidLeave);
        return this;
    }

    public void setPaidLeave(BigDecimal paidLeave) {
        this.paidLeave = paidLeave;
    }

    public BigDecimal getUnpaidLeave() {
        return this.unpaidLeave;
    }

    public Benefit unpaidLeave(BigDecimal unpaidLeave) {
        this.setUnpaidLeave(unpaidLeave);
        return this;
    }

    public void setUnpaidLeave(BigDecimal unpaidLeave) {
        this.unpaidLeave = unpaidLeave;
    }

    public BigDecimal getMaternityLeave() {
        return this.maternityLeave;
    }

    public Benefit maternityLeave(BigDecimal maternityLeave) {
        this.setMaternityLeave(maternityLeave);
        return this;
    }

    public void setMaternityLeave(BigDecimal maternityLeave) {
        this.maternityLeave = maternityLeave;
    }

    public Boolean getMedicalBenefits() {
        return this.medicalBenefits;
    }

    public Benefit medicalBenefits(Boolean medicalBenefits) {
        this.setMedicalBenefits(medicalBenefits);
        return this;
    }

    public void setMedicalBenefits(Boolean medicalBenefits) {
        this.medicalBenefits = medicalBenefits;
    }

    public Boolean getDentalAndVisionBenefits() {
        return this.dentalAndVisionBenefits;
    }

    public Benefit dentalAndVisionBenefits(Boolean dentalAndVisionBenefits) {
        this.setDentalAndVisionBenefits(dentalAndVisionBenefits);
        return this;
    }

    public void setDentalAndVisionBenefits(Boolean dentalAndVisionBenefits) {
        this.dentalAndVisionBenefits = dentalAndVisionBenefits;
    }

    public Boolean getLifeInsurance() {
        return this.lifeInsurance;
    }

    public Benefit lifeInsurance(Boolean lifeInsurance) {
        this.setLifeInsurance(lifeInsurance);
        return this;
    }

    public void setLifeInsurance(Boolean lifeInsurance) {
        this.lifeInsurance = lifeInsurance;
    }

    public Boolean getRetirementBenefits() {
        return this.retirementBenefits;
    }

    public Benefit retirementBenefits(Boolean retirementBenefits) {
        this.setRetirementBenefits(retirementBenefits);
        return this;
    }

    public void setRetirementBenefits(Boolean retirementBenefits) {
        this.retirementBenefits = retirementBenefits;
    }

    public Payroll getPayroll() {
        return this.payroll;
    }

    public void setPayroll(Payroll payroll) {
        if (this.payroll != null) {
            this.payroll.setBenefits(null);
        }
        if (payroll != null) {
            payroll.setBenefits(this);
        }
        this.payroll = payroll;
    }

    public Benefit payroll(Payroll payroll) {
        this.setPayroll(payroll);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Benefit)) {
            return false;
        }
        return id != null && id.equals(((Benefit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Benefit{" +
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
