package com.sohclick.hrps.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.hr.IntegrationTest;
import com.sohclick.hrps.hr.domain.EmergencyContact;
import com.sohclick.hrps.hr.repository.EmergencyContactRepository;
import com.sohclick.hrps.hr.service.dto.EmergencyContactDTO;
import com.sohclick.hrps.hr.service.mapper.EmergencyContactMapper;
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
 * Integration tests for the {@link EmergencyContactResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EmergencyContactResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_RELATIONSHIP = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/emergency-contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    @Autowired
    private EmergencyContactMapper emergencyContactMapper;

    @Autowired
    private WebTestClient webTestClient;

    private EmergencyContact emergencyContact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmergencyContact createEntity() {
        EmergencyContact emergencyContact = new EmergencyContact()
            .name(DEFAULT_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .relationship(DEFAULT_RELATIONSHIP);
        return emergencyContact;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmergencyContact createUpdatedEntity() {
        EmergencyContact emergencyContact = new EmergencyContact()
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .relationship(UPDATED_RELATIONSHIP);
        return emergencyContact;
    }

    @BeforeEach
    public void initTest() {
        emergencyContactRepository.deleteAll().block();
        emergencyContact = createEntity();
    }

    @Test
    void createEmergencyContact() throws Exception {
        int databaseSizeBeforeCreate = emergencyContactRepository.findAll().collectList().block().size();
        // Create the EmergencyContact
        EmergencyContactDTO emergencyContactDTO = emergencyContactMapper.toDto(emergencyContact);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emergencyContactDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the EmergencyContact in the database
        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeCreate + 1);
        EmergencyContact testEmergencyContact = emergencyContactList.get(emergencyContactList.size() - 1);
        assertThat(testEmergencyContact.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmergencyContact.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmergencyContact.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmergencyContact.getRelationship()).isEqualTo(DEFAULT_RELATIONSHIP);
    }

    @Test
    void createEmergencyContactWithExistingId() throws Exception {
        // Create the EmergencyContact with an existing ID
        emergencyContact.setId(1L);
        EmergencyContactDTO emergencyContactDTO = emergencyContactMapper.toDto(emergencyContact);

        int databaseSizeBeforeCreate = emergencyContactRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emergencyContactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmergencyContact in the database
        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = emergencyContactRepository.findAll().collectList().block().size();
        // set the field null
        emergencyContact.setName(null);

        // Create the EmergencyContact, which fails.
        EmergencyContactDTO emergencyContactDTO = emergencyContactMapper.toDto(emergencyContact);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emergencyContactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEmergencyContacts() {
        // Initialize the database
        emergencyContactRepository.save(emergencyContact).block();

        // Get all the emergencyContactList
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
            .value(hasItem(emergencyContact.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].phoneNumber")
            .value(hasItem(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].relationship")
            .value(hasItem(DEFAULT_RELATIONSHIP));
    }

    @Test
    void getEmergencyContact() {
        // Initialize the database
        emergencyContactRepository.save(emergencyContact).block();

        // Get the emergencyContact
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, emergencyContact.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(emergencyContact.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.phoneNumber")
            .value(is(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.relationship")
            .value(is(DEFAULT_RELATIONSHIP));
    }

    @Test
    void getNonExistingEmergencyContact() {
        // Get the emergencyContact
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEmergencyContact() throws Exception {
        // Initialize the database
        emergencyContactRepository.save(emergencyContact).block();

        int databaseSizeBeforeUpdate = emergencyContactRepository.findAll().collectList().block().size();

        // Update the emergencyContact
        EmergencyContact updatedEmergencyContact = emergencyContactRepository.findById(emergencyContact.getId()).block();
        updatedEmergencyContact
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .relationship(UPDATED_RELATIONSHIP);
        EmergencyContactDTO emergencyContactDTO = emergencyContactMapper.toDto(updatedEmergencyContact);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, emergencyContactDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emergencyContactDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmergencyContact in the database
        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeUpdate);
        EmergencyContact testEmergencyContact = emergencyContactList.get(emergencyContactList.size() - 1);
        assertThat(testEmergencyContact.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmergencyContact.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmergencyContact.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmergencyContact.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
    }

    @Test
    void putNonExistingEmergencyContact() throws Exception {
        int databaseSizeBeforeUpdate = emergencyContactRepository.findAll().collectList().block().size();
        emergencyContact.setId(count.incrementAndGet());

        // Create the EmergencyContact
        EmergencyContactDTO emergencyContactDTO = emergencyContactMapper.toDto(emergencyContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, emergencyContactDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emergencyContactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmergencyContact in the database
        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEmergencyContact() throws Exception {
        int databaseSizeBeforeUpdate = emergencyContactRepository.findAll().collectList().block().size();
        emergencyContact.setId(count.incrementAndGet());

        // Create the EmergencyContact
        EmergencyContactDTO emergencyContactDTO = emergencyContactMapper.toDto(emergencyContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emergencyContactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmergencyContact in the database
        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEmergencyContact() throws Exception {
        int databaseSizeBeforeUpdate = emergencyContactRepository.findAll().collectList().block().size();
        emergencyContact.setId(count.incrementAndGet());

        // Create the EmergencyContact
        EmergencyContactDTO emergencyContactDTO = emergencyContactMapper.toDto(emergencyContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emergencyContactDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EmergencyContact in the database
        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEmergencyContactWithPatch() throws Exception {
        // Initialize the database
        emergencyContactRepository.save(emergencyContact).block();

        int databaseSizeBeforeUpdate = emergencyContactRepository.findAll().collectList().block().size();

        // Update the emergencyContact using partial update
        EmergencyContact partialUpdatedEmergencyContact = new EmergencyContact();
        partialUpdatedEmergencyContact.setId(emergencyContact.getId());

        partialUpdatedEmergencyContact.name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER).email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmergencyContact.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmergencyContact))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmergencyContact in the database
        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeUpdate);
        EmergencyContact testEmergencyContact = emergencyContactList.get(emergencyContactList.size() - 1);
        assertThat(testEmergencyContact.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmergencyContact.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmergencyContact.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmergencyContact.getRelationship()).isEqualTo(DEFAULT_RELATIONSHIP);
    }

    @Test
    void fullUpdateEmergencyContactWithPatch() throws Exception {
        // Initialize the database
        emergencyContactRepository.save(emergencyContact).block();

        int databaseSizeBeforeUpdate = emergencyContactRepository.findAll().collectList().block().size();

        // Update the emergencyContact using partial update
        EmergencyContact partialUpdatedEmergencyContact = new EmergencyContact();
        partialUpdatedEmergencyContact.setId(emergencyContact.getId());

        partialUpdatedEmergencyContact
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .relationship(UPDATED_RELATIONSHIP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmergencyContact.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmergencyContact))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmergencyContact in the database
        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeUpdate);
        EmergencyContact testEmergencyContact = emergencyContactList.get(emergencyContactList.size() - 1);
        assertThat(testEmergencyContact.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmergencyContact.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmergencyContact.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmergencyContact.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
    }

    @Test
    void patchNonExistingEmergencyContact() throws Exception {
        int databaseSizeBeforeUpdate = emergencyContactRepository.findAll().collectList().block().size();
        emergencyContact.setId(count.incrementAndGet());

        // Create the EmergencyContact
        EmergencyContactDTO emergencyContactDTO = emergencyContactMapper.toDto(emergencyContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, emergencyContactDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(emergencyContactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmergencyContact in the database
        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEmergencyContact() throws Exception {
        int databaseSizeBeforeUpdate = emergencyContactRepository.findAll().collectList().block().size();
        emergencyContact.setId(count.incrementAndGet());

        // Create the EmergencyContact
        EmergencyContactDTO emergencyContactDTO = emergencyContactMapper.toDto(emergencyContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(emergencyContactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmergencyContact in the database
        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEmergencyContact() throws Exception {
        int databaseSizeBeforeUpdate = emergencyContactRepository.findAll().collectList().block().size();
        emergencyContact.setId(count.incrementAndGet());

        // Create the EmergencyContact
        EmergencyContactDTO emergencyContactDTO = emergencyContactMapper.toDto(emergencyContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(emergencyContactDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EmergencyContact in the database
        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEmergencyContact() {
        // Initialize the database
        emergencyContactRepository.save(emergencyContact).block();

        int databaseSizeBeforeDelete = emergencyContactRepository.findAll().collectList().block().size();

        // Delete the emergencyContact
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, emergencyContact.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<EmergencyContact> emergencyContactList = emergencyContactRepository.findAll().collectList().block();
        assertThat(emergencyContactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
