// Reservation.kt
package com.example.bizzy

import java.time.LocalDate
import java.util.UUID
import com.example.bizzy.Reservation


data class Reservation(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val service: String,
    val time: String, // "12:00 - 13:00"
    val date: LocalDate
)

