package rs.rbt.jobrunrrbt.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import rs.rbt.jobrunrrbt.helper.*
import rs.rbt.jobrunrrbt.model.JobrunrJob
import java.util.*

interface JobrunrJobRepository : JpaRepository<JobrunrJob, String> {

    /** This function is querying the database to find all Jobrunr jobs with a specific job signature
    that are in the "SCHEDULED" state. It uses a native SQL query with
    a parameterized job signature value. The function returns a list of JobrunrJob objects that
    match the query criteria. */
    @Query(
        """select * from jobrunr_jobs j 
        where j.jobsignature = ?1 and j.state = 'SCHEDULED'""", nativeQuery = true
    )
    fun findJobrunrJobsBySignatureIfScheduled(jobSignature: String): List<JobrunrJob>

    /**
     * `findJobrunrJobsByState` returns a `List<JobrunrJob>` and takes a `String` and a `Pageable` as
     * parameters.
     *
     * @param state The state of the job.
     * @param pageable The pageable object is used to specify the page number and the page size.
     */
    fun findJobrunrJobsByState(state: String, pageable: Pageable): List<JobrunrJob>

    fun countJobrunrJobsByState(state: String):Int

    /**
     * Find all jobs with a given state and a given method name
     *
     * @param state The state of the job (e.g. 'SCHEDULED', 'ENQUEUED', 'SUCCEEDED', 'FAILED',
     * 'DELETED')
     * @param value The class name of the job
     * @param pageable The pageable object is used to specify the page number and the page size.
     */
    @Query(
        """select j from JobrunrJob j 
        where j.state = ?1 and j.jobsignature like concat('%', ?2, '%', '(', '%')"""
    )
    fun findJobsByClassAndMethod(state: String, value: String, pageable: Pageable): List<JobrunrJob>

    /**  It selects all the jobs where the state is equal to the first parameter and the jobsignature
    matches the regex of the second parameter. */
    @Query(
        """select * from jobrunr_jobs where state =?1 and jobsignature ~ ?2""", nativeQuery = true
    )
    fun findJobsWhereClassMatches(state: String, regex: String, pageable: Pageable): MutableList<JobrunrJob>

    /**  A query that selects all the jobs where the state is equal to the first parameter and the
    jobsignature matches the regex of the second parameter. */
    @Query(
        """select * from jobrunr_jobs where state =?1 and jobsignature ~ ?2""", nativeQuery = true
    )
    fun findJobsWhereMethodMatches(state: String, regex: String, pageable: Pageable): MutableList<JobrunrJob>

    @Query(
        """select distinct jobsignature from jobrunr_jobs""", nativeQuery = true
    )
    fun findUniqueJobSignatures(): MutableList<String>

    /**  It counts the number of jobs where the state is equal to the first parameter and the
    jobsignature
    matches the regex of the second parameter. */
    @Query(
        """select count (*) from jobrunr_jobs j where j.state =?1 and j.jobsignature ~ ?2""", nativeQuery = true
    )
    fun countJobsWhereClassMatches(state: String, regex: String): Int

    /**  Counting the number of jobs where the state is equal to the first parameter and the jobsignature
    matches the regex of the second parameter. */
    @Query(
        """select count (*) from jobrunr_jobs j where j.state =?1 and j.jobsignature ~ ?2""", nativeQuery = true
    )
    fun countJobsWhereMethodMatches(state: String, regex: String): Int

    /**
     *  Counts the number of jobs by state and class and method
     *
     * @param state The state of the job, e.g. `Scheduled`, `Enqueued`, `Processing`, `Succeeded`, `Failed`
     * or `Deleted`
     * @param value The value of the parameter
     */
    @Query(
        """select count(j) from JobrunrJob j 
        where j.state = ?1 and j.jobsignature like concat('%', ?2, '%', '(', '%')"""
    )
    fun countJobsByClassAndMethod(state: String, value: String): Int

}