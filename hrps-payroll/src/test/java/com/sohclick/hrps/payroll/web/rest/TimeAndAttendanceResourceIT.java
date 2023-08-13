package com.sohclick.hrps.payroll.web.rest;

import static com.sohclick.hrps.payroll.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.payroll.IntegrationTest;
import com.sohclick.hrps.payroll.domain.TimeAndAttendance;
import com.sohclick.hrps.payroll.repository.TimeAndAttendanceRepository;
import com.sohclick.hrps.payroll.service.dto.TimeAndAttendanceDTO;
import com.sohclick.hrps.payroll.service.mapper.TimeAndAttendanceMapper;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link TimeAndAttendanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TimeAndAttendanceResourceIT {

    private static final BigDecimal DEFAULT_EMPLOYEE_WORKING_HOURS = new BigDecimal(1);
    private static final BigDecimal UPDATED_EMPLOYEE_WORKING_HOURS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_OVERTIME_HOURS = new BigDecimal(1);
    private static final BigDecimal UPDATED_OVERTIME_HOURS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LEAVES_TAKEN = new BigDecimal(1);
    private static final BigDecimal UPDATED_LEAVES_TAKEN = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/time-and-attendances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TimeAndAttendanceRepository timeAndAttendanceRepository;

    @Autowired
    private TimeAndAttendanceMapper timeAndAttendanceMapper;

    @Autowired
    private WebTestClient webTestClient;

    private TimeAndAttendance timeAndAttendance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeAndAttendance createEntity() {
        TimeAndAttendance timeAndAttendance = new TimeAndAttendance()
            .employeeWorkingHours(DEFAULT_EMPLOYEE_WORKING_HOURS)
            .overtimeHours(DEFAULT_OVERTIME_HOURS)
            .leavesTaken(DEFAULT_LEAVES_TAKEN);
        return timeAndAttendance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeAndAttendance createUpdatedEntity() {
        TimeAndAttendance timeAndAttendance = new TimeAndAttendance()
            .employeeWorkingHours(UPDATED_EMPLOYEE_WORKING_HOURS)
            .overtimeHours(UPDATED_OVERTIME_HOURS)
            .leavesTaken(UPDATED_LEAVES_TAKEN);
        return timeAndAttendance;
    }

    @BeforeEach
    public void initTest() {
        timeAndAttendanceRepository.deleteAll().block();
        timeAndAttendance = createEntity();
    }

    @Test
    void createTimeAndAttendance() throws Exception {
        int databaseSizeBeforeCreate = timeAndAttendanceRepository.findAll().collectList().block().size();
        // Create the TimeAndAttendance
        TimeAndAttendanceDTO timeAndAttendanceDTO = timeAndAttendanceMapper.toDto(timeAndAttendance);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeAndAttendanceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TimeAndAttendance in the database
        List<TimeAndAttendance> timeAndAttendanceList = timeAndAttendanceRepository.findAll().collectList().block();
        assertThat(timeAndAttendanceList).hasSize(databaseSizeBeforeCreate + 1);
        TimeAndAttendance testTimeAndAttendance = timeAndAttendanceList.get(timeAndAttendanceList.size() - 1);
        assertThat(testTimeAndAttendance.getEmployeeWorkingHours()).isEqualByComparingTo(DEFAULT_EMPLOYEE_WORKING_HOURS);
        assertThat(testTimeAndAttendance.getOvertimeHours()).isEqualByComparingTo(DEFAULT_OVERTIME_HOURS);
        assertThat(testTimeAndAttendance.getLeavesTaken()).isEqualByComparingTo(DEFAULT_LEAVES_TAKEN);
    }

    @Test
    void createTimeAndAttendanceWithExistingId() throws Exception {
        // Create the TimeAndAttendance with an existing ID
        timeAndAttendance.setId(1L);
        TimeAndAttendanceDTO timeAndAttendanceDTO = timeAndAttendanceMapper.toDto(timeAndAttendance);

        int databaseSizeBeforeCreate = timeAndAttendanceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeAndAttendanceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TimeAndAttendance in the database
        List<TimeAndAttendance> timeAndAttendanceList = timeAndAttendanceRepository.findAll().collectList().block();
        assertThat(timeAndAttendanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTimeAndAttendances() {
        // Initialize the database
        timeAndAttendanceRepository.save(timeAndAttendance).block();

        // Get all the timeAndAttendanceList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(timeAndAttendance.getId().intValue()))
            .jsonPath("$.[*].employeeWorkingHours")
            .value(hasItem(sameNumber(DEFAULT_EMPLOYEE_WORKING_HOURS)))
            .jsonPath("$.[*].overtimeHours")
            .value(hasItem(sameNumber(DEFAULT_OVERTIME_HOURS)))
            .jsonPath("$.[*].leavesTaken")
            .value(hasItem(sameNumber(DEFAULT_LEAVES_TAKEN)));
    }

    @Test
    void getTimeAndAttendance() {
        // Initialize the database
        timeAndAttendanceRepository.save(timeAndAttendance).block();

        // Get the timeAndAttendance
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, timeAndAttendance.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(timeAndAttendance.getId().intValue()))
            .jsonPath("$.employeeWorkingHours")
            .value(is(sameNumber(DEFAULT_EMPLOYEE_WORKING_HOURS)))
            .jsonPath("$.overtimeHours")
            .value(is(sameNumber(DEFAULT_OVERTIME_HOURS)))
            .jsonPath("$.leavesTaken")
            .value(is(sameNumber(DEFAULT_LEAVES_TAKEN)));
    }

    @Test
    void getNonExistingTimeAndAttendance() {
        // Get the timeAndAttendance
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTimeAndAttendance() throws Exception {
        // Initialize the database
        timeAndAttendanceRepository.save(timeAndAttendance).block();

        int databaseSizeBeforeUpdate = timeAndAttendanceRepository.findAll().collectList().block().size();

        // Update the timeAndAttendance
        TimeAndAttendance updatedTimeAndAttendance = timeAndAttendanceRepository.findById(timeAndAttendance.getId()).block();
        updatedTimeAndAttendance
            .employeeWorkingHours(UPDATED_EMPLOYEE_WORKING_HOURS)
            .overtimeHours(UPDATED_OVERTIME_HOURS)
            .leavesTaken(UPDATED_LEAVES_TAKEN);
        TimeAndAttendanceDTO timeAndAttendanceDTO = timeAndAttendanceMapper.toDto(updatedTimeAndAttendance);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, timeAndAttendanceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeAndAttendanceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TimeAndAttendance in the database
        List<TimeAndAttendance> timeAndAttendanceList = timeAndAttendanceRepository.findAll().collectList().block();
        assertThat(timeAndAttendanceList).hasSize(databaseSizeBeforeUpdate);
        TimeAndAttendance testTimeAndAttendance = timeAndAttendanceList.get(timeAndAttendanceList.size() - 1);
        assertThat(testTimeAndAttendance.getEmployeeWorkingHours()).isEqualByComparingTo(UPDATED_EMPLOYEE_WORKING_HOURS);
        assertThat(testTimeAndAttendance.getOvertimeHours()).isEqualByComparingTo(UPDATED_OVERTIME_HOURS);
        assertThat(testTimeAndAttendance.getLeavesTaken()).isEqualByComparingTo(UPDATED_LEAVES_TAKEN);
    }

    @Test
    void putNonExistingTimeAndAttendance() throws Exception {
        int databaseSizeBeforeUpdate = timeAndAttendanceRepository.findAll().collectList().block().size();
        timeAndAttendance.setId(count.incrementAndGet());

        // Create the TimeAndAttendance
        TimeAndAttendanceDTO timeAndAttendanceDTO = timeAndAttendanceMapper.toDto(timeAndAttendance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, timeAndAttendanceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeAndAttendanceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TimeAndAttendance in the database
        List<TimeAndAttendance> timeAndAttendanceList = timeAndAttendanceRepository.findAll().collectList().block();
        assertThat(timeAndAttendanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTimeAndAttendance() throws Exception {
        int databaseSizeBeforeUpdate = timeAndAttendanceRepository.findAll().collectList().block().size();
        timeAndAttendance.setId(count.incrementAndGet());

        // Create the TimeAndAttendance
        TimeAndAttendanceDTO timeAndAttendanceDTO = timeAndAttendanceMapper.toDto(timeAndAttendance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeAndAttendanceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TimeAndAttendance in the database
        List<TimeAndAttendance> timeAndAttendanceList = timeAndAttendanceRepository.findAll().collectList().block();
        assertThat(timeAndAttendanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTimeAndAttendance() throws Exception {
        int databaseSizeBeforeUpdate = timeAndAttendanceRepository.findAll().collectList().block().size();
        timeAndAttendance.setId(count.incrementAndGet());

        // Create the TimeAndAttendance
        TimeAndAttendanceDTO timeAndAttendanceDTO = timeAndAttendanceMapper.toDto(timeAndAttendance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeAndAttendanceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TimeAndAttendance in the database
        List<TimeAndAttendance> timeAndAttendanceList = timeAndAttendanceRepository.findAll().collectList().block();
        assertThat(timeAndAttendanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTimeAndAttendanceWithPatch() throws Exception {
        // Initialize the database
        timeAndAttendanceRepository.save(timeAndAttendance).block();

        int databaseSizeBeforeUpdate = timeAndAttendanceRepository.findAll().collectList().block().size();

        // Update the timeAndAttendance using partial update
        TimeAndAttendance partialUpdatedTimeAndAttendance = new TimeAndAttendance();
        partialUpdatedTimeAndAttendance.setId(timeAndAttendance.getId());

        partialUpdatedTimeAndAttendance.overtimeHours(UPDATED_OVERTIME_HOURS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTimeAndAttendance.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeAndAttendance))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TimeAndAttendance in the database
        List<TimeAndAttendance> timeAndAttendanceList = timeAndAttendanceRepository.findAll().collectList().block();
        assertThat(timeAndAttendanceList).hasSize(databaseSizeBeforeUpdate);
        TimeAndAttendance testTimeAndAttendance = timeAndAttendanceList.get(timeAndAttendanceList.size() - 1);
        assertThat(testTimeAndAttendance.getEmployeeWorkingHours()).isEqualByComparingTo(DEFAULT_EMPLOYEE_WORKING_HOURS);
        assertThat(testTimeAndAttendance.getOvertimeHours()).isEqualByComparingTo(UPDATED_OVERTIME_HOURS);
        assertThat(testTimeAndAttendance.getLeavesTaken()).isEqualByComparingTo(DEFAULT_LEAVES_TAKEN);
    }

    @Test
    void fullUpdateTimeAndAttendanceWithPatch() throws Exception {
        // Initialize the database
        timeAndAttendanceRepository.save(timeAndAttendance).block();

        int databaseSizeBeforeUpdate = timeAndAttendanceRepository.findAll().collectList().block().size();

        // Update the timeAndAttendance using partial update
        TimeAndAttendance partialUpdatedTimeAndAttendance = new TimeAndAttendance();
        partialUpdatedTimeAndAttendance.setId(timeAndAttendance.getId());

        partialUpdatedTimeAndAttendance
            .employeeWorkingHours(UPDATED_EMPLOYEE_WORKING_HOURS)
            .overtimeHours(UPDATED_OVERTIME_HOURS)
            .leavesTaken(UPDATED_LEAVES_TAKEN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTimeAndAttendance.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeAndAttendance))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TimeAndAttendance in the database
        List<TimeAndAttendance> timeAndAttendanceList = timeAndAttendanceRepository.findAll().collectList().block();
        assertThat(timeAndAttendanceList).hasSize(databaseSizeBeforeUpdate);
        TimeAndAttendance testTimeAndAttendance = timeAndAttendanceList.get(timeAndAttendanceList.size() - 1);
        assertThat(testTimeAndAttendance.getEmployeeWorkingHours()).isEqualByComparingTo(UPDATED_EMPLOYEE_WORKING_HOURS);
        assertThat(testTimeAndAttendance.getOvertimeHours()).isEqualByComparingTo(UPDATED_OVERTIME_HOURS);
        assertThat(testTimeAndAttendance.getLeavesTaken()).isEqualByComparingTo(UPDATED_LEAVES_TAKEN);
    }

    @Test
    void patchNonExistingTimeAndAttendance() throws Exception {
        int databaseSizeBeforeUpdate = timeAndAttendanceRepository.findAll().collectList().block().size();
        timeAndAttendance.setId(count.incrementAndGet());

        // Create the TimeAndAttendance
        TimeAndAttendanceDTO timeAndAttendanceDTO = timeAndAttendanceMapper.toDto(timeAndAttendance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, timeAndAttendanceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeAndAttendanceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TimeAndAttendance in the database
        List<TimeAndAttendance> timeAndAttendanceList = timeAndAttendanceRepository.findAll().collectList().block();
        assertThat(timeAndAttendanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTimeAndAttendance() throws Exception {
        int databaseSizeBeforeUpdate = timeAndAttendanceRepository.findAll().collectList().block().size();
        timeAndAttendance.setId(count.incrementAndGet());

        // Create the TimeAndAttendance
        TimeAndAttendanceDTO timeAndAttendanceDTO = timeAndAttendanceMapper.toDto(timeAndAttendance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeAndAttendanceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TimeAndAttendance in the database
        List<TimeAndAttendance> timeAndAttendanceList = timeAndAttendanceRepository.findAll().collectList().block();
        assertThat(timeAndAttendanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTimeAndAttendance() throws Exception {
        int databaseSizeBeforeUpdate = timeAndAttendanceRepository.findAll().collectList().block().size();
        timeAndAttendance.setId(count.incrementAndGet());

        // Create the TimeAndAttendance
        TimeAndAttendanceDTO timeAndAttendanceDTO = timeAndAttendanceMapper.toDto(timeAndAttendance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeAndAttendanceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TimeAndAttendance in the database
        List<TimeAndAttendance> timeAndAttendanceList = timeAndAttendanceRepository.findAll().collectList().block();
        assertThat(timeAndAttendanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTimeAndAttendance() {
        // Initialize the database
        timeAndAttendanceRepository.save(timeAndAttendance).block();

        int databaseSizeBeforeDelete = timeAndAttendanceRepository.findAll().collectList().block().size();

        // Delete the timeAndAttendance
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, timeAndAttendance.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TimeAndAttendance> timeAndAttendanceList = timeAndAttendanceRepository.findAll().collectList().block();
        assertThat(timeAndAttendanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
