package com.sohclick.hrps.payroll.web.rest;

import static com.sohclick.hrps.payroll.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.payroll.IntegrationTest;
import com.sohclick.hrps.payroll.domain.Payroll;
import com.sohclick.hrps.payroll.repository.PayrollRepository;
import com.sohclick.hrps.payroll.service.dto.PayrollDTO;
import com.sohclick.hrps.payroll.service.mapper.PayrollMapper;
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
 * Integration tests for the {@link PayrollResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PayrollResourceIT {

    private static final BigDecimal DEFAULT_BASIC_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_BASIC_SALARY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BONUS = new BigDecimal(1);
    private static final BigDecimal UPDATED_BONUS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_OVERTIME_PAY = new BigDecimal(1);
    private static final BigDecimal UPDATED_OVERTIME_PAY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_COMMISSION = new BigDecimal(1);
    private static final BigDecimal UPDATED_COMMISSION = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INCENTIVES = new BigDecimal(1);
    private static final BigDecimal UPDATED_INCENTIVES = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/payrolls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private PayrollMapper payrollMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Payroll payroll;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payroll createEntity() {
        Payroll payroll = new Payroll()
            .basicSalary(DEFAULT_BASIC_SALARY)
            .bonus(DEFAULT_BONUS)
            .overtimePay(DEFAULT_OVERTIME_PAY)
            .commission(DEFAULT_COMMISSION)
            .incentives(DEFAULT_INCENTIVES);
        return payroll;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payroll createUpdatedEntity() {
        Payroll payroll = new Payroll()
            .basicSalary(UPDATED_BASIC_SALARY)
            .bonus(UPDATED_BONUS)
            .overtimePay(UPDATED_OVERTIME_PAY)
            .commission(UPDATED_COMMISSION)
            .incentives(UPDATED_INCENTIVES);
        return payroll;
    }

    @BeforeEach
    public void initTest() {
        payrollRepository.deleteAll().block();
        payroll = createEntity();
    }

    @Test
    void createPayroll() throws Exception {
        int databaseSizeBeforeCreate = payrollRepository.findAll().collectList().block().size();
        // Create the Payroll
        PayrollDTO payrollDTO = payrollMapper.toDto(payroll);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payrollDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Payroll in the database
        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeCreate + 1);
        Payroll testPayroll = payrollList.get(payrollList.size() - 1);
        assertThat(testPayroll.getBasicSalary()).isEqualByComparingTo(DEFAULT_BASIC_SALARY);
        assertThat(testPayroll.getBonus()).isEqualByComparingTo(DEFAULT_BONUS);
        assertThat(testPayroll.getOvertimePay()).isEqualByComparingTo(DEFAULT_OVERTIME_PAY);
        assertThat(testPayroll.getCommission()).isEqualByComparingTo(DEFAULT_COMMISSION);
        assertThat(testPayroll.getIncentives()).isEqualByComparingTo(DEFAULT_INCENTIVES);
    }

    @Test
    void createPayrollWithExistingId() throws Exception {
        // Create the Payroll with an existing ID
        payroll.setId(1L);
        PayrollDTO payrollDTO = payrollMapper.toDto(payroll);

        int databaseSizeBeforeCreate = payrollRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payrollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payroll in the database
        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkBasicSalaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = payrollRepository.findAll().collectList().block().size();
        // set the field null
        payroll.setBasicSalary(null);

        // Create the Payroll, which fails.
        PayrollDTO payrollDTO = payrollMapper.toDto(payroll);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payrollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPayrolls() {
        // Initialize the database
        payrollRepository.save(payroll).block();

        // Get all the payrollList
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
            .value(hasItem(payroll.getId().intValue()))
            .jsonPath("$.[*].basicSalary")
            .value(hasItem(sameNumber(DEFAULT_BASIC_SALARY)))
            .jsonPath("$.[*].bonus")
            .value(hasItem(sameNumber(DEFAULT_BONUS)))
            .jsonPath("$.[*].overtimePay")
            .value(hasItem(sameNumber(DEFAULT_OVERTIME_PAY)))
            .jsonPath("$.[*].commission")
            .value(hasItem(sameNumber(DEFAULT_COMMISSION)))
            .jsonPath("$.[*].incentives")
            .value(hasItem(sameNumber(DEFAULT_INCENTIVES)));
    }

    @Test
    void getPayroll() {
        // Initialize the database
        payrollRepository.save(payroll).block();

        // Get the payroll
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, payroll.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(payroll.getId().intValue()))
            .jsonPath("$.basicSalary")
            .value(is(sameNumber(DEFAULT_BASIC_SALARY)))
            .jsonPath("$.bonus")
            .value(is(sameNumber(DEFAULT_BONUS)))
            .jsonPath("$.overtimePay")
            .value(is(sameNumber(DEFAULT_OVERTIME_PAY)))
            .jsonPath("$.commission")
            .value(is(sameNumber(DEFAULT_COMMISSION)))
            .jsonPath("$.incentives")
            .value(is(sameNumber(DEFAULT_INCENTIVES)));
    }

    @Test
    void getNonExistingPayroll() {
        // Get the payroll
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPayroll() throws Exception {
        // Initialize the database
        payrollRepository.save(payroll).block();

        int databaseSizeBeforeUpdate = payrollRepository.findAll().collectList().block().size();

        // Update the payroll
        Payroll updatedPayroll = payrollRepository.findById(payroll.getId()).block();
        updatedPayroll
            .basicSalary(UPDATED_BASIC_SALARY)
            .bonus(UPDATED_BONUS)
            .overtimePay(UPDATED_OVERTIME_PAY)
            .commission(UPDATED_COMMISSION)
            .incentives(UPDATED_INCENTIVES);
        PayrollDTO payrollDTO = payrollMapper.toDto(updatedPayroll);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, payrollDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payrollDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Payroll in the database
        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeUpdate);
        Payroll testPayroll = payrollList.get(payrollList.size() - 1);
        assertThat(testPayroll.getBasicSalary()).isEqualByComparingTo(UPDATED_BASIC_SALARY);
        assertThat(testPayroll.getBonus()).isEqualByComparingTo(UPDATED_BONUS);
        assertThat(testPayroll.getOvertimePay()).isEqualByComparingTo(UPDATED_OVERTIME_PAY);
        assertThat(testPayroll.getCommission()).isEqualByComparingTo(UPDATED_COMMISSION);
        assertThat(testPayroll.getIncentives()).isEqualByComparingTo(UPDATED_INCENTIVES);
    }

    @Test
    void putNonExistingPayroll() throws Exception {
        int databaseSizeBeforeUpdate = payrollRepository.findAll().collectList().block().size();
        payroll.setId(count.incrementAndGet());

        // Create the Payroll
        PayrollDTO payrollDTO = payrollMapper.toDto(payroll);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, payrollDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payrollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payroll in the database
        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPayroll() throws Exception {
        int databaseSizeBeforeUpdate = payrollRepository.findAll().collectList().block().size();
        payroll.setId(count.incrementAndGet());

        // Create the Payroll
        PayrollDTO payrollDTO = payrollMapper.toDto(payroll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payrollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payroll in the database
        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPayroll() throws Exception {
        int databaseSizeBeforeUpdate = payrollRepository.findAll().collectList().block().size();
        payroll.setId(count.incrementAndGet());

        // Create the Payroll
        PayrollDTO payrollDTO = payrollMapper.toDto(payroll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payrollDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Payroll in the database
        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePayrollWithPatch() throws Exception {
        // Initialize the database
        payrollRepository.save(payroll).block();

        int databaseSizeBeforeUpdate = payrollRepository.findAll().collectList().block().size();

        // Update the payroll using partial update
        Payroll partialUpdatedPayroll = new Payroll();
        partialUpdatedPayroll.setId(payroll.getId());

        partialUpdatedPayroll.overtimePay(UPDATED_OVERTIME_PAY).incentives(UPDATED_INCENTIVES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPayroll.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPayroll))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Payroll in the database
        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeUpdate);
        Payroll testPayroll = payrollList.get(payrollList.size() - 1);
        assertThat(testPayroll.getBasicSalary()).isEqualByComparingTo(DEFAULT_BASIC_SALARY);
        assertThat(testPayroll.getBonus()).isEqualByComparingTo(DEFAULT_BONUS);
        assertThat(testPayroll.getOvertimePay()).isEqualByComparingTo(UPDATED_OVERTIME_PAY);
        assertThat(testPayroll.getCommission()).isEqualByComparingTo(DEFAULT_COMMISSION);
        assertThat(testPayroll.getIncentives()).isEqualByComparingTo(UPDATED_INCENTIVES);
    }

    @Test
    void fullUpdatePayrollWithPatch() throws Exception {
        // Initialize the database
        payrollRepository.save(payroll).block();

        int databaseSizeBeforeUpdate = payrollRepository.findAll().collectList().block().size();

        // Update the payroll using partial update
        Payroll partialUpdatedPayroll = new Payroll();
        partialUpdatedPayroll.setId(payroll.getId());

        partialUpdatedPayroll
            .basicSalary(UPDATED_BASIC_SALARY)
            .bonus(UPDATED_BONUS)
            .overtimePay(UPDATED_OVERTIME_PAY)
            .commission(UPDATED_COMMISSION)
            .incentives(UPDATED_INCENTIVES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPayroll.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPayroll))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Payroll in the database
        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeUpdate);
        Payroll testPayroll = payrollList.get(payrollList.size() - 1);
        assertThat(testPayroll.getBasicSalary()).isEqualByComparingTo(UPDATED_BASIC_SALARY);
        assertThat(testPayroll.getBonus()).isEqualByComparingTo(UPDATED_BONUS);
        assertThat(testPayroll.getOvertimePay()).isEqualByComparingTo(UPDATED_OVERTIME_PAY);
        assertThat(testPayroll.getCommission()).isEqualByComparingTo(UPDATED_COMMISSION);
        assertThat(testPayroll.getIncentives()).isEqualByComparingTo(UPDATED_INCENTIVES);
    }

    @Test
    void patchNonExistingPayroll() throws Exception {
        int databaseSizeBeforeUpdate = payrollRepository.findAll().collectList().block().size();
        payroll.setId(count.incrementAndGet());

        // Create the Payroll
        PayrollDTO payrollDTO = payrollMapper.toDto(payroll);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, payrollDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(payrollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payroll in the database
        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPayroll() throws Exception {
        int databaseSizeBeforeUpdate = payrollRepository.findAll().collectList().block().size();
        payroll.setId(count.incrementAndGet());

        // Create the Payroll
        PayrollDTO payrollDTO = payrollMapper.toDto(payroll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(payrollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payroll in the database
        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPayroll() throws Exception {
        int databaseSizeBeforeUpdate = payrollRepository.findAll().collectList().block().size();
        payroll.setId(count.incrementAndGet());

        // Create the Payroll
        PayrollDTO payrollDTO = payrollMapper.toDto(payroll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(payrollDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Payroll in the database
        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePayroll() {
        // Initialize the database
        payrollRepository.save(payroll).block();

        int databaseSizeBeforeDelete = payrollRepository.findAll().collectList().block().size();

        // Delete the payroll
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, payroll.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Payroll> payrollList = payrollRepository.findAll().collectList().block();
        assertThat(payrollList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
