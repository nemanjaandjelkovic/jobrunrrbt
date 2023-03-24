
package rs.rbt.jobrunrrbt.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import rs.rbt.jobrunrrbt.dto.JobDTO
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.service.JobService
import java.time.Instant

/**  It contains functions that return jobs based on the state, offset, limit, order, parameter, and
value parameters */
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api")
class JobController {

    /** Injecting the JobService class into the JobController class. */
    @Autowired
    lateinit var jobService: JobService

   /**
    * This function returns a JobDTO object that contains a list of jobs that match the state
    * parameter, and the total number of jobs that match the state parameter
    * 
    * @param state The state of the job.
    * @param offset The offset of the first job to return.
    * @param limit The number of jobs to return
    * @param order The order in which the jobs are returned.
    * @return A JobDTO object
    */
    @GetMapping("/state")
    fun sendFilteredByState(
        @RequestParam(value = STATE, required = true) state: String,
        @RequestParam(value = OFFSET, required = true) offset: Int,
        @RequestParam(value = LIMIT, required = true) limit: Int,
        @RequestParam(value = ORDER, required = true) order: String
    ): JobDTO {
        return jobService.returnAllJobsWhereStateMatches(state, offset, limit, order)
    }

  /**
   * It returns all jobs that match the given state, offset, limit, order, parameter, and value
   * 
   * @param state The state of the job. Can be either "pending", "running", "completed", or "failed".
   * @param offset The offset of the first job to return.
   * @param limit The number of jobs to return
   * @param order The order in which the jobs are returned.
   * @param parameter The parameter to search by. Can be either CLASS, METHOD, or ALL.
   * @param value The value to search for
   * @return A list of jobs that match the search criteria.
   */
    @GetMapping("/search")
    fun searchByStateAndParam(
        @RequestParam(value = STATE, required = true) state: String,
        @RequestParam(value = OFFSET, required = true) offset: Int,
        @RequestParam(value = LIMIT, required = true) limit: Int,
        @RequestParam(value = ORDER, required = true) order: String,
        @RequestParam(value = SEARCH_PARAMETER, required = true) parameter: String,
        @RequestParam(value = SEARCH_VALUE, required = true) value: String,
    ): Any {
        return when (parameter) {
            CLASS -> {
                jobService.returnAllJobsWhereClassMatches(state, value, offset / limit, limit, order)
            }
            METHOD -> {
                jobService.returnAllJobsWhereMethodMatches(state, value, offset / limit, limit, order)
            }
            else -> {
                jobService.returnAllJobsWhereClassOrMethodMatch(state, value, offset / limit, limit, order)
            }
        }
    }
    @GetMapping("/jobSignatures")
    fun searchUniqueJobSignatures(): List<String> {
        return jobService.returnUniqueJobSignatures()
    }
/**
 * It updates a job with the given id, package name, method name, class name, and scheduled time
 * 
 * @param id The id of the job you want to update.
 * @param packageName The package name of the class that contains the method to be executed.
 * @param methodName The name of the method to be executed
 * @param className The name of the class that contains the method to be executed.
 * @param scheduledTime The time at which the job should be executed.
 */

    @Transactional
    @PostMapping("/update")
    fun updateJob(
        @RequestParam(value = ID, required = true) id: String,
        @RequestParam(value = PACKAGE_NAME, required = true) packageName: String,
        @RequestParam(value = METHOD_NAME, required = true) methodName: String,
        @RequestParam(value = CLASS_NAME, required = true) className: String,
        @RequestParam(value = SCHEDULED_TIME, required = false) scheduledTime: Instant?
    ) {
        when (scheduledTime) {
            (null) -> {
                jobService.updateJob(id, packageName, methodName, className)
            }
            else -> {
                jobService.updateJobWithTime(id, packageName, methodName, className, scheduledTime)

            }
        }
    }
}