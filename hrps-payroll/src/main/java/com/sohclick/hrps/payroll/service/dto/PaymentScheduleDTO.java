package com.sohclick.hrps.payroll.service.dto;

import com.sohclick.hrps.payroll.domain.enumeration.PaymentFrequency;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.payroll.domain.PaymentSchedule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentScheduleDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private LocalDate startDate;

    @NotNull(message = "must not be null")
    private LocalDate endDate;

    @NotNull(message = "must not be null")
    private PaymentFrequency frequency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public PaymentFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(PaymentFrequency frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentScheduleDTO)) {
            return false;
        }

        PaymentScheduleDTO paymentScheduleDTO = (PaymentScheduleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentScheduleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentScheduleDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", frequency='" + getFrequency() + "'" +
            "}";
    }
}
