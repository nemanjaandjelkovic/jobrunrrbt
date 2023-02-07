package rs.rbt.jobrunrrbt.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Service
import rs.rbt.jobrunrrbt.helper.JobDTO
import rs.rbt.jobrunrrbt.helper.deserialize
import rs.rbt.jobrunrrbt.helper.serialize
import rs.rbt.jobrunrrbt.model.JobJson
import rs.rbt.jobrunrrbt.model.JobrunrJob
import rs.rbt.jobrunrrbt.repository.JobrunrJobRepository
import java.util.*

@Service
class JobService {

    @Autowired
    lateinit var jobrunrJobRepository: JobrunrJobRepository

    fun returnAllJobs(): MutableList<JobJson?> {

        val jobList: List<JobrunrJob> = jobrunrJobRepository.findAllJobs()
        val returnList: MutableList<JobJson?> = mutableListOf()

        for (job in jobList) {
            returnList.add(deserialize(job.jobasjson!!))
        }
        return returnList

    }

    fun returnAllJobsWhereStateMatches(string: String): MutableList<JobJson> {

        val jobList = jobrunrJobRepository.findJobrunrJobsByState(string)
        val returnList: MutableList<JobJson> = mutableListOf()

        for (job in jobList) {
            returnList.add(deserialize(job.jobasjson!!))
        }

        return returnList

    }

    fun returnAllJobsWhereClassMatches(state: String, value: String, offset: Int, limit: Int): JobDTO {

        val jobList: List<JobrunrJob> = jobrunrJobRepository.findJobsWhereClassMatches(state,value,PageRequest.of(offset,limit))
        val returnList: MutableList<JobJson> = mutableListOf()

        for (job in jobList) {

            returnList.add(deserialize(job.jobasjson!!))
        }
        val total = jobrunrJobRepository.countJobsWhereClassMatches(state, value)
        val totalPages = (total-1) / limit + 1
        val hasNext = offset<totalPages
        val hasPrevious = offset>0

        return JobDTO(
            offset,
            hasNext,
            hasPrevious,
            returnList,
            limit,
            offset,
            total,
            totalPages
        )
    }

    fun returnAllJobsWhereClassOrMethodMatch(state: String,value: String, offset: Int, limit: Int): JobDTO {

        val jobList = jobrunrJobRepository.findJobsByClassAndMethod(state,value, PageRequest.of(offset, limit))
        val returnList: MutableList<JobJson> = mutableListOf()

        for (job in jobList) {
            returnList.add(deserialize(job.jobasjson!!))
        }

        val total = jobrunrJobRepository.countJobsByClassAndMethod(state, value)
        val totalPages = (total-1) / limit + 1
        val hasNext = offset<totalPages
        val hasPrevious = offset>0

        return JobDTO(
            offset,
            hasNext,
            hasPrevious,
            returnList,
            limit,
            offset,
            total,
            totalPages
        )

    }

    fun returnAllJobsWhereMethodMatches(state: String, value: String, offset: Int, limit: Int): JobDTO {

        var regex = "^.+[A-Z].+\$" // i tu negde add value
        val jobList = jobrunrJobRepository.findJobsWhereMethodMatches(state, regex, PageRequest.of(offset, limit))
        val returnList: MutableList<JobJson> = mutableListOf()

        for (job in jobList) {
            returnList.add(deserialize(job.jobasjson!!))
        }

        val total = jobrunrJobRepository.countJobsWhereMethodMatches(state, regex)
        val totalPages = (total-1) / limit + 1
        val hasNext = offset<totalPages
        val hasPrevious = offset>0

        return JobDTO(
            offset,
            hasNext,
            hasPrevious,
            returnList,
            limit,
            offset,
            total,
            totalPages
        )
    }

    fun updateJobPackage(id: String, newClassName: String) {

        if (jobrunrJobRepository.existsById(id)) {

            val job: Optional<JobrunrJob> = jobrunrJobRepository.findById(id)
            val jobJson: JobJson = deserialize(job.get().jobasjson!!)
            val newJobSignature: String = newClassName.plus(".")
                .plus(jobJson.jobDetails.methodName)
                .plus("(")
                .plus(jobJson.jobDetails.jobParameters)
                .plus(")")

            jobJson.jobDetails.className = newClassName
            jobJson.jobSignature = newJobSignature

            val newJobJson: String = serialize(jobJson)

            jobrunrJobRepository.updateJobSignature((id), newJobSignature)
            jobrunrJobRepository.updateJobAsJson(id, newJobJson)

        }
    }

    fun updateJobMethod(id: String, newMethodName: String) {

        if (jobrunrJobRepository.existsById(id)) {

            val job: Optional<JobrunrJob> = jobrunrJobRepository.findById(id)
            val jobJson: JobJson = deserialize(job.get().jobasjson!!)
            val newJobSignature: String = jobJson.jobDetails.className.plus(".")
                .plus(newMethodName)
                .plus("(")
                .plus(jobJson.jobDetails.jobParameters)
                .plus(")")

            jobJson.jobDetails.methodName = newMethodName
            jobJson.jobSignature = newJobSignature

            val newJobJson: String = serialize(jobJson)

            jobrunrJobRepository.updateJobSignature(id, newJobSignature)
            jobrunrJobRepository.updateJobAsJson(id, newJobJson)

        }
    }
}