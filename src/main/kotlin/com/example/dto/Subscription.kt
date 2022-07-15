package com.example.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable
data class Subscription(
    @JsonProperty("paddle_subscription_id") val id: Long,
    @JsonProperty("paddle_user_id") val userId: Long,
    @JsonProperty("paddle_plan_id") val planId: Long,
    val status: SubscriptionState,
    @JsonProperty("license_type") val licenseType: LicenseType,
    @JsonProperty("start_time") val startTime: String,
    @JsonProperty("paused_since") val pausedSince: String?,
    @JsonProperty("cancelled_since") val cancelledSince: String?,
    @JsonProperty("serials") val licenses: List<License>,
) {
    @Introspected
    @Serdeable
    data class License(
        val serial: String,
        @JsonProperty("plugin_name") val pluginName: String,
        @JsonProperty("generation_time") val generationTime: String,
        @JsonProperty("deactivation_time") val deactivationTime: String?,
    )
}

enum class SubscriptionState {
    active,
    deleted,
    paused,
    past_due,
    trialing,
}

enum class LicenseType {
    personal,
    commercial,
}
