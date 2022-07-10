package com.example.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDate
import java.util.Date

@Introspected
@Serdeable
data class Subscription(
    @JsonProperty("subscription_id") val id: Long,
    @JsonProperty("user_id") val userId: Long,
    @JsonProperty("plan_id") val planId: Long,
    @JsonProperty("user_email") val userEmail: String,
    val state: SubscriptionState,
    @JsonProperty("signup_date") val signupDate: String,
    @JsonProperty("last_payment") val lastPaymentInfo: Payment,
    @JsonProperty("next_payment") val nextPaymentInfo: Payment?,
    @JsonProperty("payment_information") val paymentInformation: PaymentInfo,
    val quantity: Int,
    // extra stuff:
    val keys: Set<String>,
) {

    @Introspected
    @Serdeable
    data class Payment(
        val amount: Double,
        val currency: Currency,
        val date: String,
    )

    @Introspected
    @Serdeable
    data class PaymentInfo(
        @JsonProperty("payment_method") val method: PaymentMethod,
        @JsonProperty("card_type") val cardType: CardType?,
        @JsonProperty("last_four_digits") val ending: String,
        @JsonProperty("expiry_date") val expiration: String,
    )

}

enum class SubscriptionState {
    ACTIVE,
}

enum class Currency {
    USD,
}

enum class PaymentMethod {
    CARD,
    PAYPAL,
}

enum class SubscriptionPeriod {
    MONTHLY,
    ANNUAL,
}

enum class CardType {
    VISA,
}
