package com.example.bizzy.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.compose.ui.text.input.KeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen() {
    val context = LocalContext.current

    var inviteToken by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var agreeToTerms by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Text("Registration", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Enter the registration information", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        RegistrationTextField("\uD83D\uDD11 Токен приглашения", inviteToken) { inviteToken = it }
        RegistrationTextField("\uD83D\uDC64 Имя", name) { name = it }
        RegistrationTextField("\u260E\uFE0F Номер телефона", phone, KeyboardType.Phone) { phone = it }

        RegistrationTextField(
            label = "\uD83D\uDD10 Пароль",
            value = password,
            onValueChange = { password = it },
            keyboardType = KeyboardType.Password,
            isPassword = true,
            showPassword = showPassword,
            togglePasswordVisibility = { showPassword = !showPassword }
        )

        RegistrationTextField(
            label = "\uD83D\uDD10 Подтвердите пароль",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            keyboardType = KeyboardType.Password,
            isPassword = true,
            showPassword = showPassword,
            togglePasswordVisibility = { showPassword = !showPassword }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = agreeToTerms,
                onCheckedChange = { agreeToTerms = it },
                colors = CheckboxDefaults.colors(checkedColor = Color.White)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("I agree with ", color = Color.White)
            Text("Terms of Use", color = Color.White, modifier = Modifier.clickable { })
            Text(" and ", color = Color.White)
            Text("Privacy Policy", color = Color.White, modifier = Modifier.clickable { })
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (agreeToTerms && password == confirmPassword) {
                    Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please check inputs", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = agreeToTerms,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (agreeToTerms) Color(0xFF9C27B0) else Color.DarkGray,
                disabledContainerColor = Color.DarkGray
            )
        ) {
            Text("\u0417арегистрироваться ✓")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationTextField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    togglePasswordVisibility: (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword && togglePasswordVisibility != null) {
            {
                val iconText = if (showPassword) "🙈" else "👁"
                Text(
                    iconText,
                    modifier = Modifier.clickable { togglePasswordVisibility() },
                    color = Color.White
                )
            }
        } else null,
        textStyle = TextStyle(color = Color.White),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.DarkGray,
            unfocusedContainerColor = Color.DarkGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )

    )
}