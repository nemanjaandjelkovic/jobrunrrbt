package rs.rbt.jobrunrrbt.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import rs.rbt.jobrunrrbt.helper.State
import java.time.LocalDateTime

@Entity
@Table(name = "nov_job_testiranje")
class Job (
    @Id
    @Column(name = "id")
    var id: String,
    @Column(name = "jobSignature")
    var jobSignature: String,
    @Column(name = "state")
    var state: State,
    @Column(name = "scheduledAt")
    var scheduledAt: LocalDateTime,

    )