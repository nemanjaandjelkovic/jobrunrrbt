package rs.rbt.jobrunrrbt.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.model.JobrunrJob
import java.util.*

interface JobrunrJobRepository : JpaRepository<JobrunrJob, UUID> {

    @Query("FROM JobrunrJob j")
    fun findAllJobs(): List<JobrunrJob>


    fun findJobrunrJobsByState(state: String):List<JobrunrJob>

    fun findJobrunrJobsByJobsignatureContains(string: String):List<JobrunrJob>
    @Modifying
    @Query("update JobrunrJob set jobsignature = ?2 where id = ?1")
    fun updateJobSignature(id: UUID, value: String)

    @Modifying
    @Query("update JobrunrJob set jobasjson = ?2 where id = ?1")
    fun updateJobAsJson(id: UUID, value: String)

//    @Query(QUERY_ALL_TO_DTO_LIST_WHERE_METHOD_MATCHES)
//    fun returnAllJobsWithMatchingMethod(string: String):List<JobrunrJob>

}