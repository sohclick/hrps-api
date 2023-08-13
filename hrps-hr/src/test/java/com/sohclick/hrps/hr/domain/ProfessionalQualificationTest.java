package com.sohclick.hrps.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalQualificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalQualification.class);
        ProfessionalQualification professionalQualification1 = new ProfessionalQualification();
        professionalQualification1.setId(1L);
        ProfessionalQualification professionalQualification2 = new ProfessionalQualification();
        professionalQualification2.setId(professionalQualification1.getId());
        assertThat(professionalQualification1).isEqualTo(professionalQualification2);
        professionalQualification2.setId(2L);
        assertThat(professionalQualification1).isNotEqualTo(professionalQualification2);
        professionalQualification1.setId(null);
        assertThat(professionalQualification1).isNotEqualTo(professionalQualification2);
    }
}
