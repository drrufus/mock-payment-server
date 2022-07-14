package com.example.controllers

import io.micronaut.core.annotation.Introspected
import io.micronaut.core.io.scan.ClassPathResourceLoader
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.http.server.types.files.StreamedFile
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.serde.annotation.Serdeable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller("licenses")
@Secured(SecurityRule.IS_AUTHENTICATED)
class LicensesController(
    private val loader: ClassPathResourceLoader,
) {

    @Post("/")
    fun getLicenses(@Body request: GetLicensesRequest): StreamedFile {
        logger.info("POST /licenses [$request]")
        return when (request.keys.size) {
            0 -> throw HttpStatusException(HttpStatus.BAD_REQUEST, "Missing keys")
            1 -> {
                val stream = loader.getResourceAsStream("sample-license.txt").get()
                StreamedFile(stream, MediaType.TEXT_PLAIN_TYPE)
            }
            else -> {
                val stream = loader.getResourceAsStream("sample-archive.zip").get()
                StreamedFile(stream, MediaType.APPLICATION_OCTET_STREAM_TYPE)
            }
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LicensesController::class.java)
    }

}

@Introspected
@Serdeable
data class GetLicensesRequest(
    val hwid: String,
    val keys: List<String>,
)
