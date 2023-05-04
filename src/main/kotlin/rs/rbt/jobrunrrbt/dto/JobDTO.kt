package rs.rbt.jobrunrrbt.dto

import rs.rbt.jobrunrrbt.model.JobJson

/**  A data transfer object (DTO) that is used to transfer data from the backend to the frontend. */
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