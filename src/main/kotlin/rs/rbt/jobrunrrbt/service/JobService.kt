package rs.rbt.jobrunrrbt.service

import rs.rbt.jobrunrrbt.helper.JobDTO
import rs.rbt.jobrunrrbt.repository.JobrunrJobRepository

class JobService {

    lateinit var jobrunrJobRepository: JobrunrJobRepository

    fun returnListOfJobDTO(): List<JobDTO> {

        return jobrunrJobRepository.returnAllJobsForFront()

    }

}