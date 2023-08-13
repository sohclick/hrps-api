package com.sohclick.hrps.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohclick.hrps.hr.domain.enumeration.DocumentType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A EmployeeDocument.
 */
@Document(collection = "employee_document")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Field("description")
    private String description;

    @NotNull(message = "must not be null")
    @Field("document_type")
    private DocumentType documentType;

    @Field("upload_date")
    private LocalDate uploadDate;

    @Field("url")
    private String url;

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

    public EmployeeDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public EmployeeDocument description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public EmployeeDocument documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public LocalDate getUploadDate() {
        return this.uploadDate;
    }

    public EmployeeDocument uploadDate(LocalDate uploadDate) {
        this.setUploadDate(uploadDate);
        return this;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUrl() {
        return this.url;
    }

    public EmployeeDocument url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeDocument employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDocument)) {
            return false;
        }
        return id != null && id.equals(((EmployeeDocument) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDocument{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
