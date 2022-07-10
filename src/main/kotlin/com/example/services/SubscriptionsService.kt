package com.example.services

import com.example.dto.*
import com.example.dto.Currency
import jakarta.inject.Singleton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
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

    fun pauseSubscription(subscriptionId: Long) {
        val sub = subscriptions[subscriptionId]!!
        if (sub.state != SubscriptionState.active) {
            throw PauseException("Unable to pause the subscription with state=${sub.state}")
        }
        subscriptions[subscriptionId] = sub.copy(
            state = SubscriptionState.paused,
            pausedAt = LocalDateTime.now().toString(),
        )
    }

    fun unpauseSubscription(subscriptionId: Long) {
        val sub = subscriptions[subscriptionId]!!
        if (sub.state != SubscriptionState.paused) {
            throw UnpauseException("Unable to unpause the subscription with state=${sub.state}")
        }
        subscriptions[subscriptionId] = sub.copy(
            state = SubscriptionState.active,
            pausedAt = null,
        )
    }

    private fun generateUserSubscription(userId: Long, index: Int): Subscription {
        val quantity = Random.nextInt(5) + 1
        val keys = List(quantity) { generateKey() }.toSet()
        val state = listOf(SubscriptionState.active, SubscriptionState.paused).random()
        return Subscription(
            1000000 + userId + index,
            userId,
            listOf(31643L, 31644L).random(),
            "user-$userId@domain.io",
            state,
            formatSignupDate(LocalDateTime.parse("2022-05-04T11:50:55")),
            Subscription.Payment(
                abs(Random.nextDouble()),
                Currency.USD,
                formatPaymentDate(LocalDateTime.parse("2022-05-05T11:50:55")),
            ),
            Subscription.Payment(
                abs(Random.nextDouble()),
                Currency.USD,
                formatPaymentDate(LocalDateTime.parse("2023-05-05T11:50:55")),
            ),
            Subscription.PaymentInfo(
                PaymentMethod.CARD,
                CardType.VISA,
                "4242",
                "12/27",
            ),
            quantity,
            keys,
            when (state) {
                SubscriptionState.paused -> LocalDateTime.now().toString()
                else -> null
            }
        ).also { subscription ->
            subscriptions[subscription.id] = subscription
            if (userSubscriptions[userId] == null) {
                userSubscriptions[userId] = mutableSetOf()
            }
            userSubscriptions[userId]!!.add(subscription.id)
        }
    }

    private fun formatSignupDate(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return formatter.format(date)
    }

    private fun formatPaymentDate(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return formatter.format(date)
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
