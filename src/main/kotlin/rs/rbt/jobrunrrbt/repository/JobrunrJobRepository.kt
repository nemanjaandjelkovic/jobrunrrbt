package rs.rbt.jobrunrrbt.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.model.JobrunrJob

interface JobrunrJobRepository : JpaRepository<JobrunrJob, String> {

    @Query(QUERY_ALL_TO_DTO_LIST)
    fun returnAllJobsForFront():List<JobDTO>

    @Query(QUERY_ALL_TO_DTO_LIST_WHERE_STATE_MATCHES)
    fun returnAllJobsWithMatchingState(state: String):List<JobDTO>

    @Query(QUERY_ALL_TO_DTO_LIST_WHERE_CLASS_MATCHES)
    fun returnAllJobsWithMatchingClass(string: String):List<JobDTO>

    @Modifying
    @Query(UPDATE_JOB_SIGNATURE)
    fun updateJobSignature(id: String, value: String)

    @Modifying
    @Query(UPDATE_JOBASJSON)
    fun updateJobAsJson(id:String, value: String)

    @Query(QUERY_ALL_TO_DTO_LIST_WHERE_METHOD_MATCHES)
    fun returnAllJobsWithMatchingMethod(string: String):List<JobDTO>

}