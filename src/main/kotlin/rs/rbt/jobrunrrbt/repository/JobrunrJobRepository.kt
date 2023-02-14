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
    fun findJobrunrJobsByState(state: String,pageable: Pageable):List<JobrunrJob>
    @Query(FIND_JOBS_BY_CLASS_AND_METHOD)
    fun findJobsByClassAndMethod(state:String,value: String,pageable: Pageable):List<JobrunrJob>
    @Query(FIND_JOBS_BY_CLASS)
    fun findJobsWhereClassMatches(state: String, regex: String,pageable: Pageable):MutableList<JobrunrJob>
    @Query(FIND_JOBS_BY_METHOD)
    fun findJobsWhereMethodMatches(state: String,regex: String,pageable: Pageable):MutableList<JobrunrJob>
    @Query(COUNT_JOBS_BY_CLASS_AND_METHOD)
    fun countJobsByClassAndMethod(state:String,value: String):Int
    @Query(COUNT_JOBS_BY_CLASS)
    fun countJobsWhereClassMatches(state: String, regex: String):Int
    @Query(COUNT_JOBS_BY_METHOD)
    fun countJobsWhereMethodMatches(state: String,regex: String):Int
    @Modifying
    @Query(UPDATE_SIGNATURE)
    fun updateJobSignature(id: String, value: String)
    @Modifying
    @Query(UPDATE_JOBASJSON)
    fun updateJobAsJson(id: String, value: String)
    @Modifying
    @Query(UPDATE_SCHEDULED_TIME)
    fun updateScheduledTime(id: String, value: Instant)
}