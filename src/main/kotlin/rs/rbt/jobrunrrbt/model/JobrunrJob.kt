package rs.rbt.jobrunrrbt.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "jobrunr_jobs")
open class JobrunrJob {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    open var id: String? = null

    @Column(name = "version", nullable = false)
    open var version: Int? = null

    @Column(name = "jobasjson", nullable = false, length = Integer.MAX_VALUE)
    open var jobasjson: String? = null

    @Column(name = "jobsignature", nullable = false, length = 512)
    open var jobsignature: String? = null

    @Column(name = "state", nullable = false, length = 36)
    open var state: String? = null

    @Column(name = "createdat", nullable = false)
    open var createdat: Instant? = null

    @Column(name = "updatedat", nullable = false)
    open var updatedat: Instant? = null

    @Column(name = "scheduledat")
    open var scheduledat: Instant? = null

    @Column(name = "recurringjobid", length = 128)
    open var recurringjobid: String? = null
}