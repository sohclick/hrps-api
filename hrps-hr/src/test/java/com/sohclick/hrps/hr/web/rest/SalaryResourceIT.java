package com.sohclick.hrps.hr.web.rest;

import static com.sohclick.hrps.hr.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.hr.IntegrationTest;
import com.sohclick.hrps.hr.domain.Salary;
import com.sohclick.hrps.hr.repository.SalaryRepository;
import com.sohclick.hrps.hr.service.dto.SalaryDTO;
import com.sohclick.hrps.hr.service.mapper.SalaryMapper;
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
 * Integration tests for the {@link SalaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SalaryResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/salaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private SalaryMapper salaryMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Salary salary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salary createEntity() {
        Salary salary = new Salary().amount(DEFAULT_AMOUNT);
        return salary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salary createUpdatedEntity() {
        Salary salary = new Salary().amount(UPDATED_AMOUNT);
        return salary;
    }

    @BeforeEach
    public void initTest() {
        salaryRepository.deleteAll().block();
        salary = createEntity();
    }

    @Test
    void createSalary() throws Exception {
        int databaseSizeBeforeCreate = salaryRepository.findAll().collectList().block().size();
        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeCreate + 1);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
    }

    @Test
    void createSalaryWithExistingId() throws Exception {
        // Create the Salary with an existing ID
        salary.setId(1L);
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        int databaseSizeBeforeCreate = salaryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryRepository.findAll().collectList().block().size();
        // set the field null
        salary.setAmount(null);

        // Create the Salary, which fails.
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSalaries() {
        // Initialize the database
        salaryRepository.save(salary).block();

        // Get all the salaryList
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
            .value(hasItem(salary.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    void getSalary() {
        // Initialize the database
        salaryRepository.save(salary).block();

        // Get the salary
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, salary.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(salary.getId().intValue()))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    void getNonExistingSalary() {
        // Get the salary
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSalary() throws Exception {
        // Initialize the database
        salaryRepository.save(salary).block();

        int databaseSizeBeforeUpdate = salaryRepository.findAll().collectList().block().size();

        // Update the salary
        Salary updatedSalary = salaryRepository.findById(salary.getId()).block();
        updatedSalary.amount(UPDATED_AMOUNT);
        SalaryDTO salaryDTO = salaryMapper.toDto(updatedSalary);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, salaryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void putNonExistingSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().collectList().block().size();
        salary.setId(count.incrementAndGet());

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, salaryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().collectList().block().size();
        salary.setId(count.incrementAndGet());

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().collectList().block().size();
        salary.setId(count.incrementAndGet());

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSalaryWithPatch() throws Exception {
        // Initialize the database
        salaryRepository.save(salary).block();

        int databaseSizeBeforeUpdate = salaryRepository.findAll().collectList().block().size();

        // Update the salary using partial update
        Salary partialUpdatedSalary = new Salary();
        partialUpdatedSalary.setId(salary.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSalary.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSalary))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
    }

    @Test
    void fullUpdateSalaryWithPatch() throws Exception {
        // Initialize the database
        salaryRepository.save(salary).block();

        int databaseSizeBeforeUpdate = salaryRepository.findAll().collectList().block().size();

        // Update the salary using partial update
        Salary partialUpdatedSalary = new Salary();
        partialUpdatedSalary.setId(salary.getId());

        partialUpdatedSalary.amount(UPDATED_AMOUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSalary.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSalary))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void patchNonExistingSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().collectList().block().size();
        salary.setId(count.incrementAndGet());

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, salaryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().collectList().block().size();
        salary.setId(count.incrementAndGet());

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().collectList().block().size();
        salary.setId(count.incrementAndGet());

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSalary() {
        // Initialize the database
        salaryRepository.save(salary).block();

        int databaseSizeBeforeDelete = salaryRepository.findAll().collectList().block().size();

        // Delete the salary
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, salary.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Salary> salaryList = salaryRepository.findAll().collectList().block();
        assertThat(salaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
