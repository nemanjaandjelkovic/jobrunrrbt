package rs.rbt.jobrunrrbt.dto

/**  This class is a data transfer object (DTO) that contains information about the current page of data. */
data class PageInfoDTO(

    val total: Int,
    val totalPages: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean,

    )