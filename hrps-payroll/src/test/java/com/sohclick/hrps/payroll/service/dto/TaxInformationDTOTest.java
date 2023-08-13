package com.sohclick.hrps.payroll.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.payroll.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxInformationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxInformationDTO.class);
        TaxInformationDTO taxInformationDTO1 = new TaxInformationDTO();
        taxInformationDTO1.setId(1L);
        TaxInformationDTO taxInformationDTO2 = new TaxInformationDTO();
        assertThat(taxInformationDTO1).isNotEqualTo(taxInformationDTO2);
        taxInformationDTO2.setId(taxInformationDTO1.getId());
        assertThat(taxInformationDTO1).isEqualTo(taxInformationDTO2);
        taxInformationDTO2.setId(2L);
        assertThat(taxInformationDTO1).isNotEqualTo(taxInformationDTO2);
        taxInformationDTO1.setId(null);
        assertThat(taxInformationDTO1).isNotEqualTo(taxInformationDTO2);
    }
}
