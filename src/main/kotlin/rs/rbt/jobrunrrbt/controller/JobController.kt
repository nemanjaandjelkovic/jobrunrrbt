package rs.rbt.jobrunrrbt.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import rs.rbt.jobrunrrbt.dto.JobDTO
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
        @RequestParam(value = STATE, required = true) state: String,
        @RequestParam(value = OFFSET, required = true) offset: Int,
        @RequestParam(value = LIMIT, required = true) limit: Int,
        @RequestParam(value = ORDER, required = true) order: String
    ): JobDTO {
        return jobService.returnAllJobsWhereStateMatches(state, offset, limit, order)
    }

    @GetMapping("/search")
    fun searchByStateAndParam(
        @RequestParam(value = STATE, required = true) state: String,
        @RequestParam(value = OFFSET, required = true) offset: Int,
        @RequestParam(value = LIMIT, required = true) limit: Int,
        @RequestParam(value = ORDER, required = true) order: String,
        @RequestParam(value = SEARCH_PARAMETER, required = true) parameter: String,
        @RequestParam(value = SEARCH_VALUE, required = true) value: String,
    ): Any {
        when (parameter) {
            CLASS -> {
                return jobService.returnAllJobsWhereClassMatches(state, value, offset / limit, limit, order)
            }

            METHOD -> {
                return jobService.returnAllJobsWhereMethodMatches(state, value, offset / limit, limit, order)
            }

            else -> {
                return jobService.returnAllJobsWhereClassOrMethodMatch(state, value, offset / limit, limit, order)
            }
        }
    }

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