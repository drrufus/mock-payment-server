package com.example.controllers

import com.example.dto.Subscription
import com.example.services.PauseException
import com.example.services.SubscriptionsService
import com.example.services.UnpauseException
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller("/subscriptions")
@Secured(SecurityRule.IS_AUTHENTICATED)
class SubscriptionsController(
    private val subscriptionsService: SubscriptionsService,
) {

    @Get
    fun getUserSubscriptions(@QueryValue userId: Long): List<Subscription> {
        logger.info("GET /subscriptions?userId=$userId")
        return subscriptionsService.getUserSubscriptions(userId)
    }

    @Post("/{id}/pause")
    fun pauseSubscription(@PathVariable id: Long) {
        logger.info("POST /subscriptions/$id/pause")
        try {
            subscriptionsService.pauseSubscription(id)
        } catch (e: PauseException) {
            throw HttpStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @Post("/{id}/unpause")
    fun unpauseSubscription(@PathVariable id: Long) {
        logger.info("POST /subscriptions/$id/unpause")
        try {
            subscriptionsService.unpauseSubscription(id)
        } catch (e: UnpauseException) {
            throw HttpStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SubscriptionsController::class.java)
    }

}
