package com.sohclick.hrps.payroll.service.dto;

import com.sohclick.hrps.payroll.domain.enumeration.EmploymentStatusType;
import com.sohclick.hrps.payroll.domain.enumeration.Gender;
import com.sohclick.hrps.payroll.domain.enumeration.MaritalStatusType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.payroll.domain.Employee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String surname;

    @NotNull(message = "must not be null")
    private String firstName;

    private String middleName;

    @NotNull(message = "must not be null")
    private LocalDate dateOfBirth;

    @NotNull(message = "must not be null")
    private Gender gender;

    @NotNull(message = "must not be null")
    private MaritalStatusType maritalStatus;

    @NotNull(message = "must not be null")
    private String residentAddress;

    @NotNull(message = "must not be null")
    private String residentCountry;

    @NotNull(message = "must not be null")
    private String residentCity;

    @NotNull(message = "must not be null")
    private String nationality;

    @NotNull(message = "must not be null")
    private String state;

    @NotNull(message = "must not be null")
    private String lga;

    private String phoneNumber;

    private String email;

    private String bvn;

    private LocalDate dateEmployed;

    private EmploymentStatusType employmentStatus;

    private String department;

    private String jobTitle;

    private PayrollDTO payroll;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MaritalStatusType getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatusType maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getResidentAddress() {
        return residentAddress;
    }

    public void setResidentAddress(String residentAddress) {
        this.residentAddress = residentAddress;
    }

    public String getResidentCountry() {
        return residentCountry;
    }

    public void setResidentCountry(String residentCountry) {
        this.residentCountry = residentCountry;
    }

    public String getResidentCity() {
        return residentCity;
    }

    public void setResidentCity(String residentCity) {
        this.residentCity = residentCity;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
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

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public LocalDate getDateEmployed() {
        return dateEmployed;
    }

    public void setDateEmployed(LocalDate dateEmployed) {
        this.dateEmployed = dateEmployed;
    }

    public EmploymentStatusType getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatusType employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public PayrollDTO getPayroll() {
        return payroll;
    }

    public void setPayroll(PayrollDTO payroll) {
        this.payroll = payroll;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO)) {
            return false;
        }

        EmployeeDTO employeeDTO = (EmployeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id=" + getId() +
            ", surname='" + getSurname() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", gender='" + getGender() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", residentAddress='" + getResidentAddress() + "'" +
            ", residentCountry='" + getResidentCountry() + "'" +
            ", residentCity='" + getResidentCity() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", state='" + getState() + "'" +
            ", lga='" + getLga() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", bvn='" + getBvn() + "'" +
            ", dateEmployed='" + getDateEmployed() + "'" +
            ", employmentStatus='" + getEmploymentStatus() + "'" +
            ", department='" + getDepartment() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", payroll=" + getPayroll() +
            "}";
    }
}
