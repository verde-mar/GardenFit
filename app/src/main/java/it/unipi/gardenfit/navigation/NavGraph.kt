package it.unipi.gardenfit.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Plant : Screen("plant/{plantId}") {
        const val ARG_PLANT_ID: String = "plantId"
        fun route(plantId: String) = "plant/$plantId"
    }
    object Zone : Screen("zone/{zoneId}") {
        const val ARG_ZONE_ID: String = "zoneId"
        fun route(zoneId: String?) = "zone/$zoneId"
    }
}