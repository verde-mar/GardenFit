package it.unipi.gardenfit

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi

class GardenFitActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GardenFitHiltApp()
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    it.unipi.gardenfit.ui.theme.GardenFitTheme {
    }
}*/