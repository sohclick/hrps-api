package com.sohclick.hrps.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ProfessionalQualification.
 */
@Document(collection = "professional_qualification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalQualification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field("qualification_name")
    private String qualificationName;

    @Field("qualification_institution")
    private String qualificationInstitution;

    @Field("qualification_date")
    private LocalDate qualificationDate;

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

    public ProfessionalQualification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQualificationName() {
        return this.qualificationName;
    }

    public ProfessionalQualification qualificationName(String qualificationName) {
        this.setQualificationName(qualificationName);
        return this;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

    public String getQualificationInstitution() {
        return this.qualificationInstitution;
    }

    public ProfessionalQualification qualificationInstitution(String qualificationInstitution) {
        this.setQualificationInstitution(qualificationInstitution);
        return this;
    }

    public void setQualificationInstitution(String qualificationInstitution) {
        this.qualificationInstitution = qualificationInstitution;
    }

    public LocalDate getQualificationDate() {
        return this.qualificationDate;
    }

    public ProfessionalQualification qualificationDate(LocalDate qualificationDate) {
        this.setQualificationDate(qualificationDate);
        return this;
    }

    public void setQualificationDate(LocalDate qualificationDate) {
        this.qualificationDate = qualificationDate;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ProfessionalQualification employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalQualification)) {
            return false;
        }
        return id != null && id.equals(((ProfessionalQualification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalQualification{" +
            "id=" + getId() +
            ", qualificationName='" + getQualificationName() + "'" +
            ", qualificationInstitution='" + getQualificationInstitution() + "'" +
            ", qualificationDate='" + getQualificationDate() + "'" +
            "}";
    }
}
