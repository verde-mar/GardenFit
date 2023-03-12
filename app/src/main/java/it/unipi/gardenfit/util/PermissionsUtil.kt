package it.unipi.gardenfit.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun checkCameraPermission(): PermissionState {
    return rememberPermissionState(android.Manifest.permission.CAMERA)
}

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun checkBluetoothPermission() : MultiplePermissionsState {
    return rememberMultiplePermissionsState(listOf(android.Manifest.permission.BLUETOOTH, android.Manifest.permission.BLUETOOTH_ADMIN, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.BLUETOOTH_CONNECT))
}
