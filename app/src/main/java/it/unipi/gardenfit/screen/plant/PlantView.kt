package it.unipi.gardenfit.screen.plant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import it.unipi.gardenfit.data.Plant
import it.unipi.gardenfit.ui.theme.GardenFitTheme

@Composable
fun PlantView(
    plant: Plant,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colors.background)
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = modifier
                .clip(MaterialTheme.shapes.medium)
                .background(GardenFitTheme.colors.iconInteractive, RoundedCornerShape(2.dp))
                .clickable(onClick = onClick),
        ) {

            Text(
                plant.name!!,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}