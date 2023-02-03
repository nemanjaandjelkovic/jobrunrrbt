package rs.rbt.jobrunrrbt.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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

        val lista: List<JobrunrJob> = jobrunrJobRepository.findAllJobs()
        val lista2: MutableList<JobJson?> = mutableListOf()

        for ( a in lista ) {
            lista2.add(deserialize(a.jobasjson!!))
        }
        return lista2

    }

    fun returnAllJobsWhereStateMatches(string: String): MutableList<JobJson> {

        val lista = jobrunrJobRepository.findJobrunrJobsByState(string)
        val lista2: MutableList<JobJson> = mutableListOf()

        for ( a in lista ) {
            lista2.add(deserialize(a.jobasjson!!))
        }

        return lista2

    }

    fun returnAllJobsWhereClassMatches(string: String): MutableList<JobJson> {

        val lista: List<JobrunrJob> = jobrunrJobRepository.findJobrunrJobsByJobsignatureStartsWith(string)
        val lista2: MutableList<JobJson> = mutableListOf()

        for ( a in lista ) {

            lista2.add(deserialize(a.jobasjson!!))
        }
        return lista2

    }

    fun returnAllJobsWhereClassAndMethodMatch(string: String): MutableList<JobJson> {

        val lista = jobrunrJobRepository.findJobrunrJobsByJobsignatureContains(string)
        val lista2: MutableList<JobJson> = mutableListOf()

        for ( a in lista ) {
            lista2.add(deserialize(a.jobasjson!!))
        }

        return lista2

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

    fun searchByStateAndParam(state: String,offset: Int,limit: Int,order: String, value: String): MutableList<JobJson> {

        val orderByList: List<String> = order.split(':')
        val order: String = orderByList[0]
        val direction: String = orderByList[1]

        println(state)
        println(offset)
        println(limit)
        println(order)
        println(value)

        return jobrunrJobRepository.searchByStateAndParam(state,offset,limit,value)



    }

//    fun returnWhereMethodMathesListOfJobDTO(string: String): List<JobDTO> {
//
//        return jobrunrJobRepository.returnAllJobsWithMatchingMethod(string)
//    }

}