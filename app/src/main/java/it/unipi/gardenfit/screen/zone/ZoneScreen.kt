package it.unipi.gardenfit.screen.zone

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.unipi.gardenfit.ui.theme.GardenFitTheme
import it.unipi.gardenfit.util.GardenFitDivider
import it.unipi.gardenfit.util.LargeTitle

@Composable
fun ZoneScreen(
    zoneId: Int
) {
    //val zoneViewModel = ZoneViewModel()
    val scrollState = rememberLazyListState()
    //val plants = zoneViewModel.fetchPlants().collectAsStateWithLifecycle(emptyList())

    //val zone = remember(zoneId) { plantRepo.getplant(plantId) }

    Surface {
        LazyColumn(
            state = scrollState
        ) {
            /* Title */
            item {
                LargeTitle("zone.name")
                GardenFitDivider()
            }

            /* Items list */
            //todo: verificare che questo pezzo di codice funzioni
            /*items(plants.value, key = { it.plantId }) { plant ->
                //item {
                StaggeredVerticalGrid(
                    crossAxisCount = 2,
                    spacing = 16.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    PlantView(
                        plant = plant,
                        modifier = Modifier.padding(bottom = 16.dp)
                    //todo: e' necessario qui l'onClick
                    ) {
                        plant
                            .let { Screen.Plant.route(plant.name) } //todo: funziona pure questo?
                        //.let { navigateTo(it) } todo: e' necessario?
                    }

                }
            }*/
            //todo: dove termina il codice ancora da testare

            /* Add a zone */
            item {
                Box(modifier = Modifier
                    .fillMaxSize(1.0f)
                    .padding(8.dp, 8.dp),
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
        //ZoneScreen()
    }
}

//todo: 2 - fare la finestra di dialogo
//todo: - - verifichi che memorizzi davvero