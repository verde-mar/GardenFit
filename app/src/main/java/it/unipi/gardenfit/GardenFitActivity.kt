package it.unipi.gardenfit

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import it.unipi.gardenfit.screen.GardenFitApp

class GardenFitActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //todo: non sono sicura se ci vada
            GardenFitApp()
        }
    }
}