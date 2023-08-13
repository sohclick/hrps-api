package com.sohclick.hrps.payroll.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.payroll.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaryComponentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryComponentDTO.class);
        SalaryComponentDTO salaryComponentDTO1 = new SalaryComponentDTO();
        salaryComponentDTO1.setId(1L);
        SalaryComponentDTO salaryComponentDTO2 = new SalaryComponentDTO();
        assertThat(salaryComponentDTO1).isNotEqualTo(salaryComponentDTO2);
        salaryComponentDTO2.setId(salaryComponentDTO1.getId());
        assertThat(salaryComponentDTO1).isEqualTo(salaryComponentDTO2);
        salaryComponentDTO2.setId(2L);
        assertThat(salaryComponentDTO1).isNotEqualTo(salaryComponentDTO2);
        salaryComponentDTO1.setId(null);
        assertThat(salaryComponentDTO1).isNotEqualTo(salaryComponentDTO2);
    }
}
