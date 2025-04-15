// AuthApi.kt
package com.example.bizzy.data.api

import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val email: String,
    val password: String,
    val device_token: String = "android_test_device"
)
data class LoginResponse(val token: String)

interface AuthApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
