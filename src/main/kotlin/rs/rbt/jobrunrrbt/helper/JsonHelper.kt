package rs.rbt.jobrunrrbt.helper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import rs.rbt.jobrunrrbt.model.JobJson
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * It takes an object and returns a JSON string
 *
 * @param obj Any - The object to serialize
 * @return A string
 */
fun serialize(obj: Any): String {

    val mapper = jacksonObjectMapper().apply {
        registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .
                build()
        )
        registerModule(JavaTimeModule())
    }
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

    val mapper = jacksonObjectMapper().apply {
        registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
        registerModule(JavaTimeModule())
    }
    mapper.findAndRegisterModules()

    return mapper.readValue(json)
}

class CustomOffsetDateTimeDeserializer : JsonDeserializer<OffsetDateTime>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): OffsetDateTime? {
        val str = p?.text?.trim()
        if (str.isNullOrEmpty()) {
            return null
        }
        return try {
            OffsetDateTime.parse(str)
        } catch (e: DateTimeParseException) {
            try {
                val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                LocalDateTime.parse(str, formatter).atOffset(ZoneOffset.UTC)
            } catch (e: DateTimeParseException) {
                null
            }
        }
    }
}

class CustomLocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalDateTime? {
        val str = p?.text?.trim()
        if (str.isNullOrEmpty()) {
            return null
        }
        return try {
            LocalDateTime.parse(str)
        } catch (e: DateTimeParseException) {
            null
        }
    }
}
