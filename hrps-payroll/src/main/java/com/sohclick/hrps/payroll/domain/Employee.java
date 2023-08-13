package com.sohclick.hrps.payroll.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohclick.hrps.payroll.domain.enumeration.EmploymentStatusType;
import com.sohclick.hrps.payroll.domain.enumeration.Gender;
import com.sohclick.hrps.payroll.domain.enumeration.MaritalStatusType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Employee.
 */
@Document(collection = "employee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Field("surname")
    private String surname;

    @NotNull(message = "must not be null")
    @Field("first_name")
    private String firstName;

    @Field("middle_name")
    private String middleName;

    @NotNull(message = "must not be null")
    @Field("date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull(message = "must not be null")
    @Field("gender")
    private Gender gender;

    @NotNull(message = "must not be null")
    @Field("marital_status")
    private MaritalStatusType maritalStatus;

    @NotNull(message = "must not be null")
    @Field("resident_address")
    private String residentAddress;

    @NotNull(message = "must not be null")
    @Field("resident_country")
    private String residentCountry;

    @NotNull(message = "must not be null")
    @Field("resident_city")
    private String residentCity;

    @NotNull(message = "must not be null")
    @Field("nationality")
    private String nationality;

    @NotNull(message = "must not be null")
    @Field("state")
    private String state;

    @NotNull(message = "must not be null")
    @Field("lga")
    private String lga;

    @Field("phone_number")
    private String phoneNumber;

    @Field("email")
    private String email;

    @Field("bvn")
    private String bvn;

    @Field("date_employed")
    private LocalDate dateEmployed;

    @Field("employment_status")
    private EmploymentStatusType employmentStatus;

    @Field("department")
    private String department;

    @Field("job_title")
    private String jobTitle;

    @Field("payroll")
    private Payroll payroll;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return this.surname;
    }

    public Employee surname(String surname) {
        this.setSurname(surname);
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Employee firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public Employee middleName(String middleName) {
        this.setMiddleName(middleName);
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Employee dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Employee gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MaritalStatusType getMaritalStatus() {
        return this.maritalStatus;
    }

    public Employee maritalStatus(MaritalStatusType maritalStatus) {
        this.setMaritalStatus(maritalStatus);
        return this;
    }

    public void setMaritalStatus(MaritalStatusType maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getResidentAddress() {
        return this.residentAddress;
    }

    public Employee residentAddress(String residentAddress) {
        this.setResidentAddress(residentAddress);
        return this;
    }

    public void setResidentAddress(String residentAddress) {
        this.residentAddress = residentAddress;
    }

    public String getResidentCountry() {
        return this.residentCountry;
    }

    public Employee residentCountry(String residentCountry) {
        this.setResidentCountry(residentCountry);
        return this;
    }

    public void setResidentCountry(String residentCountry) {
        this.residentCountry = residentCountry;
    }

    public String getResidentCity() {
        return this.residentCity;
    }

    public Employee residentCity(String residentCity) {
        this.setResidentCity(residentCity);
        return this;
    }

    public void setResidentCity(String residentCity) {
        this.residentCity = residentCity;
    }

    public String getNationality() {
        return this.nationality;
    }

    public Employee nationality(String nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getState() {
        return this.state;
    }

    public Employee state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return this.lga;
    }

    public Employee lga(String lga) {
        this.setLga(lga);
        return this;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Employee phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public Employee email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBvn() {
        return this.bvn;
    }

    public Employee bvn(String bvn) {
        this.setBvn(bvn);
        return this;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public LocalDate getDateEmployed() {
        return this.dateEmployed;
    }

    public Employee dateEmployed(LocalDate dateEmployed) {
        this.setDateEmployed(dateEmployed);
        return this;
    }

    public void setDateEmployed(LocalDate dateEmployed) {
        this.dateEmployed = dateEmployed;
    }

    public EmploymentStatusType getEmploymentStatus() {
        return this.employmentStatus;
    }

    public Employee employmentStatus(EmploymentStatusType employmentStatus) {
        this.setEmploymentStatus(employmentStatus);
        return this;
    }

    public void setEmploymentStatus(EmploymentStatusType employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getDepartment() {
        return this.department;
    }

    public Employee department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public Employee jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Payroll getPayroll() {
        return this.payroll;
    }

    public void setPayroll(Payroll payroll) {
        this.payroll = payroll;
    }

    public Employee payroll(Payroll payroll) {
        this.setPayroll(payroll);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
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
            "}";
    }
}
