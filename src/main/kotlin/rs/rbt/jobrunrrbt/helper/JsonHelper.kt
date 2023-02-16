package rs.rbt.jobrunrrbt.helper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import rs.rbt.jobrunrrbt.model.JobJson

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