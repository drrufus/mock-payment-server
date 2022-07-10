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

    private val userSubscriptions = mutableMapOf<Long, List<Subscription>>()

    fun getUserSubscriptions(userId: Long): List<Subscription> {
        return userSubscriptions[userId] ?: generateUserSubscriptions(userId).also { subscriptions ->
            userSubscriptions[userId] = subscriptions
        }
    }

    private fun generateUserSubscriptions(userId: Long): List<Subscription> {
        val quantity = Random.nextInt(5) + 1
        val keys = List(quantity) { generateKey() }.toSet()
        val mockSubscription = Subscription(
            abs(Random.nextLong()),
            userId,
            listOf(31643L, 31644L).random(),
            "user-$userId@domain.io",
            SubscriptionState.ACTIVE,
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
        )
        return listOf(mockSubscription)
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
        val allowedCharacters = "ABCDEF0123456789".split("")
        return List(16) {
            allowedCharacters.random()
        }.chunked(4).joinToString("-") { it.joinToString("") }
    }

}
