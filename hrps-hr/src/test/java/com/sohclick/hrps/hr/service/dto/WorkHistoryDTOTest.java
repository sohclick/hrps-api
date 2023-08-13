package com.sohclick.hrps.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkHistoryDTO.class);
        WorkHistoryDTO workHistoryDTO1 = new WorkHistoryDTO();
        workHistoryDTO1.setId(1L);
        WorkHistoryDTO workHistoryDTO2 = new WorkHistoryDTO();
        assertThat(workHistoryDTO1).isNotEqualTo(workHistoryDTO2);
        workHistoryDTO2.setId(workHistoryDTO1.getId());
        assertThat(workHistoryDTO1).isEqualTo(workHistoryDTO2);
        workHistoryDTO2.setId(2L);
        assertThat(workHistoryDTO1).isNotEqualTo(workHistoryDTO2);
        workHistoryDTO1.setId(null);
        assertThat(workHistoryDTO1).isNotEqualTo(workHistoryDTO2);
    }
}
