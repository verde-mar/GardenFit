package it.unipi.gardenfit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Plant : Screen("plant/{plantId}") {
        const val ARG_PLANT_ID: String = "plantId"
        fun route(plantId: String) = "plant/$plantId"
    }
    object Zone : Screen("zone/{zoneId}") {
        const val ARG_ZONE_ID: String = "zoneId"
        fun route(zoneId: String) = "zone/$zoneId"
    }
}

@Composable
inline fun <reified VM : ViewModel> NavBackStackEntry.hiltNavGraphViewModel(): VM {
    val viewModelFactory = HiltViewModelFactory(LocalContext.current, this)
    return ViewModelProvider(this, viewModelFactory)[VM::class.java]
}