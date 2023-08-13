package com.sohclick.hrps.payroll.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sohclick.hrps.payroll.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimeAndAttendanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeAndAttendance.class);
        TimeAndAttendance timeAndAttendance1 = new TimeAndAttendance();
        timeAndAttendance1.setId(1L);
        TimeAndAttendance timeAndAttendance2 = new TimeAndAttendance();
        timeAndAttendance2.setId(timeAndAttendance1.getId());
        assertThat(timeAndAttendance1).isEqualTo(timeAndAttendance2);
        timeAndAttendance2.setId(2L);
        assertThat(timeAndAttendance1).isNotEqualTo(timeAndAttendance2);
        timeAndAttendance1.setId(null);
        assertThat(timeAndAttendance1).isNotEqualTo(timeAndAttendance2);
    }
}
