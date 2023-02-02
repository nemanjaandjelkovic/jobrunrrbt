package rs.rbt.jobrunrrbt.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.model.JobJson
import rs.rbt.jobrunrrbt.model.JobrunrJob
import rs.rbt.jobrunrrbt.service.JobService
import java.util.UUID

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
    @GetMapping("/class")
    fun sendFilteredByClass(@RequestParam(value = FILTER_PARAM, required = true) filterParam: String): MutableList<JobJson?> {

        return jobService.returnAllJobsWhereClassMatches(filterParam)
    }

//    @PostMapping("/updateClass")
//    fun updateJobClass(@RequestParam(value = ID, required = true) id: String, @RequestParam(value = VALUE, required = true) value: String) {
//
//        jobService.updateJobPackage(id,value)
//    }
//
//    @GetMapping("/method")
//    fun sendFilteredByMethod(@RequestParam(value = FILTER_PARAM, required = true) filterParam: String): List<JobDTO> {
//
//
//        return jobService.returnWhereMethodMathesListOfJobDTO(filterParam)
//    }
//
//    @PostMapping("/updateMethod")
//    fun updateJobMethod(@RequestParam(value = ID, required = true) id: String, @RequestParam(value = VALUE, required = true) value: String) {
//
//        jobService.updateJobMethod(id,value)
//    }

}