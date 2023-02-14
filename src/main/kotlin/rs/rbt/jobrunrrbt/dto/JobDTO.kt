package rs.rbt.jobrunrrbt.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Id
import rs.rbt.jobrunrrbt.model.JobJson
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class JobDTO (

    var currentPage: Int,
    var hasNext: Boolean,
    var hasPrevious: Boolean,
    var items: List<JobJson>,
    var limit: Int,
    var offset: Int,
    var total: Int,
    var totalPages: Int,

    )