package com.sohclick.hrps.hr.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.hr.domain.Salary} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalaryDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private BigDecimal amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalaryDTO)) {
            return false;
        }

        SalaryDTO salaryDTO = (SalaryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, salaryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalaryDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            "}";
    }
}
