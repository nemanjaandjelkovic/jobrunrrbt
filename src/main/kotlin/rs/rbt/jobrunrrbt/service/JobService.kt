package rs.rbt.jobrunrrbt.service

import org.springframework.stereotype.Service
import rs.rbt.jobrunrrbt.repository.JobRepository

@Service
class JobService {

    lateinit var jobRepository: JobRepository


}