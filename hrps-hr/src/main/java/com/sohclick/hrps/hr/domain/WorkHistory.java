package com.sohclick.hrps.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A WorkHistory.
 */
@Document(collection = "work_history")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field("employer")
    private String employer;

    @Field("country")
    private String country;

    @Field("state")
    private String state;

    @Field("location")
    private String location;

    @Field("job_title")
    private String jobTitle;

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

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

    public WorkHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployer() {
        return this.employer;
    }

    public WorkHistory employer(String employer) {
        this.setEmployer(employer);
        return this;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getCountry() {
        return this.country;
    }

    public WorkHistory country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return this.state;
    }

    public WorkHistory state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocation() {
        return this.location;
    }

    public WorkHistory location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public WorkHistory jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public WorkHistory startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public WorkHistory endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public WorkHistory employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkHistory)) {
            return false;
        }
        return id != null && id.equals(((WorkHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkHistory{" +
            "id=" + getId() +
            ", employer='" + getEmployer() + "'" +
            ", country='" + getCountry() + "'" +
            ", state='" + getState() + "'" +
            ", location='" + getLocation() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
