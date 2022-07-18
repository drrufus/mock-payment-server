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

    fun cancelSubscription(subscriptionId: Long) {
        val sub = subscriptions[subscriptionId]!!
        if (sub.status != SubscriptionState.active) {
            throw CancelException("Unable to cancel the subscription with state=${sub.status}")
        }
        subscriptions[subscriptionId] = sub.copy(
            status = SubscriptionState.deleted,
            cancelledSince = LocalDateTime.now().toString(),
            nextPaymentDate = null,
        )
    }

    fun uncancelSubscription(subscriptionId: Long) {
        val sub = subscriptions[subscriptionId]!!
        if (sub.status != SubscriptionState.deleted) {
            throw UncancelException("Unable to uncancel the subscription with state=${sub.status}")
        }
        subscriptions[subscriptionId] = sub.copy(
            status = SubscriptionState.active,
            cancelledSince = null,
            nextPaymentDate = LocalDateTime.parse("2023-05-05T11:50:55").toString(),
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
        val state = listOf(SubscriptionState.active, SubscriptionState.deleted).random()
        return Subscription(
            1000000 + userId + index,
            userId,
            listOf(31643L, 31644L, 32079L).random(),
            state,
            listOf(LicenseType.personal, LicenseType.commercial).random(),
            LocalDateTime.parse("2022-05-05T11:50:55").toString(),
            null,
            when (state) {
                SubscriptionState.deleted -> LocalDateTime.parse("2022-06-05T11:50:55").toString()
                else -> null
            },
            keys,
            LocalDateTime.parse("2023-05-05T11:50:55").toString(),
            if (state == SubscriptionState.active) LocalDateTime.parse("2023-05-05T11:50:55").toString() else null,
            null,
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

class CancelException(message: String) : RuntimeException(message)
class UncancelException(message: String) : RuntimeException(message)
