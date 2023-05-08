package rs.rbt.jobrunrrbt.dto

import rs.rbt.jobrunrrbt.model.JobJson

data class JobDTO(
    var currentPage: Int,
    var hasNext: Boolean,
    var hasPrevious: Boolean,
    var items: List<JobJson>,
    var limit: Int,
    var offset: Int,
    var total: Int,
    var totalPages: Int,

    )