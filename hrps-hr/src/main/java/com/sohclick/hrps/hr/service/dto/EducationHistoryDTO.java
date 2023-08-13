package com.sohclick.hrps.hr.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.hr.domain.EducationHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EducationHistoryDTO implements Serializable {

    private Long id;

    private String degree;

    private String school;

    private String country;

    private String state;

    private String city;

    private LocalDate startDate;

    private LocalDate endDate;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
        if (!(o instanceof EducationHistoryDTO)) {
            return false;
        }

        EducationHistoryDTO educationHistoryDTO = (EducationHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, educationHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EducationHistoryDTO{" +
            "id=" + getId() +
            ", degree='" + getDegree() + "'" +
            ", school='" + getSchool() + "'" +
            ", country='" + getCountry() + "'" +
            ", state='" + getState() + "'" +
            ", city='" + getCity() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
