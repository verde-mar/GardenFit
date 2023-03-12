package it.unipi.gardenfit.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.unipi.gardenfit.R
import it.unipi.gardenfit.data.FirestoreProxy
import it.unipi.gardenfit.navigation.Dialog
import it.unipi.gardenfit.navigation.Screen
import it.unipi.gardenfit.screen.zone.ZoneView
import it.unipi.gardenfit.util.LargeTitle

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateTo: (String, Boolean) -> Unit
) {
    val zones = HomeViewModel(FirestoreProxy()).zones.collectAsStateWithLifecycle(initialValue = emptyList())
    val scaffoldState = rememberScaffoldState()

    androidx.compose.material.Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            LargeTitle(stringResource(R.string.my_zones))
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = { Dialog.AddZone.route.let { navigateTo(it, false) } },
                content = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add a zonename"
                    )
                }
            )
                               },
        floatingActionButtonPosition = FabPosition.End
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.padding(10.dp)){
            items(zones.value.size){
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = { i ->
                                        Dialog.LongZone.route(zones.value[it].name)
                                        .let { navigateTo(it, false) }
                                },
                                onTap = { i ->
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
                        ZoneView(zone = zones.value[it])
                    }
                }
            }
        }
    }
}