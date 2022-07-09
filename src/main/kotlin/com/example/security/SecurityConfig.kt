package com.example.security

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("auth")
class SecurityConfig {

    lateinit var user: String
    lateinit var pass: String

}
