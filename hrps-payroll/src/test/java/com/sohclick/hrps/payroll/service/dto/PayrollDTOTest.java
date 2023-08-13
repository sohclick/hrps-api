package com.sohclick.hrps.payroll.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.payroll.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PayrollDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayrollDTO.class);
        PayrollDTO payrollDTO1 = new PayrollDTO();
        payrollDTO1.setId(1L);
        PayrollDTO payrollDTO2 = new PayrollDTO();
        assertThat(payrollDTO1).isNotEqualTo(payrollDTO2);
        payrollDTO2.setId(payrollDTO1.getId());
        assertThat(payrollDTO1).isEqualTo(payrollDTO2);
        payrollDTO2.setId(2L);
        assertThat(payrollDTO1).isNotEqualTo(payrollDTO2);
        payrollDTO1.setId(null);
        assertThat(payrollDTO1).isNotEqualTo(payrollDTO2);
    }
}
