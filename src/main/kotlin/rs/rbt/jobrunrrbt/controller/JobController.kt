package rs.rbt.jobrunrrbt.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import rs.rbt.jobrunrrbt.dto.JobDTO
import rs.rbt.jobrunrrbt.dto.JobSignatureDTO
import rs.rbt.jobrunrrbt.dto.UpdateJobReceivedDTO
import rs.rbt.jobrunrrbt.exception.IllegalJobState
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.service.JobService


/* This code defines a Kotlin class called `JobController` that serves as a REST API endpoint for
handling HTTP requests related to jobs. */
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/v1/jobs")
class JobController {

    /** Injecting the JobService class into the JobController class. */
    @Autowired
    lateinit var jobService: JobService


    /**
     * This Kotlin function searches for jobs based on state and optional parameters such as offset,
     * limit, order, search parameter, and search value.
     *
     * @param state The state parameter is a path variable that is required and represents the state
     * for which the job search is being performed.
     * @param offset The offset parameter is used to specify the starting point of the search results.
     * It determines the number of items to skip before starting to return the results. The default
     * value is 0, which means that the search will start from the beginning.
     * @param limit The maximum number of results to be returned in the search. It is an optional
     * parameter with a default value of 10.
     * @param order The "order" parameter is used to specify the order in which the search results
     * should be returned. It is a string value that consists of two parts separated by a colon (":").
     * The first part specifies the field by which the results should be ordered (e.g. "updatedAt"),
     * and the second
     * @param parameter The parameter parameter is a string that represents the name of the job
     * attribute that the user wants to search for. For example, if the user wants to search for jobs
     * with a specific title, they would set parameter to "title".
     * @param value The value is the value of the parameter being passed in the request. For example,
     * in the case of the `state` parameter, the value would be the state being searched for. In the
     * case of the `offset` parameter, the value would be the number of results to skip before
     * returning the
     * @return A ResponseEntity object containing a JobDTO object is being returned.
     */
    @GetMapping("/{state}")
    fun searchByStateAndParam(
        @PathVariable(value = STATE, required = true) state: String,
        @RequestParam(value = OFFSET, required = false, defaultValue = "0") offset: Int,
        @RequestParam(value = LIMIT, required = false, defaultValue = "10") limit: Int,
        @RequestParam(value = ORDER, required = false, defaultValue = "updatedAt:DESC") order: String,
        @RequestParam(value = SEARCH_PARAMETER, required = false) parameter: String?,
        @RequestParam(value = SEARCH_VALUE, required = false) value: String?,
    ): ResponseEntity<JobDTO> {

        checkIfValidJobState(state)

        return ResponseEntity.ok(jobService.searchByStateAndParams(state, offset, limit, order, parameter, value))

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
     * needs to be updated. It is annotated with @PathVariable, which means that its value is extracted
     * from the URL path. The value of the id parameter is passed as a string.
     * @param changes The `changes` parameter is a request body parameter of type
     * `UpdateJobReceivedDTO`. It contains the updated information that needs to be applied to the job
     * with the given `id`. The `@RequestBody` annotation indicates that the parameter should be
     * deserialized from the request body.
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
     * of a POST request. The `@RequestBody` annotation indicates that the `jobs` parameter should be
     * populated with the JSON payload of the request. The method then passes this list of
     * `JobSignatureDTO
     * @return The function `receiveJobs` returns a `ResponseEntity` object with a generic type of
     * `Unit`. The `ResponseEntity` object contains an HTTP status code and an optional response body.
     * In this case, the HTTP status code is `200 OK` and the response body is the result of calling
     * the `createJobs` function of the `jobService` object with the `jobs` parameter.
     */
    @PostMapping("")
    fun receiveJobs(@RequestBody jobs: List<JobSignatureDTO>): ResponseEntity<Unit> {
        return ResponseEntity.ok(jobService.createJobs(jobs))
    }
}

/**
 * The function checks if a given job state is valid and throws an exception if it is not.
 *
 * @param state The `state` parameter is a string that represents the current state of a job. The
 * function `checkIfValidJobState` checks if the provided state is valid or not by comparing it with an
 * array of valid job states. If the provided state is not valid, it throws an `IllegalJob
 */
private fun checkIfValidJobState(state: String) {
    if (state !in arrayOf("SCHEDULED", "ENQUEUED", "PROCESSING", "FAILED", "SUCCEEDED", "DELETED"))
        throw IllegalJobState("$state is not a valid job state")
}