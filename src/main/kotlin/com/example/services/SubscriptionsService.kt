package com.example.services

import com.example.dto.*
import jakarta.inject.Singleton
import java.time.LocalDateTime
import kotlin.random.Random

@Singleton
class SubscriptionsService {

    private val userSubscriptions = mutableMapOf<Long, MutableSet<Long>>()
    private val subscriptions = mutableMapOf<Long, Subscription>()

    fun getUserSubscriptions(userId: Long): List<Subscription> {
        val subscriptionIds = userSubscriptions[userId] ?: List(listOf(2, 3, 4).random()) {
            generateUserSubscription(userId, it)
        }.map { it.id }
        return subscriptionIds.map { id ->
            subscriptions[id]!!
        }
    }

    fun getSubscriptionById(id: Long): Subscription? {
        return subscriptions[id]
    }

    fun pauseSubscription(subscriptionId: Long) {
        val sub = subscriptions[subscriptionId]!!
        if (sub.status != SubscriptionState.active) {
            throw PauseException("Unable to pause the subscription with state=${sub.status}")
        }
        subscriptions[subscriptionId] = sub.copy(
            status = SubscriptionState.paused,
            pausedSince = LocalDateTime.now().toString(),
        )
    }

    fun unpauseSubscription(subscriptionId: Long) {
        val sub = subscriptions[subscriptionId]!!
        if (sub.status != SubscriptionState.paused) {
            throw UnpauseException("Unable to unpause the subscription with state=${sub.status}")
        }
        subscriptions[subscriptionId] = sub.copy(
            status = SubscriptionState.active,
            pausedSince = null,
        )
    }

    private fun generateUserSubscription(userId: Long, index: Int): Subscription {
        val quantity = Random.nextInt(5) + 1
        val keys = List(quantity) { Subscription.License(
            generateKey(),
            listOf("SubProduct A", "SubProduct B", "SubProduct C").random(),
            LocalDateTime.parse("2022-05-05T11:50:55").toString(),
            null,
        ) }
        val state = listOf(SubscriptionState.active, SubscriptionState.paused).random()
        return Subscription(
            1000000 + userId + index,
            userId,
            listOf(31643L, 31644L, 32079L).random(),
            state,
            listOf(LicenseType.personal, LicenseType.commercial).random(),
            LocalDateTime.parse("2022-05-05T11:50:55").toString(),
            when (state) {
                SubscriptionState.paused -> LocalDateTime.parse("2022-06-05T11:50:55").toString()
                else -> null
            },
            null,
            keys,
        ).also { subscription ->
            subscriptions[subscription.id] = subscription
            if (userSubscriptions[userId] == null) {
                userSubscriptions[userId] = mutableSetOf()
            }
            userSubscriptions[userId]!!.add(subscription.id)
        }
    }

    private fun generateKey(): String {
        val allowedCharacters = "ABCDEF0123456789".toList()
        return List(16) {
            allowedCharacters.random()
        }.chunked(4).joinToString("-") { it.joinToString("") }
    }

}

class PauseException(message: String) : RuntimeException(message)
class UnpauseException(message: String) : RuntimeException(message)
