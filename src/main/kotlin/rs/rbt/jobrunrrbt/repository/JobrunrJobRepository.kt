package rs.rbt.jobrunrrbt.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.model.JobJson
import rs.rbt.jobrunrrbt.model.JobrunrJob
import java.util.*

interface JobrunrJobRepository : JpaRepository<JobrunrJob, String> {

    @Query("FROM JobrunrJob j")
    fun findAllJobs(): List<JobrunrJob>


    fun findJobrunrJobsByState(state: String):List<JobrunrJob>

    fun findJobrunrJobsByJobsignatureContains(string: String):List<JobrunrJob>

    fun findJobrunrJobsByJobsignatureStartsWith(string: String):List<JobrunrJob>
//"""
//        SELECT CASE
//                WHEN COUNT(cdr.id) > 0 THEN TRUE
//                ELSE FALSE END
//        FROM ChargeDetailRecordDbEntity cdr
//        WHERE cdr.dateOfReceipt <> :dateOfReceipt
//            AND ((cdr.externalCdrId = :externalCdrId AND (cdr.evcoId = :evcoId OR cdr.emaId = :emaId OR cdr.rfId = :rfId) AND cdr.evseId = :evseId)
//            AND (cdr.sessionStart = :sessionStart AND (cdr.evcoId = :evcoId OR cdr.emaId = :emaId OR cdr.rfId = :rfId) AND cdr.evseId = :evseId)
//        )
//    """
    @Query("(select j.jobasjson from jobrunr_jobs j where j.state =:state and j.jobsignature  like :value limit :limit offset :offset)", nativeQuery = true)
    fun searchByStateAndParam(
    @Param("state")state: String,
    @Param("offset")offset: Int,
    @Param("limit")limit: Int,
    //@Param("orderBy")order: String,
    //@Param("direction")direction: String,
    @Param("value")value: String
    ): MutableList<JobJson>

    @Modifying
    @Query("update JobrunrJob set jobsignature = ?2 where id = ?1")
    fun updateJobSignature(id: String, value: String)

    @Modifying
    @Query("update JobrunrJob set jobasjson = ?2 where id = ?1")
    fun updateJobAsJson(id: String, value: String)

//    @Query(QUERY_ALL_TO_DTO_LIST_WHERE_METHOD_MATCHES)
//    fun returnAllJobsWithMatchingMethod(string: String):List<JobrunrJob>

}