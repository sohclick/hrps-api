package com.sohclick.hrps.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.hr.IntegrationTest;
import com.sohclick.hrps.hr.domain.Benefit;
import com.sohclick.hrps.hr.domain.enumeration.BenefitType;
import com.sohclick.hrps.hr.repository.BenefitRepository;
import com.sohclick.hrps.hr.service.dto.BenefitDTO;
import com.sohclick.hrps.hr.service.mapper.BenefitMapper;
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
 * Integration tests for the {@link BenefitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BenefitResourceIT {

    private static final BenefitType DEFAULT_TYPE = BenefitType.HEALTH_INSURANCE;
    private static final BenefitType UPDATED_TYPE = BenefitType.DENTAL_INSURANCE;

    private static final String ENTITY_API_URL = "/api/benefits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private BenefitMapper benefitMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Benefit benefit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Benefit createEntity() {
        Benefit benefit = new Benefit().type(DEFAULT_TYPE);
        return benefit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Benefit createUpdatedEntity() {
        Benefit benefit = new Benefit().type(UPDATED_TYPE);
        return benefit;
    }

    @BeforeEach
    public void initTest() {
        benefitRepository.deleteAll().block();
        benefit = createEntity();
    }

    @Test
    void createBenefit() throws Exception {
        int databaseSizeBeforeCreate = benefitRepository.findAll().collectList().block().size();
        // Create the Benefit
        BenefitDTO benefitDTO = benefitMapper.toDto(benefit);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(benefitDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeCreate + 1);
        Benefit testBenefit = benefitList.get(benefitList.size() - 1);
        assertThat(testBenefit.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createBenefitWithExistingId() throws Exception {
        // Create the Benefit with an existing ID
        benefit.setId(1L);
        BenefitDTO benefitDTO = benefitMapper.toDto(benefit);

        int databaseSizeBeforeCreate = benefitRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(benefitDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = benefitRepository.findAll().collectList().block().size();
        // set the field null
        benefit.setType(null);

        // Create the Benefit, which fails.
        BenefitDTO benefitDTO = benefitMapper.toDto(benefit);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(benefitDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllBenefits() {
        // Initialize the database
        benefitRepository.save(benefit).block();

        // Get all the benefitList
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
            .value(hasItem(benefit.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()));
    }

    @Test
    void getBenefit() {
        // Initialize the database
        benefitRepository.save(benefit).block();

        // Get the benefit
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, benefit.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(benefit.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()));
    }

    @Test
    void getNonExistingBenefit() {
        // Get the benefit
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingBenefit() throws Exception {
        // Initialize the database
        benefitRepository.save(benefit).block();

        int databaseSizeBeforeUpdate = benefitRepository.findAll().collectList().block().size();

        // Update the benefit
        Benefit updatedBenefit = benefitRepository.findById(benefit.getId()).block();
        updatedBenefit.type(UPDATED_TYPE);
        BenefitDTO benefitDTO = benefitMapper.toDto(updatedBenefit);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, benefitDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(benefitDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate);
        Benefit testBenefit = benefitList.get(benefitList.size() - 1);
        assertThat(testBenefit.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingBenefit() throws Exception {
        int databaseSizeBeforeUpdate = benefitRepository.findAll().collectList().block().size();
        benefit.setId(count.incrementAndGet());

        // Create the Benefit
        BenefitDTO benefitDTO = benefitMapper.toDto(benefit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, benefitDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(benefitDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBenefit() throws Exception {
        int databaseSizeBeforeUpdate = benefitRepository.findAll().collectList().block().size();
        benefit.setId(count.incrementAndGet());

        // Create the Benefit
        BenefitDTO benefitDTO = benefitMapper.toDto(benefit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(benefitDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBenefit() throws Exception {
        int databaseSizeBeforeUpdate = benefitRepository.findAll().collectList().block().size();
        benefit.setId(count.incrementAndGet());

        // Create the Benefit
        BenefitDTO benefitDTO = benefitMapper.toDto(benefit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(benefitDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBenefitWithPatch() throws Exception {
        // Initialize the database
        benefitRepository.save(benefit).block();

        int databaseSizeBeforeUpdate = benefitRepository.findAll().collectList().block().size();

        // Update the benefit using partial update
        Benefit partialUpdatedBenefit = new Benefit();
        partialUpdatedBenefit.setId(benefit.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBenefit.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBenefit))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate);
        Benefit testBenefit = benefitList.get(benefitList.size() - 1);
        assertThat(testBenefit.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void fullUpdateBenefitWithPatch() throws Exception {
        // Initialize the database
        benefitRepository.save(benefit).block();

        int databaseSizeBeforeUpdate = benefitRepository.findAll().collectList().block().size();

        // Update the benefit using partial update
        Benefit partialUpdatedBenefit = new Benefit();
        partialUpdatedBenefit.setId(benefit.getId());

        partialUpdatedBenefit.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBenefit.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBenefit))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate);
        Benefit testBenefit = benefitList.get(benefitList.size() - 1);
        assertThat(testBenefit.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingBenefit() throws Exception {
        int databaseSizeBeforeUpdate = benefitRepository.findAll().collectList().block().size();
        benefit.setId(count.incrementAndGet());

        // Create the Benefit
        BenefitDTO benefitDTO = benefitMapper.toDto(benefit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, benefitDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(benefitDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBenefit() throws Exception {
        int databaseSizeBeforeUpdate = benefitRepository.findAll().collectList().block().size();
        benefit.setId(count.incrementAndGet());

        // Create the Benefit
        BenefitDTO benefitDTO = benefitMapper.toDto(benefit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(benefitDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBenefit() throws Exception {
        int databaseSizeBeforeUpdate = benefitRepository.findAll().collectList().block().size();
        benefit.setId(count.incrementAndGet());

        // Create the Benefit
        BenefitDTO benefitDTO = benefitMapper.toDto(benefit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(benefitDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Benefit in the database
        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBenefit() {
        // Initialize the database
        benefitRepository.save(benefit).block();

        int databaseSizeBeforeDelete = benefitRepository.findAll().collectList().block().size();

        // Delete the benefit
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, benefit.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Benefit> benefitList = benefitRepository.findAll().collectList().block();
        assertThat(benefitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
