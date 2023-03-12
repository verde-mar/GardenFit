package it.unipi.gardenfit.navigation

/**
 * Sealed class used to navigate between screens
 *
 * @param route The route
 */
sealed class Screen(val route: String) {

    // The 'Home' screen
    object Home : Screen("home")

    // The current plant's screen
    object Plant : Screen("plant/{plantId}") {
        const val ARG_PLANT_ID: String = "plantId"
        fun route(plantId: String) = "plant/$plantId"
    }

    // The current zone's screen
    object Zone : Screen("zonename/{zoneId}") {
        const val ARG_ZONE_ID: String = "zoneId"
        fun route(zoneId: String?) = "zonename/$zoneId"
    }
}

/**
 * Sealed class used to navigate between dialogs
 *
 * @param route The route
 */
sealed class Dialog(val route: String){

    // The 'Add a new zone' dialog
    object AddZone : Dialog("addZone")

    // The 'Add a new plant' dialog
    object AddPlant : Dialog("addPlant/{zoneId}") {
        const val ARG_ZONE_ID: String = "zoneId"
        fun route(zoneId: String?) = "addPlant/$zoneId"
    }

    //todo: DA SISTEMARE LE DUE COSE SOTTO

    //
    object LongZone : Dialog("longZone/{zoneId}") {
        const val ARG_ZONE_ID: String = "zoneId"
        fun route(zoneId: String?) = "longZone/$zoneId"
    }

    //
    object LongPlant : Dialog("longPlant/{zoneId}/{plantId}") {
        const val ARG_PLANT_ID: String = "plantId"
        const val ARG_ZONE_ID: String = "zoneId"
        fun route(plantId: String, zoneId: String) = "longPlant/$zoneId/$plantId"
    }
}