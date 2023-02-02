package rs.rbt.jobrunrrbt.helper

import jakarta.persistence.Column
import jakarta.persistence.Id
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class JobDTO (

    var id: UUID,
    var jobSignature: String,
    var state: String,
    var scheduledAt: Instant,

    )