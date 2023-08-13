package com.sohclick.hrps.payroll.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A TimeAndAttendance.
 */
@Document(collection = "time_and_attendance")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeAndAttendance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field("employee_working_hours")
    private BigDecimal employeeWorkingHours;

    @Field("overtime_hours")
    private BigDecimal overtimeHours;

    @Field("leaves_taken")
    private BigDecimal leavesTaken;

    private Payroll payroll;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TimeAndAttendance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getEmployeeWorkingHours() {
        return this.employeeWorkingHours;
    }

    public TimeAndAttendance employeeWorkingHours(BigDecimal employeeWorkingHours) {
        this.setEmployeeWorkingHours(employeeWorkingHours);
        return this;
    }

    public void setEmployeeWorkingHours(BigDecimal employeeWorkingHours) {
        this.employeeWorkingHours = employeeWorkingHours;
    }

    public BigDecimal getOvertimeHours() {
        return this.overtimeHours;
    }

    public TimeAndAttendance overtimeHours(BigDecimal overtimeHours) {
        this.setOvertimeHours(overtimeHours);
        return this;
    }

    public void setOvertimeHours(BigDecimal overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public BigDecimal getLeavesTaken() {
        return this.leavesTaken;
    }

    public TimeAndAttendance leavesTaken(BigDecimal leavesTaken) {
        this.setLeavesTaken(leavesTaken);
        return this;
    }

    public void setLeavesTaken(BigDecimal leavesTaken) {
        this.leavesTaken = leavesTaken;
    }

    public Payroll getPayroll() {
        return this.payroll;
    }

    public void setPayroll(Payroll payroll) {
        if (this.payroll != null) {
            this.payroll.setTimeAndAttendance(null);
        }
        if (payroll != null) {
            payroll.setTimeAndAttendance(this);
        }
        this.payroll = payroll;
    }

    public TimeAndAttendance payroll(Payroll payroll) {
        this.setPayroll(payroll);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeAndAttendance)) {
            return false;
        }
        return id != null && id.equals(((TimeAndAttendance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeAndAttendance{" +
            "id=" + getId() +
            ", employeeWorkingHours=" + getEmployeeWorkingHours() +
            ", overtimeHours=" + getOvertimeHours() +
            ", leavesTaken=" + getLeavesTaken() +
            "}";
    }
}
