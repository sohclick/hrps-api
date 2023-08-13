package com.sohclick.hrps.payroll.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PayrollMapperTest {

    private PayrollMapper payrollMapper;

    @BeforeEach
    public void setUp() {
        payrollMapper = new PayrollMapperImpl();
    }
}
