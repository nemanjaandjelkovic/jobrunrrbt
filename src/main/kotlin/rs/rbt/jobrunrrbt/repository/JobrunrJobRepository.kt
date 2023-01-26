package rs.rbt.jobrunrrbt.repository;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import rs.rbt.jobrunrrbt.helper.JobDTO
import rs.rbt.jobrunrrbt.model.JobrunrJob

interface JobrunrJobRepository : JpaRepository<JobrunrJob, String> {

    @Query("select new rs.rbt.jobrunrrbt.helper.JobDTO(JobrunrJob .id,JobrunrJob .jobsignature,JobrunrJob .state,JobrunrJob .scheduledat)")
    fun returnAllJobsForFront():List<JobDTO>

}