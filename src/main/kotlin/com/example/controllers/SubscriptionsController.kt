package com.example.controllers

import com.example.dto.Subscription
import com.example.services.SubscriptionsService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller("/subscriptions")
@Secured(SecurityRule.IS_AUTHENTICATED)
class SubscriptionsController(
    private val subscriptionsService: SubscriptionsService,
) {

    @Get("/{userId}")
    fun getUserSubscriptions(@PathVariable userId: Long): List<Subscription> {
        logger.info("GET /subscriptions/$userId")
        return subscriptionsService.getUserSubscriptions(userId)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SubscriptionsController::class.java)
    }

}
