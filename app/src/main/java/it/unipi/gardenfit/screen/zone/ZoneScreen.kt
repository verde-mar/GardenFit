package it.unipi.gardenfit.screen.zone

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import it.unipi.gardenfit.data.FirestoreProxy
import it.unipi.gardenfit.navigation.Dialog
import it.unipi.gardenfit.navigation.Screen
import it.unipi.gardenfit.screen.plant.PlantView
import it.unipi.gardenfit.util.*


@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ZoneScreen(
    zoneName: String,
    onBackPressed: () -> Unit,
    navigateTo: (String, Boolean) -> Unit
) {
    val viewmodel = ZoneViewModel(FirestoreProxy())
    val zones =
        viewmodel.zones.collectAsStateWithLifecycle(initialValue = emptyList())
    val scaffoldState = rememberScaffoldState()
    val bluetoothPermission = checkBluetoothPermission()
    val cameraPermission = checkCameraPermission()


    GardenFitSurface(Modifier.fillMaxWidth()) {
        androidx.compose.material.Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                Column {
                    Up(onBackPressed)
                    LargeTitle(zoneName)
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = { Dialog.AddPlant.route(zoneName).let { navigateTo(it, false) } },
                    content = {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add a zone"
                        )
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.padding(10.dp)) {
                zones.value.forEach { zone ->
                    if (zone.name == zoneName) {
                        zone.plants?.let { plants ->
                            items(plants.size) {
                                Card(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = { i ->
                                                    Dialog.LongPlant
                                                        .route(plants[it], zoneName)
                                                        .let { navigateTo(it, false) }
                                                },
                                                onTap = { i ->
                                                    Screen.Plant
                                                        .route(plants[it])
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
                                        if(!cameraPermission.status.isGranted){
                                            cameraPermission.launchPermissionRequest()
                                        }
                                        if(!bluetoothPermission.allPermissionsGranted){
                                            bluetoothPermission.launchMultiplePermissionRequest()
                                        }
                                        PlantView(plantName = plants[it])
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}