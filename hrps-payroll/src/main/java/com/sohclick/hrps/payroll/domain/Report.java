package com.sohclick.hrps.payroll.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Report.
 */
@Document(collection = "report")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field("payroll_summary_reports")
    private Boolean payrollSummaryReports;

    @Field("payslips")
    private Boolean payslips;

    @Field("tax_reports")
    private Boolean taxReports;

    @Field("year_end_reports")
    private Boolean yearEndReports;

    private Payroll payroll;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Report id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPayrollSummaryReports() {
        return this.payrollSummaryReports;
    }

    public Report payrollSummaryReports(Boolean payrollSummaryReports) {
        this.setPayrollSummaryReports(payrollSummaryReports);
        return this;
    }

    public void setPayrollSummaryReports(Boolean payrollSummaryReports) {
        this.payrollSummaryReports = payrollSummaryReports;
    }

    public Boolean getPayslips() {
        return this.payslips;
    }

    public Report payslips(Boolean payslips) {
        this.setPayslips(payslips);
        return this;
    }

    public void setPayslips(Boolean payslips) {
        this.payslips = payslips;
    }

    public Boolean getTaxReports() {
        return this.taxReports;
    }

    public Report taxReports(Boolean taxReports) {
        this.setTaxReports(taxReports);
        return this;
    }

    public void setTaxReports(Boolean taxReports) {
        this.taxReports = taxReports;
    }

    public Boolean getYearEndReports() {
        return this.yearEndReports;
    }

    public Report yearEndReports(Boolean yearEndReports) {
        this.setYearEndReports(yearEndReports);
        return this;
    }

    public void setYearEndReports(Boolean yearEndReports) {
        this.yearEndReports = yearEndReports;
    }

    public Payroll getPayroll() {
        return this.payroll;
    }

    public void setPayroll(Payroll payroll) {
        if (this.payroll != null) {
            this.payroll.setReportsAndAnalytics(null);
        }
        if (payroll != null) {
            payroll.setReportsAndAnalytics(this);
        }
        this.payroll = payroll;
    }

    public Report payroll(Payroll payroll) {
        this.setPayroll(payroll);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        return id != null && id.equals(((Report) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", payrollSummaryReports='" + getPayrollSummaryReports() + "'" +
            ", payslips='" + getPayslips() + "'" +
            ", taxReports='" + getTaxReports() + "'" +
            ", yearEndReports='" + getYearEndReports() + "'" +
            "}";
    }
}
