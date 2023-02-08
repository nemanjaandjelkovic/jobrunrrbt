package rs.rbt.jobrunrrbt.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.model.JobJson
import rs.rbt.jobrunrrbt.service.JobService
import java.time.Instant


@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api")
class JobController {

    @Autowired
    lateinit var jobService: JobService

    @GetMapping("/all")
    fun sendListOfJobDTO(): MutableList<JobJson?> {

        return jobService.returnAllJobs()
    }

    @GetMapping("/state")
    fun sendFilteredByState(@RequestParam(value = FILTER_PARAM, required = true) filterParam: String): MutableList<JobJson> {

        return jobService.returnAllJobsWhereStateMatches(filterParam)
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
                return jobService.returnAllJobsWhereClassMatches(state,value,offset,limit)

            }
            "Method" -> {
                return jobService.returnAllJobsWhereMethodMatches(state,value,offset,limit)

            }
            else -> {
                return jobService.returnAllJobsWhereClassOrMethodMatch(state,value,offset,limit)
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
        @RequestParam(value = "scheduledTime", defaultValue = "ne treba") scheduledTime: String// treba da bude Instant
        ) {


        when (scheduledTime) {
            "ne treba" -> {
                jobService.updateJob(id, packageName, methodName, className)
            }
            else -> {
                jobService.updateJobWithTime(id, packageName, methodName, className, scheduledTime)

            }
        }
    }
}