package com.sohclick.hrps.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EducationHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EducationHistoryDTO.class);
        EducationHistoryDTO educationHistoryDTO1 = new EducationHistoryDTO();
        educationHistoryDTO1.setId(1L);
        EducationHistoryDTO educationHistoryDTO2 = new EducationHistoryDTO();
        assertThat(educationHistoryDTO1).isNotEqualTo(educationHistoryDTO2);
        educationHistoryDTO2.setId(educationHistoryDTO1.getId());
        assertThat(educationHistoryDTO1).isEqualTo(educationHistoryDTO2);
        educationHistoryDTO2.setId(2L);
        assertThat(educationHistoryDTO1).isNotEqualTo(educationHistoryDTO2);
        educationHistoryDTO1.setId(null);
        assertThat(educationHistoryDTO1).isNotEqualTo(educationHistoryDTO2);
    }
}
