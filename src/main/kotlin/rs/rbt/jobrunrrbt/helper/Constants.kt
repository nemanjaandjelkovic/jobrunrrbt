package rs.rbt.jobrunrrbt.helper

const val QUERY_ALL_TO_DTO_LIST: String = "select new rs.rbt.jobrunrrbt.helper.JobDTO(JobrunrJob .id,JobrunrJob .jobsignature,JobrunrJob .state,JobrunrJob .scheduledat)"
const val QUERY_ALL_TO_DTO_LIST_WHERE_STATE_MATCHES: String = "select new rs.rbt.jobrunrrbt.helper.JobDTO(JobrunrJob .id,JobrunrJob .jobsignature,JobrunrJob .state,JobrunrJob .scheduledat) from JobrunrJob where JobrunrJob .state = ?1"
const val FILTER_PARAM: String = "filterParam"
const val DATE_TIME_STYLE: String = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
