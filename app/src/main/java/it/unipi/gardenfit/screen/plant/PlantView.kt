package it.unipi.gardenfit.screen.plant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.unipi.gardenfit.R
import it.unipi.gardenfit.ui.theme.GardenFitTheme

@Composable
fun PlantView(
    plantName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_eco_24),
            contentDescription = "Plant's field",
            tint = GardenFitTheme.colors.brand,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(32.dp)
        )
        Text(
            plantName,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp)
        )
    }
}