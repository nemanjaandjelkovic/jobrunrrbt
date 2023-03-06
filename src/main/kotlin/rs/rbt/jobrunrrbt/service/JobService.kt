package rs.rbt.jobrunrrbt.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import rs.rbt.jobrunrrbt.dto.JobDTO
import rs.rbt.jobrunrrbt.dto.PageInfoDTO
import rs.rbt.jobrunrrbt.helper.deserialize
import rs.rbt.jobrunrrbt.helper.serialize
import rs.rbt.jobrunrrbt.model.JobJson
import rs.rbt.jobrunrrbt.model.JobrunrJob
import rs.rbt.jobrunrrbt.repository.JobrunrJobRepository
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

/**
 *  This service contains all functions used by the controller
 */
@Service
class JobService {

    @Autowired
    lateinit var jobrunrJobRepository: JobrunrJobRepository

   /**
    *  This function returns a list of jobs where the state matches the given state
    * 
    * @param state The state of the job.
    * @param offset The offset of the first job to return.
    * @param limit The number of jobs to return
    * @param order The order in which the jobs should be returned.
    * @return A JobDTO object
    */
    fun returnAllJobsWhereStateMatches(state: String, offset: Int, limit: Int, order: String): JobDTO {

        val sort = getSortFromOrder(order)

        val jobList = jobrunrJobRepository.findJobrunrJobsByState(state, PageRequest.of(offset, limit, sort))
        val returnList = makeReturnList(jobList)

        val pageInfo = createPageInfoDTO(jobList.size, limit, offset)

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

        val leadingLetter = value.first()
        val restOfValue = value.drop(1)
        val regex = "^(.+[$leadingLetter].*$restOfValue.*\\..*\\(.+)"

        val jobList: List<JobrunrJob> =
            jobrunrJobRepository.findJobsWhereClassMatches(state, regex, PageRequest.of(offset, limit, sort))
        val returnList = makeReturnList(jobList)

        val total = jobrunrJobRepository.countJobsWhereClassMatches(state, value)

        val pageInfoDTO = createPageInfoDTO(total, limit, offset)

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
        state: String,
        value: String,
        offset: Int,
        limit: Int,
        order: String
    ): String {

        val sort = getSortFromOrder(order)

        val jobList = jobrunrJobRepository.findJobsByClassAndMethod(state, value, PageRequest.of(offset, limit, sort))
        val returnList = makeReturnList(jobList)

        val total = jobrunrJobRepository.countJobsByClassAndMethod(state, value)

        val pageInfoDTO = createPageInfoDTO(total, limit, offset)


        return serialize(
            JobDTO(
                offset,
                pageInfoDTO.hasNext,
                pageInfoDTO.hasPrevious,
                returnList,
                limit,
                offset,
                total,
                pageInfoDTO.totalPages
            )
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

        val pageInfoDTO = createPageInfoDTO(total, limit, offset)

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
     *  This function updates the job with the given id with the given new package name, new method
     * name, new class name and new scheduled time
     * 
     * @param id The id of the job you want to update
     * @param newPackageName The new package name of the job
     * @param newMethodName The name of the method that will be called when the job is executed.
     * @param newClassName The new class name of the job
     * @param newScheduledTime The new time you want to schedule the job for.
     */
    fun updateJobWithTime(
        id: String, newPackageName: String, newMethodName: String,
        newClassName: String, newScheduledTime: Instant
    ) {

        if (jobrunrJobRepository.existsById(id)) {

            val job: Optional<JobrunrJob> = jobrunrJobRepository.findById(id)
            var jobJson: JobJson = deserialize(job.get().jobasjson!!)

            jobJson = updateJobJsonFields(jobJson, newPackageName, newClassName, newMethodName)

            jobJson.jobHistory[jobJson.jobHistory.size - 1].scheduledAt =
                newScheduledTime.minus(1, ChronoUnit.HOURS).toString()

            val newJobJson: String = serialize(jobJson)

            jobrunrJobRepository.updateJobSignature((id), jobJson.jobSignature)
            jobrunrJobRepository.updateJobAsJson(id, newJobJson)
            jobrunrJobRepository.updateScheduledTime(
                id,
                Instant.parse(jobJson.jobHistory[jobJson.jobHistory.size - 1].scheduledAt).minus(1, ChronoUnit.HOURS)
            )

        }
    }

   /**
    * It updates the job's package name, class name and method name in the database
    * 
    * @param id The id of the job you want to update
    * @param newPackageName The new package name of the job
    * @param newMethodName The new method name of the job
    * @param newClassName The new class name of the job
    */
    fun updateJob(id: String, newPackageName: String, newMethodName: String, newClassName: String) {

        if (jobrunrJobRepository.existsById(id)) {

            val job: Optional<JobrunrJob> = jobrunrJobRepository.findById(id)
            var jobJson: JobJson = deserialize(job.get().jobasjson!!)

            jobJson = updateJobJsonFields(jobJson, newPackageName, newClassName, newMethodName)

            val newJobJson: String = serialize(jobJson)

            jobrunrJobRepository.updateJobSignature((id), jobJson.jobSignature)
            jobrunrJobRepository.updateJobAsJson(id, newJobJson)

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
 * It creates a PageInfoDTO object with the given total, limit, and offset
 * 
 * @param total The total number of items in the collection.
 * @param limit The number of items to return in the page.
 * @param offset The page number.
 * @return PageInfoDTO
 */
private fun createPageInfoDTO(total: Int, limit: Int, offset: Int): PageInfoDTO {

    val totalPages = (total - 1) / limit + 1
    val hasNext = offset < totalPages
    val hasPrevious = offset > 0

    return PageInfoDTO(total, totalPages, hasNext, hasPrevious)
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
    jobJson: JobJson,
    newPackageName: String,
    newClassName: String,
    newMethodName: String
): JobJson {

    val newJobSignature = "$newPackageName.$newClassName.$newMethodName(${jobJson.jobDetails.jobParameters})"

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