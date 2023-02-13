package rs.rbt.jobrunrrbt.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.model.JobrunrJob
import java.time.Instant
import java.util.*

interface JobrunrJobRepository : JpaRepository<JobrunrJob, String> {

    @Query("FROM JobrunrJob j")
    fun findAllJobs(): List<JobrunrJob>
    fun findJobrunrJobsByState(state: String,pageable: Pageable):List<JobrunrJob>
    @Query(
        """select j from JobrunrJob j 
        where j.state = ?1 and j.jobsignature like concat('%', ?2, '%', '(', '%')"""
    )
    fun findJobsByClassAndMethod(state:String,value: String,pageable: Pageable):List<JobrunrJob>
    @Query(
        """select * from jobrunr_jobs where state =?1 and jobsignature ~ ?2""", nativeQuery = true
    )
    fun findJobsWhereClassMatches(state: String, regex: String,pageable: Pageable):MutableList<JobrunrJob>
    @Query(
        """select * from jobrunr_jobs where state =?1 and jobsignature ~ ?2""", nativeQuery = true
    )
    fun findJobsWhereMethodMatches(state: String,regex: String,pageable: Pageable):MutableList<JobrunrJob>

    @Query(
        """select count (*) from jobrunr_jobs j where j.state =?1 and j.jobsignature ~ ?2""", nativeQuery = true
    )
    fun countJobsWhereClassMatches(state: String, regex: String):Int
    @Query(
        """select count (*) from jobrunr_jobs j where j.state =?1 and j.jobsignature ~ ?2""", nativeQuery = true
    )
    fun countJobsWhereMethodMatches(state: String,regex: String):Int

    @Query(
        """select count(j) from JobrunrJob j 
        where j.state = ?1 and j.jobsignature like concat('%', ?2, '%', '(', '%')"""
    )
    fun countJobsByClassAndMethod(state:String,value: String):Int


    @Modifying
    @Query("update JobrunrJob set jobsignature = ?2 where id = ?1")
    fun updateJobSignature(id: String, value: String)

    @Modifying
    @Query("update JobrunrJob set jobasjson = ?2 where id = ?1")
    fun updateJobAsJson(id: String, value: String)

    @Modifying
    @Query("update JobrunrJob set scheduledat = ?2 where id = ?1")
    fun updateScheduledTime(id: String, value: Instant)
}