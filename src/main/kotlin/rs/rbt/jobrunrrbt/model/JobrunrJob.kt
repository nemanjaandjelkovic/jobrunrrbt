package rs.rbt.jobrunrrbt.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import rs.rbt.jobrunrrbt.helper.*
import java.time.Instant

@Entity
@Table(name = JOBRUNR_JOBS_TABLE_NAME)
open class JobrunrJob {
    @Id
    @Column(name = ID, nullable = false, length = 36)
    open var id: String? = null

    @Column(name = VERSION, nullable = false)
    open var version: Int? = null

    @Column(name = JOBASJSON, nullable = false, length = Integer.MAX_VALUE)
    open var jobasjson: String? = null

    @Column(name = JOBSIGNATURE, nullable = false, length = 512)
    open var jobsignature: String? = null

    @Column(name = STATE, nullable = false, length = 36)
    open var state: String? = null

    @Column(name = CREATEDAT, nullable = false)
    open var createdat: Instant? = null

    @Column(name = UPDATEDAT, nullable = false)
    open var updatedat: Instant? = null

    @Column(name = SCHEDULETAT)
    open var scheduledat: Instant? = null

    @Column(name = RECCURINGJOBID, length = 128)
    open var recurringjobid: String? = null
}