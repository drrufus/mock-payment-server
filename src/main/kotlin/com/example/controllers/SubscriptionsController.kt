package com.example.controllers

import com.example.dto.Subscription
import com.example.services.CancelException
import com.example.services.SubscriptionsService
import com.example.services.UncancelException
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

    @Get("/{id}")
    fun getSubscriptionById(@PathVariable id: Long): Subscription {
        logger.info("GET /subscriptions/$id")
        return subscriptionsService.getSubscriptionById(id)
            ?: throw HttpStatusException(HttpStatus.NOT_FOUND, "Subscription #$id not found")
    }

    @Post("/{id}/cancel")
    fun cancelSubscription(@PathVariable id: Long) {
        logger.info("POST /subscriptions/$id/cancel")
        try {
            subscriptionsService.cancelSubscription(id)
        } catch (e: CancelException) {
            throw HttpStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @Post("/{id}/uncancel")
    fun uncancelSubscription(@PathVariable id: Long) {
        logger.info("POST /subscriptions/$id/uncancel")
        try {
            subscriptionsService.uncancelSubscription(id)
        } catch (e: UncancelException) {
            throw HttpStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SubscriptionsController::class.java)
    }

}
