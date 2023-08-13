package com.sohclick.hrps.hr.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.hr.domain.Referee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RefereeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String phoneNumber;

    private String email;

    private String jobTitle;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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
        if (!(o instanceof RefereeDTO)) {
            return false;
        }

        RefereeDTO refereeDTO = (RefereeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, refereeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RefereeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
