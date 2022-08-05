package com.example.controllers

import io.micronaut.core.annotation.Introspected
import io.micronaut.core.io.scan.ClassPathResourceLoader
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.http.server.types.files.StreamedFile
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller("licenses")
@Secured(SecurityRule.IS_AUTHENTICATED)
class LicensesController(
    private val loader: ClassPathResourceLoader,
) {

    @Post("/")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    fun getLicenses(@Body request: GetLicensesRequest): ByteArray {
        logger.info("POST /licenses [$request]")
        return when (request.keys.size) {
            0 -> throw HttpStatusException(HttpStatus.BAD_REQUEST, "Missing keys")
            1 -> {
                loader.getResourceAsStream("sample-license.txt").get().readBytes()
            }
            else -> {
                loader.getResourceAsStream("sample-archive.zip").get().readBytes()
            }
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LicensesController::class.java)
    }

}

@Introspected
data class GetLicensesRequest(
    val hwid: String,
    val keys: List<String>,
)
