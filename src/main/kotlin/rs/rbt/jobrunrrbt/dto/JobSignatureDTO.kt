package rs.rbt.jobrunrrbt.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import rs.rbt.jobrunrrbt.helper.*
import java.time.OffsetDateTime

/**
 * The JobSignatureDTO is a data class that contains information about a job's signature, arguments,
 * and time.
 * @property {String} jobSignature - jobSignature is a property of type String in the JobSignatureDTO
 * data class. It is annotated with @JsonProperty(JOB_SIGNATURE_CAMEL_CASE) which indicates that it is
 * the JSON property name for this field.
 * @property {Array<JobArgumentsDTO>} jobArguments - `jobArguments` is an array of `JobArgumentsDTO`
 * objects, which contain the arguments for the job. The `Array` type indicates that there can be
 * multiple arguments passed to the job. The `JobArgumentsDTO` class likely contains properties that
 * represent the specific arguments being passed to the job
 * @property {OffsetDateTime} jobTime - jobTime is a property of type OffsetDateTime that represents
 * the time at which a job was executed. It is annotated with @JsonProperty to specify the name of the
 * property in the JSON representation of the object. It is also annotated with @DateTimeFormat and
 * @JsonFormat to specify the format of the date
 */
data class JobSignatureDTO(
    @JsonProperty(JOB_SIGNATURE_CAMEL_CASE)
    val jobSignature: String,
    @JsonProperty(JOB_ARGUMENTS)
    val jobArguments: Array<JobArgumentsDTO>,
    @JsonProperty(JOB_DATE)
    @DateTimeFormat(style = PATTERN_SSSSSSZ)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[S][S][S][S][S][S]XXX")
    val jobTime: OffsetDateTime
)
