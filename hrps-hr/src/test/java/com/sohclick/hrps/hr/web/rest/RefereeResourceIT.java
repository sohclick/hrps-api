package com.sohclick.hrps.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.hr.IntegrationTest;
import com.sohclick.hrps.hr.domain.Referee;
import com.sohclick.hrps.hr.repository.RefereeRepository;
import com.sohclick.hrps.hr.service.dto.RefereeDTO;
import com.sohclick.hrps.hr.service.mapper.RefereeMapper;
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
 * Integration tests for the {@link RefereeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RefereeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/referees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RefereeRepository refereeRepository;

    @Autowired
    private RefereeMapper refereeMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Referee referee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referee createEntity() {
        Referee referee = new Referee()
            .name(DEFAULT_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .jobTitle(DEFAULT_JOB_TITLE);
        return referee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referee createUpdatedEntity() {
        Referee referee = new Referee()
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .jobTitle(UPDATED_JOB_TITLE);
        return referee;
    }

    @BeforeEach
    public void initTest() {
        refereeRepository.deleteAll().block();
        referee = createEntity();
    }

    @Test
    void createReferee() throws Exception {
        int databaseSizeBeforeCreate = refereeRepository.findAll().collectList().block().size();
        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(refereeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeCreate + 1);
        Referee testReferee = refereeList.get(refereeList.size() - 1);
        assertThat(testReferee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReferee.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testReferee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testReferee.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
    }

    @Test
    void createRefereeWithExistingId() throws Exception {
        // Create the Referee with an existing ID
        referee.setId(1L);
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        int databaseSizeBeforeCreate = refereeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(refereeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = refereeRepository.findAll().collectList().block().size();
        // set the field null
        referee.setName(null);

        // Create the Referee, which fails.
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(refereeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllReferees() {
        // Initialize the database
        refereeRepository.save(referee).block();

        // Get all the refereeList
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
            .value(hasItem(referee.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].phoneNumber")
            .value(hasItem(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].jobTitle")
            .value(hasItem(DEFAULT_JOB_TITLE));
    }

    @Test
    void getReferee() {
        // Initialize the database
        refereeRepository.save(referee).block();

        // Get the referee
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, referee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(referee.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.phoneNumber")
            .value(is(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.jobTitle")
            .value(is(DEFAULT_JOB_TITLE));
    }

    @Test
    void getNonExistingReferee() {
        // Get the referee
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReferee() throws Exception {
        // Initialize the database
        refereeRepository.save(referee).block();

        int databaseSizeBeforeUpdate = refereeRepository.findAll().collectList().block().size();

        // Update the referee
        Referee updatedReferee = refereeRepository.findById(referee.getId()).block();
        updatedReferee.name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER).email(UPDATED_EMAIL).jobTitle(UPDATED_JOB_TITLE);
        RefereeDTO refereeDTO = refereeMapper.toDto(updatedReferee);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, refereeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(refereeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
        Referee testReferee = refereeList.get(refereeList.size() - 1);
        assertThat(testReferee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReferee.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testReferee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testReferee.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
    }

    @Test
    void putNonExistingReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().collectList().block().size();
        referee.setId(count.incrementAndGet());

        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, refereeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(refereeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().collectList().block().size();
        referee.setId(count.incrementAndGet());

        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(refereeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().collectList().block().size();
        referee.setId(count.incrementAndGet());

        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(refereeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRefereeWithPatch() throws Exception {
        // Initialize the database
        refereeRepository.save(referee).block();

        int databaseSizeBeforeUpdate = refereeRepository.findAll().collectList().block().size();

        // Update the referee using partial update
        Referee partialUpdatedReferee = new Referee();
        partialUpdatedReferee.setId(referee.getId());

        partialUpdatedReferee.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReferee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReferee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
        Referee testReferee = refereeList.get(refereeList.size() - 1);
        assertThat(testReferee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReferee.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testReferee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testReferee.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
    }

    @Test
    void fullUpdateRefereeWithPatch() throws Exception {
        // Initialize the database
        refereeRepository.save(referee).block();

        int databaseSizeBeforeUpdate = refereeRepository.findAll().collectList().block().size();

        // Update the referee using partial update
        Referee partialUpdatedReferee = new Referee();
        partialUpdatedReferee.setId(referee.getId());

        partialUpdatedReferee.name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER).email(UPDATED_EMAIL).jobTitle(UPDATED_JOB_TITLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReferee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReferee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
        Referee testReferee = refereeList.get(refereeList.size() - 1);
        assertThat(testReferee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReferee.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testReferee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testReferee.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
    }

    @Test
    void patchNonExistingReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().collectList().block().size();
        referee.setId(count.incrementAndGet());

        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, refereeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(refereeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().collectList().block().size();
        referee.setId(count.incrementAndGet());

        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(refereeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().collectList().block().size();
        referee.setId(count.incrementAndGet());

        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(refereeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReferee() {
        // Initialize the database
        refereeRepository.save(referee).block();

        int databaseSizeBeforeDelete = refereeRepository.findAll().collectList().block().size();

        // Delete the referee
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, referee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Referee> refereeList = refereeRepository.findAll().collectList().block();
        assertThat(refereeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
