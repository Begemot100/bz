// AddReservationScreen.kt
package com.example.bizzy.ui

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bizzy.Reservation
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReservationScreen(onCreate: (Reservation) -> Unit) {
    val context = LocalContext.current

    var client by remember { mutableStateOf("") }
    var selectedBranch by remember { mutableStateOf("Example_1") }
    var master by remember { mutableStateOf("Kir") }
    var service by remember { mutableStateOf("Hair / Haircut") }
    var duration by remember { mutableStateOf("45") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var notes by remember { mutableStateOf("") }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    MaterialDialog(dialogState = dateDialogState, buttons = {
        positiveButton("OK")
        negativeButton("Cancel")
    }) {
        datepicker(initialDate = selectedDate) {
            selectedDate = it
        }
    }

    MaterialDialog(dialogState = timeDialogState, buttons = {
        positiveButton("OK")
        negativeButton("Cancel")
    }) {
        timepicker(initialTime = selectedTime) {
            selectedTime = it
        }
    }

    Scaffold(
        bottomBar = {},
        floatingActionButton = {},
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(40.dp)
        ) {
            Text("Create new reservation", fontSize = 22.sp, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Client:", color = Color.White)
            Text(text = if (client.isBlank()) "Select Client" else client, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Select Branch", color = Color.White)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Example_1", "Example_2").forEach {
                    Text(
                        text = it,
                        color = if (it == selectedBranch) Color.White else Color.Gray,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .clickable { selectedBranch = it }
                            .background(Color.DarkGray, shape = RoundedCornerShape(12.dp))
                            .padding(12.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Master:", color = Color.White)
            Text(master, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))
            Text("Service:", color = Color.White)
            Text(service, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))
            Text("Duration (min):", color = Color.White)
            Text(duration, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))
            Text("Date and Time", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .background(Color.DarkGray, shape = RoundedCornerShape(12.dp))
                        .clickable { dateDialogState.show() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(selectedDate.format(dateFormatter), color = Color.White)
                }
                Box(
                    modifier = Modifier
                        .background(Color.DarkGray, shape = RoundedCornerShape(12.dp))
                        .clickable { timeDialogState.show() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(selectedTime.format(timeFormatter), color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Notes", color = Color.White)
            TextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(color = Color.White)
            )




            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val newReservation = Reservation(
                        name = client,
                        service = service,
                        time = selectedTime.format(timeFormatter),
                        date = selectedDate
                    )
                    onCreate(newReservation)
                },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                Text("Create Reservation")
            }
        }
    }
}


