package it.unipi.gardenfit.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.unipi.gardenfit.R
import it.unipi.gardenfit.data.FirestoreProxy
import it.unipi.gardenfit.data.Zone
import it.unipi.gardenfit.navigation.Dialog
import it.unipi.gardenfit.navigation.Screen
import it.unipi.gardenfit.ui.theme.GardenFitTheme
import it.unipi.gardenfit.util.LargeTitle

/**
 * This function displays the first screen you'll se in the app: Home Screen, where you can find all of your stored zones.
 */

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateTo: (String, Boolean) -> Unit
) {
    // Zones stored in Firestore
    val zones = HomeViewModel(FirestoreProxy()).zones.collectAsStateWithLifecycle(initialValue = emptyList())

    val scaffoldState = rememberScaffoldState()

    androidx.compose.material.Scaffold(

        scaffoldState = scaffoldState,
        // Displays the title
        topBar = {
            LargeTitle(stringResource(R.string.youre_areas))
        },
        // Adds a zone
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = { Dialog.AddZone.route.let { navigateTo(it, false) } },
                content = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(R.string.Add_a_zone)
                    )
                }
            )
                               },
        floatingActionButtonPosition = FabPosition.End
    ) {
        // Displays the zone
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.padding(10.dp)){
            items(zones.value.size){

                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                // Brings to deleting the zone
                                onLongPress = { _ ->
                                        Dialog.LongZone.route(zones.value[it].name)
                                        .let { navigateTo(it, false) }
                                },
                                // Brings to the zone
                                onTap = { _ ->
                                    Screen.Zone.route(zones.value[it].name)
                                        .let { navigateTo(it, false) }
                                }
                            )
                        },
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation()
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // The zone object displayed in Home Screen
                        ZoneView(zone = zones.value[it])
                    }
                }
            }
        }
    }
}

/**
 * This functions displays a zone in Home Screen
 */
@Composable
fun ZoneView(
    zone: Zone,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium),
    ) {
        // Zone's icon
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_eco_24),
            contentDescription = "Zone's icon",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(32.dp),
            tint = GardenFitTheme.colors.gradient31[0]
        )
        // Zone's name
        androidx.compose.material.Text(
            zone.name!!,
            style = MaterialTheme.typography.body1,
            modifier = modifier.padding(8.dp)
        )
    }
}
