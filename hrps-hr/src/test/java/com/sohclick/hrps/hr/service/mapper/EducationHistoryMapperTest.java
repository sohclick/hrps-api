package com.sohclick.hrps.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EducationHistoryMapperTest {

    private EducationHistoryMapper educationHistoryMapper;

    @BeforeEach
    public void setUp() {
        educationHistoryMapper = new EducationHistoryMapperImpl();
    }
}
