package com.sohclick.hrps.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.hr.IntegrationTest;
import com.sohclick.hrps.hr.domain.NextOfKin;
import com.sohclick.hrps.hr.repository.NextOfKinRepository;
import com.sohclick.hrps.hr.service.dto.NextOfKinDTO;
import com.sohclick.hrps.hr.service.mapper.NextOfKinMapper;
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
 * Integration tests for the {@link NextOfKinResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class NextOfKinResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_INFORMATION = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_INFORMATION = "BBBBBBBBBB";

    private static final String DEFAULT_RELATIONSHIP = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-of-kins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NextOfKinRepository nextOfKinRepository;

    @Autowired
    private NextOfKinMapper nextOfKinMapper;

    @Autowired
    private WebTestClient webTestClient;

    private NextOfKin nextOfKin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOfKin createEntity() {
        NextOfKin nextOfKin = new NextOfKin()
            .name(DEFAULT_NAME)
            .contactInformation(DEFAULT_CONTACT_INFORMATION)
            .relationship(DEFAULT_RELATIONSHIP);
        return nextOfKin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOfKin createUpdatedEntity() {
        NextOfKin nextOfKin = new NextOfKin()
            .name(UPDATED_NAME)
            .contactInformation(UPDATED_CONTACT_INFORMATION)
            .relationship(UPDATED_RELATIONSHIP);
        return nextOfKin;
    }

    @BeforeEach
    public void initTest() {
        nextOfKinRepository.deleteAll().block();
        nextOfKin = createEntity();
    }

    @Test
    void createNextOfKin() throws Exception {
        int databaseSizeBeforeCreate = nextOfKinRepository.findAll().collectList().block().size();
        // Create the NextOfKin
        NextOfKinDTO nextOfKinDTO = nextOfKinMapper.toDto(nextOfKin);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nextOfKinDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the NextOfKin in the database
        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeCreate + 1);
        NextOfKin testNextOfKin = nextOfKinList.get(nextOfKinList.size() - 1);
        assertThat(testNextOfKin.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNextOfKin.getContactInformation()).isEqualTo(DEFAULT_CONTACT_INFORMATION);
        assertThat(testNextOfKin.getRelationship()).isEqualTo(DEFAULT_RELATIONSHIP);
    }

    @Test
    void createNextOfKinWithExistingId() throws Exception {
        // Create the NextOfKin with an existing ID
        nextOfKin.setId(1L);
        NextOfKinDTO nextOfKinDTO = nextOfKinMapper.toDto(nextOfKin);

        int databaseSizeBeforeCreate = nextOfKinRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nextOfKinDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NextOfKin in the database
        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nextOfKinRepository.findAll().collectList().block().size();
        // set the field null
        nextOfKin.setName(null);

        // Create the NextOfKin, which fails.
        NextOfKinDTO nextOfKinDTO = nextOfKinMapper.toDto(nextOfKin);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nextOfKinDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkContactInformationIsRequired() throws Exception {
        int databaseSizeBeforeTest = nextOfKinRepository.findAll().collectList().block().size();
        // set the field null
        nextOfKin.setContactInformation(null);

        // Create the NextOfKin, which fails.
        NextOfKinDTO nextOfKinDTO = nextOfKinMapper.toDto(nextOfKin);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nextOfKinDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllNextOfKins() {
        // Initialize the database
        nextOfKinRepository.save(nextOfKin).block();

        // Get all the nextOfKinList
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
            .value(hasItem(nextOfKin.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].contactInformation")
            .value(hasItem(DEFAULT_CONTACT_INFORMATION))
            .jsonPath("$.[*].relationship")
            .value(hasItem(DEFAULT_RELATIONSHIP));
    }

    @Test
    void getNextOfKin() {
        // Initialize the database
        nextOfKinRepository.save(nextOfKin).block();

        // Get the nextOfKin
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, nextOfKin.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(nextOfKin.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.contactInformation")
            .value(is(DEFAULT_CONTACT_INFORMATION))
            .jsonPath("$.relationship")
            .value(is(DEFAULT_RELATIONSHIP));
    }

    @Test
    void getNonExistingNextOfKin() {
        // Get the nextOfKin
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingNextOfKin() throws Exception {
        // Initialize the database
        nextOfKinRepository.save(nextOfKin).block();

        int databaseSizeBeforeUpdate = nextOfKinRepository.findAll().collectList().block().size();

        // Update the nextOfKin
        NextOfKin updatedNextOfKin = nextOfKinRepository.findById(nextOfKin.getId()).block();
        updatedNextOfKin.name(UPDATED_NAME).contactInformation(UPDATED_CONTACT_INFORMATION).relationship(UPDATED_RELATIONSHIP);
        NextOfKinDTO nextOfKinDTO = nextOfKinMapper.toDto(updatedNextOfKin);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, nextOfKinDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nextOfKinDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NextOfKin in the database
        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeUpdate);
        NextOfKin testNextOfKin = nextOfKinList.get(nextOfKinList.size() - 1);
        assertThat(testNextOfKin.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNextOfKin.getContactInformation()).isEqualTo(UPDATED_CONTACT_INFORMATION);
        assertThat(testNextOfKin.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
    }

    @Test
    void putNonExistingNextOfKin() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKinRepository.findAll().collectList().block().size();
        nextOfKin.setId(count.incrementAndGet());

        // Create the NextOfKin
        NextOfKinDTO nextOfKinDTO = nextOfKinMapper.toDto(nextOfKin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, nextOfKinDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nextOfKinDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NextOfKin in the database
        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNextOfKin() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKinRepository.findAll().collectList().block().size();
        nextOfKin.setId(count.incrementAndGet());

        // Create the NextOfKin
        NextOfKinDTO nextOfKinDTO = nextOfKinMapper.toDto(nextOfKin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nextOfKinDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NextOfKin in the database
        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNextOfKin() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKinRepository.findAll().collectList().block().size();
        nextOfKin.setId(count.incrementAndGet());

        // Create the NextOfKin
        NextOfKinDTO nextOfKinDTO = nextOfKinMapper.toDto(nextOfKin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nextOfKinDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NextOfKin in the database
        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNextOfKinWithPatch() throws Exception {
        // Initialize the database
        nextOfKinRepository.save(nextOfKin).block();

        int databaseSizeBeforeUpdate = nextOfKinRepository.findAll().collectList().block().size();

        // Update the nextOfKin using partial update
        NextOfKin partialUpdatedNextOfKin = new NextOfKin();
        partialUpdatedNextOfKin.setId(nextOfKin.getId());

        partialUpdatedNextOfKin.contactInformation(UPDATED_CONTACT_INFORMATION).relationship(UPDATED_RELATIONSHIP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNextOfKin.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNextOfKin))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NextOfKin in the database
        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeUpdate);
        NextOfKin testNextOfKin = nextOfKinList.get(nextOfKinList.size() - 1);
        assertThat(testNextOfKin.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNextOfKin.getContactInformation()).isEqualTo(UPDATED_CONTACT_INFORMATION);
        assertThat(testNextOfKin.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
    }

    @Test
    void fullUpdateNextOfKinWithPatch() throws Exception {
        // Initialize the database
        nextOfKinRepository.save(nextOfKin).block();

        int databaseSizeBeforeUpdate = nextOfKinRepository.findAll().collectList().block().size();

        // Update the nextOfKin using partial update
        NextOfKin partialUpdatedNextOfKin = new NextOfKin();
        partialUpdatedNextOfKin.setId(nextOfKin.getId());

        partialUpdatedNextOfKin.name(UPDATED_NAME).contactInformation(UPDATED_CONTACT_INFORMATION).relationship(UPDATED_RELATIONSHIP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNextOfKin.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNextOfKin))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NextOfKin in the database
        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeUpdate);
        NextOfKin testNextOfKin = nextOfKinList.get(nextOfKinList.size() - 1);
        assertThat(testNextOfKin.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNextOfKin.getContactInformation()).isEqualTo(UPDATED_CONTACT_INFORMATION);
        assertThat(testNextOfKin.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
    }

    @Test
    void patchNonExistingNextOfKin() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKinRepository.findAll().collectList().block().size();
        nextOfKin.setId(count.incrementAndGet());

        // Create the NextOfKin
        NextOfKinDTO nextOfKinDTO = nextOfKinMapper.toDto(nextOfKin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, nextOfKinDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(nextOfKinDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NextOfKin in the database
        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNextOfKin() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKinRepository.findAll().collectList().block().size();
        nextOfKin.setId(count.incrementAndGet());

        // Create the NextOfKin
        NextOfKinDTO nextOfKinDTO = nextOfKinMapper.toDto(nextOfKin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(nextOfKinDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NextOfKin in the database
        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNextOfKin() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKinRepository.findAll().collectList().block().size();
        nextOfKin.setId(count.incrementAndGet());

        // Create the NextOfKin
        NextOfKinDTO nextOfKinDTO = nextOfKinMapper.toDto(nextOfKin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(nextOfKinDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NextOfKin in the database
        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNextOfKin() {
        // Initialize the database
        nextOfKinRepository.save(nextOfKin).block();

        int databaseSizeBeforeDelete = nextOfKinRepository.findAll().collectList().block().size();

        // Delete the nextOfKin
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, nextOfKin.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<NextOfKin> nextOfKinList = nextOfKinRepository.findAll().collectList().block();
        assertThat(nextOfKinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
