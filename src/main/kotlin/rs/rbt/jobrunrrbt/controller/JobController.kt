package rs.rbt.jobrunrrbt.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import rs.rbt.jobrunrrbt.dto.JobDTO
import rs.rbt.jobrunrrbt.dto.JobSignatureDTO
import rs.rbt.jobrunrrbt.dto.UpdateJobReceivedDTO
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.model.JobState
import rs.rbt.jobrunrrbt.service.JobService


/** This code defines a Kotlin class called `JobController` that serves as a REST API endpoint for
handling HTTP requests related to jobs. */
@CrossOrigin(origins = ["\${CORS_STRING}"])
@RestController
@RequestMapping("/api/v1/jobs")
class JobController {

    @Autowired
    private lateinit var jobService: JobService


    /**
     * This Kotlin function searches for jobs based on state and optional parameters such as offset,
     * limit, order, search parameter, and search value.
     *
     * @param jobState The state parameter is a path variable that is required and represents the state
     * for which the job search is being performed.
     * @param offset The offset parameter is used to specify the starting point of the search results.
     * It determines the number of items to skip before starting to return the results. The default
     * value is 0, which means that the search will start from the beginning.
     * @param limit The maximum number of results to be returned in the search. It is an optional
     * parameter with a default value of 10.
     * @param order The "order" parameter is used to specify the order in which the search results
     * should be returned. It is a string value that consists of two parts separated by a colon (":").
     * The first part specifies the field by which the results should be ordered (e.g. "updatedAt"),
     * and the second specifies if results should be showed ascending or descending (e.g "ASC")
     * @param parameter The parameter is a string that represents the search parameter for the job
     * that the user wants to search for with value. It can be Class, Method or both.
     * @param value The value is the value being passed in the search request.
     * @return A ResponseEntity object containing a JobDTO object is being returned.
     */
    @GetMapping("/{state}")
    fun searchByStateAndParam(
        @PathVariable(value = STATE, required = true) jobState: JobState,
        @RequestParam(value = OFFSET, required = false, defaultValue = "0") offset: Int,
        @RequestParam(value = LIMIT, required = false, defaultValue = "10") limit: Int,
        @RequestParam(value = ORDER, required = false, defaultValue = "updatedAt:DESC") order: String,
        @RequestParam(value = SEARCH_PARAMETER, required = false) parameter: String?,
        @RequestParam(value = SEARCH_VALUE, required = false) value: String?,
    ): ResponseEntity<JobDTO> {
        return ResponseEntity.ok(jobService.searchByStateAndParams(jobState, offset, limit, order, parameter, value))
    }

    /**
     * This Kotlin function returns a list of unique job signatures as a response entity.
     *
     * @return A ResponseEntity object containing a list of unique job signatures as strings is being
     * returned.
     */
    @GetMapping("/unique-signatures")
    fun searchUniqueJobSignatures(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(jobService.returnUniqueJobSignatures())
    }


    /**
     * This is a Kotlin function that updates a job with the given ID and changes specified in the
     * request body.
     *
     * @param id The id parameter is a path variable that is used to identify the specific job that
     * needs to be updated.
     * @param changes The `changes` parameter is a request body parameter of type
     * `UpdateJobReceivedDTO`. It contains the updated information that needs to be applied to the job
     * with the given `id`.
     * @return A ResponseEntity object containing a String response with the result of calling the
     * jobService's updateJob method with the provided id and changes.
     */
    @PutMapping("/{id}")
    fun updateJob(
        @PathVariable(value = ID, required = true) id: String,
        @RequestBody changes: UpdateJobReceivedDTO
    ): ResponseEntity<String> {
        return ResponseEntity.ok(jobService.updateJob(id, changes))
    }

    /**
     * This Kotlin function receives a list of job signatures and creates jobs using a job service.
     *
     * @param jobs `jobs` is a list of `JobSignatureDTO` objects that are received in the request body
     * of a POST request.
     */
    @PostMapping("")
    fun receiveJobs(@RequestBody jobs: List<JobSignatureDTO>): ResponseEntity<String> {
        jobService.createJobs(jobs)
        return ResponseEntity.ok(null)
    }
}