package rs.rbt.jobrunrrbt.helper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import rs.rbt.jobrunrrbt.model.JobJson

/**
 * It takes an object and returns a JSON string
 * 
 * @param obj Any - The object to serialize
 * @return A string
 */
fun serialize(obj: Any): String {

    val mapper = jacksonObjectMapper()
    mapper.findAndRegisterModules()

    return mapper.writeValueAsString(obj)
}
/**
 * It takes a JSON string and returns a JobJson object
 * 
 * @param json String - The JSON string to deserialize
 * @return JobJson
 */

fun deserialize(json: String): JobJson {

    val mapper = jacksonObjectMapper()
    mapper.findAndRegisterModules()

    return mapper.readValue(json)
}