package rs.rbt.jobrunrrbt.controller

import org.springframework.web.bind.annotation.*
import rs.rbt.jobrunrrbt.helper.FILTER_PARAM
import rs.rbt.jobrunrrbt.helper.ID
import rs.rbt.jobrunrrbt.helper.JobDTO
import rs.rbt.jobrunrrbt.helper.VALUE
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

    @PostMapping("/updateMethod")
    fun updateJobClass(@RequestParam(value = ID, required = true) id: String, @RequestParam(value = VALUE, required = true) value: String) {

        jobService.updateJobMethod(id,value)
    }


}