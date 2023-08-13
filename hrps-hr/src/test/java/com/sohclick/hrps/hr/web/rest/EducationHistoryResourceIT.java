package com.sohclick.hrps.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.hr.IntegrationTest;
import com.sohclick.hrps.hr.domain.EducationHistory;
import com.sohclick.hrps.hr.repository.EducationHistoryRepository;
import com.sohclick.hrps.hr.service.dto.EducationHistoryDTO;
import com.sohclick.hrps.hr.service.mapper.EducationHistoryMapper;
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
 * Integration tests for the {@link EducationHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EducationHistoryResourceIT {

    private static final String DEFAULT_DEGREE = "AAAAAAAAAA";
    private static final String UPDATED_DEGREE = "BBBBBBBBBB";

    private static final String DEFAULT_SCHOOL = "AAAAAAAAAA";
    private static final String UPDATED_SCHOOL = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/education-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EducationHistoryRepository educationHistoryRepository;

    @Autowired
    private EducationHistoryMapper educationHistoryMapper;

    @Autowired
    private WebTestClient webTestClient;

    private EducationHistory educationHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EducationHistory createEntity() {
        EducationHistory educationHistory = new EducationHistory()
            .degree(DEFAULT_DEGREE)
            .school(DEFAULT_SCHOOL)
            .country(DEFAULT_COUNTRY)
            .state(DEFAULT_STATE)
            .city(DEFAULT_CITY)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return educationHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EducationHistory createUpdatedEntity() {
        EducationHistory educationHistory = new EducationHistory()
            .degree(UPDATED_DEGREE)
            .school(UPDATED_SCHOOL)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .city(UPDATED_CITY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return educationHistory;
    }

    @BeforeEach
    public void initTest() {
        educationHistoryRepository.deleteAll().block();
        educationHistory = createEntity();
    }

    @Test
    void createEducationHistory() throws Exception {
        int databaseSizeBeforeCreate = educationHistoryRepository.findAll().collectList().block().size();
        // Create the EducationHistory
        EducationHistoryDTO educationHistoryDTO = educationHistoryMapper.toDto(educationHistory);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(educationHistoryDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the EducationHistory in the database
        List<EducationHistory> educationHistoryList = educationHistoryRepository.findAll().collectList().block();
        assertThat(educationHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        EducationHistory testEducationHistory = educationHistoryList.get(educationHistoryList.size() - 1);
        assertThat(testEducationHistory.getDegree()).isEqualTo(DEFAULT_DEGREE);
        assertThat(testEducationHistory.getSchool()).isEqualTo(DEFAULT_SCHOOL);
        assertThat(testEducationHistory.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testEducationHistory.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testEducationHistory.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testEducationHistory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEducationHistory.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    void createEducationHistoryWithExistingId() throws Exception {
        // Create the EducationHistory with an existing ID
        educationHistory.setId(1L);
        EducationHistoryDTO educationHistoryDTO = educationHistoryMapper.toDto(educationHistory);

        int databaseSizeBeforeCreate = educationHistoryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(educationHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EducationHistory in the database
        List<EducationHistory> educationHistoryList = educationHistoryRepository.findAll().collectList().block();
        assertThat(educationHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEducationHistories() {
        // Initialize the database
        educationHistoryRepository.save(educationHistory).block();

        // Get all the educationHistoryList
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
            .value(hasItem(educationHistory.getId().intValue()))
            .jsonPath("$.[*].degree")
            .value(hasItem(DEFAULT_DEGREE))
            .jsonPath("$.[*].school")
            .value(hasItem(DEFAULT_SCHOOL))
            .jsonPath("$.[*].country")
            .value(hasItem(DEFAULT_COUNTRY))
            .jsonPath("$.[*].state")
            .value(hasItem(DEFAULT_STATE))
            .jsonPath("$.[*].city")
            .value(hasItem(DEFAULT_CITY))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()));
    }

    @Test
    void getEducationHistory() {
        // Initialize the database
        educationHistoryRepository.save(educationHistory).block();

        // Get the educationHistory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, educationHistory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(educationHistory.getId().intValue()))
            .jsonPath("$.degree")
            .value(is(DEFAULT_DEGREE))
            .jsonPath("$.school")
            .value(is(DEFAULT_SCHOOL))
            .jsonPath("$.country")
            .value(is(DEFAULT_COUNTRY))
            .jsonPath("$.state")
            .value(is(DEFAULT_STATE))
            .jsonPath("$.city")
            .value(is(DEFAULT_CITY))
            .jsonPath("$.startDate")
            .value(is(DEFAULT_START_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()));
    }

    @Test
    void getNonExistingEducationHistory() {
        // Get the educationHistory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEducationHistory() throws Exception {
        // Initialize the database
        educationHistoryRepository.save(educationHistory).block();

        int databaseSizeBeforeUpdate = educationHistoryRepository.findAll().collectList().block().size();

        // Update the educationHistory
        EducationHistory updatedEducationHistory = educationHistoryRepository.findById(educationHistory.getId()).block();
        updatedEducationHistory
            .degree(UPDATED_DEGREE)
            .school(UPDATED_SCHOOL)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .city(UPDATED_CITY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        EducationHistoryDTO educationHistoryDTO = educationHistoryMapper.toDto(updatedEducationHistory);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, educationHistoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(educationHistoryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EducationHistory in the database
        List<EducationHistory> educationHistoryList = educationHistoryRepository.findAll().collectList().block();
        assertThat(educationHistoryList).hasSize(databaseSizeBeforeUpdate);
        EducationHistory testEducationHistory = educationHistoryList.get(educationHistoryList.size() - 1);
        assertThat(testEducationHistory.getDegree()).isEqualTo(UPDATED_DEGREE);
        assertThat(testEducationHistory.getSchool()).isEqualTo(UPDATED_SCHOOL);
        assertThat(testEducationHistory.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testEducationHistory.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testEducationHistory.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testEducationHistory.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEducationHistory.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    void putNonExistingEducationHistory() throws Exception {
        int databaseSizeBeforeUpdate = educationHistoryRepository.findAll().collectList().block().size();
        educationHistory.setId(count.incrementAndGet());

        // Create the EducationHistory
        EducationHistoryDTO educationHistoryDTO = educationHistoryMapper.toDto(educationHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, educationHistoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(educationHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EducationHistory in the database
        List<EducationHistory> educationHistoryList = educationHistoryRepository.findAll().collectList().block();
        assertThat(educationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEducationHistory() throws Exception {
        int databaseSizeBeforeUpdate = educationHistoryRepository.findAll().collectList().block().size();
        educationHistory.setId(count.incrementAndGet());

        // Create the EducationHistory
        EducationHistoryDTO educationHistoryDTO = educationHistoryMapper.toDto(educationHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(educationHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EducationHistory in the database
        List<EducationHistory> educationHistoryList = educationHistoryRepository.findAll().collectList().block();
        assertThat(educationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEducationHistory() throws Exception {
        int databaseSizeBeforeUpdate = educationHistoryRepository.findAll().collectList().block().size();
        educationHistory.setId(count.incrementAndGet());

        // Create the EducationHistory
        EducationHistoryDTO educationHistoryDTO = educationHistoryMapper.toDto(educationHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(educationHistoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EducationHistory in the database
        List<EducationHistory> educationHistoryList = educationHistoryRepository.findAll().collectList().block();
        assertThat(educationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEducationHistoryWithPatch() throws Exception {
        // Initialize the database
        educationHistoryRepository.save(educationHistory).block();

        int databaseSizeBeforeUpdate = educationHistoryRepository.findAll().collectList().block().size();

        // Update the educationHistory using partial update
        EducationHistory partialUpdatedEducationHistory = new EducationHistory();
        partialUpdatedEducationHistory.setId(educationHistory.getId());

        partialUpdatedEducationHistory.school(UPDATED_SCHOOL).state(UPDATED_STATE).endDate(UPDATED_END_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEducationHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEducationHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EducationHistory in the database
        List<EducationHistory> educationHistoryList = educationHistoryRepository.findAll().collectList().block();
        assertThat(educationHistoryList).hasSize(databaseSizeBeforeUpdate);
        EducationHistory testEducationHistory = educationHistoryList.get(educationHistoryList.size() - 1);
        assertThat(testEducationHistory.getDegree()).isEqualTo(DEFAULT_DEGREE);
        assertThat(testEducationHistory.getSchool()).isEqualTo(UPDATED_SCHOOL);
        assertThat(testEducationHistory.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testEducationHistory.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testEducationHistory.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testEducationHistory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEducationHistory.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    void fullUpdateEducationHistoryWithPatch() throws Exception {
        // Initialize the database
        educationHistoryRepository.save(educationHistory).block();

        int databaseSizeBeforeUpdate = educationHistoryRepository.findAll().collectList().block().size();

        // Update the educationHistory using partial update
        EducationHistory partialUpdatedEducationHistory = new EducationHistory();
        partialUpdatedEducationHistory.setId(educationHistory.getId());

        partialUpdatedEducationHistory
            .degree(UPDATED_DEGREE)
            .school(UPDATED_SCHOOL)
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .city(UPDATED_CITY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEducationHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEducationHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EducationHistory in the database
        List<EducationHistory> educationHistoryList = educationHistoryRepository.findAll().collectList().block();
        assertThat(educationHistoryList).hasSize(databaseSizeBeforeUpdate);
        EducationHistory testEducationHistory = educationHistoryList.get(educationHistoryList.size() - 1);
        assertThat(testEducationHistory.getDegree()).isEqualTo(UPDATED_DEGREE);
        assertThat(testEducationHistory.getSchool()).isEqualTo(UPDATED_SCHOOL);
        assertThat(testEducationHistory.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testEducationHistory.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testEducationHistory.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testEducationHistory.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEducationHistory.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    void patchNonExistingEducationHistory() throws Exception {
        int databaseSizeBeforeUpdate = educationHistoryRepository.findAll().collectList().block().size();
        educationHistory.setId(count.incrementAndGet());

        // Create the EducationHistory
        EducationHistoryDTO educationHistoryDTO = educationHistoryMapper.toDto(educationHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, educationHistoryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(educationHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EducationHistory in the database
        List<EducationHistory> educationHistoryList = educationHistoryRepository.findAll().collectList().block();
        assertThat(educationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEducationHistory() throws Exception {
        int databaseSizeBeforeUpdate = educationHistoryRepository.findAll().collectList().block().size();
        educationHistory.setId(count.incrementAndGet());

        // Create the EducationHistory
        EducationHistoryDTO educationHistoryDTO = educationHistoryMapper.toDto(educationHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(educationHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EducationHistory in the database
        List<EducationHistory> educationHistoryList = educationHistoryRepository.findAll().collectList().block();
        assertThat(educationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEducationHistory() throws Exception {
        int databaseSizeBeforeUpdate = educationHistoryRepository.findAll().collectList().block().size();
        educationHistory.setId(count.incrementAndGet());

        // Create the EducationHistory
        EducationHistoryDTO educationHistoryDTO = educationHistoryMapper.toDto(educationHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(educationHistoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EducationHistory in the database
        List<EducationHistory> educationHistoryList = educationHistoryRepository.findAll().collectList().block();
        assertThat(educationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEducationHistory() {
        // Initialize the database
        educationHistoryRepository.save(educationHistory).block();

        int databaseSizeBeforeDelete = educationHistoryRepository.findAll().collectList().block().size();

        // Delete the educationHistory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, educationHistory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<EducationHistory> educationHistoryList = educationHistoryRepository.findAll().collectList().block();
        assertThat(educationHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
