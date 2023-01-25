package rs.rbt.jobrunrrbt.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rs.rbt.jobrunrrbt.helper.JobDTO
import rs.rbt.jobrunrrbt.service.JobService

@RestController
@RequestMapping("/api")
class JobController {

    lateinit var jobService: JobService

    @GetMapping("all")
    fun sendListOfJobDTO(): List<JobDTO> {

        return jobService.returnListOfJobDTO()
    }




}