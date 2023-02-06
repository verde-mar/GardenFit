package it.unipi.gardenfit.screen.home

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
fun HomeScreen() {
    //val homeViewModel = HomeViewModel()
    val scrollState = rememberLazyListState()
    //val zones = homeViewModel.fetchZones().collectAsStateWithLifecycle(emptyList())

    Surface {
        LazyColumn(
            state = scrollState
        ) {
            /* Title */
            item {
                LargeTitle("My zones")
                GardenFitDivider()
            }


            /* Items list */
            //todo: verificare che questo pezzo di codice funzioni
            /*items(zones.value, key = { it.id }) { zone ->
                //item {
                StaggeredVerticalGrid(
                    crossAxisCount = 2,
                    spacing = 16.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    ZoneView(
                        zone = zone,
                        modifier = Modifier.padding(bottom = 16.dp)
                    //todo: serve onClick?
                    ) {
                        zone
                            .let { Screen.Zone.route(zone.name) } //todo: funziona pure questo?
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
                            contentDescription = "Add a zone"
                        )
                        /* Space between the icon and the text */
                        Spacer(modifier = Modifier.padding(5.dp))
                        /* Text */
                        Text(text = "Add zone")

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
fun HomePreview() {
    GardenFitTheme {
        HomeScreen()
    }
}

//todo: 2 - fare la finestra di dialogo
//todo: - - verifichi che memorizzi davvero