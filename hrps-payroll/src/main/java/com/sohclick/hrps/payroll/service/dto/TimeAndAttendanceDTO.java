package com.sohclick.hrps.payroll.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.payroll.domain.TimeAndAttendance} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeAndAttendanceDTO implements Serializable {

    private Long id;

    private BigDecimal employeeWorkingHours;

    private BigDecimal overtimeHours;

    private BigDecimal leavesTaken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getEmployeeWorkingHours() {
        return employeeWorkingHours;
    }

    public void setEmployeeWorkingHours(BigDecimal employeeWorkingHours) {
        this.employeeWorkingHours = employeeWorkingHours;
    }

    public BigDecimal getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(BigDecimal overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public BigDecimal getLeavesTaken() {
        return leavesTaken;
    }

    public void setLeavesTaken(BigDecimal leavesTaken) {
        this.leavesTaken = leavesTaken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeAndAttendanceDTO)) {
            return false;
        }

        TimeAndAttendanceDTO timeAndAttendanceDTO = (TimeAndAttendanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timeAndAttendanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeAndAttendanceDTO{" +
            "id=" + getId() +
            ", employeeWorkingHours=" + getEmployeeWorkingHours() +
            ", overtimeHours=" + getOvertimeHours() +
            ", leavesTaken=" + getLeavesTaken() +
            "}";
    }
}
