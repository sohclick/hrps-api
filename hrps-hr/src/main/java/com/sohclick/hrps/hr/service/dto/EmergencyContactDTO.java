package com.sohclick.hrps.hr.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.hr.domain.EmergencyContact} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmergencyContactDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String phoneNumber;

    private String email;

    private String relationship;

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

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmergencyContactDTO)) {
            return false;
        }

        EmergencyContactDTO emergencyContactDTO = (EmergencyContactDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emergencyContactDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmergencyContactDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", relationship='" + getRelationship() + "'" +
            "}";
    }
}
