package com.sohclick.hrps.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EducationHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EducationHistory.class);
        EducationHistory educationHistory1 = new EducationHistory();
        educationHistory1.setId(1L);
        EducationHistory educationHistory2 = new EducationHistory();
        educationHistory2.setId(educationHistory1.getId());
        assertThat(educationHistory1).isEqualTo(educationHistory2);
        educationHistory2.setId(2L);
        assertThat(educationHistory1).isNotEqualTo(educationHistory2);
        educationHistory1.setId(null);
        assertThat(educationHistory1).isNotEqualTo(educationHistory2);
    }
}
