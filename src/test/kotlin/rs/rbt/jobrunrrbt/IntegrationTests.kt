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
import org.springframework.test.web.servlet.MockMvc
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
    fun setup() {
        Flyway.configure()
            .dataSource(dataSource)
            .baselineOnMigrate(true)
            .validateOnMigrate(true)
            .load()
    }

    @BeforeEach
    fun setupData() {
        jdbcTemplateObject.update("delete from jobrunr_jobs")
        populateJobrunrJobs()
    }

    @Test
    fun `test if job signatures are returned accordingly`() {
        val response =
            restTemplate.getForObject("http://localhost:${port}/api/v1/jobs/unique-signatures", List::class.java)
        val data =
            listOf("java.lang.System.out.print(java.lang.String)", "java.lang.System.out.println(java.lang.String)")
        assertThat(response.size).isEqualTo(2)
        assertThat(response).isEqualTo(data)
    }

    @Test
    fun `test when state is correct`() {
        val response = restTemplate.getForObject("http://localhost:${port}/api/v1/jobs/SUCCEEDED", JobDTO::class.java)
        assertThat(response).isEqualTo(singleStateJobDTO)
    }

    @Test
    fun `test when state is incorrect`() {
        val response =
            restTemplate.getForObject("http://localhost:${port}/api/v1/jobs/ILLEGAL-STATE", IllegalJobState::class.java)
        assertThat(response.message).isEqualTo("ILLEGAL-STATE is not a valid job state")
    }

    @Test
    fun `test state and class exists`() {

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
    fun `test state and class doesnt exist`() {

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
    fun `test state and method`() {

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
    fun `test state and method doesnt exist`() {

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
    fun `test state and class or method`() {

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
    fun `test state and class or method doesnt exist`() {

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
    fun `update job by id without time`() {
        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/${jobJson1.id}")
            .build()
            .toUri()
        val response =
            restTemplate.exchange(uri, HttpMethod.PUT, HttpEntity(jobChangeRequestWithoutTimeDTO), String::class.java)

        assertThat(response.body).isEqualTo(jobJson1.id)

        val queriedJobSignature = jdbcTemplateObject.queryForObject(
            "SELECT jobSignature FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJson1.id),
            String::class.java
        )

        assertThat(queriedJobSignature).isEqualTo("package.Class.method(java.lang.String)")

        val queriedJobJson = jdbcTemplateObject.queryForObject(
            "SELECT jobAsJson FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJson1.id),
            String::class.java
        )

        assertThat(deserialize(queriedJobJson)).isEqualTo(updatedJobJsonWithoutTime)
    }

    @Test
    fun `update job by id without time when id is wrong`() {
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
    fun `update job by id without time when state is wrong`() {
        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/${jobJson5.id}")
            .build()
            .toUri()
        val response =
            restTemplate.exchange(uri, HttpMethod.PUT, HttpEntity(jobChangeRequestWithoutTimeDTO), String::class.java)

        assertThat(
            ObjectMapper().readTree(response.body).get("message").asText()
        ).isEqualTo("Jobs that are enqueued or processing can not be modified")
    }

    @Test
    fun `update job by id with time`() {
        val uri = UriComponentsBuilder
            .fromUriString("http://localhost:${port}/api/v1/jobs/${jobJson4.id}")
            .build()
            .toUri()
        val response =
            restTemplate.exchange(uri, HttpMethod.PUT, HttpEntity(jobChangeRequestWithTimeDTO), String::class.java)

        assertThat(response.body).isEqualTo(jobJson4.id)


        val queriedJobSignature = jdbcTemplateObject.queryForObject(
            "SELECT jobSignature FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJson4.id),
            String::class.java
        )

        assertThat(queriedJobSignature).isEqualTo("package.Class.method(java.lang.String)")


        val queriedScheduledAt = jdbcTemplateObject.queryForObject(
            "SELECT scheduledAt FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJson4.id),
            LocalDateTime::class.java
        )

        assertThat(queriedScheduledAt).isEqualTo(
            convertToLocalDateTime(
                OffsetDateTime.of(2023, 4, 25, 17, 3, 40, 113, ZoneOffset.UTC)
            )
        )

    }

    @Test
    fun `insert multiple jobs without duplicate jobsignature`() {
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

        val countOfJobSignature = jdbcTemplateObject.queryForObject(
            "SELECT jobsignature FROM jobrunr_jobs WHERE jobsignature = ?",
            arrayOf("com.example.Class.path.to.method(java.lang.String,java.lang.String)"),
            String::class.java
        )

        assertThat(countOfJobSignature).isEqualTo("com.example.Class.path.to.method(java.lang.String,java.lang.String)")

        val queriedScheduledAt = jdbcTemplateObject.queryForObject(
            "SELECT scheduledAt FROM jobrunr_jobs WHERE jobSignature = ?",
            arrayOf("com.example.Class.path.to.method(java.lang.String,java.lang.String)"),
            LocalDateTime::class.java
        )

        assertThat(queriedScheduledAt).isEqualTo(
            convertToLocalDateTime(
                OffsetDateTime.of(2023, 4, 25, 17, 3, 40, 113, ZoneOffset.UTC)
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
    fun `insert multiple jobs with duplicate jobsignature`() {
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

        val countOfJobSignature = jdbcTemplateObject.queryForObject(
            "SELECT jobsignature FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJson4.id),
            String::class.java
        )

        assertThat(countOfJobSignature).isEqualTo("java.lang.System.out.println(java.lang.String)")

        val queriedScheduledAt = jdbcTemplateObject.queryForObject(
            "SELECT scheduledAt FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJson4.id),
            LocalDateTime::class.java
        )

        assertThat(queriedScheduledAt).isCloseTo(
            convertToLocalDateTime(
                OffsetDateTime.of(2023, 5, 24, 8, 7, 28, 361810, ZoneOffset.UTC)
            ), within(1, ChronoUnit.SECONDS)
        )

        val queriedJobJson = jdbcTemplateObject.queryForObject(
            "SELECT jobAsJson FROM jobrunr_jobs WHERE id = ?",
            arrayOf(jobJson4.id),
            String::class.java
        )

        assertThat(deserialize(queriedJobJson).jobDetails.jobParameters[0].jobObject).isEqualTo(
            jobArgumentsForJobSignatureThatOverrides[0].argData
        )

    }


    final val jobJson1 = JobJson(
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

    final val jobJson2 = JobJson(
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

    final val jobJson3 = JobJson(
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

    final val jobJson4 = JobJson(
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

    final val jobJson5 = JobJson(
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

    final val jobJson6 = JobJson(
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

    val serializedjobjson1 = serialize(jobJson1)
    val serializedjobjson2 = serialize(jobJson2)
    val serializedjobjson3 = serialize(jobJson3)
    val serializedjobjson4 = serialize(jobJson4)
    val serializedjobjson5 = serialize(jobJson5)
    val serializedjobjson6 = serialize(jobJson6)


    final val singleStateJobDTO = JobDTO(
        currentPage = 0,
        hasNext = false,
        hasPrevious = false,
        items = listOf(jobJson1),
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
            jobTime = OffsetDateTime.of(2023, 4, 25, 17, 3, 40, 113000, ZoneOffset.UTC)
        )

    final val jobSignatureThatOverrides = JobSignatureDTO(
        jobSignature = "com.example.Class.path.to.method(java.lang.String)",
        jobArguments = jobArgumentsForJobSignatureThatOverrides,
        jobTime = OffsetDateTime.of(2023, 4, 25, 17, 3, 40, 113000, ZoneOffset.UTC)

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


    private fun populateJobrunrJobs() {
        jdbcTemplateObject.update(
            "" +
                    "INSERT INTO jobrunr_jobs (id,version,jobasjson,jobsignature,state,createdat,updatedat,scheduledat,recurringjobid ) VALUES ('13c47255-3c06-44dd-9b1a-5421641d26ee',1,'${serializedjobjson1}','java.lang.System.out.print(java.lang.String)','SUCCEEDED','2023-04-24 08:07:28.361810','2023-04-24 08:07:28.361810',null,null);" +
                    "INSERT INTO jobrunr_jobs (id,version,jobasjson,jobsignature,state,createdat,updatedat,scheduledat,recurringjobid ) VALUES ('23c47255-3c06-44dd-9b1a-5421641d26ee',1,'${serializedjobjson2}','java.lang.System.out.print(java.lang.String)','FAILED','2023-04-24 08:07:28.361810','2023-04-24 08:07:28.361810',null,null);" +
                    "INSERT INTO jobrunr_jobs (id,version,jobasjson,jobsignature,state,createdat,updatedat,scheduledat,recurringjobid ) VALUES ('33c47255-3c06-44dd-9b1a-5421641d26ee',1,'${serializedjobjson3}','java.lang.System.out.println(java.lang.String)','DELETED','2023-04-24 08:07:28.361810','2023-04-24 08:07:28.361810',null,null);" +
                    "INSERT INTO jobrunr_jobs (id,version,jobasjson,jobsignature,state,createdat,updatedat,scheduledat,recurringjobid ) VALUES ('43c47255-3c06-44dd-9b1a-5421641d26ee',1,'${serializedjobjson4}','java.lang.System.out.println(java.lang.String)','SCHEDULED','2023-04-24 08:07:28.361810','2023-04-24 08:07:28.361810','2023-05-24 08:07:28.361810',null);" +
                    "INSERT INTO jobrunr_jobs (id,version,jobasjson,jobsignature,state,createdat,updatedat,scheduledat,recurringjobid ) VALUES ('53c47255-3c06-44dd-9b1a-5421641d26ee',1,'${serializedjobjson5}','java.lang.System.out.println(java.lang.String)','ENQUEUED','2023-04-24 08:07:28.361810','2023-04-24 08:07:28.361810','2023-04-24 08:07:28.361810',null);" +
                    "INSERT INTO jobrunr_jobs (id,version,jobasjson,jobsignature,state,createdat,updatedat,scheduledat,recurringjobid ) VALUES ('63c47255-3c06-44dd-9b1a-5421641d26ee',1,'${serializedjobjson6}','java.lang.System.out.println(java.lang.String)','PROCESSING','2023-04-24 08:07:28.361810','2023-04-24 08:07:28.361810','2023-04-24 08:07:28.361810',null);"
        )

    }

    private fun convertToLocalDateTime(timeToConvert: OffsetDateTime): LocalDateTime {

        return LocalDateTime.parse(
            timeToConvert.atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(PATTERN_SSZ)),
            DateTimeFormatter.ofPattern(PATTERN_SSZ)
        )
    }
}