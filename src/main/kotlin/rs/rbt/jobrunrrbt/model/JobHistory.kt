package rs.rbt.jobrunrrbt.model

import rs.rbt.jobrunrrbt.helper.State
import java.time.LocalDateTime

class JobHistory (
    var atClass: String,
    var state: State,
    var createdAt: LocalDateTime,
    var scheduledAt: LocalDateTime,
    var recurringJobId: String?,
    var reason: String?,

    )