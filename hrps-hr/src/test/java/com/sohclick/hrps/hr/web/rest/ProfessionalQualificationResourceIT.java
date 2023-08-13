package com.sohclick.hrps.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.hr.IntegrationTest;
import com.sohclick.hrps.hr.domain.ProfessionalQualification;
import com.sohclick.hrps.hr.repository.ProfessionalQualificationRepository;
import com.sohclick.hrps.hr.service.dto.ProfessionalQualificationDTO;
import com.sohclick.hrps.hr.service.mapper.ProfessionalQualificationMapper;
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
 * Integration tests for the {@link ProfessionalQualificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProfessionalQualificationResourceIT {

    private static final String DEFAULT_QUALIFICATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_QUALIFICATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_QUALIFICATION_INSTITUTION = "AAAAAAAAAA";
    private static final String UPDATED_QUALIFICATION_INSTITUTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_QUALIFICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_QUALIFICATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/professional-qualifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfessionalQualificationRepository professionalQualificationRepository;

    @Autowired
    private ProfessionalQualificationMapper professionalQualificationMapper;

    @Autowired
    private WebTestClient webTestClient;

    private ProfessionalQualification professionalQualification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalQualification createEntity() {
        ProfessionalQualification professionalQualification = new ProfessionalQualification()
            .qualificationName(DEFAULT_QUALIFICATION_NAME)
            .qualificationInstitution(DEFAULT_QUALIFICATION_INSTITUTION)
            .qualificationDate(DEFAULT_QUALIFICATION_DATE);
        return professionalQualification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalQualification createUpdatedEntity() {
        ProfessionalQualification professionalQualification = new ProfessionalQualification()
            .qualificationName(UPDATED_QUALIFICATION_NAME)
            .qualificationInstitution(UPDATED_QUALIFICATION_INSTITUTION)
            .qualificationDate(UPDATED_QUALIFICATION_DATE);
        return professionalQualification;
    }

    @BeforeEach
    public void initTest() {
        professionalQualificationRepository.deleteAll().block();
        professionalQualification = createEntity();
    }

    @Test
    void createProfessionalQualification() throws Exception {
        int databaseSizeBeforeCreate = professionalQualificationRepository.findAll().collectList().block().size();
        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll().collectList().block();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeCreate + 1);
        ProfessionalQualification testProfessionalQualification = professionalQualificationList.get(
            professionalQualificationList.size() - 1
        );
        assertThat(testProfessionalQualification.getQualificationName()).isEqualTo(DEFAULT_QUALIFICATION_NAME);
        assertThat(testProfessionalQualification.getQualificationInstitution()).isEqualTo(DEFAULT_QUALIFICATION_INSTITUTION);
        assertThat(testProfessionalQualification.getQualificationDate()).isEqualTo(DEFAULT_QUALIFICATION_DATE);
    }

    @Test
    void createProfessionalQualificationWithExistingId() throws Exception {
        // Create the ProfessionalQualification with an existing ID
        professionalQualification.setId(1L);
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        int databaseSizeBeforeCreate = professionalQualificationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll().collectList().block();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllProfessionalQualifications() {
        // Initialize the database
        professionalQualificationRepository.save(professionalQualification).block();

        // Get all the professionalQualificationList
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
            .value(hasItem(professionalQualification.getId().intValue()))
            .jsonPath("$.[*].qualificationName")
            .value(hasItem(DEFAULT_QUALIFICATION_NAME))
            .jsonPath("$.[*].qualificationInstitution")
            .value(hasItem(DEFAULT_QUALIFICATION_INSTITUTION))
            .jsonPath("$.[*].qualificationDate")
            .value(hasItem(DEFAULT_QUALIFICATION_DATE.toString()));
    }

    @Test
    void getProfessionalQualification() {
        // Initialize the database
        professionalQualificationRepository.save(professionalQualification).block();

        // Get the professionalQualification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, professionalQualification.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(professionalQualification.getId().intValue()))
            .jsonPath("$.qualificationName")
            .value(is(DEFAULT_QUALIFICATION_NAME))
            .jsonPath("$.qualificationInstitution")
            .value(is(DEFAULT_QUALIFICATION_INSTITUTION))
            .jsonPath("$.qualificationDate")
            .value(is(DEFAULT_QUALIFICATION_DATE.toString()));
    }

    @Test
    void getNonExistingProfessionalQualification() {
        // Get the professionalQualification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingProfessionalQualification() throws Exception {
        // Initialize the database
        professionalQualificationRepository.save(professionalQualification).block();

        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().collectList().block().size();

        // Update the professionalQualification
        ProfessionalQualification updatedProfessionalQualification = professionalQualificationRepository
            .findById(professionalQualification.getId())
            .block();
        updatedProfessionalQualification
            .qualificationName(UPDATED_QUALIFICATION_NAME)
            .qualificationInstitution(UPDATED_QUALIFICATION_INSTITUTION)
            .qualificationDate(UPDATED_QUALIFICATION_DATE);
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(updatedProfessionalQualification);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, professionalQualificationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll().collectList().block();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);
        ProfessionalQualification testProfessionalQualification = professionalQualificationList.get(
            professionalQualificationList.size() - 1
        );
        assertThat(testProfessionalQualification.getQualificationName()).isEqualTo(UPDATED_QUALIFICATION_NAME);
        assertThat(testProfessionalQualification.getQualificationInstitution()).isEqualTo(UPDATED_QUALIFICATION_INSTITUTION);
        assertThat(testProfessionalQualification.getQualificationDate()).isEqualTo(UPDATED_QUALIFICATION_DATE);
    }

    @Test
    void putNonExistingProfessionalQualification() throws Exception {
        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().collectList().block().size();
        professionalQualification.setId(count.incrementAndGet());

        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, professionalQualificationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll().collectList().block();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProfessionalQualification() throws Exception {
        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().collectList().block().size();
        professionalQualification.setId(count.incrementAndGet());

        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll().collectList().block();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProfessionalQualification() throws Exception {
        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().collectList().block().size();
        professionalQualification.setId(count.incrementAndGet());

        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll().collectList().block();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProfessionalQualificationWithPatch() throws Exception {
        // Initialize the database
        professionalQualificationRepository.save(professionalQualification).block();

        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().collectList().block().size();

        // Update the professionalQualification using partial update
        ProfessionalQualification partialUpdatedProfessionalQualification = new ProfessionalQualification();
        partialUpdatedProfessionalQualification.setId(professionalQualification.getId());

        partialUpdatedProfessionalQualification.qualificationName(UPDATED_QUALIFICATION_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProfessionalQualification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProfessionalQualification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll().collectList().block();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);
        ProfessionalQualification testProfessionalQualification = professionalQualificationList.get(
            professionalQualificationList.size() - 1
        );
        assertThat(testProfessionalQualification.getQualificationName()).isEqualTo(UPDATED_QUALIFICATION_NAME);
        assertThat(testProfessionalQualification.getQualificationInstitution()).isEqualTo(DEFAULT_QUALIFICATION_INSTITUTION);
        assertThat(testProfessionalQualification.getQualificationDate()).isEqualTo(DEFAULT_QUALIFICATION_DATE);
    }

    @Test
    void fullUpdateProfessionalQualificationWithPatch() throws Exception {
        // Initialize the database
        professionalQualificationRepository.save(professionalQualification).block();

        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().collectList().block().size();

        // Update the professionalQualification using partial update
        ProfessionalQualification partialUpdatedProfessionalQualification = new ProfessionalQualification();
        partialUpdatedProfessionalQualification.setId(professionalQualification.getId());

        partialUpdatedProfessionalQualification
            .qualificationName(UPDATED_QUALIFICATION_NAME)
            .qualificationInstitution(UPDATED_QUALIFICATION_INSTITUTION)
            .qualificationDate(UPDATED_QUALIFICATION_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProfessionalQualification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProfessionalQualification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll().collectList().block();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);
        ProfessionalQualification testProfessionalQualification = professionalQualificationList.get(
            professionalQualificationList.size() - 1
        );
        assertThat(testProfessionalQualification.getQualificationName()).isEqualTo(UPDATED_QUALIFICATION_NAME);
        assertThat(testProfessionalQualification.getQualificationInstitution()).isEqualTo(UPDATED_QUALIFICATION_INSTITUTION);
        assertThat(testProfessionalQualification.getQualificationDate()).isEqualTo(UPDATED_QUALIFICATION_DATE);
    }

    @Test
    void patchNonExistingProfessionalQualification() throws Exception {
        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().collectList().block().size();
        professionalQualification.setId(count.incrementAndGet());

        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, professionalQualificationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll().collectList().block();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProfessionalQualification() throws Exception {
        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().collectList().block().size();
        professionalQualification.setId(count.incrementAndGet());

        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll().collectList().block();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProfessionalQualification() throws Exception {
        int databaseSizeBeforeUpdate = professionalQualificationRepository.findAll().collectList().block().size();
        professionalQualification.setId(count.incrementAndGet());

        // Create the ProfessionalQualification
        ProfessionalQualificationDTO professionalQualificationDTO = professionalQualificationMapper.toDto(professionalQualification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(professionalQualificationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ProfessionalQualification in the database
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll().collectList().block();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProfessionalQualification() {
        // Initialize the database
        professionalQualificationRepository.save(professionalQualification).block();

        int databaseSizeBeforeDelete = professionalQualificationRepository.findAll().collectList().block().size();

        // Delete the professionalQualification
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, professionalQualification.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ProfessionalQualification> professionalQualificationList = professionalQualificationRepository.findAll().collectList().block();
        assertThat(professionalQualificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
