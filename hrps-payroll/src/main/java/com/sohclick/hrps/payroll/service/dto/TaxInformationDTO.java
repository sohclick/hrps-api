package com.sohclick.hrps.payroll.service.dto;

import com.sohclick.hrps.payroll.domain.enumeration.TaxFilingStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.sohclick.hrps.payroll.domain.TaxInformation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaxInformationDTO implements Serializable {

    private Long id;

    private String employeeTaxDeclaration;

    private TaxFilingStatus taxFilingStatus;

    private BigDecimal withholdingTaxInformation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeTaxDeclaration() {
        return employeeTaxDeclaration;
    }

    public void setEmployeeTaxDeclaration(String employeeTaxDeclaration) {
        this.employeeTaxDeclaration = employeeTaxDeclaration;
    }

    public TaxFilingStatus getTaxFilingStatus() {
        return taxFilingStatus;
    }

    public void setTaxFilingStatus(TaxFilingStatus taxFilingStatus) {
        this.taxFilingStatus = taxFilingStatus;
    }

    public BigDecimal getWithholdingTaxInformation() {
        return withholdingTaxInformation;
    }

    public void setWithholdingTaxInformation(BigDecimal withholdingTaxInformation) {
        this.withholdingTaxInformation = withholdingTaxInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxInformationDTO)) {
            return false;
        }

        TaxInformationDTO taxInformationDTO = (TaxInformationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taxInformationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxInformationDTO{" +
            "id=" + getId() +
            ", employeeTaxDeclaration='" + getEmployeeTaxDeclaration() + "'" +
            ", taxFilingStatus='" + getTaxFilingStatus() + "'" +
            ", withholdingTaxInformation=" + getWithholdingTaxInformation() +
            "}";
    }
}
