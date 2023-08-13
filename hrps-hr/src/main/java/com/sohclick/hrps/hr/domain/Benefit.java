package com.sohclick.hrps.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohclick.hrps.hr.domain.enumeration.BenefitType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
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

    @NotNull(message = "must not be null")
    @Field("type")
    private BenefitType type;

    @Field("employee")
    @JsonIgnoreProperties(
        value = {
            "emergencyContact",
            "nextOfKin",
            "workHistories",
            "referees",
            "educationHistories",
            "skills",
            "benefits",
            "professionalQualifications",
            "documents",
            "salary",
        },
        allowSetters = true
    )
    private Employee employee;

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

    public BenefitType getType() {
        return this.type;
    }

    public Benefit type(BenefitType type) {
        this.setType(type);
        return this;
    }

    public void setType(BenefitType type) {
        this.type = type;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Benefit employee(Employee employee) {
        this.setEmployee(employee);
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
            ", type='" + getType() + "'" +
            "}";
    }
}
