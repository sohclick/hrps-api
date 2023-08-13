package com.sohclick.hrps.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.hr.IntegrationTest;
import com.sohclick.hrps.hr.domain.EmployeeDocument;
import com.sohclick.hrps.hr.domain.enumeration.DocumentType;
import com.sohclick.hrps.hr.repository.EmployeeDocumentRepository;
import com.sohclick.hrps.hr.service.dto.EmployeeDocumentDTO;
import com.sohclick.hrps.hr.service.mapper.EmployeeDocumentMapper;
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
 * Integration tests for the {@link EmployeeDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EmployeeDocumentResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final DocumentType DEFAULT_DOCUMENT_TYPE = DocumentType.RESUME;
    private static final DocumentType UPDATED_DOCUMENT_TYPE = DocumentType.COVER_LETTER;

    private static final LocalDate DEFAULT_UPLOAD_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPLOAD_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employee-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeDocumentRepository employeeDocumentRepository;

    @Autowired
    private EmployeeDocumentMapper employeeDocumentMapper;

    @Autowired
    private WebTestClient webTestClient;

    private EmployeeDocument employeeDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeDocument createEntity() {
        EmployeeDocument employeeDocument = new EmployeeDocument()
            .description(DEFAULT_DESCRIPTION)
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .uploadDate(DEFAULT_UPLOAD_DATE)
            .url(DEFAULT_URL);
        return employeeDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeDocument createUpdatedEntity() {
        EmployeeDocument employeeDocument = new EmployeeDocument()
            .description(UPDATED_DESCRIPTION)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .url(UPDATED_URL);
        return employeeDocument;
    }

    @BeforeEach
    public void initTest() {
        employeeDocumentRepository.deleteAll().block();
        employeeDocument = createEntity();
    }

    @Test
    void createEmployeeDocument() throws Exception {
        int databaseSizeBeforeCreate = employeeDocumentRepository.findAll().collectList().block().size();
        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeDocument testEmployeeDocument = employeeDocumentList.get(employeeDocumentList.size() - 1);
        assertThat(testEmployeeDocument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEmployeeDocument.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testEmployeeDocument.getUploadDate()).isEqualTo(DEFAULT_UPLOAD_DATE);
        assertThat(testEmployeeDocument.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    void createEmployeeDocumentWithExistingId() throws Exception {
        // Create the EmployeeDocument with an existing ID
        employeeDocument.setId(1L);
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        int databaseSizeBeforeCreate = employeeDocumentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeDocumentRepository.findAll().collectList().block().size();
        // set the field null
        employeeDocument.setDescription(null);

        // Create the EmployeeDocument, which fails.
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDocumentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeDocumentRepository.findAll().collectList().block().size();
        // set the field null
        employeeDocument.setDocumentType(null);

        // Create the EmployeeDocument, which fails.
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEmployeeDocuments() {
        // Initialize the database
        employeeDocumentRepository.save(employeeDocument).block();

        // Get all the employeeDocumentList
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
            .value(hasItem(employeeDocument.getId().intValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].documentType")
            .value(hasItem(DEFAULT_DOCUMENT_TYPE.toString()))
            .jsonPath("$.[*].uploadDate")
            .value(hasItem(DEFAULT_UPLOAD_DATE.toString()))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL));
    }

    @Test
    void getEmployeeDocument() {
        // Initialize the database
        employeeDocumentRepository.save(employeeDocument).block();

        // Get the employeeDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, employeeDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(employeeDocument.getId().intValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.documentType")
            .value(is(DEFAULT_DOCUMENT_TYPE.toString()))
            .jsonPath("$.uploadDate")
            .value(is(DEFAULT_UPLOAD_DATE.toString()))
            .jsonPath("$.url")
            .value(is(DEFAULT_URL));
    }

    @Test
    void getNonExistingEmployeeDocument() {
        // Get the employeeDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEmployeeDocument() throws Exception {
        // Initialize the database
        employeeDocumentRepository.save(employeeDocument).block();

        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().collectList().block().size();

        // Update the employeeDocument
        EmployeeDocument updatedEmployeeDocument = employeeDocumentRepository.findById(employeeDocument.getId()).block();
        updatedEmployeeDocument
            .description(UPDATED_DESCRIPTION)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .url(UPDATED_URL);
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(updatedEmployeeDocument);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, employeeDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
        EmployeeDocument testEmployeeDocument = employeeDocumentList.get(employeeDocumentList.size() - 1);
        assertThat(testEmployeeDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmployeeDocument.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testEmployeeDocument.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
        assertThat(testEmployeeDocument.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    void putNonExistingEmployeeDocument() throws Exception {
        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().collectList().block().size();
        employeeDocument.setId(count.incrementAndGet());

        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, employeeDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEmployeeDocument() throws Exception {
        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().collectList().block().size();
        employeeDocument.setId(count.incrementAndGet());

        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEmployeeDocument() throws Exception {
        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().collectList().block().size();
        employeeDocument.setId(count.incrementAndGet());

        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEmployeeDocumentWithPatch() throws Exception {
        // Initialize the database
        employeeDocumentRepository.save(employeeDocument).block();

        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().collectList().block().size();

        // Update the employeeDocument using partial update
        EmployeeDocument partialUpdatedEmployeeDocument = new EmployeeDocument();
        partialUpdatedEmployeeDocument.setId(employeeDocument.getId());

        partialUpdatedEmployeeDocument.description(UPDATED_DESCRIPTION).url(UPDATED_URL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmployeeDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
        EmployeeDocument testEmployeeDocument = employeeDocumentList.get(employeeDocumentList.size() - 1);
        assertThat(testEmployeeDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmployeeDocument.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testEmployeeDocument.getUploadDate()).isEqualTo(DEFAULT_UPLOAD_DATE);
        assertThat(testEmployeeDocument.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    void fullUpdateEmployeeDocumentWithPatch() throws Exception {
        // Initialize the database
        employeeDocumentRepository.save(employeeDocument).block();

        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().collectList().block().size();

        // Update the employeeDocument using partial update
        EmployeeDocument partialUpdatedEmployeeDocument = new EmployeeDocument();
        partialUpdatedEmployeeDocument.setId(employeeDocument.getId());

        partialUpdatedEmployeeDocument
            .description(UPDATED_DESCRIPTION)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .url(UPDATED_URL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmployeeDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
        EmployeeDocument testEmployeeDocument = employeeDocumentList.get(employeeDocumentList.size() - 1);
        assertThat(testEmployeeDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmployeeDocument.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testEmployeeDocument.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
        assertThat(testEmployeeDocument.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    void patchNonExistingEmployeeDocument() throws Exception {
        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().collectList().block().size();
        employeeDocument.setId(count.incrementAndGet());

        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, employeeDocumentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEmployeeDocument() throws Exception {
        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().collectList().block().size();
        employeeDocument.setId(count.incrementAndGet());

        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEmployeeDocument() throws Exception {
        int databaseSizeBeforeUpdate = employeeDocumentRepository.findAll().collectList().block().size();
        employeeDocument.setId(count.incrementAndGet());

        // Create the EmployeeDocument
        EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentMapper.toDto(employeeDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EmployeeDocument in the database
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEmployeeDocument() {
        // Initialize the database
        employeeDocumentRepository.save(employeeDocument).block();

        int databaseSizeBeforeDelete = employeeDocumentRepository.findAll().collectList().block().size();

        // Delete the employeeDocument
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, employeeDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<EmployeeDocument> employeeDocumentList = employeeDocumentRepository.findAll().collectList().block();
        assertThat(employeeDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
