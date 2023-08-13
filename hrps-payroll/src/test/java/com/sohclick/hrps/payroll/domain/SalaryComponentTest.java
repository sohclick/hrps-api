package com.sohclick.hrps.payroll.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.payroll.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaryComponentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryComponent.class);
        SalaryComponent salaryComponent1 = new SalaryComponent();
        salaryComponent1.setId(1L);
        SalaryComponent salaryComponent2 = new SalaryComponent();
        salaryComponent2.setId(salaryComponent1.getId());
        assertThat(salaryComponent1).isEqualTo(salaryComponent2);
        salaryComponent2.setId(2L);
        assertThat(salaryComponent1).isNotEqualTo(salaryComponent2);
        salaryComponent1.setId(null);
        assertThat(salaryComponent1).isNotEqualTo(salaryComponent2);
    }
}
