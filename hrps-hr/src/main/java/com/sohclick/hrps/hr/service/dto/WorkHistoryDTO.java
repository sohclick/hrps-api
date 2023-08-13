package com.sohclick.hrps.hr.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.hr.domain.WorkHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkHistoryDTO implements Serializable {

    private Long id;

    private String employer;

    private String country;

    private String state;

    private String location;

    private String jobTitle;

    private LocalDate startDate;

    private LocalDate endDate;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
        if (!(o instanceof WorkHistoryDTO)) {
            return false;
        }

        WorkHistoryDTO workHistoryDTO = (WorkHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkHistoryDTO{" +
            "id=" + getId() +
            ", employer='" + getEmployer() + "'" +
            ", country='" + getCountry() + "'" +
            ", state='" + getState() + "'" +
            ", location='" + getLocation() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
