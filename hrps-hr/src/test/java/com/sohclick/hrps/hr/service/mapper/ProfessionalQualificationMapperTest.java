package com.sohclick.hrps.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalQualificationMapperTest {

    private ProfessionalQualificationMapper professionalQualificationMapper;

    @BeforeEach
    public void setUp() {
        professionalQualificationMapper = new ProfessionalQualificationMapperImpl();
    }
}
