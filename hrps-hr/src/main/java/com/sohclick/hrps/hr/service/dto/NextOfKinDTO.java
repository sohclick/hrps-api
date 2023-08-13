package com.sohclick.hrps.hr.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.hr.domain.NextOfKin} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextOfKinDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private String contactInformation;

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

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
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
        if (!(o instanceof NextOfKinDTO)) {
            return false;
        }

        NextOfKinDTO nextOfKinDTO = (NextOfKinDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextOfKinDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOfKinDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contactInformation='" + getContactInformation() + "'" +
            ", relationship='" + getRelationship() + "'" +
            "}";
    }
}
