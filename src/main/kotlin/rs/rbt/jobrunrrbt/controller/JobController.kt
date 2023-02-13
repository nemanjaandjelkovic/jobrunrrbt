package rs.rbt.jobrunrrbt.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.service.JobService
import java.time.Instant


@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api")
class JobController {

    @Autowired
    lateinit var jobService: JobService

    @GetMapping("/state")
    fun sendFilteredByState(
        @RequestParam(value = "state", required = true) state: String,
        @RequestParam(value = "offset", required = true) offset: Int,
        @RequestParam(value = "limit", required = true) limit: Int,
        @RequestParam(value = "order", required = true) order: String
    ): JobDTO {

        return jobService.returnAllJobsWhereStateMatches(state, offset, limit, order)
    }

    @GetMapping("/search")
    fun searchByStateAndParam(
        @RequestParam(value = "state", required = true) state: String,
        @RequestParam(value = "offset", required = true) offset: Int,
        @RequestParam(value = "limit", required = true) limit: Int,
        @RequestParam(value = "order", required = true) order: String,
        @RequestParam(value = "searchParameter", required = true) parameter: String,
        @RequestParam(value = "searchValue", required = true) value: String,
    ): Any {

        when (parameter) {

            "Class" -> {
                return jobService.returnAllJobsWhereClassMatches(state, value, offset/limit, limit, order)

            }

            "Method" -> {
                return jobService.returnAllJobsWhereMethodMatches(state, value, offset/limit, limit, order)

            }

            else -> {
                return jobService.returnAllJobsWhereClassOrMethodMatch(state, value, offset/limit, limit, order)
            }
        }
    }

    @Transactional
    @PostMapping("/update")
    fun updateJob(
        @RequestParam(value = ID, required = true) id: String,
        @RequestParam(value = "packageName", required = true) packageName: String,
        @RequestParam(value = "methodName", required = true) methodName: String,
        @RequestParam(value = "className", required = true) className: String,
       @RequestParam(value = "scheduledTime", required = false) scheduledTime: Instant?
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