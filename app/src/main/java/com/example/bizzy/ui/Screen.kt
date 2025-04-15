//package com.example.bizzy.ui
//
//import androidx.annotation.DrawableRes
//import com.example.bizzy.R
//
//sealed class Screen(val title: String, @DrawableRes val iconResId: Int) {
//    object Calendar : Screen("Calendar", R.drawable.calendar) // Correctly referencing calendar.png
//    object Schedule : Screen("Schedule", R.drawable.schedule) // Correctly referencing schedule.png
//    object Analytics : Screen("Analytics", R.drawable.analytics) // Correctly referencing analytics.png
//    object Settings : Screen("Settings", R.drawable.settings) // Correctly referencing settings.png
//}