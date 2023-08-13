package com.sohclick.hrps.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A NextOfKin.
 */
@Document(collection = "next_of_kin")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOfKin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Field("name")
    private String name;

    @NotNull(message = "must not be null")
    @Field("contact_information")
    private String contactInformation;

    @Field("relationship")
    private String relationship;

    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NextOfKin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public NextOfKin name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInformation() {
        return this.contactInformation;
    }

    public NextOfKin contactInformation(String contactInformation) {
        this.setContactInformation(contactInformation);
        return this;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getRelationship() {
        return this.relationship;
    }

    public NextOfKin relationship(String relationship) {
        this.setRelationship(relationship);
        return this;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        if (this.employee != null) {
            this.employee.setNextOfKin(null);
        }
        if (employee != null) {
            employee.setNextOfKin(this);
        }
        this.employee = employee;
    }

    public NextOfKin employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOfKin)) {
            return false;
        }
        return id != null && id.equals(((NextOfKin) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOfKin{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contactInformation='" + getContactInformation() + "'" +
            ", relationship='" + getRelationship() + "'" +
            "}";
    }
}
