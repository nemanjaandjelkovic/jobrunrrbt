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

    //@Modifying
    //@Query("update EARAttachment ear set ear.status = ?1 where ear.id = ?2")
    //int setStatusForEARAttachment(Integer status, Long id);

    @Modifying
    @Query(UPDATE_JOB_SIGNATURE)
    fun updateJobSignature(id: String, value: String)

    @Modifying
    @Query(UPDATE_JOBASJSON)
    fun updateJobAsJson(id:String, value: String)

}