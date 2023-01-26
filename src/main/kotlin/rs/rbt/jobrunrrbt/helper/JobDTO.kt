package rs.rbt.jobrunrrbt.helper

import jakarta.persistence.Column
import jakarta.persistence.Id
import java.time.Instant
import java.time.LocalDateTime

class JobDTO (

    var id: String,
    var jobSignature: String,
    var state: String,
    var scheduledAt: Instant,

    )