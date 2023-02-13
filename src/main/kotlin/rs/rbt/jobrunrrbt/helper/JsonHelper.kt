package rs.rbt.jobrunrrbt.helper

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import rs.rbt.jobrunrrbt.model.JobHistory
import rs.rbt.jobrunrrbt.model.JobJson
import java.text.SimpleDateFormat


fun serialize(obj: Any): String {

    val mapper = jacksonObjectMapper()
    mapper.findAndRegisterModules()

    return mapper.writeValueAsString(obj)

}

fun deserialize(json: String): JobJson {

    val mapper = jacksonObjectMapper()
    mapper.findAndRegisterModules()

    return mapper.readValue(json)
}

fun asdf(json: String): List<JobHistory> {

    val objectMapper = ObjectMapper()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    objectMapper.setDateFormat(dateFormat)

    return objectMapper.readValue(json)
}