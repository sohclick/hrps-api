package com.sohclick.hrps.payroll.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohclick.hrps.payroll.domain.enumeration.PaymentFrequency;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PaymentSchedule.
 */
@Document(collection = "payment_schedule")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Field("start_date")
    private LocalDate startDate;

    @NotNull(message = "must not be null")
    @Field("end_date")
    private LocalDate endDate;

    @NotNull(message = "must not be null")
    @Field("frequency")
    private PaymentFrequency frequency;

    private Payroll payroll;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentSchedule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public PaymentSchedule startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public PaymentSchedule endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public PaymentFrequency getFrequency() {
        return this.frequency;
    }

    public PaymentSchedule frequency(PaymentFrequency frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(PaymentFrequency frequency) {
        this.frequency = frequency;
    }

    public Payroll getPayroll() {
        return this.payroll;
    }

    public void setPayroll(Payroll payroll) {
        if (this.payroll != null) {
            this.payroll.setPaymentSchedule(null);
        }
        if (payroll != null) {
            payroll.setPaymentSchedule(this);
        }
        this.payroll = payroll;
    }

    public PaymentSchedule payroll(Payroll payroll) {
        this.setPayroll(payroll);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentSchedule)) {
            return false;
        }
        return id != null && id.equals(((PaymentSchedule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentSchedule{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", frequency='" + getFrequency() + "'" +
            "}";
    }
}
