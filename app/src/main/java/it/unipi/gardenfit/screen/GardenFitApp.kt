package it.unipi.gardenfit.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.unipi.gardenfit.navigation.Screen
import it.unipi.gardenfit.screen.home.HomeScreen
import it.unipi.gardenfit.screen.plant.PlantScreen
import it.unipi.gardenfit.screen.zone.ZoneScreen
import it.unipi.gardenfit.ui.theme.GardenFitTheme


@RequiresApi(Build.VERSION_CODES.N)
@Composable
@ExperimentalMaterialApi
@Preview
fun GardenFitApp() {
    GardenFitTheme {
        Surface(color = MaterialTheme.colors.background) {
            val navController = rememberNavController()
            NavHost(navController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) {
                    HomeScreen { route, clearBackStack ->
                        navController.navigate(route = route) {
                            if (clearBackStack) popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
                composable(Screen.Plant.route) {
                    val plantName = it.arguments?.getString(Screen.Plant.ARG_PLANT_ID).orEmpty()
                    PlantScreen(
                        plantName = plantName,
                        upPress = { navController.navigateUp() } //todo: navigateUp porta ad una schermata bianca
                    )
                }
                composable(Screen.Zone.route) {
                    val zoneName = it.arguments?.getString(Screen.Zone.ARG_ZONE_ID).orEmpty()
                    ZoneScreen(
                        zoneName = zoneName,
                        onBackPressed = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}