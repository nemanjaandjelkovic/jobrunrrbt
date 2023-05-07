package rs.rbt.jobrunrrbt.dto

data class PageInfo(
    val total: Int,
    val totalPages: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean,

    )