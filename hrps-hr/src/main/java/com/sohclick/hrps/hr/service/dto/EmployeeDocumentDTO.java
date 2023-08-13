package com.sohclick.hrps.hr.service.dto;

import com.sohclick.hrps.hr.domain.enumeration.DocumentType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.hr.domain.EmployeeDocument} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDocumentDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String description;

    @NotNull(message = "must not be null")
    private DocumentType documentType;

    private LocalDate uploadDate;

    private String url;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        if (!(o instanceof EmployeeDocumentDTO)) {
            return false;
        }

        EmployeeDocumentDTO employeeDocumentDTO = (EmployeeDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDocumentDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            ", url='" + getUrl() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
