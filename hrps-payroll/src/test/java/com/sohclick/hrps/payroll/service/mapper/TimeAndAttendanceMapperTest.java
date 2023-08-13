package com.sohclick.hrps.payroll.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeAndAttendanceMapperTest {

    private TimeAndAttendanceMapper timeAndAttendanceMapper;

    @BeforeEach
    public void setUp() {
        timeAndAttendanceMapper = new TimeAndAttendanceMapperImpl();
    }
}
