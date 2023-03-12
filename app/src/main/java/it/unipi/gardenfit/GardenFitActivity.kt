package it.unipi.gardenfit

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import it.unipi.gardenfit.screen.GardenFitApp

class GardenFitActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GardenFitApp()
        }
    }
}