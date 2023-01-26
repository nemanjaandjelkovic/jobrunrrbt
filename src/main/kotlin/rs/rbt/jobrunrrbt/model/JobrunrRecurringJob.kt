package rs.rbt.jobrunrrbt.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "jobrunr_recurring_jobs")
open class JobrunrRecurringJob {
    @Id
    @Column(name = "id", nullable = false, length = 128)
    open var id: String? = null

    @Column(name = "version", nullable = false)
    open var version: Int? = null

    @Column(name = "jobasjson", nullable = false, length = Integer.MAX_VALUE)
    open var jobasjson: String? = null

    @Column(name = "createdat", nullable = false)
    open var createdat: Long? = null
}