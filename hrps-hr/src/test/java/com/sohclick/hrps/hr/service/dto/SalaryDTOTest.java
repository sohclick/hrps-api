package com.sohclick.hrps.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryDTO.class);
        SalaryDTO salaryDTO1 = new SalaryDTO();
        salaryDTO1.setId(1L);
        SalaryDTO salaryDTO2 = new SalaryDTO();
        assertThat(salaryDTO1).isNotEqualTo(salaryDTO2);
        salaryDTO2.setId(salaryDTO1.getId());
        assertThat(salaryDTO1).isEqualTo(salaryDTO2);
        salaryDTO2.setId(2L);
        assertThat(salaryDTO1).isNotEqualTo(salaryDTO2);
        salaryDTO1.setId(null);
        assertThat(salaryDTO1).isNotEqualTo(salaryDTO2);
    }
}
