package com.sohclick.hrps.payroll.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.sohclick.hrps.payroll.IntegrationTest;
import com.sohclick.hrps.payroll.domain.Employee;
import com.sohclick.hrps.payroll.domain.enumeration.EmploymentStatusType;
import com.sohclick.hrps.payroll.domain.enumeration.Gender;
import com.sohclick.hrps.payroll.domain.enumeration.MaritalStatusType;
import com.sohclick.hrps.payroll.repository.EmployeeRepository;
import com.sohclick.hrps.payroll.service.dto.EmployeeDTO;
import com.sohclick.hrps.payroll.service.mapper.EmployeeMapper;
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
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EmployeeResourceIT {

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final MaritalStatusType DEFAULT_MARITAL_STATUS = MaritalStatusType.SINGLE;
    private static final MaritalStatusType UPDATED_MARITAL_STATUS = MaritalStatusType.MARRIED;

    private static final String DEFAULT_RESIDENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_RESIDENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_RESIDENT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_RESIDENT_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_RESIDENT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_RESIDENT_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_LGA = "AAAAAAAAAA";
    private static final String UPDATED_LGA = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_BVN = "AAAAAAAAAA";
    private static final String UPDATED_BVN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_EMPLOYED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EMPLOYED = LocalDate.now(ZoneId.systemDefault());

    private static final EmploymentStatusType DEFAULT_EMPLOYMENT_STATUS = EmploymentStatusType.FULL_TIME;
    private static final EmploymentStatusType UPDATED_EMPLOYMENT_STATUS = EmploymentStatusType.PART_TIME;

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity() {
        Employee employee = new Employee()
            .surname(DEFAULT_SURNAME)
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .gender(DEFAULT_GENDER)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .residentAddress(DEFAULT_RESIDENT_ADDRESS)
            .residentCountry(DEFAULT_RESIDENT_COUNTRY)
            .residentCity(DEFAULT_RESIDENT_CITY)
            .nationality(DEFAULT_NATIONALITY)
            .state(DEFAULT_STATE)
            .lga(DEFAULT_LGA)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .bvn(DEFAULT_BVN)
            .dateEmployed(DEFAULT_DATE_EMPLOYED)
            .employmentStatus(DEFAULT_EMPLOYMENT_STATUS)
            .department(DEFAULT_DEPARTMENT)
            .jobTitle(DEFAULT_JOB_TITLE);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity() {
        Employee employee = new Employee()
            .surname(UPDATED_SURNAME)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .residentAddress(UPDATED_RESIDENT_ADDRESS)
            .residentCountry(UPDATED_RESIDENT_COUNTRY)
            .residentCity(UPDATED_RESIDENT_CITY)
            .nationality(UPDATED_NATIONALITY)
            .state(UPDATED_STATE)
            .lga(UPDATED_LGA)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .bvn(UPDATED_BVN)
            .dateEmployed(UPDATED_DATE_EMPLOYED)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .department(UPDATED_DEPARTMENT)
            .jobTitle(UPDATED_JOB_TITLE);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employeeRepository.deleteAll().block();
        employee = createEntity();
    }

    @Test
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().collectList().block().size();
        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testEmployee.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testEmployee.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testEmployee.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testEmployee.getResidentAddress()).isEqualTo(DEFAULT_RESIDENT_ADDRESS);
        assertThat(testEmployee.getResidentCountry()).isEqualTo(DEFAULT_RESIDENT_COUNTRY);
        assertThat(testEmployee.getResidentCity()).isEqualTo(DEFAULT_RESIDENT_CITY);
        assertThat(testEmployee.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
        assertThat(testEmployee.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testEmployee.getLga()).isEqualTo(DEFAULT_LGA);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getBvn()).isEqualTo(DEFAULT_BVN);
        assertThat(testEmployee.getDateEmployed()).isEqualTo(DEFAULT_DATE_EMPLOYED);
        assertThat(testEmployee.getEmploymentStatus()).isEqualTo(DEFAULT_EMPLOYMENT_STATUS);
        assertThat(testEmployee.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testEmployee.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
    }

    @Test
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        int databaseSizeBeforeCreate = employeeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setSurname(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setFirstName(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setDateOfBirth(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setGender(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMaritalStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setMaritalStatus(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkResidentAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setResidentAddress(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkResidentCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setResidentCountry(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkResidentCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setResidentCity(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNationalityIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setNationality(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setState(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLgaIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().collectList().block().size();
        // set the field null
        employee.setLga(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEmployees() {
        // Initialize the database
        employeeRepository.save(employee).block();

        // Get all the employeeList
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
            .value(hasItem(employee.getId().intValue()))
            .jsonPath("$.[*].surname")
            .value(hasItem(DEFAULT_SURNAME))
            .jsonPath("$.[*].firstName")
            .value(hasItem(DEFAULT_FIRST_NAME))
            .jsonPath("$.[*].middleName")
            .value(hasItem(DEFAULT_MIDDLE_NAME))
            .jsonPath("$.[*].dateOfBirth")
            .value(hasItem(DEFAULT_DATE_OF_BIRTH.toString()))
            .jsonPath("$.[*].gender")
            .value(hasItem(DEFAULT_GENDER.toString()))
            .jsonPath("$.[*].maritalStatus")
            .value(hasItem(DEFAULT_MARITAL_STATUS.toString()))
            .jsonPath("$.[*].residentAddress")
            .value(hasItem(DEFAULT_RESIDENT_ADDRESS))
            .jsonPath("$.[*].residentCountry")
            .value(hasItem(DEFAULT_RESIDENT_COUNTRY))
            .jsonPath("$.[*].residentCity")
            .value(hasItem(DEFAULT_RESIDENT_CITY))
            .jsonPath("$.[*].nationality")
            .value(hasItem(DEFAULT_NATIONALITY))
            .jsonPath("$.[*].state")
            .value(hasItem(DEFAULT_STATE))
            .jsonPath("$.[*].lga")
            .value(hasItem(DEFAULT_LGA))
            .jsonPath("$.[*].phoneNumber")
            .value(hasItem(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].bvn")
            .value(hasItem(DEFAULT_BVN))
            .jsonPath("$.[*].dateEmployed")
            .value(hasItem(DEFAULT_DATE_EMPLOYED.toString()))
            .jsonPath("$.[*].employmentStatus")
            .value(hasItem(DEFAULT_EMPLOYMENT_STATUS.toString()))
            .jsonPath("$.[*].department")
            .value(hasItem(DEFAULT_DEPARTMENT))
            .jsonPath("$.[*].jobTitle")
            .value(hasItem(DEFAULT_JOB_TITLE));
    }

    @Test
    void getEmployee() {
        // Initialize the database
        employeeRepository.save(employee).block();

        // Get the employee
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, employee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(employee.getId().intValue()))
            .jsonPath("$.surname")
            .value(is(DEFAULT_SURNAME))
            .jsonPath("$.firstName")
            .value(is(DEFAULT_FIRST_NAME))
            .jsonPath("$.middleName")
            .value(is(DEFAULT_MIDDLE_NAME))
            .jsonPath("$.dateOfBirth")
            .value(is(DEFAULT_DATE_OF_BIRTH.toString()))
            .jsonPath("$.gender")
            .value(is(DEFAULT_GENDER.toString()))
            .jsonPath("$.maritalStatus")
            .value(is(DEFAULT_MARITAL_STATUS.toString()))
            .jsonPath("$.residentAddress")
            .value(is(DEFAULT_RESIDENT_ADDRESS))
            .jsonPath("$.residentCountry")
            .value(is(DEFAULT_RESIDENT_COUNTRY))
            .jsonPath("$.residentCity")
            .value(is(DEFAULT_RESIDENT_CITY))
            .jsonPath("$.nationality")
            .value(is(DEFAULT_NATIONALITY))
            .jsonPath("$.state")
            .value(is(DEFAULT_STATE))
            .jsonPath("$.lga")
            .value(is(DEFAULT_LGA))
            .jsonPath("$.phoneNumber")
            .value(is(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.bvn")
            .value(is(DEFAULT_BVN))
            .jsonPath("$.dateEmployed")
            .value(is(DEFAULT_DATE_EMPLOYED.toString()))
            .jsonPath("$.employmentStatus")
            .value(is(DEFAULT_EMPLOYMENT_STATUS.toString()))
            .jsonPath("$.department")
            .value(is(DEFAULT_DEPARTMENT))
            .jsonPath("$.jobTitle")
            .value(is(DEFAULT_JOB_TITLE));
    }

    @Test
    void getNonExistingEmployee() {
        // Get the employee
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).block();
        updatedEmployee
            .surname(UPDATED_SURNAME)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .residentAddress(UPDATED_RESIDENT_ADDRESS)
            .residentCountry(UPDATED_RESIDENT_COUNTRY)
            .residentCity(UPDATED_RESIDENT_CITY)
            .nationality(UPDATED_NATIONALITY)
            .state(UPDATED_STATE)
            .lga(UPDATED_LGA)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .bvn(UPDATED_BVN)
            .dateEmployed(UPDATED_DATE_EMPLOYED)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .department(UPDATED_DEPARTMENT)
            .jobTitle(UPDATED_JOB_TITLE);
        EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, employeeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testEmployee.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testEmployee.getResidentAddress()).isEqualTo(UPDATED_RESIDENT_ADDRESS);
        assertThat(testEmployee.getResidentCountry()).isEqualTo(UPDATED_RESIDENT_COUNTRY);
        assertThat(testEmployee.getResidentCity()).isEqualTo(UPDATED_RESIDENT_CITY);
        assertThat(testEmployee.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testEmployee.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testEmployee.getLga()).isEqualTo(UPDATED_LGA);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getBvn()).isEqualTo(UPDATED_BVN);
        assertThat(testEmployee.getDateEmployed()).isEqualTo(UPDATED_DATE_EMPLOYED);
        assertThat(testEmployee.getEmploymentStatus()).isEqualTo(UPDATED_EMPLOYMENT_STATUS);
        assertThat(testEmployee.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testEmployee.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
    }

    @Test
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, employeeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .surname(UPDATED_SURNAME)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .residentCountry(UPDATED_RESIDENT_COUNTRY)
            .residentCity(UPDATED_RESIDENT_CITY)
            .state(UPDATED_STATE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testEmployee.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testEmployee.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testEmployee.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testEmployee.getResidentAddress()).isEqualTo(DEFAULT_RESIDENT_ADDRESS);
        assertThat(testEmployee.getResidentCountry()).isEqualTo(UPDATED_RESIDENT_COUNTRY);
        assertThat(testEmployee.getResidentCity()).isEqualTo(UPDATED_RESIDENT_CITY);
        assertThat(testEmployee.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
        assertThat(testEmployee.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testEmployee.getLga()).isEqualTo(DEFAULT_LGA);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getBvn()).isEqualTo(DEFAULT_BVN);
        assertThat(testEmployee.getDateEmployed()).isEqualTo(DEFAULT_DATE_EMPLOYED);
        assertThat(testEmployee.getEmploymentStatus()).isEqualTo(UPDATED_EMPLOYMENT_STATUS);
        assertThat(testEmployee.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testEmployee.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
    }

    @Test
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .surname(UPDATED_SURNAME)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .residentAddress(UPDATED_RESIDENT_ADDRESS)
            .residentCountry(UPDATED_RESIDENT_COUNTRY)
            .residentCity(UPDATED_RESIDENT_CITY)
            .nationality(UPDATED_NATIONALITY)
            .state(UPDATED_STATE)
            .lga(UPDATED_LGA)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .bvn(UPDATED_BVN)
            .dateEmployed(UPDATED_DATE_EMPLOYED)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .department(UPDATED_DEPARTMENT)
            .jobTitle(UPDATED_JOB_TITLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testEmployee.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testEmployee.getResidentAddress()).isEqualTo(UPDATED_RESIDENT_ADDRESS);
        assertThat(testEmployee.getResidentCountry()).isEqualTo(UPDATED_RESIDENT_COUNTRY);
        assertThat(testEmployee.getResidentCity()).isEqualTo(UPDATED_RESIDENT_CITY);
        assertThat(testEmployee.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testEmployee.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testEmployee.getLga()).isEqualTo(UPDATED_LGA);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getBvn()).isEqualTo(UPDATED_BVN);
        assertThat(testEmployee.getDateEmployed()).isEqualTo(UPDATED_DATE_EMPLOYED);
        assertThat(testEmployee.getEmploymentStatus()).isEqualTo(UPDATED_EMPLOYMENT_STATUS);
        assertThat(testEmployee.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testEmployee.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
    }

    @Test
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, employeeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().collectList().block().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(employeeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEmployee() {
        // Initialize the database
        employeeRepository.save(employee).block();

        int databaseSizeBeforeDelete = employeeRepository.findAll().collectList().block().size();

        // Delete the employee
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, employee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll().collectList().block();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
