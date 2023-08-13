package com.sohclick.hrps.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmergencyContactMapperTest {

    private EmergencyContactMapper emergencyContactMapper;

    @BeforeEach
    public void setUp() {
        emergencyContactMapper = new EmergencyContactMapperImpl();
    }
}
