package rs.rbt.jobrunrrbt.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import rs.rbt.jobrunrrbt.dto.*
import rs.rbt.jobrunrrbt.exception.IdNotFoundException
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.model.*
import rs.rbt.jobrunrrbt.repository.JobrunrJobRepository
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

/**
 *  This service contains all functions used by the controller
 */
@Service
class JobService {

    @Autowired
    private lateinit var jobrunrJobRepository: JobrunrJobRepository

    /**
     * The function searches for job data based on state, offset, limit, order, and optional parameters
     * and values.
     *
     * @param jobState The state parameter is a string that represents the state for which the job search
     * is being performed.
     * @param offset The starting index of the results to be returned.
     * @param limit The maximum number of results to be returned in a single query.
     * @param order The order parameter specifies the order in which the results should be returned. It
     * could be ascending or descending order based on a particular field.
     * @param parameter The parameter is a string that specifies the type of search to be performed. It
     * can be either "CLASS", "METHOD", or any other string value.
     * @param value The value parameter is a string that represents the value to search for in the
     * specified parameter (class, method, or both) in the job data.
     * @return The function `searchByStateAndParams` returns a `JobDTO` object.
     */
    fun searchByStateAndParams(
        jobState: JobState,
        offset: Int,
        limit: Int,
        order: String,
        parameter: String?,
        value: String?
    ): JobDTO {
        if (parameter.isNullOrBlank() || value.isNullOrBlank())
            return returnAllJobsWhereStateMatches(jobState, offset, limit, order)

        return when (parameter) {
            CLASS -> {
                returnAllJobsWhereClassMatches(jobState.name, value, offset / limit, limit, order)
            }

            METHOD -> {
                returnAllJobsWhereMethodMatches(jobState.name, value, offset / limit, limit, order)
            }

            else -> {
                returnAllJobsWhereClassOrMethodMatch(jobState.name, value, offset / limit, limit, order)
            }
        }
    }

    /**
     *  This function returns a list of jobs where the state matches the given state
     *
     * @param jobState The state of the job.
     * @param offset The offset of the first job to return.
     * @param limit The number of jobs to return
     * @param order The order in which the jobs should be returned.
     * @return A JobDTO object
     */
    fun returnAllJobsWhereStateMatches(jobState: JobState, offset: Int, limit: Int, order: String): JobDTO {

        val sort = getSortFromOrder(order)
        val pageRequest = PageRequest.of(offset, limit, sort)

        val jobList = jobrunrJobRepository.findJobrunrJobsByState(jobState.name, pageRequest)
        val total = jobrunrJobRepository.countJobrunrJobsByState(jobState.name)
        val returnList = makeReturnList(jobList)

        val pageInfo = createPageInfo(total, limit, offset, returnList)

        return JobDTO(
            offset,
            pageInfo.hasNext,
            pageInfo.hasPrevious,
            returnList,
            limit,
            offset,
            pageInfo.total,
            pageInfo.totalPages
        )
    }

    /**
     *  This function returns a list of jobs where the class name matches the given value
     *
     * @param state The state of the job.
     * @param value The value to search for in the class name.
     * @param offset The offset of the first job to return
     * @param limit The number of jobs to return
     * @param order The order in which the jobs should be returned.
     * @return A JobDTO object
     */
    fun returnAllJobsWhereClassMatches(state: String, value: String, offset: Int, limit: Int, order: String): JobDTO {

        val sort = getSortFromOrder(order)
        val regex = "^(.+\\.${value}.*\\..*\\(.+)"

        val jobList: List<JobrunrJob> =
            jobrunrJobRepository.findJobsWhereClassMatches(state, regex, PageRequest.of(offset, limit, sort))
        val returnList = makeReturnList(jobList)

        val total = jobrunrJobRepository.countJobsWhereClassMatches(state, value)

        val pageInfoDTO = createPageInfo(total, limit, offset, returnList)

        return JobDTO(
            offset,
            pageInfoDTO.hasNext,
            pageInfoDTO.hasPrevious,
            returnList,
            limit,
            offset,
            total,
            pageInfoDTO.totalPages
        )
    }

    /**
     *  This function returns a list of jobs where the class or method matches the given value
     *
     * @param state The state of the job. Can be one of the following:
     * @param value The value to search for in the class or method name.
     * @param offset The offset of the first job to return.
     * @param limit The number of jobs to return
     * @param order The order in which the jobs should be returned.
     * @return A list of jobs that match the state and value.
     */
    fun returnAllJobsWhereClassOrMethodMatch(
        state: String, value: String, offset: Int, limit: Int, order: String
    ): JobDTO {

        val sort = getSortFromOrder(order)

        val jobList = jobrunrJobRepository.findJobsByClassAndMethod(state, value, PageRequest.of(offset, limit, sort))
        val returnList = makeReturnList(jobList)

        val total = jobrunrJobRepository.countJobsByClassAndMethod(state, value)

        val pageInfoDTO = createPageInfo(total, limit, offset, returnList)


        return JobDTO(
            offset,
            pageInfoDTO.hasNext,
            pageInfoDTO.hasPrevious,
            returnList,
            limit,
            offset,
            total,
            pageInfoDTO.totalPages
        )

    }

    /**
     *  Return all jobs where the method matches the given value
     *
     * @param state The state of the job. Can be one of the following:
     * @param value The value of the method name you want to search for.
     * @param offset The offset of the first job to return
     * @param limit The number of jobs to return
     * @param order The order in which the jobs should be returned.
     * @return A list of jobs that match the given state and method name.
     */
    fun returnAllJobsWhereMethodMatches(state: String, value: String, offset: Int, limit: Int, order: String): JobDTO {

        val sort = getSortFromOrder(order)
        val regex = "^(.+[A-Z].+\\.$value.*\\(.+)"

        val jobList = jobrunrJobRepository.findJobsWhereMethodMatches(state, regex, PageRequest.of(offset, limit, sort))
        val returnList = makeReturnList(jobList)


        val total = jobrunrJobRepository.countJobsWhereMethodMatches(state, regex)

        val pageInfoDTO = createPageInfo(total, limit, offset, returnList)

        return JobDTO(
            offset,
            pageInfoDTO.hasNext,
            pageInfoDTO.hasPrevious,
            returnList,
            limit,
            offset,
            total,
            pageInfoDTO.totalPages
        )
    }

    /**
     * This Kotlin function returns a list of unique job signatures from a job repository.
     *
     * @return A list of unique job signatures is being returned.
     */
    fun returnUniqueJobSignatures(): List<String> {
        return jobrunrJobRepository.findUniqueJobSignatures()
    }

    /** Kotlin function that updates a job in a job queue. It takes in an ID of the
    job to be updated and a DTO containing the changes to be made to the job. The function first
    retrieves the job from the job queue repository using the ID provided. It then checks if the job
    is in the "ENQUEUED" or "PROCESSING" state, and if so, throws an exception as such jobs cannot
    be modified. */
    @Transactional
    fun updateJob(
        id: String,
        changes: UpdateJobReceivedDTO
    ): String {

        val job = jobrunrJobRepository.findById(id).orElseThrow { IdNotFoundException("Job with ID: $id not found") }

        if (job.state.equals("ENQUEUED") || job.state.equals("PROCESSING"))
            throw IllegalArgumentException("Jobs that are enqueued or processing can not be modified")

        val jobJson: JobJson = deserialize(job.jobasjson!!)
        val newJobJson = updateJobJsonFields(jobJson, changes.packageName, changes.className, changes.methodName)

        if (changes.scheduledTime != null) {
            when (job.state) {
                "SCHEDULED" -> {
                    newJobJson.jobHistory.last().scheduledAt = changes.scheduledTime
                    job.scheduledat = convertToLocalDateTime(changes.scheduledTime)
                }

                else -> {
                    newJobJson.jobHistory.add(
                        JobHistory(
                            atClass = DEFAULT_AT_CLASS,
                            state = STATE_SCHEDULED,
                            createdAt = LocalDateTime.now(ZoneId.of("UTC")),
                            scheduledAt = changes.scheduledTime,
                            recurringJobId = null,
                            reason = null
                        )
                    )
                }
            }
            newJobJson.jobHistory.last().scheduledAt = changes.scheduledTime
            job.scheduledat = convertToLocalDateTime(changes.scheduledTime)
        }

        job.jobsignature = jobJson.jobSignature
        job.jobasjson = serialize(newJobJson)

        return job.id!!
    }


    /** Kotlin function that creates jobs based on a list of JobSignatureDTO
    objects. It first checks if the job signature is valid using a regular expression. If it is
    valid, it checks if there are any possible duplicates of the job in the job repository. If there
    are duplicates, it checks if the job arguments are the same and updates the scheduled time of
    the duplicate job. If there are no duplicates, it creates a new job with the given signature,
    arguments, and scheduled time. */
    @Transactional
    fun createJobs(jobs: List<JobSignatureDTO>) {

        jobs.forEach { oneOfJobs ->
            if (Regex(REGEX_TO_CHECK_IF_SIGNATURE_IS_VALID).find(oneOfJobs.jobSignature)?.value.equals(oneOfJobs.jobSignature)) {

                val listOfPossibleDuplicates =
                    jobrunrJobRepository.findJobrunrJobsBySignatureIfScheduled(oneOfJobs.jobSignature)

                if (listOfPossibleDuplicates.isNotEmpty()) {
                    val duplicate = checkForDuplicates(oneOfJobs.jobArguments, listOfPossibleDuplicates)

                    if (duplicate != null) {

                        duplicate.scheduledat = oneOfJobs.jobTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()
                        duplicate.updatedat = LocalDateTime.now(ZoneId.of("UTC"))
                        duplicate.state = STATE_SCHEDULED

                        val duplicateJobJson = deserialize(duplicate.jobasjson!!)

                        duplicateJobJson.jobHistory.add(
                            JobHistory(
                                atClass = DEFAULT_AT_CLASS,
                                state = STATE_SCHEDULED,
                                createdAt = LocalDateTime.now(ZoneId.of("UTC")),
                                scheduledAt = oneOfJobs.jobTime,
                                recurringJobId = null,
                                reason = null
                            )
                        )

                        duplicate.jobasjson = serialize(duplicateJobJson)
                        jobrunrJobRepository.save(duplicate)


                    } else {
                        val newJob = createJob(oneOfJobs.jobSignature, oneOfJobs.jobArguments, oneOfJobs.jobTime)
                        jobrunrJobRepository.save(newJob)
                    }
                } else {
                    val newJob = createJob(oneOfJobs.jobSignature, oneOfJobs.jobArguments, oneOfJobs.jobTime)
                    jobrunrJobRepository.save(newJob)
                }
            }
        }
    }
}

/**
 * It takes a string like "name:asc" and returns a Sort object that can be used to sort a query
 *
 * @param order The order parameter is a string that contains the field name and the direction of the
 * sort.
 * @return A Sort object
 */
private fun getSortFromOrder(order: String): Sort {

    val splitOrder = order.split(':')
    val direction = Sort.Direction.valueOf(splitOrder[1])

    return Sort.by(direction, splitOrder[0].lowercase())
}

/**
 * It takes a list of Jobrunr jobs, deserializes them into JobJson objects, and returns a list of
 * JobJson objects.
 *
 * @param jobList List<JobrunrJob>
 * @return A list of JobJson objects
 */
private fun makeReturnList(jobList: List<JobrunrJob>): List<JobJson> {

    return jobList.map { deserialize(it.jobasjson!!) }
}

/**
 * It creates a PageInfo object with the given total, limit, and offset
 *
 * @param total The total number of items in the collection.
 * @param limit The number of items to return in the page.
 * @param offset The page number.
 * @return PageInfo
 */
private fun createPageInfo(total: Int, limit: Int, offset: Int, returnList: List<JobJson>): PageInfo {

    val pageRequest = PageRequest.of(offset/limit, limit)
    val page: Page<*> = PageImpl<Any?>(returnList, pageRequest, total.toLong())

    val totalPages = page.totalPages
    val hasNext = page.hasNext()
    val hasPrevious = page.hasPrevious()

    return PageInfo(total, totalPages.coerceAtLeast(1), hasNext, hasPrevious)
}

/**
 * It takes a jobJson object, a new package name, a new class name, and a new method name, and returns
 * a new jobJson object with the jobDetails and jobSignature fields updated to reflect the new package
 * name, class name, and method name
 *
 * @param jobJson The jobJson object that we're updating.
 * @param newPackageName The new package name for the job
 * @param newClassName The new class name
 * @param newMethodName The new method name that the user has entered.
 * @return A JobJson object with updated fields.
 */
private fun updateJobJsonFields(
    jobJson: JobJson, newPackageName: String, newClassName: String, newMethodName: String
): JobJson {

    val newJobSignature =
        "$newPackageName.$newClassName.$newMethodName(${jobJson.jobDetails.jobParameters.joinToString(",")})"

    val splitMethodName = newMethodName.split(".")
    val splitLength = splitMethodName.size - 1
    val newStaticFieldName = newMethodName.dropLast(splitMethodName[splitLength].length + 1)
    val methodNameForJobDetails = splitMethodName[splitLength]

    jobJson.jobDetails.className = "$newPackageName.$newClassName"
    jobJson.jobDetails.methodName = methodNameForJobDetails
    jobJson.jobDetails.staticFieldName = newStaticFieldName
    jobJson.jobSignature = newJobSignature

    return jobJson
}

/**
 * This Kotlin function creates a JobrunrJob object with specified parameters and returns it.
 *
 * @param jobSignature A string representing the signature of the job, which includes the class name,
 * method name, and argument types.
 * @param jobArguments jobArguments is an array of JobArgumentsDTO objects, which contain data for the
 * arguments of the job being created.
 * @param jobTime jobTime is an OffsetDateTime object representing the time at which the job is
 * scheduled to run.
 * @return a JobrunrJob object.
 */
private fun createJob(jobSignature: String, jobArguments: Array<JobArgumentsDTO>, jobTime: OffsetDateTime): JobrunrJob {
    val job = JobrunrJob()

    val classNameList = Regex(REGEX_TO_GET_ARGUMENTS).find(jobSignature)!!.groupValues[1].split(",")
    val jobParameters: MutableList<JobParameters> = mutableListOf()
    val argumentList: MutableList<String?> = mutableListOf()

    jobArguments.forEachIndexed { index, element ->
        jobParameters.add(
            JobParameters(
                classNameList[index], classNameList[index], element.argData
            )
        )
        argumentList.add(element.argData)
    }

    val jobDetailsClassName = Regex(REGEX_TO_GET_CLASS_NAME).find(jobSignature)!!.value

    val staticFieldAndMethodName = Regex(REGEX_TO_GET_STATIC_FIELD_AND_METHOD_NAME).find(jobSignature)!!.groupValues[1]

    var staticFieldName: String? = null

    if (staticFieldAndMethodName.substringBeforeLast(".", "") != "")
        staticFieldName = staticFieldAndMethodName.substringBeforeLast(".", "")

    val methodName = staticFieldAndMethodName.substringAfterLast(".")

    val jobName =
        Regex(REGEX_TO_GET_EVERYTHING_BEFORE_FIRST_ARGUMENT).find(jobSignature)!!.value + argumentList.joinToString(",") + ")"

    val generatedID = UUID.randomUUID().toString()
    val creationTime = LocalDateTime.now(ZoneId.of("UTC"))

    val jobDetails = JobDetails(jobDetailsClassName, staticFieldName, methodName, jobParameters, true)

    val jobJson = JobJson(
        1, jobSignature, jobName, null, arrayListOf(), jobDetails, generatedID, arrayListOf(
            JobHistory(
                atClass = DEFAULT_AT_CLASS,
                state = STATE_SCHEDULED,
                createdAt = creationTime,
                scheduledAt = jobTime,
                recurringJobId = null,
                reason = null
            )
        ), metadata = null
    )

    job.id = generatedID
    job.version = 1
    job.jobasjson = serialize(jobJson)
    job.jobsignature = jobSignature
    job.state = STATE_SCHEDULED
    job.createdat = creationTime
    job.updatedat = creationTime
    job.scheduledat = convertToLocalDateTime(jobTime)
    job.recurringjobid = null

    return job
}

/**
 * The function checks for duplicates in a list of Jobrunr jobs based on matching job arguments.
 *
 * @param jobArguments An array of JobArgumentsDTO objects, which likely contain arguments for a job
 * that needs to be executed.
 * @param listOfPossibleDuplicates A list of JobrunrJob objects that are potential duplicates of the
 * job being checked for duplicates.
 * @return The function `checkForDuplicates` returns a `JobrunrJob` object if a duplicate job is found
 * in the `listOfPossibleDuplicates` that matches all the arguments in `jobArguments`. If no duplicate
 * is found, the function returns `null`.
 */
private fun checkForDuplicates(
    jobArguments: Array<JobArgumentsDTO>, listOfPossibleDuplicates: List<JobrunrJob>
): JobrunrJob? {

    listOfPossibleDuplicates.forEach { possibleDuplicate ->
        val jobJson = possibleDuplicate.jobasjson?.let { deserialize(it) }

        if (jobJson?.let { checkIfAllArgumentsMatch(jobArguments, it) } == true) {
            return possibleDuplicate
        }
    }
    return null
}

/**
 * The function checks if all arguments in a given array match the corresponding parameters in a job
 * JSON object.
 *
 * @param jobArguments An array of JobArgumentsDTO objects, which likely contains data passed as
 * arguments to a job.
 * @param jobJson JobJson is likely a data class or object that represents a JSON object containing
 * information about a job. It probably has a property called "jobDetails" which in turn has a property
 * called "jobParameters" that is an array of objects representing the parameters of the job.
 * @return a boolean value. It returns `true` if all the job arguments in the `jobArguments` array
 * match the corresponding job objects in the `jobJson` parameter, and `false` otherwise.
 */
private fun checkIfAllArgumentsMatch(jobArguments: Array<JobArgumentsDTO>, jobJson: JobJson): Boolean {

    jobJson.jobDetails.jobParameters.forEachIndexed { index, jobParameter ->
        if (!jobParameter.jobObject.equals(jobArguments[index].argData)) {
            return false
        }
    }
    return true
}

/**
 * The function converts an OffsetDateTime object to a LocalDateTime object in Kotlin.
 *
 * @param timeToConvert `timeToConvert` is an instance of `OffsetDateTime` class, which represents a
 * date-time with an offset from UTC/Greenwich in the ISO-8601 calendar system. It contains information
 * about the date, time, and offset from UTC/Greenwich.
 * @return The function `convertToLocalDateTime` is returning a `LocalDateTime` object.
 */
private fun convertToLocalDateTime(timeToConvert: OffsetDateTime): LocalDateTime {

    return LocalDateTime.parse(
        timeToConvert.atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(PATTERN_SSZ)),
        DateTimeFormatter.ofPattern(PATTERN_SSZ)
    )
}