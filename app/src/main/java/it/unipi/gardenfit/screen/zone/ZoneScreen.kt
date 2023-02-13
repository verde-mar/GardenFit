package it.unipi.gardenfit.screen.zone

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.unipi.gardenfit.data.FirestoreProxy
import it.unipi.gardenfit.data.Zone
import it.unipi.gardenfit.navigation.Screen
import it.unipi.gardenfit.screen.plant.PlantView
import it.unipi.gardenfit.ui.theme.GardenFitTheme
import it.unipi.gardenfit.util.LargeTitle
import it.unipi.gardenfit.util.StaggeredVerticalGrid
import it.unipi.gardenfit.util.Up

@Composable
fun ZoneScreen(
    zoneName: String,
    onBackPressed: () -> Unit
) {
    val viewmodel = ZoneViewModel(FirestoreProxy())
    val scrollState = rememberLazyListState()


    Surface {
        LazyColumn(
            state = scrollState
        ) {
            /* Title */
            item {
                Up(onBackPressed)
                LargeTitle(zoneName)
            }
            //viewmodel.addPlant(zone, "salvia")

            item {
                StaggeredVerticalGrid(
                    crossAxisCount = 2,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    val zones = viewmodel.zones.collectAsStateWithLifecycle(initialValue = emptyList())

                    val zone = remember {
                        for(zona in zones.value) {
                            if (zona.name.equals(zoneName))
                                return@remember zona
                        }
                        return@remember Zone("null", listOf())
                    }
                    Log.e("ZONESCREEN", "SIZE DI ZONES IN HOMESCREEN: ${zones.value.size}")

                    for(plant in zone.plants!!) {
                        PlantView(
                            plantName = plant,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 24.dp)
                        ) {
                            plant
                                .let { Screen.Plant.route(plant) } //todo: funziona pure questo?
                                .let { onBackPressed() }
                        }
                    }
                }

            }

            /* Add a plant */
            item {
                Box(modifier = Modifier
                    .fillMaxSize(1.0f)
                    .padding(4.dp, 4.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    ExtendedFloatingActionButton(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(8.dp)

                    ) {
                        /* Add icon */
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add a plant"
                        )
                        /* Space between the icon and the text */
                        Spacer(modifier = Modifier.padding(5.dp))
                        /* Text */
                        Text(text = "Add plant")

                    }
                }
            }

            /* Create space between the last element and the end of the screen */
            item {
                Box(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(bottom = 24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ZonePreview() {
    GardenFitTheme {
    }
}

//todo: 2 - fare la finestra di dialogo
//todo: - - verifichi che memorizzi davvero