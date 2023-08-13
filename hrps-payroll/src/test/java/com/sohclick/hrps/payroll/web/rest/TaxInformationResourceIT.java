package com.sohclick.hrps.payroll.web.rest;

import static com.sohclick.hrps.payroll.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.payroll.IntegrationTest;
import com.sohclick.hrps.payroll.domain.TaxInformation;
import com.sohclick.hrps.payroll.domain.enumeration.TaxFilingStatus;
import com.sohclick.hrps.payroll.repository.TaxInformationRepository;
import com.sohclick.hrps.payroll.service.dto.TaxInformationDTO;
import com.sohclick.hrps.payroll.service.mapper.TaxInformationMapper;
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
 * Integration tests for the {@link TaxInformationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TaxInformationResourceIT {

    private static final String DEFAULT_EMPLOYEE_TAX_DECLARATION = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_TAX_DECLARATION = "BBBBBBBBBB";

    private static final TaxFilingStatus DEFAULT_TAX_FILING_STATUS = TaxFilingStatus.SINGLE;
    private static final TaxFilingStatus UPDATED_TAX_FILING_STATUS = TaxFilingStatus.MARRIED;

    private static final BigDecimal DEFAULT_WITHHOLDING_TAX_INFORMATION = new BigDecimal(1);
    private static final BigDecimal UPDATED_WITHHOLDING_TAX_INFORMATION = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/tax-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaxInformationRepository taxInformationRepository;

    @Autowired
    private TaxInformationMapper taxInformationMapper;

    @Autowired
    private WebTestClient webTestClient;

    private TaxInformation taxInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxInformation createEntity() {
        TaxInformation taxInformation = new TaxInformation()
            .employeeTaxDeclaration(DEFAULT_EMPLOYEE_TAX_DECLARATION)
            .taxFilingStatus(DEFAULT_TAX_FILING_STATUS)
            .withholdingTaxInformation(DEFAULT_WITHHOLDING_TAX_INFORMATION);
        return taxInformation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxInformation createUpdatedEntity() {
        TaxInformation taxInformation = new TaxInformation()
            .employeeTaxDeclaration(UPDATED_EMPLOYEE_TAX_DECLARATION)
            .taxFilingStatus(UPDATED_TAX_FILING_STATUS)
            .withholdingTaxInformation(UPDATED_WITHHOLDING_TAX_INFORMATION);
        return taxInformation;
    }

    @BeforeEach
    public void initTest() {
        taxInformationRepository.deleteAll().block();
        taxInformation = createEntity();
    }

    @Test
    void createTaxInformation() throws Exception {
        int databaseSizeBeforeCreate = taxInformationRepository.findAll().collectList().block().size();
        // Create the TaxInformation
        TaxInformationDTO taxInformationDTO = taxInformationMapper.toDto(taxInformation);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInformationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TaxInformation in the database
        List<TaxInformation> taxInformationList = taxInformationRepository.findAll().collectList().block();
        assertThat(taxInformationList).hasSize(databaseSizeBeforeCreate + 1);
        TaxInformation testTaxInformation = taxInformationList.get(taxInformationList.size() - 1);
        assertThat(testTaxInformation.getEmployeeTaxDeclaration()).isEqualTo(DEFAULT_EMPLOYEE_TAX_DECLARATION);
        assertThat(testTaxInformation.getTaxFilingStatus()).isEqualTo(DEFAULT_TAX_FILING_STATUS);
        assertThat(testTaxInformation.getWithholdingTaxInformation()).isEqualByComparingTo(DEFAULT_WITHHOLDING_TAX_INFORMATION);
    }

    @Test
    void createTaxInformationWithExistingId() throws Exception {
        // Create the TaxInformation with an existing ID
        taxInformation.setId(1L);
        TaxInformationDTO taxInformationDTO = taxInformationMapper.toDto(taxInformation);

        int databaseSizeBeforeCreate = taxInformationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInformationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxInformation in the database
        List<TaxInformation> taxInformationList = taxInformationRepository.findAll().collectList().block();
        assertThat(taxInformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTaxInformations() {
        // Initialize the database
        taxInformationRepository.save(taxInformation).block();

        // Get all the taxInformationList
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
            .value(hasItem(taxInformation.getId().intValue()))
            .jsonPath("$.[*].employeeTaxDeclaration")
            .value(hasItem(DEFAULT_EMPLOYEE_TAX_DECLARATION))
            .jsonPath("$.[*].taxFilingStatus")
            .value(hasItem(DEFAULT_TAX_FILING_STATUS.toString()))
            .jsonPath("$.[*].withholdingTaxInformation")
            .value(hasItem(sameNumber(DEFAULT_WITHHOLDING_TAX_INFORMATION)));
    }

    @Test
    void getTaxInformation() {
        // Initialize the database
        taxInformationRepository.save(taxInformation).block();

        // Get the taxInformation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, taxInformation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(taxInformation.getId().intValue()))
            .jsonPath("$.employeeTaxDeclaration")
            .value(is(DEFAULT_EMPLOYEE_TAX_DECLARATION))
            .jsonPath("$.taxFilingStatus")
            .value(is(DEFAULT_TAX_FILING_STATUS.toString()))
            .jsonPath("$.withholdingTaxInformation")
            .value(is(sameNumber(DEFAULT_WITHHOLDING_TAX_INFORMATION)));
    }

    @Test
    void getNonExistingTaxInformation() {
        // Get the taxInformation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTaxInformation() throws Exception {
        // Initialize the database
        taxInformationRepository.save(taxInformation).block();

        int databaseSizeBeforeUpdate = taxInformationRepository.findAll().collectList().block().size();

        // Update the taxInformation
        TaxInformation updatedTaxInformation = taxInformationRepository.findById(taxInformation.getId()).block();
        updatedTaxInformation
            .employeeTaxDeclaration(UPDATED_EMPLOYEE_TAX_DECLARATION)
            .taxFilingStatus(UPDATED_TAX_FILING_STATUS)
            .withholdingTaxInformation(UPDATED_WITHHOLDING_TAX_INFORMATION);
        TaxInformationDTO taxInformationDTO = taxInformationMapper.toDto(updatedTaxInformation);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, taxInformationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInformationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TaxInformation in the database
        List<TaxInformation> taxInformationList = taxInformationRepository.findAll().collectList().block();
        assertThat(taxInformationList).hasSize(databaseSizeBeforeUpdate);
        TaxInformation testTaxInformation = taxInformationList.get(taxInformationList.size() - 1);
        assertThat(testTaxInformation.getEmployeeTaxDeclaration()).isEqualTo(UPDATED_EMPLOYEE_TAX_DECLARATION);
        assertThat(testTaxInformation.getTaxFilingStatus()).isEqualTo(UPDATED_TAX_FILING_STATUS);
        assertThat(testTaxInformation.getWithholdingTaxInformation()).isEqualByComparingTo(UPDATED_WITHHOLDING_TAX_INFORMATION);
    }

    @Test
    void putNonExistingTaxInformation() throws Exception {
        int databaseSizeBeforeUpdate = taxInformationRepository.findAll().collectList().block().size();
        taxInformation.setId(count.incrementAndGet());

        // Create the TaxInformation
        TaxInformationDTO taxInformationDTO = taxInformationMapper.toDto(taxInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, taxInformationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInformationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxInformation in the database
        List<TaxInformation> taxInformationList = taxInformationRepository.findAll().collectList().block();
        assertThat(taxInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTaxInformation() throws Exception {
        int databaseSizeBeforeUpdate = taxInformationRepository.findAll().collectList().block().size();
        taxInformation.setId(count.incrementAndGet());

        // Create the TaxInformation
        TaxInformationDTO taxInformationDTO = taxInformationMapper.toDto(taxInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInformationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxInformation in the database
        List<TaxInformation> taxInformationList = taxInformationRepository.findAll().collectList().block();
        assertThat(taxInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTaxInformation() throws Exception {
        int databaseSizeBeforeUpdate = taxInformationRepository.findAll().collectList().block().size();
        taxInformation.setId(count.incrementAndGet());

        // Create the TaxInformation
        TaxInformationDTO taxInformationDTO = taxInformationMapper.toDto(taxInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInformationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TaxInformation in the database
        List<TaxInformation> taxInformationList = taxInformationRepository.findAll().collectList().block();
        assertThat(taxInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTaxInformationWithPatch() throws Exception {
        // Initialize the database
        taxInformationRepository.save(taxInformation).block();

        int databaseSizeBeforeUpdate = taxInformationRepository.findAll().collectList().block().size();

        // Update the taxInformation using partial update
        TaxInformation partialUpdatedTaxInformation = new TaxInformation();
        partialUpdatedTaxInformation.setId(taxInformation.getId());

        partialUpdatedTaxInformation
            .employeeTaxDeclaration(UPDATED_EMPLOYEE_TAX_DECLARATION)
            .taxFilingStatus(UPDATED_TAX_FILING_STATUS)
            .withholdingTaxInformation(UPDATED_WITHHOLDING_TAX_INFORMATION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTaxInformation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxInformation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TaxInformation in the database
        List<TaxInformation> taxInformationList = taxInformationRepository.findAll().collectList().block();
        assertThat(taxInformationList).hasSize(databaseSizeBeforeUpdate);
        TaxInformation testTaxInformation = taxInformationList.get(taxInformationList.size() - 1);
        assertThat(testTaxInformation.getEmployeeTaxDeclaration()).isEqualTo(UPDATED_EMPLOYEE_TAX_DECLARATION);
        assertThat(testTaxInformation.getTaxFilingStatus()).isEqualTo(UPDATED_TAX_FILING_STATUS);
        assertThat(testTaxInformation.getWithholdingTaxInformation()).isEqualByComparingTo(UPDATED_WITHHOLDING_TAX_INFORMATION);
    }

    @Test
    void fullUpdateTaxInformationWithPatch() throws Exception {
        // Initialize the database
        taxInformationRepository.save(taxInformation).block();

        int databaseSizeBeforeUpdate = taxInformationRepository.findAll().collectList().block().size();

        // Update the taxInformation using partial update
        TaxInformation partialUpdatedTaxInformation = new TaxInformation();
        partialUpdatedTaxInformation.setId(taxInformation.getId());

        partialUpdatedTaxInformation
            .employeeTaxDeclaration(UPDATED_EMPLOYEE_TAX_DECLARATION)
            .taxFilingStatus(UPDATED_TAX_FILING_STATUS)
            .withholdingTaxInformation(UPDATED_WITHHOLDING_TAX_INFORMATION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTaxInformation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxInformation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TaxInformation in the database
        List<TaxInformation> taxInformationList = taxInformationRepository.findAll().collectList().block();
        assertThat(taxInformationList).hasSize(databaseSizeBeforeUpdate);
        TaxInformation testTaxInformation = taxInformationList.get(taxInformationList.size() - 1);
        assertThat(testTaxInformation.getEmployeeTaxDeclaration()).isEqualTo(UPDATED_EMPLOYEE_TAX_DECLARATION);
        assertThat(testTaxInformation.getTaxFilingStatus()).isEqualTo(UPDATED_TAX_FILING_STATUS);
        assertThat(testTaxInformation.getWithholdingTaxInformation()).isEqualByComparingTo(UPDATED_WITHHOLDING_TAX_INFORMATION);
    }

    @Test
    void patchNonExistingTaxInformation() throws Exception {
        int databaseSizeBeforeUpdate = taxInformationRepository.findAll().collectList().block().size();
        taxInformation.setId(count.incrementAndGet());

        // Create the TaxInformation
        TaxInformationDTO taxInformationDTO = taxInformationMapper.toDto(taxInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, taxInformationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInformationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxInformation in the database
        List<TaxInformation> taxInformationList = taxInformationRepository.findAll().collectList().block();
        assertThat(taxInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTaxInformation() throws Exception {
        int databaseSizeBeforeUpdate = taxInformationRepository.findAll().collectList().block().size();
        taxInformation.setId(count.incrementAndGet());

        // Create the TaxInformation
        TaxInformationDTO taxInformationDTO = taxInformationMapper.toDto(taxInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInformationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxInformation in the database
        List<TaxInformation> taxInformationList = taxInformationRepository.findAll().collectList().block();
        assertThat(taxInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTaxInformation() throws Exception {
        int databaseSizeBeforeUpdate = taxInformationRepository.findAll().collectList().block().size();
        taxInformation.setId(count.incrementAndGet());

        // Create the TaxInformation
        TaxInformationDTO taxInformationDTO = taxInformationMapper.toDto(taxInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInformationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TaxInformation in the database
        List<TaxInformation> taxInformationList = taxInformationRepository.findAll().collectList().block();
        assertThat(taxInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTaxInformation() {
        // Initialize the database
        taxInformationRepository.save(taxInformation).block();

        int databaseSizeBeforeDelete = taxInformationRepository.findAll().collectList().block().size();

        // Delete the taxInformation
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, taxInformation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TaxInformation> taxInformationList = taxInformationRepository.findAll().collectList().block();
        assertThat(taxInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
