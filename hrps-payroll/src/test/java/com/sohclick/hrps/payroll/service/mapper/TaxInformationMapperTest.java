package com.sohclick.hrps.payroll.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaxInformationMapperTest {

    private TaxInformationMapper taxInformationMapper;

    @BeforeEach
    public void setUp() {
        taxInformationMapper = new TaxInformationMapperImpl();
    }
}
