package com.sohclick.hrps.payroll.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.payroll.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimeAndAttendanceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeAndAttendanceDTO.class);
        TimeAndAttendanceDTO timeAndAttendanceDTO1 = new TimeAndAttendanceDTO();
        timeAndAttendanceDTO1.setId(1L);
        TimeAndAttendanceDTO timeAndAttendanceDTO2 = new TimeAndAttendanceDTO();
        assertThat(timeAndAttendanceDTO1).isNotEqualTo(timeAndAttendanceDTO2);
        timeAndAttendanceDTO2.setId(timeAndAttendanceDTO1.getId());
        assertThat(timeAndAttendanceDTO1).isEqualTo(timeAndAttendanceDTO2);
        timeAndAttendanceDTO2.setId(2L);
        assertThat(timeAndAttendanceDTO1).isNotEqualTo(timeAndAttendanceDTO2);
        timeAndAttendanceDTO1.setId(null);
        assertThat(timeAndAttendanceDTO1).isNotEqualTo(timeAndAttendanceDTO2);
    }
}
