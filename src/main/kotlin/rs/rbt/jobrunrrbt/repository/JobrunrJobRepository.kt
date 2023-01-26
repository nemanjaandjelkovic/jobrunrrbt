package rs.rbt.jobrunrrbt.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.model.JobrunrJob

interface JobrunrJobRepository : JpaRepository<JobrunrJob, String> {

    @Query(QUERY_ALL_TO_DTO_LIST)
    fun returnAllJobsForFront():List<JobDTO>

    @Query(QUERY_ALL_TO_DTO_LIST_WHERE_STATE_MATCHES)
    fun returnAllJobsWithMatchingState(state: String):List<JobDTO>

}