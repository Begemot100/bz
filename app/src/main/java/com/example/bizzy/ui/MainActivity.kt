package com.example.bizzy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.*
import com.example.bizzy.ui.AddReservationScreen
import com.example.bizzy.ui.CalendarScreen
import com.example.bizzy.ui.LoginScreen
import com.example.bizzy.ui.RegistrationScreen
import com.example.bizzy.ui.theme.BizzyTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.statusBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            BizzyTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()

    var reservations by remember { mutableStateOf<List<Reservation>>(emptyList()) }

    fun addReservation(reservation: Reservation) {
        reservations = reservations + reservation
    }

    val hideBars = currentRoute?.destination?.route in listOf("addReservation", "registration", "login")
    val items = listOf(
        Screen.Calendar,
        Screen.Schedule,
        Screen.Analytics,
        Screen.Settings
    )

    val updateReservation: (Reservation, LocalDateTime) -> Unit = { reservation, newDateTime ->
        reservations = reservations.map {
            if (it.id == reservation.id) it.copy(
                time = newDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                date = newDateTime.toLocalDate()
            ) else it
        }
    }

    Scaffold(
        topBar = { if (!hideBars) TopBar() },
        bottomBar = {
            if (!hideBars) {
                NavigationBar(containerColor = Color.Black) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val current = navBackStackEntry?.destination?.route

                    items.forEach { screen ->
                        NavigationBarItem(
                            selected = current == screen.route,
                            onClick = {
                                if (current != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = screen.iconResId),
                                    contentDescription = screen.title,
                                    modifier = Modifier
                                        .size(42.dp)
                                        .padding(4.dp)
                                        .animateContentSize(
                                            animationSpec = tween(
                                                durationMillis = 500,
                                                easing = FastOutSlowInEasing
                                            )
                                        )
                                )
                            },
                            label = { Text(screen.title) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (!hideBars) {
                FloatingActionButton(
                    onClick = { navController.navigate("addReservation") },
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Add",
                        modifier = Modifier.size(46.dp)
                    )
                }
            }
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = "login") {

                composable("login") {
                    LoginScreen(onLoginSuccess = {
                        navController.navigate(Screen.Calendar.route) {
                            popUpTo("login") { inclusive = true }
                        }
                    })
                }

                composable(Screen.Calendar.route) {
                    CalendarScreen(
                        reservations = reservations,
                        onUpdate = updateReservation
                    )
                }

                composable(Screen.Schedule.route) { ProfileScreen() }
                composable(Screen.Analytics.route) { AnalyticsScreen() }
                composable(Screen.Settings.route) { SettingsScreen() }
                composable("registration") {
                    RegistrationScreen()
                }
                composable("addReservation") {
                    AddReservationScreen(onCreate = { reservation ->
                        addReservation(reservation)
                        navController.popBackStack()
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier.padding(top = 44.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Bizzy", color = Color.White)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("admin", color = Color.White, modifier = Modifier.padding(end = 18.dp))
                    Image(
                        painter = painterResource(id = R.drawable.addp),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
    )
}

@Composable
fun HomeScreen() {
    CenteredText("Calendar Screen")
}

@Composable
fun ProfileScreen() {
    CenteredText("Schedule Screen")
}

@Composable
fun AnalyticsScreen() {
    CenteredText("Analytics Screen")
}

@Composable
fun SettingsScreen() {
    CenteredText("Settings Screen")
}

@Composable
fun CenteredText(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}

sealed class Screen(val route: String, val title: String, val iconResId: Int) {
    object Calendar : Screen("calendar", "Calendar", R.drawable.calendar)
    object Schedule : Screen("schedule", "Schedule", R.drawable.schedule)
    object Analytics : Screen("analytics", "Analytics", R.drawable.analytics)
    object Settings : Screen("settings", "Settings", R.drawable.settings)
}
