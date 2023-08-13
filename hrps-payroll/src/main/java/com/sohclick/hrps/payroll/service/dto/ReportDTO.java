package com.sohclick.hrps.payroll.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.payroll.domain.Report} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportDTO implements Serializable {

    private Long id;

    private Boolean payrollSummaryReports;

    private Boolean payslips;

    private Boolean taxReports;

    private Boolean yearEndReports;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPayrollSummaryReports() {
        return payrollSummaryReports;
    }

    public void setPayrollSummaryReports(Boolean payrollSummaryReports) {
        this.payrollSummaryReports = payrollSummaryReports;
    }

    public Boolean getPayslips() {
        return payslips;
    }

    public void setPayslips(Boolean payslips) {
        this.payslips = payslips;
    }

    public Boolean getTaxReports() {
        return taxReports;
    }

    public void setTaxReports(Boolean taxReports) {
        this.taxReports = taxReports;
    }

    public Boolean getYearEndReports() {
        return yearEndReports;
    }

    public void setYearEndReports(Boolean yearEndReports) {
        this.yearEndReports = yearEndReports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportDTO)) {
            return false;
        }

        ReportDTO reportDTO = (ReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDTO{" +
            "id=" + getId() +
            ", payrollSummaryReports='" + getPayrollSummaryReports() + "'" +
            ", payslips='" + getPayslips() + "'" +
            ", taxReports='" + getTaxReports() + "'" +
            ", yearEndReports='" + getYearEndReports() + "'" +
            "}";
    }
}
