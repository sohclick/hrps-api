package com.sohclick.hrps.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BenefitDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BenefitDTO.class);
        BenefitDTO benefitDTO1 = new BenefitDTO();
        benefitDTO1.setId(1L);
        BenefitDTO benefitDTO2 = new BenefitDTO();
        assertThat(benefitDTO1).isNotEqualTo(benefitDTO2);
        benefitDTO2.setId(benefitDTO1.getId());
        assertThat(benefitDTO1).isEqualTo(benefitDTO2);
        benefitDTO2.setId(2L);
        assertThat(benefitDTO1).isNotEqualTo(benefitDTO2);
        benefitDTO1.setId(null);
        assertThat(benefitDTO1).isNotEqualTo(benefitDTO2);
    }
}
