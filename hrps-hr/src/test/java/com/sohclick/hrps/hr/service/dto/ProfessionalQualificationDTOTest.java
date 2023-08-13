package com.sohclick.hrps.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalQualificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalQualificationDTO.class);
        ProfessionalQualificationDTO professionalQualificationDTO1 = new ProfessionalQualificationDTO();
        professionalQualificationDTO1.setId(1L);
        ProfessionalQualificationDTO professionalQualificationDTO2 = new ProfessionalQualificationDTO();
        assertThat(professionalQualificationDTO1).isNotEqualTo(professionalQualificationDTO2);
        professionalQualificationDTO2.setId(professionalQualificationDTO1.getId());
        assertThat(professionalQualificationDTO1).isEqualTo(professionalQualificationDTO2);
        professionalQualificationDTO2.setId(2L);
        assertThat(professionalQualificationDTO1).isNotEqualTo(professionalQualificationDTO2);
        professionalQualificationDTO1.setId(null);
        assertThat(professionalQualificationDTO1).isNotEqualTo(professionalQualificationDTO2);
    }
}
