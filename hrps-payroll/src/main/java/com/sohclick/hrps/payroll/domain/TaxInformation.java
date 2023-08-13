package com.sohclick.hrps.payroll.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohclick.hrps.payroll.domain.enumeration.TaxFilingStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A TaxInformation.
 */
@Document(collection = "tax_information")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaxInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field("employee_tax_declaration")
    private String employeeTaxDeclaration;

    @Field("tax_filing_status")
    private TaxFilingStatus taxFilingStatus;

    @Field("withholding_tax_information")
    private BigDecimal withholdingTaxInformation;

    private Payroll payroll;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TaxInformation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeTaxDeclaration() {
        return this.employeeTaxDeclaration;
    }

    public TaxInformation employeeTaxDeclaration(String employeeTaxDeclaration) {
        this.setEmployeeTaxDeclaration(employeeTaxDeclaration);
        return this;
    }

    public void setEmployeeTaxDeclaration(String employeeTaxDeclaration) {
        this.employeeTaxDeclaration = employeeTaxDeclaration;
    }

    public TaxFilingStatus getTaxFilingStatus() {
        return this.taxFilingStatus;
    }

    public TaxInformation taxFilingStatus(TaxFilingStatus taxFilingStatus) {
        this.setTaxFilingStatus(taxFilingStatus);
        return this;
    }

    public void setTaxFilingStatus(TaxFilingStatus taxFilingStatus) {
        this.taxFilingStatus = taxFilingStatus;
    }

    public BigDecimal getWithholdingTaxInformation() {
        return this.withholdingTaxInformation;
    }

    public TaxInformation withholdingTaxInformation(BigDecimal withholdingTaxInformation) {
        this.setWithholdingTaxInformation(withholdingTaxInformation);
        return this;
    }

    public void setWithholdingTaxInformation(BigDecimal withholdingTaxInformation) {
        this.withholdingTaxInformation = withholdingTaxInformation;
    }

    public Payroll getPayroll() {
        return this.payroll;
    }

    public void setPayroll(Payroll payroll) {
        if (this.payroll != null) {
            this.payroll.setTaxInformation(null);
        }
        if (payroll != null) {
            payroll.setTaxInformation(this);
        }
        this.payroll = payroll;
    }

    public TaxInformation payroll(Payroll payroll) {
        this.setPayroll(payroll);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxInformation)) {
            return false;
        }
        return id != null && id.equals(((TaxInformation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxInformation{" +
            "id=" + getId() +
            ", employeeTaxDeclaration='" + getEmployeeTaxDeclaration() + "'" +
            ", taxFilingStatus='" + getTaxFilingStatus() + "'" +
            ", withholdingTaxInformation=" + getWithholdingTaxInformation() +
            "}";
    }
}
