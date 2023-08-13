package com.sohclick.hrps.payroll.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.payroll.IntegrationTest;
import com.sohclick.hrps.payroll.domain.PaymentSchedule;
import com.sohclick.hrps.payroll.domain.enumeration.PaymentFrequency;
import com.sohclick.hrps.payroll.repository.PaymentScheduleRepository;
import com.sohclick.hrps.payroll.service.dto.PaymentScheduleDTO;
import com.sohclick.hrps.payroll.service.mapper.PaymentScheduleMapper;
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
 * Integration tests for the {@link PaymentScheduleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PaymentScheduleResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final PaymentFrequency DEFAULT_FREQUENCY = PaymentFrequency.WEEKLY;
    private static final PaymentFrequency UPDATED_FREQUENCY = PaymentFrequency.BI_WEEKLY;

    private static final String ENTITY_API_URL = "/api/payment-schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    private PaymentScheduleMapper paymentScheduleMapper;

    @Autowired
    private WebTestClient webTestClient;

    private PaymentSchedule paymentSchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentSchedule createEntity() {
        PaymentSchedule paymentSchedule = new PaymentSchedule()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .frequency(DEFAULT_FREQUENCY);
        return paymentSchedule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentSchedule createUpdatedEntity() {
        PaymentSchedule paymentSchedule = new PaymentSchedule()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .frequency(UPDATED_FREQUENCY);
        return paymentSchedule;
    }

    @BeforeEach
    public void initTest() {
        paymentScheduleRepository.deleteAll().block();
        paymentSchedule = createEntity();
    }

    @Test
    void createPaymentSchedule() throws Exception {
        int databaseSizeBeforeCreate = paymentScheduleRepository.findAll().collectList().block().size();
        // Create the PaymentSchedule
        PaymentScheduleDTO paymentScheduleDTO = paymentScheduleMapper.toDto(paymentSchedule);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentScheduleDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentSchedule testPaymentSchedule = paymentScheduleList.get(paymentScheduleList.size() - 1);
        assertThat(testPaymentSchedule.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPaymentSchedule.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPaymentSchedule.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
    }

    @Test
    void createPaymentScheduleWithExistingId() throws Exception {
        // Create the PaymentSchedule with an existing ID
        paymentSchedule.setId(1L);
        PaymentScheduleDTO paymentScheduleDTO = paymentScheduleMapper.toDto(paymentSchedule);

        int databaseSizeBeforeCreate = paymentScheduleRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentScheduleRepository.findAll().collectList().block().size();
        // set the field null
        paymentSchedule.setStartDate(null);

        // Create the PaymentSchedule, which fails.
        PaymentScheduleDTO paymentScheduleDTO = paymentScheduleMapper.toDto(paymentSchedule);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentScheduleRepository.findAll().collectList().block().size();
        // set the field null
        paymentSchedule.setEndDate(null);

        // Create the PaymentSchedule, which fails.
        PaymentScheduleDTO paymentScheduleDTO = paymentScheduleMapper.toDto(paymentSchedule);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFrequencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentScheduleRepository.findAll().collectList().block().size();
        // set the field null
        paymentSchedule.setFrequency(null);

        // Create the PaymentSchedule, which fails.
        PaymentScheduleDTO paymentScheduleDTO = paymentScheduleMapper.toDto(paymentSchedule);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPaymentSchedules() {
        // Initialize the database
        paymentScheduleRepository.save(paymentSchedule).block();

        // Get all the paymentScheduleList
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
            .value(hasItem(paymentSchedule.getId().intValue()))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].frequency")
            .value(hasItem(DEFAULT_FREQUENCY.toString()));
    }

    @Test
    void getPaymentSchedule() {
        // Initialize the database
        paymentScheduleRepository.save(paymentSchedule).block();

        // Get the paymentSchedule
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, paymentSchedule.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(paymentSchedule.getId().intValue()))
            .jsonPath("$.startDate")
            .value(is(DEFAULT_START_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
            .jsonPath("$.frequency")
            .value(is(DEFAULT_FREQUENCY.toString()));
    }

    @Test
    void getNonExistingPaymentSchedule() {
        // Get the paymentSchedule
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPaymentSchedule() throws Exception {
        // Initialize the database
        paymentScheduleRepository.save(paymentSchedule).block();

        int databaseSizeBeforeUpdate = paymentScheduleRepository.findAll().collectList().block().size();

        // Update the paymentSchedule
        PaymentSchedule updatedPaymentSchedule = paymentScheduleRepository.findById(paymentSchedule.getId()).block();
        updatedPaymentSchedule.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).frequency(UPDATED_FREQUENCY);
        PaymentScheduleDTO paymentScheduleDTO = paymentScheduleMapper.toDto(updatedPaymentSchedule);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentScheduleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentScheduleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeUpdate);
        PaymentSchedule testPaymentSchedule = paymentScheduleList.get(paymentScheduleList.size() - 1);
        assertThat(testPaymentSchedule.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPaymentSchedule.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPaymentSchedule.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
    }

    @Test
    void putNonExistingPaymentSchedule() throws Exception {
        int databaseSizeBeforeUpdate = paymentScheduleRepository.findAll().collectList().block().size();
        paymentSchedule.setId(count.incrementAndGet());

        // Create the PaymentSchedule
        PaymentScheduleDTO paymentScheduleDTO = paymentScheduleMapper.toDto(paymentSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentScheduleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPaymentSchedule() throws Exception {
        int databaseSizeBeforeUpdate = paymentScheduleRepository.findAll().collectList().block().size();
        paymentSchedule.setId(count.incrementAndGet());

        // Create the PaymentSchedule
        PaymentScheduleDTO paymentScheduleDTO = paymentScheduleMapper.toDto(paymentSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPaymentSchedule() throws Exception {
        int databaseSizeBeforeUpdate = paymentScheduleRepository.findAll().collectList().block().size();
        paymentSchedule.setId(count.incrementAndGet());

        // Create the PaymentSchedule
        PaymentScheduleDTO paymentScheduleDTO = paymentScheduleMapper.toDto(paymentSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentScheduleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePaymentScheduleWithPatch() throws Exception {
        // Initialize the database
        paymentScheduleRepository.save(paymentSchedule).block();

        int databaseSizeBeforeUpdate = paymentScheduleRepository.findAll().collectList().block().size();

        // Update the paymentSchedule using partial update
        PaymentSchedule partialUpdatedPaymentSchedule = new PaymentSchedule();
        partialUpdatedPaymentSchedule.setId(paymentSchedule.getId());

        partialUpdatedPaymentSchedule.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).frequency(UPDATED_FREQUENCY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaymentSchedule.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentSchedule))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeUpdate);
        PaymentSchedule testPaymentSchedule = paymentScheduleList.get(paymentScheduleList.size() - 1);
        assertThat(testPaymentSchedule.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPaymentSchedule.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPaymentSchedule.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
    }

    @Test
    void fullUpdatePaymentScheduleWithPatch() throws Exception {
        // Initialize the database
        paymentScheduleRepository.save(paymentSchedule).block();

        int databaseSizeBeforeUpdate = paymentScheduleRepository.findAll().collectList().block().size();

        // Update the paymentSchedule using partial update
        PaymentSchedule partialUpdatedPaymentSchedule = new PaymentSchedule();
        partialUpdatedPaymentSchedule.setId(paymentSchedule.getId());

        partialUpdatedPaymentSchedule.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).frequency(UPDATED_FREQUENCY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaymentSchedule.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentSchedule))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeUpdate);
        PaymentSchedule testPaymentSchedule = paymentScheduleList.get(paymentScheduleList.size() - 1);
        assertThat(testPaymentSchedule.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPaymentSchedule.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPaymentSchedule.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
    }

    @Test
    void patchNonExistingPaymentSchedule() throws Exception {
        int databaseSizeBeforeUpdate = paymentScheduleRepository.findAll().collectList().block().size();
        paymentSchedule.setId(count.incrementAndGet());

        // Create the PaymentSchedule
        PaymentScheduleDTO paymentScheduleDTO = paymentScheduleMapper.toDto(paymentSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, paymentScheduleDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPaymentSchedule() throws Exception {
        int databaseSizeBeforeUpdate = paymentScheduleRepository.findAll().collectList().block().size();
        paymentSchedule.setId(count.incrementAndGet());

        // Create the PaymentSchedule
        PaymentScheduleDTO paymentScheduleDTO = paymentScheduleMapper.toDto(paymentSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentScheduleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPaymentSchedule() throws Exception {
        int databaseSizeBeforeUpdate = paymentScheduleRepository.findAll().collectList().block().size();
        paymentSchedule.setId(count.incrementAndGet());

        // Create the PaymentSchedule
        PaymentScheduleDTO paymentScheduleDTO = paymentScheduleMapper.toDto(paymentSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentScheduleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PaymentSchedule in the database
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePaymentSchedule() {
        // Initialize the database
        paymentScheduleRepository.save(paymentSchedule).block();

        int databaseSizeBeforeDelete = paymentScheduleRepository.findAll().collectList().block().size();

        // Delete the paymentSchedule
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, paymentSchedule.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll().collectList().block();
        assertThat(paymentScheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
