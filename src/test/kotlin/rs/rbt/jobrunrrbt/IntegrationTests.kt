import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.util.UriComponentsBuilder
import rs.rbt.jobrunrrbt.JobrunrrbtApplication
import rs.rbt.jobrunrrbt.dto.JobArgumentsDTO
import rs.rbt.jobrunrrbt.dto.JobDTO
import rs.rbt.jobrunrrbt.dto.JobSignatureDTO
import rs.rbt.jobrunrrbt.dto.UpdateJobReceivedDTO
import rs.rbt.jobrunrrbt.exception.IllegalJobState
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.model.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.sql.DataSource


@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(classes = [JobrunrrbtApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class MyIntegrationTest {


    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var jdbcTemplateObject: JdbcTemplate

    @LocalServerPort
    var port: Int = 0

    @BeforeAll
    fun configureFlyway() {
        Flyway.configure()
            .dataSource(dataSource)
            .baselineOnMigrate(true)
            .validateOnMigrate(true)
            .load()
    }

    @BeforeEach
    fun resetData() {
        jdbcTemplateObject.update("delete from jobrunr_jobs")
        populateJobrunrJobs()
    }

    @Test
    fun `unique-signatures endpoint`() {
        val response =
            restTemplate.getForObject("http://localhost:${port}/api/v1/jobs/unique-signatures", List::class.java)
        val data =
            listOf("java.lang.System.out.print(java.lang.String)", "java.lang.System.out.println(java.lang.String)")
        assertThat(response.size).isEqualTo(2)
        assertThat(response).isEqualTo(data)
    }

    @Test
    fun `search by state with correct parameter`() {
        val response = restTemplate.getForObject("http://localhost:${port}/api/v1/jobs/SUCCEEDED", JobDTO::class.java)
        assertThat(response).isEqualTo(singleStateJobDTO)
    }

    @Test
    fun `search by state with incorrect parameter`() {
        val response =
            restTemplate.getForObject("http://localhost:${port}/api/v1/jobs/ILLEGAL-STATE", IllegalJobState::class.java)
        assertThat(response.message).isEqualTo("No enum constant rs.rbt.jobrunrrbt.model.JobState.ILLEGAL-STATE")
    }

    @Test
    fun `search by state and class`() {

        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/SUCCEEDED")
            .queryParam("offset", "0")
            .queryParam("limit", 10)
            .queryParam("order", "updatedAt:DESC")
            .queryParam("parameter", "Class")
            .queryParam("value", "System")
            .build()
            .toUri()
        val response = restTemplate.getForObject(uri, String::class.java)

        assertThat(response).isEqualTo(serialize(singleStateJobDTO))
    }

    @Test
    fun `search by state and class that does not exist`() {

        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/SUCCEEDED")
            .queryParam("offset", "0")
            .queryParam("limit", 10)
            .queryParam("order", "updatedAt:DESC")
            .queryParam("searchParameter", "Class")
            .queryParam("searchValue", "WrongClassName")
            .build()
            .toUri()
        val response = restTemplate.getForObject(uri, String::class.java)

        assertThat(response).isEqualTo(serialize(singleStateDTOWhenParameterIsWrong))
    }

    @Test
    fun `search by state and method`() {

        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/SUCCEEDED")
            .queryParam("offset", "0")
            .queryParam("limit", 10)
            .queryParam("order", "updatedAt:DESC")
            .queryParam("searchParameter", "Method")
            .queryParam("searchValue", "print")
            .build()
            .toUri()
        val response = restTemplate.getForObject(uri, String::class.java)

        assertThat(response).isEqualTo(serialize(singleStateJobDTO))
    }

    @Test
    fun `search by state and method that does not exist`() {

        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/SUCCEEDED")
            .queryParam("offset", "0")
            .queryParam("limit", 10)
            .queryParam("order", "updatedAt:DESC")
            .queryParam("searchParameter", "Method")
            .queryParam("searchValue", "doesntexist")
            .build()
            .toUri()
        val response = restTemplate.getForObject(uri, String::class.java)

        assertThat(response).isEqualTo(serialize(singleStateDTOWhenParameterIsWrong))
    }

    @Test
    fun `search by state and both class and method`() {

        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/SUCCEEDED")
            .queryParam("offset", "0")
            .queryParam("limit", 10)
            .queryParam("order", "updatedAt:DESC")
            .queryParam("searchParameter", "ANYTHING")
            .queryParam("searchValue", "print")
            .build()
            .toUri()
        val response = restTemplate.getForObject(uri, String::class.java)

        assertThat(response).isEqualTo(serialize(singleStateJobDTO))
    }

    @Test
    fun `search by state and class where method does not exist`() {

        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/SUCCEEDED")
            .queryParam("offset", "0")
            .queryParam("limit", 10)
            .queryParam("order", "updatedAt:DESC")
            .queryParam("searchParameter", "ANYTHING")
            .queryParam("searchValue", "doesntexist")
            .build()
            .toUri()
        val response = restTemplate.getForObject(uri, String::class.java)

        assertThat(response).isEqualTo(serialize(singleStateDTOWhenParameterIsWrong))
    }

    @Test
    fun `update job using id, package name, class name and method name`() {
        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/${jobJsonPrintJob1.id}")
            .build()
            .toUri()
        val response =
            restTemplate.exchange(uri, HttpMethod.PUT, HttpEntity(jobChangeRequestWithoutTimeDTO), String::class.java)

        assertThat(response.body).isEqualTo(jobJsonPrintJob1.id)

        val queriedJobSignature = jdbcTemplateObject.queryForObject(
            "SELECT jobSignature FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJsonPrintJob1.id),
            String::class.java
        )

        assertThat(queriedJobSignature).isEqualTo("package.Class.method(java.lang.String)")

        val queriedJobJson = jdbcTemplateObject.queryForObject(
            "SELECT jobAsJson FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJsonPrintJob1.id),
            String::class.java
        )

        assertThat(deserialize(queriedJobJson)).isEqualTo(updatedJobJsonWithoutTime)
    }

    @Test
    fun `update job using id, package name, class name and method name when id does not exist`() {
        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/wrong-id")
            .build()
            .toUri()
        val response =
            restTemplate.exchange(uri, HttpMethod.PUT, HttpEntity(jobChangeRequestWithoutTimeDTO), String::class.java)

        assertThat(
            ObjectMapper().readTree(response.body).get("message").asText()
        ).isEqualTo("Job with ID: wrong-id not found")
    }

    @Test
    fun `update job using id, package name, class name and method name when state is incorrect`() {
        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/${jobJsonPrintlnJob5.id}")
            .build()
            .toUri()
        val response =
            restTemplate.exchange(uri, HttpMethod.PUT, HttpEntity(jobChangeRequestWithoutTimeDTO), String::class.java)

        assertThat(
            ObjectMapper().readTree(response.body).get("message").asText()
        ).isEqualTo("Jobs that are enqueued or processing can not be modified")
    }

    @Test
    fun `update job using id, package name, class name, method name and time`() {
        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/${jobJsonPrintlnJob4.id}")
            .build()
            .toUri()
        val response =
            restTemplate.exchange(uri, HttpMethod.PUT, HttpEntity(jobChangeRequestWithTimeDTO), String::class.java)

        assertThat(response.body).isEqualTo(jobJsonPrintlnJob4.id)


        val queriedJobSignature = jdbcTemplateObject.queryForObject(
            "SELECT jobSignature FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJsonPrintlnJob4.id),
            String::class.java
        )

        assertThat(queriedJobSignature).isEqualTo("package.Class.method(java.lang.String)")


        val queriedScheduledAt = jdbcTemplateObject.queryForObject(
            "SELECT scheduledAt FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJsonPrintlnJob4.id),
            LocalDateTime::class.java
        )

        assertThat(queriedScheduledAt).isEqualTo(
            convertToLocalDateTime(
                OffsetDateTime.of(2023, 4, 25, 17, 3, 40, 113, ZoneOffset.UTC)
            )
        )

    }

    @Test
    fun `schedule list of jobs using jobsignature, parameters of given job, and time when jobsignature is unique`() {
        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs")
            .build()
            .toUri()
        val response = restTemplate.exchange(
            uri,
            HttpMethod.POST,
            HttpEntity(listOf(jobSignatureThatDoesntOverride)),
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val queriedJobSignature = jdbcTemplateObject.queryForObject(
            "SELECT jobsignature FROM jobrunr_jobs WHERE jobsignature = ?",
            arrayOf("com.example.Class.path.to.method(java.lang.String,java.lang.String)"),
            String::class.java
        )

        assertThat(queriedJobSignature).isEqualTo("com.example.Class.path.to.method(java.lang.String,java.lang.String)")

        val queriedScheduledAt = jdbcTemplateObject.queryForObject(
            "SELECT scheduledAt FROM jobrunr_jobs WHERE jobSignature = ?",
            arrayOf("com.example.Class.path.to.method(java.lang.String,java.lang.String)"),
            LocalDateTime::class.java
        )

        assertThat(queriedScheduledAt).isEqualTo(
            convertToLocalDateTime(
                OffsetDateTime.of(2023, 5, 25, 17, 3, 40, 113, ZoneOffset.UTC)
            )
        )

        val queriedJobJson = jdbcTemplateObject.queryForObject(
            "SELECT jobAsJson FROM jobrunr_jobs WHERE jobSignature = ?",
            arrayOf("com.example.Class.path.to.method(java.lang.String,java.lang.String)"),
            String::class.java
        )

        assertThat(deserialize(queriedJobJson).jobDetails.jobParameters[0].jobObject).isEqualTo(
            jobArgumentsForJobSignatureThatDoesntOverride[0].argData
        )
        assertThat(deserialize(queriedJobJson).jobDetails.jobParameters[1].jobObject).isEqualTo(
            jobArgumentsForJobSignatureThatDoesntOverride[1].argData
        )

    }

    @Test
    fun `schedule list of jobs using jobsignature, parameters of given job, and time when jobsignature is not unique`() {
        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs")
            .build()
            .toUri()
        val response = restTemplate.exchange(
            uri,
            HttpMethod.POST,
            HttpEntity(listOf(jobSignatureThatOverrides)),
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val queriedJobSignature = jdbcTemplateObject.queryForObject(
            "SELECT jobsignature FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJsonPrintlnJob4.id),
            String::class.java
        )

        assertThat(queriedJobSignature).isEqualTo("java.lang.System.out.println(java.lang.String)")

        val queriedScheduledAt = jdbcTemplateObject.queryForObject(
            "SELECT scheduledAt FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJsonPrintlnJob4.id),
            LocalDateTime::class.java
        )

        assertThat(queriedScheduledAt).isCloseTo(
            convertToLocalDateTime(
                OffsetDateTime.of(2023, 4, 24, 8, 7, 28, 361810, ZoneOffset.UTC)
            ), within(1, ChronoUnit.SECONDS)
        )

        val queriedJobJson = jdbcTemplateObject.queryForObject(
            "SELECT jobAsJson FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJsonPrintlnJob4.id),
            String::class.java
        )

        assertThat(deserialize(queriedJobJson).jobDetails.jobParameters[0].jobObject).isEqualTo(
            jobArgumentsForJobSignatureThatOverrides[0].argData
        )

    }


    final val jobJsonPrintJob1 = JobJson(
        version = 1,
        jobSignature = "java.lang.System.out.print(java.lang.String)",
        jobName = "java.lang.System.out.print(job1)",
        amountOfRetries = null,
        labels = listOf(),
        jobDetails = JobDetails(
            className = "java.lang.System",
            staticFieldName = "out",
            methodName = "print",
            jobParameters = arrayListOf(
                JobParameters(
                    className = "java.lang.String",
                    actualClassName = "java.lang.String",
                    jobObject = "job1"
                )
            ),
            cacheable = true
        ),
        id = "13c47255-3c06-44dd-9b1a-5421641d26ee",
        jobHistory = arrayListOf(),
        metadata = null
    )

    final val jobJsonPrintJob2 = JobJson(
        version = 1,
        jobSignature = "java.lang.System.out.print(java.lang.String)",
        jobName = "java.lang.System.out.print(job2)",
        amountOfRetries = null,
        labels = listOf(),
        jobDetails = JobDetails(
            className = "java.lang.System",
            staticFieldName = "out",
            methodName = "print",
            jobParameters = arrayListOf(
                JobParameters(
                    className = "java.lang.String",
                    actualClassName = "java.lang.String",
                    jobObject = "job2"
                )
            ),
            cacheable = true
        ),
        id = "23c47255-3c06-44dd-9b1a-5421641d26ee",
        jobHistory = arrayListOf(),
        metadata = null
    )

    final val jobJsonPrintlnJob3 = JobJson(
        version = 1,
        jobSignature = "java.lang.System.out.println(java.lang.String)",
        jobName = "java.lang.System.out.println(job3)",
        amountOfRetries = null,
        labels = listOf(),
        jobDetails = JobDetails(
            className = "java.lang.System",
            staticFieldName = "out",
            methodName = "println",
            jobParameters = arrayListOf(
                JobParameters(
                    className = "java.lang.String",
                    actualClassName = "java.lang.String",
                    jobObject = "job3"
                )
            ),
            cacheable = true
        ),
        id = "33c47255-3c06-44dd-9b1a-5421641d26ee",
        jobHistory = arrayListOf(),
        metadata = null
    )

    final val jobJsonPrintlnJob4 = JobJson(
        version = 1,
        jobSignature = "java.lang.System.out.println(java.lang.String)",
        jobName = "java.lang.System.out.println(job4)",
        amountOfRetries = null,
        labels = listOf(),
        jobDetails = JobDetails(
            className = "java.lang.System",
            staticFieldName = "out",
            methodName = "println",
            jobParameters = arrayListOf(
                JobParameters(
                    className = "java.lang.String",
                    actualClassName = "java.lang.String",
                    jobObject = "job4"
                )
            ),
            cacheable = true
        ),
        id = "43c47255-3c06-44dd-9b1a-5421641d26ee",
        jobHistory = arrayListOf(
            JobHistory(
                atClass = DEFAULT_AT_CLASS,
                state = STATE_SCHEDULED,
                createdAt = LocalDateTime.now(ZoneId.of("UTC")),
                scheduledAt = OffsetDateTime.of(2023, 4, 25, 17, 3, 40, 113, ZoneOffset.UTC),
                recurringJobId = null,
                reason = null
            )
        ),
        metadata = null
    )

    final val jobJsonPrintlnJob5 = JobJson(
        version = 1,
        jobSignature = "java.lang.System.out.println(java.lang.String)",
        jobName = "java.lang.System.out.println(job5)",
        amountOfRetries = null,
        labels = listOf(),
        jobDetails = JobDetails(
            className = "java.lang.System",
            staticFieldName = "out",
            methodName = "println",
            jobParameters = arrayListOf(
                JobParameters(
                    className = "java.lang.String",
                    actualClassName = "java.lang.String",
                    jobObject = "job5"
                )
            ),
            cacheable = true
        ),
        id = "53c47255-3c06-44dd-9b1a-5421641d26ee",
        jobHistory = arrayListOf(),
        metadata = null
    )

    final val jobJsonPrintlnJob6 = JobJson(
        version = 1,
        jobSignature = "java.lang.System.out.println(java.lang.String)",
        jobName = "java.lang.System.out.print(job6)",
        amountOfRetries = null,
        labels = listOf(),
        jobDetails = JobDetails(
            className = "java.lang.System",
            staticFieldName = "out",
            methodName = "println",
            jobParameters = arrayListOf(
                JobParameters(
                    className = "java.lang.String",
                    actualClassName = "java.lang.String",
                    jobObject = "job6"
                )
            ),
            cacheable = true
        ),
        id = "63c47255-3c06-44dd-9b1a-5421641d26ee",
        jobHistory = arrayListOf(),
        metadata = null
    )

    final val serializedjobJsonPrintJob1 = serialize(jobJsonPrintJob1)
    final val serializedjobJsonPrintJob2 = serialize(jobJsonPrintJob2)
    final val serializedjobJsonPrintlnJob3 = serialize(jobJsonPrintlnJob3)
    final val serializedjobJsonPrintlnJob4 = serialize(jobJsonPrintlnJob4)
    final val serializedjobJsonPrintlnJob5 = serialize(jobJsonPrintlnJob5)
    final val serializedjobJsonPrintlnJob6 = serialize(jobJsonPrintlnJob6)


    final val singleStateJobDTO = JobDTO(
        currentPage = 0,
        hasNext = false,
        hasPrevious = false,
        items = listOf(jobJsonPrintJob1),
        limit = 10,
        offset = 0,
        total = 1,
        totalPages = 1
    )

    final val singleStateDTOWhenParameterIsWrong = JobDTO(
        currentPage = 0,
        hasNext = false,
        hasPrevious = false,
        items = listOf(),
        limit = 10,
        offset = 0,
        total = 0,
        totalPages = 1
    )

    final val jobChangeRequestWithoutTimeDTO = UpdateJobReceivedDTO(
        packageName = "package",
        className = "Class",
        methodName = "method",
        scheduledTime = null
    )

    final val jobChangeRequestWithTimeDTO = UpdateJobReceivedDTO(
        packageName = "package",
        className = "Class",
        methodName = "method",
        scheduledTime = OffsetDateTime.of(2023, 4, 25, 17, 3, 40, 113, ZoneOffset.UTC)

    )


    final val jobArgumentsForJobSignatureThatDoesntOverride = arrayOf(
        JobArgumentsDTO(
            argData = "jobArgumentsForJobSignatureMultipleItems data 1",
            argRow = 0
        ),
        JobArgumentsDTO(
            argData = "jobArgumentsForJobSignatureMultipleItems data 2",
            argRow = 1
        )
    )

    final val jobArgumentsForJobSignatureThatOverrides = arrayOf(
        JobArgumentsDTO(
            argData = "job4",
            argRow = 0
        )
    )

    final val jobSignatureThatDoesntOverride =
        JobSignatureDTO(
            jobSignature = "com.example.Class.path.to.method(java.lang.String,java.lang.String)",
            jobArguments = jobArgumentsForJobSignatureThatDoesntOverride,
            jobTime = OffsetDateTime.of(2023, 5, 25, 17, 3, 40, 113000, ZoneOffset.UTC)
        )

    final val jobSignatureThatOverrides = JobSignatureDTO(
        jobSignature = "java.lang.System.out.println(java,lang.String)",
        jobArguments = jobArgumentsForJobSignatureThatOverrides,
        jobTime = OffsetDateTime.of(2023, 5, 25, 17, 3, 40, 113000, ZoneOffset.UTC)

    )

    final val updatedJobJsonWithoutTime = JobJson(
        version = 1,
        jobSignature = "package.Class.method(java.lang.String)",
        jobName = "java.lang.System.out.print(job1)",
        amountOfRetries = null,
        labels = listOf(),
        jobDetails = JobDetails(
            className = "package.Class",
            staticFieldName = "",
            methodName = "method",
            jobParameters = arrayListOf(
                JobParameters(
                    className = "java.lang.String",
                    actualClassName = "java.lang.String",
                    jobObject = "job1"
                )
            ),
            cacheable = true
        ),
        id = "13c47255-3c06-44dd-9b1a-5421641d26ee",
        jobHistory = arrayListOf(),
        metadata = null
    )


    val listOfJobs = listOf(
        arrayOf("13c47255-3c06-44dd-9b1a-5421641d26ee",
            1,
            serializedjobJsonPrintJob1,
            "java.lang.System.out.print(java.lang.String)",
            "SUCCEEDED",
            "2023-04-24 08:07:28.361810",
            "2023-04-24 08:07:28.361810",
            null,
            null
        ),
        arrayOf("23c47255-3c06-44dd-9b1a-5421641d26ee",
            1,
            serializedjobJsonPrintJob2,
            "java.lang.System.out.print(java.lang.String)",
            "FAILED",
            "2023-04-24 08:07:28.361810",
            "2023-04-24 08:07:28.361810",
            null,
            null
        ),
        arrayOf("33c47255-3c06-44dd-9b1a-5421641d26ee",
            1,
            serializedjobJsonPrintlnJob3,
            "java.lang.System.out.println(java.lang.String)",
            "DELETED",
            "2023-04-24 08:07:28.361810",
            "2023-04-24 08:07:28.361810",
            null,
            null
        ),
        arrayOf("43c47255-3c06-44dd-9b1a-5421641d26ee",
            1,
            serializedjobJsonPrintlnJob4,
            "java.lang.System.out.println(java.lang.String)",
            "SCHEDULED",
            "2023-04-24 08:07:28.361810",
            "2023-04-24 08:07:28.361810",
            "2023-04-24 08:07:28.361810",
            null
        ),
        arrayOf("53c47255-3c06-44dd-9b1a-5421641d26ee",
            1,
            serializedjobJsonPrintlnJob5,
            "java.lang.System.out.println(java.lang.String)",
            "ENQUEUED",
            "2023-04-24 08:07:28.361810",
            "2023-04-24 08:07:28.361810",
            "2023-04-24 08:07:28.361810",
            null
        ),
        arrayOf("63c47255-3c06-44dd-9b1a-5421641d26ee",
            1,
            serializedjobJsonPrintlnJob6,
            "java.lang.System.out.println(java.lang.String)",
            "PROCESSING",
            "2023-04-24 08:07:28.361810",
            "2023-04-24 08:07:28.361810",
            "2023-04-24 08:07:28.361810",
            null
        )
    )


    private fun populateJobrunrJobs() {

        for (job in listOfJobs) {
            jdbcTemplateObject.update(
                "INSERT INTO jobrunr_jobs (id, version, jobasjson, jobsignature, state, createdat, updatedat, scheduledat, recurringjobid) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                *job
            )
        }
    }

    private fun convertToLocalDateTime(timeToConvert: OffsetDateTime): LocalDateTime {

        return LocalDateTime.parse(
            timeToConvert.atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(PATTERN_SSZ)),
            DateTimeFormatter.ofPattern(PATTERN_SSZ)
        )
    }
}