package it.unipi.gardenfit.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import it.unipi.gardenfit.data.FirestoreProxy
import it.unipi.gardenfit.navigation.Dialog
import it.unipi.gardenfit.navigation.Screen
import it.unipi.gardenfit.screen.home.HomeScreen
import it.unipi.gardenfit.screen.plant.PlantScreen
import it.unipi.gardenfit.screen.plant.PlantViewModel
import it.unipi.gardenfit.screen.zone.ZoneScreen
import it.unipi.gardenfit.screen.zone.ZoneViewModel
import it.unipi.gardenfit.ui.theme.GardenFitTheme

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@ExperimentalMaterialApi
fun GardenFitApp() {
    GardenFitTheme {
        Surface(color = MaterialTheme.colors.background) {
            val navController = rememberNavController()
            NavHost(navController, startDestination = Screen.Home.route) {

                // Brings to the home screen
                composable(Screen.Home.route) {
                    HomeScreen { route, clearBackStack ->
                        navController.navigate(route = route) {
                            if (clearBackStack) popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }

                // Brings to the zone screen
                composable(Screen.Zone.route) {
                    val zoneName = it.arguments?.getString(Screen.Zone.ARG_ZONE_ID).orEmpty()
                    ZoneScreen(
                        zoneName = zoneName,
                        onBackPressed = { navController.navigateUp() }
                    ) { route, clearBackStack ->
                        navController.navigate(route = route) {
                            if (clearBackStack) popUpTo(Screen.Home.route) { inclusive = false }
                        }
                    }
                }

                // Brings to the plant screen
                composable(Screen.Plant.route) {
                    val plantName = it.arguments?.getString(Screen.Plant.ARG_PLANT_ID).orEmpty()
                    PlantScreen(
                        plantName = plantName,
                        upPress = { navController.navigateUp() }
                    )
                }

                // Brings to the Add A Zone dialog
                dialog(
                    Dialog.AddZone.route,
                    dialogProperties = DialogProperties(
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true,
                        )){
                        val viewmodel = ZoneViewModel(FirestoreProxy())

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Surface(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .border(8.dp, Color.Transparent, MaterialTheme.shapes.small),
                                shape = androidx.compose.material3.MaterialTheme.shapes.extraLarge,
                            ) {

                                Column(modifier = Modifier.padding(16.dp)) {

                                    // Title
                                    Text(text = "Add a new zone")

                                    // Spacer
                                    Spacer(modifier = Modifier.padding(8.dp))

                                    // Form to fill
                                    OutlinedTextField(
                                        value = viewmodel.newName,
                                        onValueChange = { newValue -> viewmodel.updateNewName(newValue) },
                                        shape = RoundedCornerShape(4.dp)
                                    )

                                    // Spacer
                                    Spacer(modifier = Modifier.padding(8.dp))

                                    // Dismiss button
                                    Button(
                                        onClick = {
                                            viewmodel.addNewZone(viewmodel.newName)
                                            let { navController.navigateUp() }
                                        },
                                        content = { Text(text = "OK", color = Color.White) }
                                    )
                                }
                            }
                        }
                }

                // Brings to the Delete A Zone Dialog
                dialog(
                    Dialog.LongZone.route,
                    dialogProperties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                    )){
                    val zoneName = it.arguments?.getString(Dialog.LongZone.ARG_ZONE_ID).orEmpty()
                    val viewmodel = ZoneViewModel(FirestoreProxy())
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(2.dp)
                                .border(16.dp, Color.Transparent, MaterialTheme.shapes.small)
                                .align(Alignment.Center),
                            shape = androidx.compose.material3.MaterialTheme.shapes.extraLarge,
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Do you want to delete this zone?")

                                Spacer(modifier = Modifier.padding(8.dp))

                                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                    Button(
                                        onClick = {
                                            viewmodel.deleteZone(zoneName)
                                            navController.navigateUp()
                                        }
                                    ) {
                                        Text(text = "Yes")
                                    }

                                    Spacer(modifier = Modifier.padding(32.dp))

                                    Button(
                                        onClick = { navController.navigateUp() }
                                    ) {
                                        Text(text = "No")
                                    }

                                }
                            }
                        }
                    }
                }

                // Brings to the Delete A Plant dialog
                //todo: da testare
                dialog(
                    Dialog.LongPlant.route,
                    dialogProperties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                    )){
                    val plantName = it.arguments?.getString(Dialog.LongPlant.ARG_PLANT_ID).orEmpty()
                    val zoneName = it.arguments?.getString(Dialog.LongPlant.ARG_ZONE_ID).orEmpty()
                    val viewmodel = PlantViewModel(FirestoreProxy())

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(2.dp)
                                .align(Alignment.Center)
                                .border(16.dp, Color.Transparent, MaterialTheme.shapes.small),
                            shape = androidx.compose.material3.MaterialTheme.shapes.extraLarge,
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Do you want to delete this plant?")

                                Spacer(modifier = Modifier.padding(8.dp))

                                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                    Button(
                                        onClick = {
                                            viewmodel.deletePlant(zoneName, plantName)
                                            navController.navigateUp()
                                        }
                                    ) {
                                        Text(text = "Yes")
                                    }

                                    Spacer(modifier = Modifier.padding(32.dp))

                                    Button(
                                        onClick = { navController.navigateUp() }
                                    ) {
                                        Text(text = "No")
                                    }

                                }
                            }
                        }
                    }
                }

                // Brings to the Add A Plant dialog
                dialog(
                    Dialog.AddPlant.route,
                    dialogProperties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                    )
                    ){
                    val viewmodel = PlantViewModel(FirestoreProxy())
                    val zoneName = it.arguments?.getString(Dialog.AddPlant.ARG_ZONE_ID).orEmpty()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(2.dp)
                                .border(8.dp, Color.Transparent, MaterialTheme.shapes.small),
                            shape = androidx.compose.material3.MaterialTheme.shapes.extraLarge,
                        ) {

                            Column(modifier = Modifier.padding(16.dp)) {

                                // Title
                                Text(text = "Add a new plant")

                                // Spacer
                                Spacer(modifier = Modifier.padding(8.dp))

                                // Form to fill
                                OutlinedTextField(
                                    value = viewmodel.newName,
                                    onValueChange = { newValue -> viewmodel.updateNewName(newValue) },
                                    shape = RoundedCornerShape(4.dp)
                                )

                                // Spacer
                                Spacer(modifier = Modifier.padding(8.dp))

                                // Dismiss button
                                Button(
                                    onClick = {
                                        viewmodel.addNewPlant(viewmodel.newName, zoneName)
                                        let { navController.navigateUp() }
                                    },
                                    content = { Text(text = "OK", color = Color.White) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}