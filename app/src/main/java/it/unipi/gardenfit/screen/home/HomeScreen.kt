package it.unipi.gardenfit.screen.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.unipi.gardenfit.R
import it.unipi.gardenfit.data.FirestoreProxy
import it.unipi.gardenfit.navigation.Screen
import it.unipi.gardenfit.screen.zone.ZoneView
import it.unipi.gardenfit.ui.theme.GardenFitTheme
import it.unipi.gardenfit.util.GardenFitDivider
import it.unipi.gardenfit.util.LargeTitle
import it.unipi.gardenfit.util.StaggeredVerticalGrid

@Composable
fun HomeScreen(
    navigateTo: (String, Boolean) -> Unit
) {
    val zones = HomeViewModel(FirestoreProxy()).zones.collectAsStateWithLifecycle(initialValue = emptyList())
    val scrollState = rememberLazyListState()

    Surface {
        LazyColumn(
            state = scrollState
        ) {
            /* Title */
            item {
                LargeTitle(stringResource(R.string.my_zones))
                GardenFitDivider()
            }

            /* Items list */
            item {
                StaggeredVerticalGrid(
                    crossAxisCount = 2,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Log.e("HOMESCREEN", "SIZE DI ZONES IN HOMESCREEN: ${zones.value.size}")
                    zones.value.forEach { zone ->
                        ZoneView(
                            zone = zone,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            zone
                                .let { Screen.Zone.route(zone.name) }
                                .let { navigateTo(it, true) }
                        }
                    }
                }
            }


            /* Add a zone */
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
                            contentDescription = "Add a zone"
                        )
                        /* Space between the icon and the text */
                        Spacer(modifier = Modifier.padding(5.dp))
                        /* Text */
                        Text(text = stringResource(R.string.add_a_zone))

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
        //HomeScreen()
    }
}

//todo: 2 - fare la finestra di dialogo
//todo: - - verifichi che memorizzi davvero