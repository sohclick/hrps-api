package com.sohclick.hrps.payroll.web.rest;

import static com.sohclick.hrps.payroll.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.payroll.IntegrationTest;
import com.sohclick.hrps.payroll.domain.Deduction;
import com.sohclick.hrps.payroll.repository.DeductionRepository;
import com.sohclick.hrps.payroll.service.dto.DeductionDTO;
import com.sohclick.hrps.payroll.service.mapper.DeductionMapper;
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
 * Integration tests for the {@link DeductionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DeductionResourceIT {

    private static final BigDecimal DEFAULT_TAX_DEDUCTIONS = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX_DEDUCTIONS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PROVIDENT_FUND = new BigDecimal(1);
    private static final BigDecimal UPDATED_PROVIDENT_FUND = new BigDecimal(2);

    private static final BigDecimal DEFAULT_HEALTH_INSURANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_HEALTH_INSURANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LOAN_REPAYMENTS = new BigDecimal(1);
    private static final BigDecimal UPDATED_LOAN_REPAYMENTS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_OTHER_DEDUCTIONS = new BigDecimal(1);
    private static final BigDecimal UPDATED_OTHER_DEDUCTIONS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_DEDUCTION = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_DEDUCTION = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/deductions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeductionRepository deductionRepository;

    @Autowired
    private DeductionMapper deductionMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Deduction deduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deduction createEntity() {
        Deduction deduction = new Deduction()
            .taxDeductions(DEFAULT_TAX_DEDUCTIONS)
            .providentFund(DEFAULT_PROVIDENT_FUND)
            .healthInsurance(DEFAULT_HEALTH_INSURANCE)
            .loanRepayments(DEFAULT_LOAN_REPAYMENTS)
            .otherDeductions(DEFAULT_OTHER_DEDUCTIONS)
            .totalDeduction(DEFAULT_TOTAL_DEDUCTION);
        return deduction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deduction createUpdatedEntity() {
        Deduction deduction = new Deduction()
            .taxDeductions(UPDATED_TAX_DEDUCTIONS)
            .providentFund(UPDATED_PROVIDENT_FUND)
            .healthInsurance(UPDATED_HEALTH_INSURANCE)
            .loanRepayments(UPDATED_LOAN_REPAYMENTS)
            .otherDeductions(UPDATED_OTHER_DEDUCTIONS)
            .totalDeduction(UPDATED_TOTAL_DEDUCTION);
        return deduction;
    }

    @BeforeEach
    public void initTest() {
        deductionRepository.deleteAll().block();
        deduction = createEntity();
    }

    @Test
    void createDeduction() throws Exception {
        int databaseSizeBeforeCreate = deductionRepository.findAll().collectList().block().size();
        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deductionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll().collectList().block();
        assertThat(deductionList).hasSize(databaseSizeBeforeCreate + 1);
        Deduction testDeduction = deductionList.get(deductionList.size() - 1);
        assertThat(testDeduction.getTaxDeductions()).isEqualByComparingTo(DEFAULT_TAX_DEDUCTIONS);
        assertThat(testDeduction.getProvidentFund()).isEqualByComparingTo(DEFAULT_PROVIDENT_FUND);
        assertThat(testDeduction.getHealthInsurance()).isEqualByComparingTo(DEFAULT_HEALTH_INSURANCE);
        assertThat(testDeduction.getLoanRepayments()).isEqualByComparingTo(DEFAULT_LOAN_REPAYMENTS);
        assertThat(testDeduction.getOtherDeductions()).isEqualByComparingTo(DEFAULT_OTHER_DEDUCTIONS);
        assertThat(testDeduction.getTotalDeduction()).isEqualByComparingTo(DEFAULT_TOTAL_DEDUCTION);
    }

    @Test
    void createDeductionWithExistingId() throws Exception {
        // Create the Deduction with an existing ID
        deduction.setId(1L);
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        int databaseSizeBeforeCreate = deductionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deductionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll().collectList().block();
        assertThat(deductionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDeductions() {
        // Initialize the database
        deductionRepository.save(deduction).block();

        // Get all the deductionList
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
            .value(hasItem(deduction.getId().intValue()))
            .jsonPath("$.[*].taxDeductions")
            .value(hasItem(sameNumber(DEFAULT_TAX_DEDUCTIONS)))
            .jsonPath("$.[*].providentFund")
            .value(hasItem(sameNumber(DEFAULT_PROVIDENT_FUND)))
            .jsonPath("$.[*].healthInsurance")
            .value(hasItem(sameNumber(DEFAULT_HEALTH_INSURANCE)))
            .jsonPath("$.[*].loanRepayments")
            .value(hasItem(sameNumber(DEFAULT_LOAN_REPAYMENTS)))
            .jsonPath("$.[*].otherDeductions")
            .value(hasItem(sameNumber(DEFAULT_OTHER_DEDUCTIONS)))
            .jsonPath("$.[*].totalDeduction")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_DEDUCTION)));
    }

    @Test
    void getDeduction() {
        // Initialize the database
        deductionRepository.save(deduction).block();

        // Get the deduction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, deduction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(deduction.getId().intValue()))
            .jsonPath("$.taxDeductions")
            .value(is(sameNumber(DEFAULT_TAX_DEDUCTIONS)))
            .jsonPath("$.providentFund")
            .value(is(sameNumber(DEFAULT_PROVIDENT_FUND)))
            .jsonPath("$.healthInsurance")
            .value(is(sameNumber(DEFAULT_HEALTH_INSURANCE)))
            .jsonPath("$.loanRepayments")
            .value(is(sameNumber(DEFAULT_LOAN_REPAYMENTS)))
            .jsonPath("$.otherDeductions")
            .value(is(sameNumber(DEFAULT_OTHER_DEDUCTIONS)))
            .jsonPath("$.totalDeduction")
            .value(is(sameNumber(DEFAULT_TOTAL_DEDUCTION)));
    }

    @Test
    void getNonExistingDeduction() {
        // Get the deduction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDeduction() throws Exception {
        // Initialize the database
        deductionRepository.save(deduction).block();

        int databaseSizeBeforeUpdate = deductionRepository.findAll().collectList().block().size();

        // Update the deduction
        Deduction updatedDeduction = deductionRepository.findById(deduction.getId()).block();
        updatedDeduction
            .taxDeductions(UPDATED_TAX_DEDUCTIONS)
            .providentFund(UPDATED_PROVIDENT_FUND)
            .healthInsurance(UPDATED_HEALTH_INSURANCE)
            .loanRepayments(UPDATED_LOAN_REPAYMENTS)
            .otherDeductions(UPDATED_OTHER_DEDUCTIONS)
            .totalDeduction(UPDATED_TOTAL_DEDUCTION);
        DeductionDTO deductionDTO = deductionMapper.toDto(updatedDeduction);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, deductionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deductionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll().collectList().block();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
        Deduction testDeduction = deductionList.get(deductionList.size() - 1);
        assertThat(testDeduction.getTaxDeductions()).isEqualByComparingTo(UPDATED_TAX_DEDUCTIONS);
        assertThat(testDeduction.getProvidentFund()).isEqualByComparingTo(UPDATED_PROVIDENT_FUND);
        assertThat(testDeduction.getHealthInsurance()).isEqualByComparingTo(UPDATED_HEALTH_INSURANCE);
        assertThat(testDeduction.getLoanRepayments()).isEqualByComparingTo(UPDATED_LOAN_REPAYMENTS);
        assertThat(testDeduction.getOtherDeductions()).isEqualByComparingTo(UPDATED_OTHER_DEDUCTIONS);
        assertThat(testDeduction.getTotalDeduction()).isEqualByComparingTo(UPDATED_TOTAL_DEDUCTION);
    }

    @Test
    void putNonExistingDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().collectList().block().size();
        deduction.setId(count.incrementAndGet());

        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, deductionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deductionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll().collectList().block();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().collectList().block().size();
        deduction.setId(count.incrementAndGet());

        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deductionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll().collectList().block();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().collectList().block().size();
        deduction.setId(count.incrementAndGet());

        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deductionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll().collectList().block();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDeductionWithPatch() throws Exception {
        // Initialize the database
        deductionRepository.save(deduction).block();

        int databaseSizeBeforeUpdate = deductionRepository.findAll().collectList().block().size();

        // Update the deduction using partial update
        Deduction partialUpdatedDeduction = new Deduction();
        partialUpdatedDeduction.setId(deduction.getId());

        partialUpdatedDeduction
            .providentFund(UPDATED_PROVIDENT_FUND)
            .healthInsurance(UPDATED_HEALTH_INSURANCE)
            .loanRepayments(UPDATED_LOAN_REPAYMENTS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDeduction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDeduction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll().collectList().block();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
        Deduction testDeduction = deductionList.get(deductionList.size() - 1);
        assertThat(testDeduction.getTaxDeductions()).isEqualByComparingTo(DEFAULT_TAX_DEDUCTIONS);
        assertThat(testDeduction.getProvidentFund()).isEqualByComparingTo(UPDATED_PROVIDENT_FUND);
        assertThat(testDeduction.getHealthInsurance()).isEqualByComparingTo(UPDATED_HEALTH_INSURANCE);
        assertThat(testDeduction.getLoanRepayments()).isEqualByComparingTo(UPDATED_LOAN_REPAYMENTS);
        assertThat(testDeduction.getOtherDeductions()).isEqualByComparingTo(DEFAULT_OTHER_DEDUCTIONS);
        assertThat(testDeduction.getTotalDeduction()).isEqualByComparingTo(DEFAULT_TOTAL_DEDUCTION);
    }

    @Test
    void fullUpdateDeductionWithPatch() throws Exception {
        // Initialize the database
        deductionRepository.save(deduction).block();

        int databaseSizeBeforeUpdate = deductionRepository.findAll().collectList().block().size();

        // Update the deduction using partial update
        Deduction partialUpdatedDeduction = new Deduction();
        partialUpdatedDeduction.setId(deduction.getId());

        partialUpdatedDeduction
            .taxDeductions(UPDATED_TAX_DEDUCTIONS)
            .providentFund(UPDATED_PROVIDENT_FUND)
            .healthInsurance(UPDATED_HEALTH_INSURANCE)
            .loanRepayments(UPDATED_LOAN_REPAYMENTS)
            .otherDeductions(UPDATED_OTHER_DEDUCTIONS)
            .totalDeduction(UPDATED_TOTAL_DEDUCTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDeduction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDeduction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll().collectList().block();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
        Deduction testDeduction = deductionList.get(deductionList.size() - 1);
        assertThat(testDeduction.getTaxDeductions()).isEqualByComparingTo(UPDATED_TAX_DEDUCTIONS);
        assertThat(testDeduction.getProvidentFund()).isEqualByComparingTo(UPDATED_PROVIDENT_FUND);
        assertThat(testDeduction.getHealthInsurance()).isEqualByComparingTo(UPDATED_HEALTH_INSURANCE);
        assertThat(testDeduction.getLoanRepayments()).isEqualByComparingTo(UPDATED_LOAN_REPAYMENTS);
        assertThat(testDeduction.getOtherDeductions()).isEqualByComparingTo(UPDATED_OTHER_DEDUCTIONS);
        assertThat(testDeduction.getTotalDeduction()).isEqualByComparingTo(UPDATED_TOTAL_DEDUCTION);
    }

    @Test
    void patchNonExistingDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().collectList().block().size();
        deduction.setId(count.incrementAndGet());

        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, deductionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deductionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll().collectList().block();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().collectList().block().size();
        deduction.setId(count.incrementAndGet());

        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deductionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll().collectList().block();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().collectList().block().size();
        deduction.setId(count.incrementAndGet());

        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deductionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll().collectList().block();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDeduction() {
        // Initialize the database
        deductionRepository.save(deduction).block();

        int databaseSizeBeforeDelete = deductionRepository.findAll().collectList().block().size();

        // Delete the deduction
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, deduction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Deduction> deductionList = deductionRepository.findAll().collectList().block();
        assertThat(deductionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
