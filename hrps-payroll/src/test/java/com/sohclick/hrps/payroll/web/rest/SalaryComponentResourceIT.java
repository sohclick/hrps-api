package com.sohclick.hrps.payroll.web.rest;

import static com.sohclick.hrps.payroll.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.payroll.IntegrationTest;
import com.sohclick.hrps.payroll.domain.SalaryComponent;
import com.sohclick.hrps.payroll.repository.SalaryComponentRepository;
import com.sohclick.hrps.payroll.service.dto.SalaryComponentDTO;
import com.sohclick.hrps.payroll.service.mapper.SalaryComponentMapper;
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
 * Integration tests for the {@link SalaryComponentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SalaryComponentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/salary-components";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalaryComponentRepository salaryComponentRepository;

    @Autowired
    private SalaryComponentMapper salaryComponentMapper;

    @Autowired
    private WebTestClient webTestClient;

    private SalaryComponent salaryComponent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalaryComponent createEntity() {
        SalaryComponent salaryComponent = new SalaryComponent().name(DEFAULT_NAME).amount(DEFAULT_AMOUNT);
        return salaryComponent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalaryComponent createUpdatedEntity() {
        SalaryComponent salaryComponent = new SalaryComponent().name(UPDATED_NAME).amount(UPDATED_AMOUNT);
        return salaryComponent;
    }

    @BeforeEach
    public void initTest() {
        salaryComponentRepository.deleteAll().block();
        salaryComponent = createEntity();
    }

    @Test
    void createSalaryComponent() throws Exception {
        int databaseSizeBeforeCreate = salaryComponentRepository.findAll().collectList().block().size();
        // Create the SalaryComponent
        SalaryComponentDTO salaryComponentDTO = salaryComponentMapper.toDto(salaryComponent);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryComponentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SalaryComponent in the database
        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeCreate + 1);
        SalaryComponent testSalaryComponent = salaryComponentList.get(salaryComponentList.size() - 1);
        assertThat(testSalaryComponent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSalaryComponent.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
    }

    @Test
    void createSalaryComponentWithExistingId() throws Exception {
        // Create the SalaryComponent with an existing ID
        salaryComponent.setId(1L);
        SalaryComponentDTO salaryComponentDTO = salaryComponentMapper.toDto(salaryComponent);

        int databaseSizeBeforeCreate = salaryComponentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryComponentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SalaryComponent in the database
        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryComponentRepository.findAll().collectList().block().size();
        // set the field null
        salaryComponent.setName(null);

        // Create the SalaryComponent, which fails.
        SalaryComponentDTO salaryComponentDTO = salaryComponentMapper.toDto(salaryComponent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryComponentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryComponentRepository.findAll().collectList().block().size();
        // set the field null
        salaryComponent.setAmount(null);

        // Create the SalaryComponent, which fails.
        SalaryComponentDTO salaryComponentDTO = salaryComponentMapper.toDto(salaryComponent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryComponentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSalaryComponents() {
        // Initialize the database
        salaryComponentRepository.save(salaryComponent).block();

        // Get all the salaryComponentList
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
            .value(hasItem(salaryComponent.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    void getSalaryComponent() {
        // Initialize the database
        salaryComponentRepository.save(salaryComponent).block();

        // Get the salaryComponent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, salaryComponent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(salaryComponent.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    void getNonExistingSalaryComponent() {
        // Get the salaryComponent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSalaryComponent() throws Exception {
        // Initialize the database
        salaryComponentRepository.save(salaryComponent).block();

        int databaseSizeBeforeUpdate = salaryComponentRepository.findAll().collectList().block().size();

        // Update the salaryComponent
        SalaryComponent updatedSalaryComponent = salaryComponentRepository.findById(salaryComponent.getId()).block();
        updatedSalaryComponent.name(UPDATED_NAME).amount(UPDATED_AMOUNT);
        SalaryComponentDTO salaryComponentDTO = salaryComponentMapper.toDto(updatedSalaryComponent);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, salaryComponentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryComponentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SalaryComponent in the database
        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeUpdate);
        SalaryComponent testSalaryComponent = salaryComponentList.get(salaryComponentList.size() - 1);
        assertThat(testSalaryComponent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSalaryComponent.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void putNonExistingSalaryComponent() throws Exception {
        int databaseSizeBeforeUpdate = salaryComponentRepository.findAll().collectList().block().size();
        salaryComponent.setId(count.incrementAndGet());

        // Create the SalaryComponent
        SalaryComponentDTO salaryComponentDTO = salaryComponentMapper.toDto(salaryComponent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, salaryComponentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryComponentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SalaryComponent in the database
        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSalaryComponent() throws Exception {
        int databaseSizeBeforeUpdate = salaryComponentRepository.findAll().collectList().block().size();
        salaryComponent.setId(count.incrementAndGet());

        // Create the SalaryComponent
        SalaryComponentDTO salaryComponentDTO = salaryComponentMapper.toDto(salaryComponent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryComponentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SalaryComponent in the database
        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSalaryComponent() throws Exception {
        int databaseSizeBeforeUpdate = salaryComponentRepository.findAll().collectList().block().size();
        salaryComponent.setId(count.incrementAndGet());

        // Create the SalaryComponent
        SalaryComponentDTO salaryComponentDTO = salaryComponentMapper.toDto(salaryComponent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryComponentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SalaryComponent in the database
        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSalaryComponentWithPatch() throws Exception {
        // Initialize the database
        salaryComponentRepository.save(salaryComponent).block();

        int databaseSizeBeforeUpdate = salaryComponentRepository.findAll().collectList().block().size();

        // Update the salaryComponent using partial update
        SalaryComponent partialUpdatedSalaryComponent = new SalaryComponent();
        partialUpdatedSalaryComponent.setId(salaryComponent.getId());

        partialUpdatedSalaryComponent.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSalaryComponent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSalaryComponent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SalaryComponent in the database
        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeUpdate);
        SalaryComponent testSalaryComponent = salaryComponentList.get(salaryComponentList.size() - 1);
        assertThat(testSalaryComponent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSalaryComponent.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
    }

    @Test
    void fullUpdateSalaryComponentWithPatch() throws Exception {
        // Initialize the database
        salaryComponentRepository.save(salaryComponent).block();

        int databaseSizeBeforeUpdate = salaryComponentRepository.findAll().collectList().block().size();

        // Update the salaryComponent using partial update
        SalaryComponent partialUpdatedSalaryComponent = new SalaryComponent();
        partialUpdatedSalaryComponent.setId(salaryComponent.getId());

        partialUpdatedSalaryComponent.name(UPDATED_NAME).amount(UPDATED_AMOUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSalaryComponent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSalaryComponent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SalaryComponent in the database
        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeUpdate);
        SalaryComponent testSalaryComponent = salaryComponentList.get(salaryComponentList.size() - 1);
        assertThat(testSalaryComponent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSalaryComponent.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void patchNonExistingSalaryComponent() throws Exception {
        int databaseSizeBeforeUpdate = salaryComponentRepository.findAll().collectList().block().size();
        salaryComponent.setId(count.incrementAndGet());

        // Create the SalaryComponent
        SalaryComponentDTO salaryComponentDTO = salaryComponentMapper.toDto(salaryComponent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, salaryComponentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryComponentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SalaryComponent in the database
        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSalaryComponent() throws Exception {
        int databaseSizeBeforeUpdate = salaryComponentRepository.findAll().collectList().block().size();
        salaryComponent.setId(count.incrementAndGet());

        // Create the SalaryComponent
        SalaryComponentDTO salaryComponentDTO = salaryComponentMapper.toDto(salaryComponent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryComponentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SalaryComponent in the database
        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSalaryComponent() throws Exception {
        int databaseSizeBeforeUpdate = salaryComponentRepository.findAll().collectList().block().size();
        salaryComponent.setId(count.incrementAndGet());

        // Create the SalaryComponent
        SalaryComponentDTO salaryComponentDTO = salaryComponentMapper.toDto(salaryComponent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaryComponentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SalaryComponent in the database
        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSalaryComponent() {
        // Initialize the database
        salaryComponentRepository.save(salaryComponent).block();

        int databaseSizeBeforeDelete = salaryComponentRepository.findAll().collectList().block().size();

        // Delete the salaryComponent
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, salaryComponent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SalaryComponent> salaryComponentList = salaryComponentRepository.findAll().collectList().block();
        assertThat(salaryComponentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
