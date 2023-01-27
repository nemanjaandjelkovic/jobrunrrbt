package rs.rbt.jobrunrrbt.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import rs.rbt.jobrunrrbt.helper.FILTER_PARAM
import rs.rbt.jobrunrrbt.helper.JobDTO
import rs.rbt.jobrunrrbt.service.JobService

@RestController
@RequestMapping("/api")
class JobController {

    lateinit var jobService: JobService

    @GetMapping("/all")
    fun sendListOfJobDTO(): List<JobDTO> {

        return jobService.returnListOfJobDTO()
    }

    @GetMapping("/state")
    fun sendFilteredByState(@RequestParam(value = FILTER_PARAM, required = true) filterParam: String): List<JobDTO> {

        return jobService.returnWhereStateMatchesListOfJobDTO(filterParam)
    }

    @GetMapping("/class")
    fun sendFilteredByClass(@RequestParam(value = FILTER_PARAM, required = true) filterParam: String): List<JobDTO> {

        return jobService.returnWhereClassMAtchesListOfJobDTO(filterParam)
    }

}