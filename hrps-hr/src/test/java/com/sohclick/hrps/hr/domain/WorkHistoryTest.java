package com.sohclick.hrps.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.hr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkHistory.class);
        WorkHistory workHistory1 = new WorkHistory();
        workHistory1.setId(1L);
        WorkHistory workHistory2 = new WorkHistory();
        workHistory2.setId(workHistory1.getId());
        assertThat(workHistory1).isEqualTo(workHistory2);
        workHistory2.setId(2L);
        assertThat(workHistory1).isNotEqualTo(workHistory2);
        workHistory1.setId(null);
        assertThat(workHistory1).isNotEqualTo(workHistory2);
    }
}
