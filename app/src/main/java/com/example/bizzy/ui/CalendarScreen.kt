package com.example.bizzy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bizzy.Reservation
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun CalendarScreen(
    reservations: List<Reservation>,
    onUpdate: (Reservation, LocalDateTime) -> Unit
)
 {
    val today = LocalDate.now()
    val selectedDate = remember { mutableStateOf(today) }
    var selectedReservation by remember { mutableStateOf<Reservation?>(null) }

    val startDate = today.minusDays(30)
    val endDate = today.plusDays(30)
    val dateRange = (0..ChronoUnit.DAYS.between(startDate, endDate).toInt())
        .map { startDate.plusDays(it.toLong()) }

    val todayIndex = remember { dateRange.indexOf(today) }
    val listState = rememberLazyListState()
    val reservationsState = remember { mutableStateOf(reservations) }

    LaunchedEffect(Unit) {
        listState.scrollToItem(todayIndex)
    }

    val timeSlots = List(11) { index -> LocalTime.of(8 + index, 0) }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        Text("Calendar", fontSize = 28.sp, color = Color.White)

        Text(
            text = selectedDate.value.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")),
            fontSize = 16.sp,
            color = Color.Yellow,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(dateRange) { date ->
                val isSelected = date == selectedDate.value

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isSelected) Color.White else Color.Transparent)
                        .clickable { selectedDate.value = date }
                        .padding(12.dp)
                ) {
                    Text(
                        text = date.dayOfWeek.name.take(3),
                        color = if (isSelected) Color.Black else Color.White
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = if (isSelected) Color.Black else Color.White
                    )
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(Color.Red, CircleShape)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn {
            items(timeSlots) { time ->
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = time.format(DateTimeFormatter.ofPattern("HH:mm")),
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    val reservationsAtThisTime = reservationsState.value.filter { reservation ->
                        val reservationStartTime = LocalTime.parse(reservation.time)
                        reservationStartTime == time && reservation.date == selectedDate.value
                    }

                    reservationsAtThisTime.forEach { reservation ->
                        var offsetY by remember { mutableStateOf(0f) }

                        Column(
                            modifier = Modifier
                                .background(Color(0xFF4CAF50), shape = MaterialTheme.shapes.medium)
                                .padding(12.dp)
                                .fillMaxWidth()
                                .clickable { selectedReservation = reservation }
                                .pointerInput(reservation) {
                                    detectDragGestures { _, dragAmount ->
                                        offsetY += dragAmount.y
                                        if (offsetY > 100f) {
                                            val newDateTime = LocalDateTime.of(
                                                reservation.date,
                                                time.plusMinutes(30)
                                            )
                                            updateReservation(reservationsState, reservation, newDateTime)
                                            offsetY = 0f
                                        } else if (offsetY < -100f) {
                                            val newDateTime = LocalDateTime.of(
                                                reservation.date,
                                                time.minusMinutes(30)
                                            )
                                            updateReservation(reservationsState, reservation, newDateTime)
                                            offsetY = 0f
                                        }
                                    }
                                }
                        ) {
                            Text(reservation.name, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(reservation.service, color = Color.White)
                            Text(reservation.time, color = Color.White)
                        }
                    }

                    if (reservationsAtThisTime.isEmpty()) {
                        Text(
                            "No reservations",
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Модальное окно редактирования
    selectedReservation?.let { reservation ->
        var editedName by remember { mutableStateOf(reservation.name) }
        var editedService by remember { mutableStateOf(reservation.service) }
        var editedTime by remember { mutableStateOf(reservation.time) }

        AlertDialog(
            onDismissRequest = { selectedReservation = null },
            title = { Text("Edit Reservation") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Client Name") }
                    )
                    OutlinedTextField(
                        value = editedService,
                        onValueChange = { editedService = it },
                        label = { Text("Service") }
                    )
                    OutlinedTextField(
                        value = editedTime,
                        onValueChange = { editedTime = it },
                        label = { Text("Time (HH:mm)") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newTimeParsed = try {
                            LocalTime.parse(editedTime)
                        } catch (e: Exception) {
                            null
                        }

                        if (newTimeParsed != null) {
                            val newDateTime = LocalDateTime.of(reservation.date, newTimeParsed)
                            val updatedReservation = reservation.copy(
                                name = editedName,
                                service = editedService,
                                time = editedTime
                            )

                            reservationsState.value = reservationsState.value.map {
                                if (it.id == reservation.id) updatedReservation else it
                            }

                            selectedReservation = null
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedReservation = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Функция обновления брони с новым временем
private fun updateReservation(
    reservationsState: MutableState<List<Reservation>>,
    reservation: Reservation,
    newDateTime: LocalDateTime
) {
    val updatedReservation = reservation.copy(
        time = newDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
        date = newDateTime.toLocalDate()
    )

    reservationsState.value = reservationsState.value.map {
        if (it.id == reservation.id) updatedReservation else it
    }
}
