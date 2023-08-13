package com.sohclick.hrps.hr.service.dto;

import com.sohclick.hrps.hr.domain.enumeration.BenefitType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.hr.domain.Benefit} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BenefitDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private BenefitType type;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BenefitType getType() {
        return type;
    }

    public void setType(BenefitType type) {
        this.type = type;
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
            ", type='" + getType() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
