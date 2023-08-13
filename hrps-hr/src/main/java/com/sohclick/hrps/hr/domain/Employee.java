package com.sohclick.hrps.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohclick.hrps.hr.domain.enumeration.EmploymentStatusType;
import com.sohclick.hrps.hr.domain.enumeration.GenderType;
import com.sohclick.hrps.hr.domain.enumeration.MaritalStatusType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
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
    private GenderType gender;

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

    @Field("emergencyContact")
    private EmergencyContact emergencyContact;

    @Field("nextOfKin")
    private NextOfKin nextOfKin;

    @Field("workHistory")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<WorkHistory> workHistories = new HashSet<>();

    @Field("referees")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Referee> referees = new HashSet<>();

    @Field("educationHistory")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<EducationHistory> educationHistories = new HashSet<>();

    @Field("skills")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Skill> skills = new HashSet<>();

    @Field("benefits")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Benefit> benefits = new HashSet<>();

    @Field("professionalQualifications")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<ProfessionalQualification> professionalQualifications = new HashSet<>();

    @Field("documents")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<EmployeeDocument> documents = new HashSet<>();

    @Field("salary")
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Salary salary;

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

    public GenderType getGender() {
        return this.gender;
    }

    public Employee gender(GenderType gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(GenderType gender) {
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

    public EmergencyContact getEmergencyContact() {
        return this.emergencyContact;
    }

    public void setEmergencyContact(EmergencyContact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public Employee emergencyContact(EmergencyContact emergencyContact) {
        this.setEmergencyContact(emergencyContact);
        return this;
    }

    public NextOfKin getNextOfKin() {
        return this.nextOfKin;
    }

    public void setNextOfKin(NextOfKin nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public Employee nextOfKin(NextOfKin nextOfKin) {
        this.setNextOfKin(nextOfKin);
        return this;
    }

    public Set<WorkHistory> getWorkHistories() {
        return this.workHistories;
    }

    public void setWorkHistories(Set<WorkHistory> workHistories) {
        if (this.workHistories != null) {
            this.workHistories.forEach(i -> i.setEmployee(null));
        }
        if (workHistories != null) {
            workHistories.forEach(i -> i.setEmployee(this));
        }
        this.workHistories = workHistories;
    }

    public Employee workHistories(Set<WorkHistory> workHistories) {
        this.setWorkHistories(workHistories);
        return this;
    }

    public Employee addWorkHistory(WorkHistory workHistory) {
        this.workHistories.add(workHistory);
        workHistory.setEmployee(this);
        return this;
    }

    public Employee removeWorkHistory(WorkHistory workHistory) {
        this.workHistories.remove(workHistory);
        workHistory.setEmployee(null);
        return this;
    }

    public Set<Referee> getReferees() {
        return this.referees;
    }

    public void setReferees(Set<Referee> referees) {
        if (this.referees != null) {
            this.referees.forEach(i -> i.setEmployee(null));
        }
        if (referees != null) {
            referees.forEach(i -> i.setEmployee(this));
        }
        this.referees = referees;
    }

    public Employee referees(Set<Referee> referees) {
        this.setReferees(referees);
        return this;
    }

    public Employee addReferees(Referee referee) {
        this.referees.add(referee);
        referee.setEmployee(this);
        return this;
    }

    public Employee removeReferees(Referee referee) {
        this.referees.remove(referee);
        referee.setEmployee(null);
        return this;
    }

    public Set<EducationHistory> getEducationHistories() {
        return this.educationHistories;
    }

    public void setEducationHistories(Set<EducationHistory> educationHistories) {
        if (this.educationHistories != null) {
            this.educationHistories.forEach(i -> i.setEmployee(null));
        }
        if (educationHistories != null) {
            educationHistories.forEach(i -> i.setEmployee(this));
        }
        this.educationHistories = educationHistories;
    }

    public Employee educationHistories(Set<EducationHistory> educationHistories) {
        this.setEducationHistories(educationHistories);
        return this;
    }

    public Employee addEducationHistory(EducationHistory educationHistory) {
        this.educationHistories.add(educationHistory);
        educationHistory.setEmployee(this);
        return this;
    }

    public Employee removeEducationHistory(EducationHistory educationHistory) {
        this.educationHistories.remove(educationHistory);
        educationHistory.setEmployee(null);
        return this;
    }

    public Set<Skill> getSkills() {
        return this.skills;
    }

    public void setSkills(Set<Skill> skills) {
        if (this.skills != null) {
            this.skills.forEach(i -> i.setEmployee(null));
        }
        if (skills != null) {
            skills.forEach(i -> i.setEmployee(this));
        }
        this.skills = skills;
    }

    public Employee skills(Set<Skill> skills) {
        this.setSkills(skills);
        return this;
    }

    public Employee addSkills(Skill skill) {
        this.skills.add(skill);
        skill.setEmployee(this);
        return this;
    }

    public Employee removeSkills(Skill skill) {
        this.skills.remove(skill);
        skill.setEmployee(null);
        return this;
    }

    public Set<Benefit> getBenefits() {
        return this.benefits;
    }

    public void setBenefits(Set<Benefit> benefits) {
        if (this.benefits != null) {
            this.benefits.forEach(i -> i.setEmployee(null));
        }
        if (benefits != null) {
            benefits.forEach(i -> i.setEmployee(this));
        }
        this.benefits = benefits;
    }

    public Employee benefits(Set<Benefit> benefits) {
        this.setBenefits(benefits);
        return this;
    }

    public Employee addBenefits(Benefit benefit) {
        this.benefits.add(benefit);
        benefit.setEmployee(this);
        return this;
    }

    public Employee removeBenefits(Benefit benefit) {
        this.benefits.remove(benefit);
        benefit.setEmployee(null);
        return this;
    }

    public Set<ProfessionalQualification> getProfessionalQualifications() {
        return this.professionalQualifications;
    }

    public void setProfessionalQualifications(Set<ProfessionalQualification> professionalQualifications) {
        if (this.professionalQualifications != null) {
            this.professionalQualifications.forEach(i -> i.setEmployee(null));
        }
        if (professionalQualifications != null) {
            professionalQualifications.forEach(i -> i.setEmployee(this));
        }
        this.professionalQualifications = professionalQualifications;
    }

    public Employee professionalQualifications(Set<ProfessionalQualification> professionalQualifications) {
        this.setProfessionalQualifications(professionalQualifications);
        return this;
    }

    public Employee addProfessionalQualifications(ProfessionalQualification professionalQualification) {
        this.professionalQualifications.add(professionalQualification);
        professionalQualification.setEmployee(this);
        return this;
    }

    public Employee removeProfessionalQualifications(ProfessionalQualification professionalQualification) {
        this.professionalQualifications.remove(professionalQualification);
        professionalQualification.setEmployee(null);
        return this;
    }

    public Set<EmployeeDocument> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<EmployeeDocument> employeeDocuments) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setEmployee(null));
        }
        if (employeeDocuments != null) {
            employeeDocuments.forEach(i -> i.setEmployee(this));
        }
        this.documents = employeeDocuments;
    }

    public Employee documents(Set<EmployeeDocument> employeeDocuments) {
        this.setDocuments(employeeDocuments);
        return this;
    }

    public Employee addDocuments(EmployeeDocument employeeDocument) {
        this.documents.add(employeeDocument);
        employeeDocument.setEmployee(this);
        return this;
    }

    public Employee removeDocuments(EmployeeDocument employeeDocument) {
        this.documents.remove(employeeDocument);
        employeeDocument.setEmployee(null);
        return this;
    }

    public Salary getSalary() {
        return this.salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    public Employee salary(Salary salary) {
        this.setSalary(salary);
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
