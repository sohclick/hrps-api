package com.sohclick.hrps.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.hr.IntegrationTest;
import com.sohclick.hrps.hr.domain.WorkHistory;
import com.sohclick.hrps.hr.repository.WorkHistoryRepository;
import com.sohclick.hrps.hr.service.dto.WorkHistoryDTO;
import com.sohclick.hrps.hr.service.mapper.WorkHistoryMapper;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link WorkHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class WorkHistoryResourceIT {

    private static final String DEFAULT_EMPLOYER = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYER = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/work-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkHistoryRepository workHistoryRepository;

    @Autowired
    private WorkHistoryMapper workHistoryMapper;

    @Autowired
    private WebTestClient webTestClient;

    private WorkHistory workHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkHistory createEntity() {
        WorkHistory workHistory = new WorkHistory()
            .employer(DEFAULT_EMPLOYER)
            .country(DEFAULT_COUNTRY)
            .state(DEFAULT_STATE)
            .location(DEFAULT_LOCATION)
            .jobTitle(DEFAULT_JOB_TITLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return workHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkHistory createUpdatedEntity() {
        WorkHistory workHistory = new WorkHistory()
            .employer(UPDATED_EMPLOYER)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .location(UPDATED_LOCATION)
            .jobTitle(UPDATED_JOB_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return workHistory;
    }

    @BeforeEach
    public void initTest() {
        workHistoryRepository.deleteAll().block();
        workHistory = createEntity();
    }

    @Test
    void createWorkHistory() throws Exception {
        int databaseSizeBeforeCreate = workHistoryRepository.findAll().collectList().block().size();
        // Create the WorkHistory
        WorkHistoryDTO workHistoryDTO = workHistoryMapper.toDto(workHistory);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workHistoryDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll().collectList().block();
        assertThat(workHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        WorkHistory testWorkHistory = workHistoryList.get(workHistoryList.size() - 1);
        assertThat(testWorkHistory.getEmployer()).isEqualTo(DEFAULT_EMPLOYER);
        assertThat(testWorkHistory.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testWorkHistory.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testWorkHistory.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testWorkHistory.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testWorkHistory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testWorkHistory.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    void createWorkHistoryWithExistingId() throws Exception {
        // Create the WorkHistory with an existing ID
        workHistory.setId(1L);
        WorkHistoryDTO workHistoryDTO = workHistoryMapper.toDto(workHistory);

        int databaseSizeBeforeCreate = workHistoryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll().collectList().block();
        assertThat(workHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllWorkHistories() {
        // Initialize the database
        workHistoryRepository.save(workHistory).block();

        // Get all the workHistoryList
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
            .value(hasItem(workHistory.getId().intValue()))
            .jsonPath("$.[*].employer")
            .value(hasItem(DEFAULT_EMPLOYER))
            .jsonPath("$.[*].country")
            .value(hasItem(DEFAULT_COUNTRY))
            .jsonPath("$.[*].state")
            .value(hasItem(DEFAULT_STATE))
            .jsonPath("$.[*].location")
            .value(hasItem(DEFAULT_LOCATION))
            .jsonPath("$.[*].jobTitle")
            .value(hasItem(DEFAULT_JOB_TITLE))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()));
    }

    @Test
    void getWorkHistory() {
        // Initialize the database
        workHistoryRepository.save(workHistory).block();

        // Get the workHistory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, workHistory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(workHistory.getId().intValue()))
            .jsonPath("$.employer")
            .value(is(DEFAULT_EMPLOYER))
            .jsonPath("$.country")
            .value(is(DEFAULT_COUNTRY))
            .jsonPath("$.state")
            .value(is(DEFAULT_STATE))
            .jsonPath("$.location")
            .value(is(DEFAULT_LOCATION))
            .jsonPath("$.jobTitle")
            .value(is(DEFAULT_JOB_TITLE))
            .jsonPath("$.startDate")
            .value(is(DEFAULT_START_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()));
    }

    @Test
    void getNonExistingWorkHistory() {
        // Get the workHistory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingWorkHistory() throws Exception {
        // Initialize the database
        workHistoryRepository.save(workHistory).block();

        int databaseSizeBeforeUpdate = workHistoryRepository.findAll().collectList().block().size();

        // Update the workHistory
        WorkHistory updatedWorkHistory = workHistoryRepository.findById(workHistory.getId()).block();
        updatedWorkHistory
            .employer(UPDATED_EMPLOYER)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .location(UPDATED_LOCATION)
            .jobTitle(UPDATED_JOB_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        WorkHistoryDTO workHistoryDTO = workHistoryMapper.toDto(updatedWorkHistory);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, workHistoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workHistoryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll().collectList().block();
        assertThat(workHistoryList).hasSize(databaseSizeBeforeUpdate);
        WorkHistory testWorkHistory = workHistoryList.get(workHistoryList.size() - 1);
        assertThat(testWorkHistory.getEmployer()).isEqualTo(UPDATED_EMPLOYER);
        assertThat(testWorkHistory.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testWorkHistory.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testWorkHistory.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testWorkHistory.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testWorkHistory.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWorkHistory.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    void putNonExistingWorkHistory() throws Exception {
        int databaseSizeBeforeUpdate = workHistoryRepository.findAll().collectList().block().size();
        workHistory.setId(count.incrementAndGet());

        // Create the WorkHistory
        WorkHistoryDTO workHistoryDTO = workHistoryMapper.toDto(workHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, workHistoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll().collectList().block();
        assertThat(workHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWorkHistory() throws Exception {
        int databaseSizeBeforeUpdate = workHistoryRepository.findAll().collectList().block().size();
        workHistory.setId(count.incrementAndGet());

        // Create the WorkHistory
        WorkHistoryDTO workHistoryDTO = workHistoryMapper.toDto(workHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll().collectList().block();
        assertThat(workHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWorkHistory() throws Exception {
        int databaseSizeBeforeUpdate = workHistoryRepository.findAll().collectList().block().size();
        workHistory.setId(count.incrementAndGet());

        // Create the WorkHistory
        WorkHistoryDTO workHistoryDTO = workHistoryMapper.toDto(workHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(workHistoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll().collectList().block();
        assertThat(workHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWorkHistoryWithPatch() throws Exception {
        // Initialize the database
        workHistoryRepository.save(workHistory).block();

        int databaseSizeBeforeUpdate = workHistoryRepository.findAll().collectList().block().size();

        // Update the workHistory using partial update
        WorkHistory partialUpdatedWorkHistory = new WorkHistory();
        partialUpdatedWorkHistory.setId(workHistory.getId());

        partialUpdatedWorkHistory.location(UPDATED_LOCATION).endDate(UPDATED_END_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWorkHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll().collectList().block();
        assertThat(workHistoryList).hasSize(databaseSizeBeforeUpdate);
        WorkHistory testWorkHistory = workHistoryList.get(workHistoryList.size() - 1);
        assertThat(testWorkHistory.getEmployer()).isEqualTo(DEFAULT_EMPLOYER);
        assertThat(testWorkHistory.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testWorkHistory.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testWorkHistory.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testWorkHistory.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testWorkHistory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testWorkHistory.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    void fullUpdateWorkHistoryWithPatch() throws Exception {
        // Initialize the database
        workHistoryRepository.save(workHistory).block();

        int databaseSizeBeforeUpdate = workHistoryRepository.findAll().collectList().block().size();

        // Update the workHistory using partial update
        WorkHistory partialUpdatedWorkHistory = new WorkHistory();
        partialUpdatedWorkHistory.setId(workHistory.getId());

        partialUpdatedWorkHistory
            .employer(UPDATED_EMPLOYER)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .location(UPDATED_LOCATION)
            .jobTitle(UPDATED_JOB_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWorkHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll().collectList().block();
        assertThat(workHistoryList).hasSize(databaseSizeBeforeUpdate);
        WorkHistory testWorkHistory = workHistoryList.get(workHistoryList.size() - 1);
        assertThat(testWorkHistory.getEmployer()).isEqualTo(UPDATED_EMPLOYER);
        assertThat(testWorkHistory.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testWorkHistory.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testWorkHistory.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testWorkHistory.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testWorkHistory.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWorkHistory.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    void patchNonExistingWorkHistory() throws Exception {
        int databaseSizeBeforeUpdate = workHistoryRepository.findAll().collectList().block().size();
        workHistory.setId(count.incrementAndGet());

        // Create the WorkHistory
        WorkHistoryDTO workHistoryDTO = workHistoryMapper.toDto(workHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, workHistoryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(workHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll().collectList().block();
        assertThat(workHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWorkHistory() throws Exception {
        int databaseSizeBeforeUpdate = workHistoryRepository.findAll().collectList().block().size();
        workHistory.setId(count.incrementAndGet());

        // Create the WorkHistory
        WorkHistoryDTO workHistoryDTO = workHistoryMapper.toDto(workHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(workHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll().collectList().block();
        assertThat(workHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWorkHistory() throws Exception {
        int databaseSizeBeforeUpdate = workHistoryRepository.findAll().collectList().block().size();
        workHistory.setId(count.incrementAndGet());

        // Create the WorkHistory
        WorkHistoryDTO workHistoryDTO = workHistoryMapper.toDto(workHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(workHistoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll().collectList().block();
        assertThat(workHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWorkHistory() {
        // Initialize the database
        workHistoryRepository.save(workHistory).block();

        int databaseSizeBeforeDelete = workHistoryRepository.findAll().collectList().block().size();

        // Delete the workHistory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, workHistory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<WorkHistory> workHistoryList = workHistoryRepository.findAll().collectList().block();
        assertThat(workHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
