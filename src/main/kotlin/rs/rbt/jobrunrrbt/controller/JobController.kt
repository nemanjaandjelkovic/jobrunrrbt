package rs.rbt.jobrunrrbt.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.model.JobJson
import rs.rbt.jobrunrrbt.service.JobService


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
    @GetMapping("/class")
    fun sendFilteredByClass(@RequestParam(value = FILTER_PARAM, required = true) filterParam: String): MutableList<JobJson> {

        return jobService.returnAllJobsWhereClassMatches(filterParam)
    }
    @Transactional
    @PostMapping("/updateClass")
    fun updateJobClass(@RequestParam(value = ID, required = true) id: String, @RequestParam(value = VALUE, required = true) value: String) {

        jobService.updateJobPackage(id,value)
    }
//    @GetMapping("/method")
//    fun sendFilteredByMethod(@RequestParam(value = FILTER_PARAM, required = true) filterParam: String): List<JobDTO> {
//
//
//        return jobService.returnWhereMethodMathesListOfJobDTO(filterParam)
//    }
    @Transactional
    @PostMapping("/updateMethod")
    fun updateJobMethod(@RequestParam(value = ID, required = true) id: String, @RequestParam(value = VALUE, required = true) value: String) {

        jobService.updateJobMethod(id,value)
    }

    @GetMapping("/search")
    fun searchByStateAndParam(
        @RequestParam(value = "state", required = true) state: String,
        @RequestParam(value = "offset", required = true) offset: Int,
        @RequestParam(value = "limit", required = true) limit: Int,
        @RequestParam(value = "order", required = true) order: String,
        @RequestParam(value = "searchParameter", required = true) parameter: String,
        @RequestParam(value = "searchValue", required = true) value: String,
        ): MutableList<JobJson> {

        println(limit)
        var state = "'$state'"

        when (parameter) {

            "Class" -> {
                val value2: String = "'%.%$value%('"
                return jobService.searchByStateAndParam(state,offset,limit,order,value2)

            }
            "Method" -> {
                val value2: String = "'%$value%('"
                return jobService.searchByStateAndParam(state,offset,limit,order,value2)

            }
            else -> {
                val value2: String = "'%$value%('"
                return jobService.searchByStateAndParam(state,offset,limit,order,value2)

            }
        }
        //return jobService.searchByStateAndParam(state, offset, limit, order, value)

    }

//    state: DELETED
//    offset: 0
//    limit: 20
//    order: updatedAt:ASC
//    searchParameter:parameter,
//    searchValue:value

}