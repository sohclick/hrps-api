package com.sohclick.hrps.payroll.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.payroll.IntegrationTest;
import com.sohclick.hrps.payroll.domain.Report;
import com.sohclick.hrps.payroll.repository.ReportRepository;
import com.sohclick.hrps.payroll.service.dto.ReportDTO;
import com.sohclick.hrps.payroll.service.mapper.ReportMapper;
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
 * Integration tests for the {@link ReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReportResourceIT {

    private static final Boolean DEFAULT_PAYROLL_SUMMARY_REPORTS = false;
    private static final Boolean UPDATED_PAYROLL_SUMMARY_REPORTS = true;

    private static final Boolean DEFAULT_PAYSLIPS = false;
    private static final Boolean UPDATED_PAYSLIPS = true;

    private static final Boolean DEFAULT_TAX_REPORTS = false;
    private static final Boolean UPDATED_TAX_REPORTS = true;

    private static final Boolean DEFAULT_YEAR_END_REPORTS = false;
    private static final Boolean UPDATED_YEAR_END_REPORTS = true;

    private static final String ENTITY_API_URL = "/api/reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Report report;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createEntity() {
        Report report = new Report()
            .payrollSummaryReports(DEFAULT_PAYROLL_SUMMARY_REPORTS)
            .payslips(DEFAULT_PAYSLIPS)
            .taxReports(DEFAULT_TAX_REPORTS)
            .yearEndReports(DEFAULT_YEAR_END_REPORTS);
        return report;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createUpdatedEntity() {
        Report report = new Report()
            .payrollSummaryReports(UPDATED_PAYROLL_SUMMARY_REPORTS)
            .payslips(UPDATED_PAYSLIPS)
            .taxReports(UPDATED_TAX_REPORTS)
            .yearEndReports(UPDATED_YEAR_END_REPORTS);
        return report;
    }

    @BeforeEach
    public void initTest() {
        reportRepository.deleteAll().block();
        report = createEntity();
    }

    @Test
    void createReport() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().collectList().block().size();
        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll().collectList().block();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate + 1);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getPayrollSummaryReports()).isEqualTo(DEFAULT_PAYROLL_SUMMARY_REPORTS);
        assertThat(testReport.getPayslips()).isEqualTo(DEFAULT_PAYSLIPS);
        assertThat(testReport.getTaxReports()).isEqualTo(DEFAULT_TAX_REPORTS);
        assertThat(testReport.getYearEndReports()).isEqualTo(DEFAULT_YEAR_END_REPORTS);
    }

    @Test
    void createReportWithExistingId() throws Exception {
        // Create the Report with an existing ID
        report.setId(1L);
        ReportDTO reportDTO = reportMapper.toDto(report);

        int databaseSizeBeforeCreate = reportRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll().collectList().block();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllReports() {
        // Initialize the database
        reportRepository.save(report).block();

        // Get all the reportList
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
            .value(hasItem(report.getId().intValue()))
            .jsonPath("$.[*].payrollSummaryReports")
            .value(hasItem(DEFAULT_PAYROLL_SUMMARY_REPORTS.booleanValue()))
            .jsonPath("$.[*].payslips")
            .value(hasItem(DEFAULT_PAYSLIPS.booleanValue()))
            .jsonPath("$.[*].taxReports")
            .value(hasItem(DEFAULT_TAX_REPORTS.booleanValue()))
            .jsonPath("$.[*].yearEndReports")
            .value(hasItem(DEFAULT_YEAR_END_REPORTS.booleanValue()));
    }

    @Test
    void getReport() {
        // Initialize the database
        reportRepository.save(report).block();

        // Get the report
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, report.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(report.getId().intValue()))
            .jsonPath("$.payrollSummaryReports")
            .value(is(DEFAULT_PAYROLL_SUMMARY_REPORTS.booleanValue()))
            .jsonPath("$.payslips")
            .value(is(DEFAULT_PAYSLIPS.booleanValue()))
            .jsonPath("$.taxReports")
            .value(is(DEFAULT_TAX_REPORTS.booleanValue()))
            .jsonPath("$.yearEndReports")
            .value(is(DEFAULT_YEAR_END_REPORTS.booleanValue()));
    }

    @Test
    void getNonExistingReport() {
        // Get the report
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReport() throws Exception {
        // Initialize the database
        reportRepository.save(report).block();

        int databaseSizeBeforeUpdate = reportRepository.findAll().collectList().block().size();

        // Update the report
        Report updatedReport = reportRepository.findById(report.getId()).block();
        updatedReport
            .payrollSummaryReports(UPDATED_PAYROLL_SUMMARY_REPORTS)
            .payslips(UPDATED_PAYSLIPS)
            .taxReports(UPDATED_TAX_REPORTS)
            .yearEndReports(UPDATED_YEAR_END_REPORTS);
        ReportDTO reportDTO = reportMapper.toDto(updatedReport);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll().collectList().block();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getPayrollSummaryReports()).isEqualTo(UPDATED_PAYROLL_SUMMARY_REPORTS);
        assertThat(testReport.getPayslips()).isEqualTo(UPDATED_PAYSLIPS);
        assertThat(testReport.getTaxReports()).isEqualTo(UPDATED_TAX_REPORTS);
        assertThat(testReport.getYearEndReports()).isEqualTo(UPDATED_YEAR_END_REPORTS);
    }

    @Test
    void putNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().collectList().block().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll().collectList().block();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().collectList().block().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll().collectList().block();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().collectList().block().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll().collectList().block();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.save(report).block();

        int databaseSizeBeforeUpdate = reportRepository.findAll().collectList().block().size();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport.payrollSummaryReports(UPDATED_PAYROLL_SUMMARY_REPORTS).yearEndReports(UPDATED_YEAR_END_REPORTS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll().collectList().block();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getPayrollSummaryReports()).isEqualTo(UPDATED_PAYROLL_SUMMARY_REPORTS);
        assertThat(testReport.getPayslips()).isEqualTo(DEFAULT_PAYSLIPS);
        assertThat(testReport.getTaxReports()).isEqualTo(DEFAULT_TAX_REPORTS);
        assertThat(testReport.getYearEndReports()).isEqualTo(UPDATED_YEAR_END_REPORTS);
    }

    @Test
    void fullUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.save(report).block();

        int databaseSizeBeforeUpdate = reportRepository.findAll().collectList().block().size();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport
            .payrollSummaryReports(UPDATED_PAYROLL_SUMMARY_REPORTS)
            .payslips(UPDATED_PAYSLIPS)
            .taxReports(UPDATED_TAX_REPORTS)
            .yearEndReports(UPDATED_YEAR_END_REPORTS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll().collectList().block();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getPayrollSummaryReports()).isEqualTo(UPDATED_PAYROLL_SUMMARY_REPORTS);
        assertThat(testReport.getPayslips()).isEqualTo(UPDATED_PAYSLIPS);
        assertThat(testReport.getTaxReports()).isEqualTo(UPDATED_TAX_REPORTS);
        assertThat(testReport.getYearEndReports()).isEqualTo(UPDATED_YEAR_END_REPORTS);
    }

    @Test
    void patchNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().collectList().block().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reportDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll().collectList().block();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().collectList().block().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll().collectList().block();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().collectList().block().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll().collectList().block();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReport() {
        // Initialize the database
        reportRepository.save(report).block();

        int databaseSizeBeforeDelete = reportRepository.findAll().collectList().block().size();

        // Delete the report
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, report.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Report> reportList = reportRepository.findAll().collectList().block();
        assertThat(reportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
