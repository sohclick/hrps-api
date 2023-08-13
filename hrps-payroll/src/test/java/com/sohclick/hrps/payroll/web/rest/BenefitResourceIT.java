package com.sohclick.hrps.payroll.web.rest;

import static com.sohclick.hrps.payroll.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.payroll.IntegrationTest;
import com.sohclick.hrps.payroll.domain.Benefit;
import com.sohclick.hrps.payroll.repository.BenefitRepository;
import com.sohclick.hrps.payroll.service.dto.BenefitDTO;
import com.sohclick.hrps.payroll.service.mapper.BenefitMapper;
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
 * Integration tests for the {@link BenefitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BenefitResourceIT {

    private static final BigDecimal DEFAULT_PAID_LEAVE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAID_LEAVE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_UNPAID_LEAVE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNPAID_LEAVE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MATERNITY_LEAVE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MATERNITY_LEAVE = new BigDecimal(2);

    private static final Boolean DEFAULT_MEDICAL_BENEFITS = false;
    private static final Boolean UPDATED_MEDICAL_BENEFITS = true;

    private static final Boolean DEFAULT_DENTAL_AND_VISION_BENEFITS = false;
    private static final Boolean UPDATED_DENTAL_AND_VISION_BENEFITS = true;

    private static final Boolean DEFAULT_LIFE_INSURANCE = false;
    private static final Boolean UPDATED_LIFE_INSURANCE = true;

    private static final Boolean DEFAULT_RETIREMENT_BENEFITS = false;
    private static final Boolean UPDATED_RETIREMENT_BENEFITS = true;

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
        Benefit benefit = new Benefit()
            .paidLeave(DEFAULT_PAID_LEAVE)
            .unpaidLeave(DEFAULT_UNPAID_LEAVE)
            .maternityLeave(DEFAULT_MATERNITY_LEAVE)
            .medicalBenefits(DEFAULT_MEDICAL_BENEFITS)
            .dentalAndVisionBenefits(DEFAULT_DENTAL_AND_VISION_BENEFITS)
            .lifeInsurance(DEFAULT_LIFE_INSURANCE)
            .retirementBenefits(DEFAULT_RETIREMENT_BENEFITS);
        return benefit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Benefit createUpdatedEntity() {
        Benefit benefit = new Benefit()
            .paidLeave(UPDATED_PAID_LEAVE)
            .unpaidLeave(UPDATED_UNPAID_LEAVE)
            .maternityLeave(UPDATED_MATERNITY_LEAVE)
            .medicalBenefits(UPDATED_MEDICAL_BENEFITS)
            .dentalAndVisionBenefits(UPDATED_DENTAL_AND_VISION_BENEFITS)
            .lifeInsurance(UPDATED_LIFE_INSURANCE)
            .retirementBenefits(UPDATED_RETIREMENT_BENEFITS);
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
        assertThat(testBenefit.getPaidLeave()).isEqualByComparingTo(DEFAULT_PAID_LEAVE);
        assertThat(testBenefit.getUnpaidLeave()).isEqualByComparingTo(DEFAULT_UNPAID_LEAVE);
        assertThat(testBenefit.getMaternityLeave()).isEqualByComparingTo(DEFAULT_MATERNITY_LEAVE);
        assertThat(testBenefit.getMedicalBenefits()).isEqualTo(DEFAULT_MEDICAL_BENEFITS);
        assertThat(testBenefit.getDentalAndVisionBenefits()).isEqualTo(DEFAULT_DENTAL_AND_VISION_BENEFITS);
        assertThat(testBenefit.getLifeInsurance()).isEqualTo(DEFAULT_LIFE_INSURANCE);
        assertThat(testBenefit.getRetirementBenefits()).isEqualTo(DEFAULT_RETIREMENT_BENEFITS);
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
            .jsonPath("$.[*].paidLeave")
            .value(hasItem(sameNumber(DEFAULT_PAID_LEAVE)))
            .jsonPath("$.[*].unpaidLeave")
            .value(hasItem(sameNumber(DEFAULT_UNPAID_LEAVE)))
            .jsonPath("$.[*].maternityLeave")
            .value(hasItem(sameNumber(DEFAULT_MATERNITY_LEAVE)))
            .jsonPath("$.[*].medicalBenefits")
            .value(hasItem(DEFAULT_MEDICAL_BENEFITS.booleanValue()))
            .jsonPath("$.[*].dentalAndVisionBenefits")
            .value(hasItem(DEFAULT_DENTAL_AND_VISION_BENEFITS.booleanValue()))
            .jsonPath("$.[*].lifeInsurance")
            .value(hasItem(DEFAULT_LIFE_INSURANCE.booleanValue()))
            .jsonPath("$.[*].retirementBenefits")
            .value(hasItem(DEFAULT_RETIREMENT_BENEFITS.booleanValue()));
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
            .jsonPath("$.paidLeave")
            .value(is(sameNumber(DEFAULT_PAID_LEAVE)))
            .jsonPath("$.unpaidLeave")
            .value(is(sameNumber(DEFAULT_UNPAID_LEAVE)))
            .jsonPath("$.maternityLeave")
            .value(is(sameNumber(DEFAULT_MATERNITY_LEAVE)))
            .jsonPath("$.medicalBenefits")
            .value(is(DEFAULT_MEDICAL_BENEFITS.booleanValue()))
            .jsonPath("$.dentalAndVisionBenefits")
            .value(is(DEFAULT_DENTAL_AND_VISION_BENEFITS.booleanValue()))
            .jsonPath("$.lifeInsurance")
            .value(is(DEFAULT_LIFE_INSURANCE.booleanValue()))
            .jsonPath("$.retirementBenefits")
            .value(is(DEFAULT_RETIREMENT_BENEFITS.booleanValue()));
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
        updatedBenefit
            .paidLeave(UPDATED_PAID_LEAVE)
            .unpaidLeave(UPDATED_UNPAID_LEAVE)
            .maternityLeave(UPDATED_MATERNITY_LEAVE)
            .medicalBenefits(UPDATED_MEDICAL_BENEFITS)
            .dentalAndVisionBenefits(UPDATED_DENTAL_AND_VISION_BENEFITS)
            .lifeInsurance(UPDATED_LIFE_INSURANCE)
            .retirementBenefits(UPDATED_RETIREMENT_BENEFITS);
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
        assertThat(testBenefit.getPaidLeave()).isEqualByComparingTo(UPDATED_PAID_LEAVE);
        assertThat(testBenefit.getUnpaidLeave()).isEqualByComparingTo(UPDATED_UNPAID_LEAVE);
        assertThat(testBenefit.getMaternityLeave()).isEqualByComparingTo(UPDATED_MATERNITY_LEAVE);
        assertThat(testBenefit.getMedicalBenefits()).isEqualTo(UPDATED_MEDICAL_BENEFITS);
        assertThat(testBenefit.getDentalAndVisionBenefits()).isEqualTo(UPDATED_DENTAL_AND_VISION_BENEFITS);
        assertThat(testBenefit.getLifeInsurance()).isEqualTo(UPDATED_LIFE_INSURANCE);
        assertThat(testBenefit.getRetirementBenefits()).isEqualTo(UPDATED_RETIREMENT_BENEFITS);
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

        partialUpdatedBenefit
            .maternityLeave(UPDATED_MATERNITY_LEAVE)
            .dentalAndVisionBenefits(UPDATED_DENTAL_AND_VISION_BENEFITS)
            .lifeInsurance(UPDATED_LIFE_INSURANCE)
            .retirementBenefits(UPDATED_RETIREMENT_BENEFITS);

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
        assertThat(testBenefit.getPaidLeave()).isEqualByComparingTo(DEFAULT_PAID_LEAVE);
        assertThat(testBenefit.getUnpaidLeave()).isEqualByComparingTo(DEFAULT_UNPAID_LEAVE);
        assertThat(testBenefit.getMaternityLeave()).isEqualByComparingTo(UPDATED_MATERNITY_LEAVE);
        assertThat(testBenefit.getMedicalBenefits()).isEqualTo(DEFAULT_MEDICAL_BENEFITS);
        assertThat(testBenefit.getDentalAndVisionBenefits()).isEqualTo(UPDATED_DENTAL_AND_VISION_BENEFITS);
        assertThat(testBenefit.getLifeInsurance()).isEqualTo(UPDATED_LIFE_INSURANCE);
        assertThat(testBenefit.getRetirementBenefits()).isEqualTo(UPDATED_RETIREMENT_BENEFITS);
    }

    @Test
    void fullUpdateBenefitWithPatch() throws Exception {
        // Initialize the database
        benefitRepository.save(benefit).block();

        int databaseSizeBeforeUpdate = benefitRepository.findAll().collectList().block().size();

        // Update the benefit using partial update
        Benefit partialUpdatedBenefit = new Benefit();
        partialUpdatedBenefit.setId(benefit.getId());

        partialUpdatedBenefit
            .paidLeave(UPDATED_PAID_LEAVE)
            .unpaidLeave(UPDATED_UNPAID_LEAVE)
            .maternityLeave(UPDATED_MATERNITY_LEAVE)
            .medicalBenefits(UPDATED_MEDICAL_BENEFITS)
            .dentalAndVisionBenefits(UPDATED_DENTAL_AND_VISION_BENEFITS)
            .lifeInsurance(UPDATED_LIFE_INSURANCE)
            .retirementBenefits(UPDATED_RETIREMENT_BENEFITS);

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
        assertThat(testBenefit.getPaidLeave()).isEqualByComparingTo(UPDATED_PAID_LEAVE);
        assertThat(testBenefit.getUnpaidLeave()).isEqualByComparingTo(UPDATED_UNPAID_LEAVE);
        assertThat(testBenefit.getMaternityLeave()).isEqualByComparingTo(UPDATED_MATERNITY_LEAVE);
        assertThat(testBenefit.getMedicalBenefits()).isEqualTo(UPDATED_MEDICAL_BENEFITS);
        assertThat(testBenefit.getDentalAndVisionBenefits()).isEqualTo(UPDATED_DENTAL_AND_VISION_BENEFITS);
        assertThat(testBenefit.getLifeInsurance()).isEqualTo(UPDATED_LIFE_INSURANCE);
        assertThat(testBenefit.getRetirementBenefits()).isEqualTo(UPDATED_RETIREMENT_BENEFITS);
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
