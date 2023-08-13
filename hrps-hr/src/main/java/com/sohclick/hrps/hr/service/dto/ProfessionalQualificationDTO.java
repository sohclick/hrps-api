package com.sohclick.hrps.hr.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.hr.domain.ProfessionalQualification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalQualificationDTO implements Serializable {

    private Long id;

    private String qualificationName;

    private String qualificationInstitution;

    private LocalDate qualificationDate;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

    public String getQualificationInstitution() {
        return qualificationInstitution;
    }

    public void setQualificationInstitution(String qualificationInstitution) {
        this.qualificationInstitution = qualificationInstitution;
    }

    public LocalDate getQualificationDate() {
        return qualificationDate;
    }

    public void setQualificationDate(LocalDate qualificationDate) {
        this.qualificationDate = qualificationDate;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalQualificationDTO)) {
            return false;
        }

        ProfessionalQualificationDTO professionalQualificationDTO = (ProfessionalQualificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalQualificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalQualificationDTO{" +
            "id=" + getId() +
            ", qualificationName='" + getQualificationName() + "'" +
            ", qualificationInstitution='" + getQualificationInstitution() + "'" +
            ", qualificationDate='" + getQualificationDate() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
