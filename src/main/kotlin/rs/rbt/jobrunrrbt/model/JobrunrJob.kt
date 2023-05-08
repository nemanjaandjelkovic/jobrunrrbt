package rs.rbt.jobrunrrbt.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import rs.rbt.jobrunrrbt.helper.*
import java.time.LocalDateTime

/** This is the Kotlin version of the JobrunrJob class. */
@Entity
@Table(name = JOBRUNR_JOBS_TABLE_NAME)
open class JobrunrJob {
    @Id
    @Column(name = ID, nullable = false)
    open var id: String? = null

    @Column(name = VERSION, nullable = false)
    open var version: Int? = null

    @Column(name = JOB_AS_JSON, nullable = false, length = Integer.MAX_VALUE)
    open var jobasjson: String? = null

    @Column(name = JOB_SIGNATURE, nullable = false, length = 512)
    open var jobsignature: String? = null

    @Column(name = STATE, nullable = false, length = 36)
    open var state: String? = null

    @Column(name = CREATED_AT, nullable = false)
    open var createdat: LocalDateTime? = null

    @Column(name = UPDATED_AT, nullable = false)
    open var updatedat: LocalDateTime? = null

    @Column(name = SCHEDULED_AT)
    open var scheduledat: LocalDateTime? = null

    @Column(name = RECURRING_JOB_ID, length = 128)
    open var recurringjobid: String? = null
}