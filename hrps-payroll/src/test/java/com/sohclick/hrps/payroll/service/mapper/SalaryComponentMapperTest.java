package com.sohclick.hrps.payroll.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalaryComponentMapperTest {

    private SalaryComponentMapper salaryComponentMapper;

    @BeforeEach
    public void setUp() {
        salaryComponentMapper = new SalaryComponentMapperImpl();
    }
}
